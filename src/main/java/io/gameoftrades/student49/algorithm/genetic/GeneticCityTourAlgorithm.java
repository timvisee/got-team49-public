package io.gameoftrades.student49.algorithm.genetic;

import com.timvisee.voxeltex.util.swing.ProgressDialog;
import io.gameoftrades.debug.Debuggable;
import io.gameoftrades.debug.Debugger;
import io.gameoftrades.model.algoritme.StedenTourAlgoritme;
import io.gameoftrades.model.kaart.Kaart;
import io.gameoftrades.model.kaart.Stad;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GeneticCityTourAlgorithm implements StedenTourAlgoritme, Debuggable {

    /**
     * Maximum amount of tries.
     */
    private final static int MAX_TRIES = 20;

    /**
     * List of cities to take in consideration.
     */
    private ArrayList<Stad> cities;

    /**
     * Debugger instance.
     */
    private Debugger debugger;

    @Override
    public List<Stad> bereken(Kaart map, List<Stad> cities) {
        // Create a progress dialog instance
        // TODO: Attach progress dialog to proper window.
        final ProgressDialog progress = new ProgressDialog(Frame.getFrames()[0], "Processing...", false, "Starting...", true);

        // Store the parameters
        this.cities = new ArrayList<>(cities);

        // Create a population list
        final ArrayList<Population> populationList = new ArrayList<>();

        // Store the current time, used for profiling
        final long time = System.currentTimeMillis();

        // Setup the progress bar
        progress.setProgressMax(MAX_TRIES + 1);
        progress.setProgressValue(0);
        progress.setShowProgress(true);

        // Run the algorithm a few times
        for(int i = 0; i < MAX_TRIES; i++) {
            // Update the progress
            progress.setProgressValue(i);
            progress.setStatus("Simulating evolution " + (i + 1) + "...");

            // Define the population
            Population population = new Population(50, cities, true);

            // Keep track of the fittest individual and generation count
            int sameFitness = 0;
            int fittest = -1;
            int generationCount = 0;

            // Find a maximum of 50 that have the same fitness
            while(sameFitness <= 50) {
                // Create a new generation
                generationCount++;
                population = evolvePopulation(population);

                // Store the individual if fitter than the current
                if(fittest == -1 || population.getFittest().getFitness() < fittest) {
                    fittest = population.getFittest().getFitness();
                    sameFitness = 0;
                }

                // Increase the sameFitness variable if we encounter the same individual
                if(fittest == population.getFittest().getFitness())
                    sameFitness++;
            }

            // Store the current population
            populationList.add(population);

            // Show a simple debug message
            System.out.println("Try " + (i + 1) + ". Evolved through " + generationCount + " generations. Best solution: " + population.getFittest().getFitness());
        }

        // Update the progress
        progress.setProgressValue(MAX_TRIES);
        progress.setStatus("Finding most efficient individual...");

        // Show profiler information
        System.out.println("Found best route, took " + (System.currentTimeMillis() - time) + " ms.");

        // Get the fittest population
        final Population fittestPopulation = getFittestPopulation(populationList);

        // Find the fittest individual
        final Individual fittest = fittestPopulation.getFittest();

        // Update the progress and dispose the dialog
        progress.setProgressValue(progress.getProgressMax());
        progress.dispose();

        // Debug the most efficient route
        System.out.println("The most efficient route is " + fittest.getFitness() + ".");
        debugger.debugSteden(map, fittest.getCities());

        // Return the list of cities that define the most efficient path
        return fittest.getCities();
    }

    /**
     * Get the fittest population.
     *
     * @param populationList List of populations.
     * @return Fittest population.
     */
    private Population getFittestPopulation(ArrayList<Population> populationList) {
        // Keep track of the
        int population = -1;
        int index = 0;

        // Loop through the populations to find the lowest
        for(int i = 0, length = populationList.size(); i < length; i++) {
            // Get the fittest individual fitness
            final int fitness = populationList.get(i).getFittest().getFitness();

            // Store the population if it's fitter
            if(population == -1 || fitness < population) {
                population = fitness;
                index = i;
            }
        }

        // Return the fittest population
        return populationList.get(index);
    }

    /**
     * Evolve the population.
     *
     * @param population Population to evolve.
     * @return Evolved population.
     */
    private Population evolvePopulation(Population population) {
        // Create a new population
        final Population evolved = new Population(population.getSize(), this.cities, false);

        // Evolve the fittest individual from the current population
        evolved.saveIndividual(0, population.getFittest());

        // Cross over individuals from the given population
        for(int i = 1, length = population.getSize(); i < length; i++)
            evolved.saveIndividual(i, crossover(tournament(population), tournament(population)));

        // Mutate evolved individuals
        for(int i = 1, length = evolved.getSize(); i < length; i++)
            mutate(evolved.getIndividual(i));

        // Return the evolved population
        return evolved;
    }

    /**
     * Do a population tournament, and get the fittest individual.
     * This creates a new population with a random combination of individuals from the given population, returns the fittest individual.
     *
     * @param population Population to do a tournament for.
     * @return Fittest individual after a tournament.
     */
    private Individual tournament(Population population) {
        // Create a tournament population
        final Population tournament = new Population(5, this.cities, false);

        // Get the population size
        final int populationSize = population.getSize();

        // Take random individuals from the current population
        for(int i = 0, length = tournament.getSize(); i < length; i++)
            tournament.saveIndividual(i, population.getPopulation()[(int) (Math.random() * populationSize)]);

        // Return the fittest individual from the tournament population
        return tournament.getFittest();
    }

    /**
     * Cross over the cities from the first and second individuals into a new evolved individual.
     *
     * @param first  First individual.
     * @param second Second individual.
     * @return Evolved individual.
     */
    private Individual crossover(Individual first, Individual second) {
        // Create a new individual
        final Individual evolved = new Individual();

        // Loop through the list of cities, and randomly add a city to the evolved from the first or second individual
        boolean useFirst;
        for(int i = 0, length = cities.size(); i < length; i++)
            getCityFrom(evolved, (useFirst = Math.random() <= 0.5) ? first : second, useFirst ? second : first, i);

        // Return the evolved individual
        return evolved;
    }

    /**
     * Add a city to the evolved individual from the {@param first} or {@param second} individual at the given index.
     * Add's all cities that aren't in the evolved individual if the cities provided by {@param first} or {@param second} aren't new.
     *
     * @param evolved Evolved individual.
     * @param first   First individual.
     * @param second  Second individual.
     * @param i       Index of the city.
     */
    private void getCityFrom(Individual evolved, Individual first, Individual second, int i) {
        // Add the city from first at the given index to the evolved individual
        if(!evolved.hasCity(first.getCity(i))) {
            evolved.addCity(first.getCity(i));
            return;
        }

        // Add the city from second at the given index to the evolved individual
        if(!evolved.hasCity(second.getCity(i))) {
            evolved.addCity(second.getCity(i));
            return;
        }

        // Add each city that isn't in the evolved individual from the list of cities
        this.cities.stream().filter(s -> !evolved.hasCity(s)).forEach(evolved::addCity);
    }

    /**
     * Mutate the given individual.
     * This modifies the list of cities of the individual slightly.
     *
     * @param individual Individual to mutate.
     */
    private void mutate(Individual individual) {
        // Loop through the list of cities of the individual, except the last city
        for(int i = 0, length = individual.getCities().size() - 1; i < length; i++) {
            // Flip the current and a random city with a 1.5% chance
            if(Math.random() < 0.015) {
                // Generate a random city index
                final int random = (int) (Math.random() * ((individual.getCities().size() - 1)));

                // Get both cities
                final Stad first = individual.getCities().get(random);
                final Stad second = individual.getCities().get(i);

                // Store both cities flipped
                individual.setCity(i, first);
                individual.setCity(random, second);
            }

            // Flip the current and followed city with a 7.5% chance
            if(Math.random() < 0.075) {
                // Get both cities
                final Stad first = individual.getCities().get(i);
                final Stad second = individual.getCities().get(i + 1);

                // Store both cities flipped
                individual.setCity(i, second);
                individual.setCity(i + 1, first);
            }
        }
    }

    /**
     * String representation of this class, which defines the algorithm name.
     *
     * @return Algorithm name.
     */
    @Override
    public String toString() {
        return "Genetic Algorithm";
    }

    @Override
    public void setDebugger(Debugger debugger) {
        this.debugger = debugger;
    }
}
