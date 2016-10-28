package io.gameoftrades.student49.algorithm.traderoute;

/**
 * Population class.
 */
class TradePopulation {

    /**
     * List of individuals that are part of this population.
     */
    private TradeIndividual[] population;

    /**
     * Maximum number of actions the individuals in this population can perform.
     */
    private int maxActions;

    /**
     * Generation index.
     */
    private int generation;

    /**
     * Constructor.
     *
     * @param algorithm  Trade route algorithm instance.
     * @param maxActions The maximum number of actions the individuals in this population can perform.
     * @param generation Generation index.
     * @param size       Population size.
     * @param init       True to initialize, false if not.
     */
    TradePopulation(TradeRouteAlgorithm algorithm, int maxActions, int generation, int size, boolean init) {
        // Create a new individual array with the proper size and set the maximum number of action
        this.population = new TradeIndividual[size];
        this.maxActions = maxActions;
        this.generation = generation;

        // Return if we don't need to initialize
        if(!init)
            return;

        // Fill the list of individuals
        for(int i = 0; i < size; i++)
            // Generate a new individual
            population[i] = new TradeIndividual(algorithm, maxActions, generation, true);
    }

    /**
     * Get the population.
     *
     * @return List of individuals.
     */
    TradeIndividual[] getPopulation() {
        return this.population;
    }

    /**
     * Get the individual at the given index.
     *
     * @param i Individual index.
     * @return Individual.
     */
    TradeIndividual getIndividual(int i) {
        return this.population[i];
    }

    /**
     * Save the given individual.
     *
     * @param i               Individual index.
     * @param tradeIndividual Individual to save.
     */
    void saveIndividual(int i, TradeIndividual tradeIndividual) {
        // Save the individual
        this.population[i] = tradeIndividual;

        // Set the generation
        tradeIndividual.setGeneration(this.generation);
    }

    /**
     * Get the fittest individual.
     *
     * @return Fittest individual.
     */
    TradeIndividual getFittest() {
        // Keep track of the fittest individual
        float fitness = -1;
        TradeIndividual tradeIndividual = null;

        // Loop through the population
        for(TradeIndividual entry : this.population) {
            // Store the individual if it's fitter than the current
            if(fitness < 0 || entry.getFitness() < fitness) {
                fitness = entry.getFitness();
                tradeIndividual = entry;
            }
        }

        // Return the fittest individual
        return tradeIndividual;
    }

    /**
     * Get the population size of this population.
     * This counts the number of individuals.
     *
     * @return Population size.
     */
    public int getSize() {
        return this.population.length;
    }

    /**
     * Get the maximum number of actions the individuals in this population can perform.
     *
     * @return Maximum number of actions.
     */
    int getMaxActions() {
        return this.maxActions;
    }

    /**
     * Generation index.
     *
     * @return Generation index.
     */
    int getGeneration() {
        return this.generation;
    }
}
