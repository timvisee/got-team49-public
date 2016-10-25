package io.gameoftrades.student49;

import io.gameoftrades.model.kaart.Kaart;
import io.gameoftrades.model.kaart.Stad;

import java.util.List;

/**
 * Population class.
 */
public class Population {

    /**
     * List of individuals that are part of this population.
     */
    private Individual[] population;

    /**
     * Constructor.
     *
     * @param size Population size.
     * @param map Relevant map.
     * @param cities List of cities.
     * @param init True to initialize, false if not.
     */
    public Population(int size, Kaart map, List <Stad> cities, boolean init){
        // Create a new individual array with the proper size
        population = new Individual[size];

        // Return if we don't need to initialize
        if(!init)
            return;

        // Fill the list of individuals
        for(int i = 0; i < population.length; i++) {
            // Create a new individual instance
            Individual individual = new Individual();

            // Generate the individual based on the list of cities
            individual.generateIndividual(cities);

            // Set the individual
            population[i] = individual;
        }
    }

    /**
     * Get the population.
     *
     * @return List of individuals.
     */
    public Individual[] getPopulation(){
        return this.population;
    }

    /**
     * Get the individual at the given index.
     *
     * @param i Individual index.
     *
     * @return Individual.
     */
    public Individual getIndividual(int i){
        return this.population[i];
    }

    /**
     * Save the given individual.
     *
     * @param i Individual index.
     * @param individual Individual to save.
     */
    public void saveIndividual(int i, Individual individual){
        this.population[i] = individual;
    }

    /**
     * Get the fittest individual.
     *
     * @return Fittest individual.
     */
    public Individual getFittest() {
        // Keep track of the fittest individual
        int min = -1;
        Individual individual = null;

        // Loop through the population
        for(int i = 0; i < population.length; i++) {
            // Store the individual if it's fitter than the current
            if(min == -1 || population[i].getFitness() < min){
                min = population[i].getFitness();
                individual = population[i];
            }
        }

        // Return the fittest individual
        return individual;
    }

    /**
     * Get the population size of this population.
     * This counts the number of individuals.
     *
     * @return Population size.
     */
    public int getSize(){
        return this.population.length;
    }
}
