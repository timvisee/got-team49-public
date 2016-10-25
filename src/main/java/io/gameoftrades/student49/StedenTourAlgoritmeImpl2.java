package io.gameoftrades.student49;

import io.gameoftrades.debug.Debuggable;
import io.gameoftrades.debug.Debugger;
import io.gameoftrades.model.algoritme.StedenTourAlgoritme;
import io.gameoftrades.model.kaart.Kaart;
import io.gameoftrades.model.kaart.Stad;

import java.util.ArrayList;
import java.util.List;

public class StedenTourAlgoritmeImpl2 implements StedenTourAlgoritme, Debuggable {

    /**
     * Maximum amount of tries.
     */
    private final static int MAX_TRIES = 20;

    /**
     * Map instance.
     */
    private Kaart map;

    /**
     * List of cities to take in consideration.
     */
    private ArrayList<Stad> cities;

    /**
     * Debugger instance.
     */
    private Debugger debugger;

    /**
     * Path checker instance.
     */
    private PathChecker pathChecker;

    @Override
    public List<Stad> bereken(Kaart map, List<Stad> cities) {
        // Store the parameters
        this.map = map;
        this.cities = new ArrayList<>(cities);

        // Create a population list
        final ArrayList<Population> populationList = new ArrayList<>();

        // Create a new path checker instance, if there isn't one yet
        if(pathChecker == null)
            pathChecker = new PathChecker(this.cities, this.map);

        // Store the current time, used for profiling
        final long time = System.currentTimeMillis();

        // Run the algorithm a few times
        for(int i = 0; i < MAX_TRIES; i++) {
            // Define the population
            Population population = new Population(50, map, cities, true);

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

        // Show profiler information
        System.out.println("Found best route, took " + (System.currentTimeMillis() - time) + " ms.");

        // Get the fittest population
        final Population fittestPop = getFittestPopulation(populationList);

        // Debug the most efficient route
        System.out.println("The most efficient route is " +  fittestPop.getFittest().getFitness() + ".");
        debugger.debugSteden(this.map, fittestPop.getFittest().getCities());

        // Return the list of cities that define the most efficient path
        return fittestPop.getFittest().getCities();
    }

    /**
     * Get the fittest population.
     *
     * @param populationList List of populations.
     *
     * @return Fittest population.
     */
    private Population getFittestPopulation(ArrayList<Population> populationList){
        // Keep track of the
        int lowest = -1;
        int count = 0;

        // Loop through the populations to find the lowest
        for(int i = 0; i < populationList.size(); i++) {
            if(lowest == -1 || populationList.get(i).getFittest().getFitness() < lowest){
                lowest =  populationList.get(i).getFittest().getFitness();
                count = i;
            }
        }

        // Return the fittest population
        return populationList.get(count);
    }

    /**
     * Evolve the population.
     *
     * @param population Population to evolve.
     *
     * @return Evolved population.
     */
    private Population evolvePopulation(Population population){
        // Create a new population
        final Population evolved = new Population(population.getSize(), this.map, this.cities, false);

        // Evolve the fittest individual from the current population
        evolved.saveIndividual(0, population.getFittest());

        //
        for(int i = 1; i < population.getSize(); i++) {
            final Individual first = tournament(population);
            final Individual second = tournament(population);
            final Individual evolvedIndividual = crossover(first, second);
            evolved.saveIndividual(i, evolvedIndividual);
        }

        //
        for(int i = 1; i < evolved.getSize(); i++)
            mutate(evolved.getIndividual(i));

        // Return the evolved population
        return evolved;
    }

    /**
     * Do a population tournament, and get the fittest individual.
     * This creates a new population with a random combination of individuals from the given population, returns the fittest individual.
     *
     * @param population Population to do a tournament for.
     *
     * @return Fittest individual after a tournament.
     */
    private Individual tournament(Population population){
        // Create a tournament population
        final Population tournament = new Population(5, this.map, this.cities, false);

        // Take random individuals from the current population
        for(int i = 0; i < tournament.getSize(); i++) {
            final int r = (int) (Math.random() * population.getSize());
            tournament.saveIndividual(i, population.getPopulation()[r]);
        }

        // Return the fittest individual from the tournament population
        return tournament.getFittest();
    }

    /**
     * Cross over the cities from the first and second individuals into a new evolved individual.
     *
     * @param first First individual.
     * @param second Second individual.
     * @return Evolved individual.
     */
    private Individual crossover(Individual first, Individual second){
        // Create a new individual
        final Individual evolved = new Individual();

        // Loop through the list of cities
        for(int i = 0; i < cities.size(); i++)

            if(Math.random() <= 0.5)
                getCityFrom(evolved, first, second, i);
            else
                getCityFrom(evolved, second, first, i);

        // Return the evolved individual
        return evolved;
    }

    /**
     * Add a city to the evolved individual from the {@param first} or {@param second} individual at the given index.
     * Add's all cities that aren't in the evolved individual if the cities provided by {@param first} or {@param second} aren't new.
     *
     * @param evolved Evolved individual.
     * @param first First individual.
     * @param second Second individual.
     * @param i Index of the city.
     */
    private void getCityFrom(Individual evolved, Individual first, Individual second, int i){
        // Add the city from first at the given index to the evolved individual
        if(!evolved.hasCity(first.getCity(i)))
            evolved.addCity(first.getCity(i));

        // Add the city from second at the given index to the evolved individual
        else if(!evolved.hasCity(second.getCity(i)))
            evolved.addCity(second.getCity(i));

        // Add each city that isn't in the evolved individual from the list of cities
        else
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
        for(int i = 0; i < individual.getCities().size() - 1; i++) {
            // Flip the current and a random city with a 1.5% chance
            if(Math.random() < 0.015) {
                // Generate a random city index
                final int random = (int) (Math.random() * ((individual.getCities().size() - 1)));

                // Get both cities
                Stad first = individual.getCities().get(random);
                Stad second = individual.getCities().get(i);

                // Store both cities flipped
                individual.setCity(i, first);
                individual.setCity(random, second);
            }

            // Flip the current and followed city with a 7.5% chance
            if(Math.random() < 0.075) {
                // Get both cities
                Stad first = individual.getCities().get(i);
                Stad second = individual.getCities().get(i + 1);

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
    public String toString(){
        return "Genetic Algorithm";
    }

    @Override
    public void setDebugger(Debugger debugger) {
        this.debugger = debugger;
    }
}
