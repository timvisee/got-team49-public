package io.gameoftrades.student49.algorithm.genetic;

import io.gameoftrades.model.kaart.Stad;

import java.util.List;

/**
 * Population class.
 */
class Population {

    /**
     * List of individuals that are part of this population.
     */
    private Individual[] population;

    /**
     * Constructor.
     *
     * @param size Population size.
     * @param cities List of cities.
     * @param init True to initialize, false if not.
     */
    Population(int size, List<Stad> cities, boolean init){
        // Create a new individual array with the proper size
        this.population = new Individual[size];

        // Return if we don't need to initialize
        if(!init)
            return;

        // Fill the list of individuals
        for(int i = 0; i < size; i++)
            // Generate a new individual
            population[i] = new Individual(cities);
    }

    /**
     * Get the population.
     *
     * @return List of individuals.
     */
    Individual[] getPopulation(){
        return this.population;
    }

    /**
     * Get the individual at the given index.
     *
     * @param i Individual index.
     *
     * @return Individual.
     */
    Individual getIndividual(int i){
        return this.population[i];
    }

    /**
     * Save the given individual.
     *
     * @param i Individual index.
     * @param individual Individual to save.
     */
    void saveIndividual(int i, Individual individual){
        this.population[i] = individual;
    }

    /**
     * Get the fittest individual.
     *
     * @return Fittest individual.
     */
    Individual getFittest() {
        // Keep track of the fittest individual
        int fitness = -1;
        Individual individual = null;

        // Loop through the population
        for(Individual entry : this.population) {
            // Store the individual if it's fitter than the current
            if(fitness == -1 || entry.getFitness() < fitness) {
                fitness = entry.getFitness();
                individual = entry;
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
