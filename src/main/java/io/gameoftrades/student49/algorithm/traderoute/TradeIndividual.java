package io.gameoftrades.student49.algorithm.traderoute;

import io.gameoftrades.model.kaart.Stad;
import io.gameoftrades.student49.TradeRoute;
import io.gameoftrades.student49.util.PathCacheManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Individual class.
 */
@SuppressWarnings("WeakerAccess")
class TradeIndividual {

    /**
     * The action threshold.
     * Stop retrying to find fitting actions when the number of actions left is equal or lower to the given threshold.
     */
    private static final int ACTION_THRESHOLD = 4;

    /**
     * Maximum number of times to retry to find fitting actions.
     */
    private static final int ACTION_RETRY_MAX = 10;

    /**
     * Trade route algorithm instance.
     */
    private final TradeRouteAlgorithm algorithm;

    /**
     * List of trade actions.
     */
    private List<TradeAction> tradeActions = new ArrayList<>();

    /**
     * Maximum amount of actions this individual can do.
     */
    private int actionMax;

    /**
     * Action count.
     */
    private int actionCount = 0;

    /**
     * Fitness of the individual.
     */
    private float fitness = 0;

    /**
     * Generation index.
     */
    private int generation = 0;

    /**
     * Constructor.
     *
     * @param algorithm  Trade route algorithm instance.
     * @param actionMax  Maximum number of actions this individual can perform.
     * @param generation Generation index.
     * @param generate   True to generate, false to not.
     */
    TradeIndividual(TradeRouteAlgorithm algorithm, int actionMax, int generation, boolean generate) {
        // Set the algorithm instance and the maximum amount of actions
        this.algorithm = algorithm;
        this.actionMax = actionMax;
        this.generation = generation;

        // Generate
        if(generate)
            generate();
    }

    /**
     * Generate a new individual based on a list of trade routes.
     */
    void generate() {
        generate(-1);
    }

    /**
     * Generate a new individual based on a list of trade routes.
     *
     * @param amount Amount to generate. -1 to generate until full.
     */
    void generate(int amount) {
        // Get the list of trade routes
        final List<TradeRoute> tradeRoutes = this.getAlgorithm().tradeRoutes;

        // Determine the current city
        Stad currentCity = this.algorithm.startCity;
        if(this.tradeActions.size() > 0)
            currentCity = this.tradeActions.get(this.tradeActions.size() - 1).getCity();

        // Create a retry counter
        int retry = 0;

        // Fill the list of trade actions
        while(getActionsLeft() > ACTION_THRESHOLD && retry < ACTION_RETRY_MAX && amount != 0) {
            // Get a random trade route
            final TradeRoute tradeRoute = tradeRoutes.get(this.algorithm.random.nextInt(tradeRoutes.size()));

            // Calculate the cost to process these two actions
            int cost = PathCacheManager.getCityPathCost(currentCity, tradeRoute.getOfferCity()) +
                PathCacheManager.getCityPathCost(tradeRoute.getOfferCity(), tradeRoute.getDemandCity()) + 2;

            // Check how many actions we've left
            final int actionsLeft = getActionsLeft();

            // Make sure we've enough actions left
            if(cost > actionsLeft) {
                // Break the loop if we're below the action threshold
                if(actionsLeft <= ACTION_THRESHOLD)
                    break;

                // Increase the retry counter and continue to retry once more
                retry++;
                continue;
            }

            // Apply the actions cost
            this.actionCount += cost;

            // Create and add the trade actions
            tradeActions.add(new TradeAction(tradeRoute.getOfferCity(), true, tradeRoute.getGoods(), tradeRoute.getBuy(), 0f));
            tradeActions.add(new TradeAction(tradeRoute.getDemandCity(), false, tradeRoute.getGoods(), tradeRoute.getSell(), tradeRoute.getEfficiency()));

            // Decrease the amount
            amount--;
        }
    }

    /**
     * Get the trade route algorithm instance.
     *
     * @return Trade route algorithm instance.
     */
    public TradeRouteAlgorithm getAlgorithm() {
        return this.algorithm;
    }

    /**
     * Add a trade action to the individual.
     *
     * @param tradeAction Trade action to add.
     */
    void addTradeAction(TradeAction tradeAction) {
        // Add the action
        this.tradeActions.add(tradeAction);

        // Reset the fitness and action count
        fitness = 0;
        actionCount = 0;
    }

    /**
     * Get a city from the individual at the given index.
     *
     * @param i City index.
     * @return City.
     */
    TradeAction getTradeAction(int i) {
        return this.tradeActions.get(i);
    }

    /**
     * Set the city at the given index.
     *
     * @param i           Index to set the city at.
     * @param tradeAction Trade action to set.
     */
    void setTradeAction(int i, TradeAction tradeAction) {
        // Set the city
        this.tradeActions.set(i, tradeAction);

        // Reset the fitness and action count
        fitness = 0;
        actionCount = 0;
    }

    /**
     * Get the list of trade actions.
     *
     * @return List of trade actions.
     */
    List<TradeAction> getTradeActions() {
        return this.tradeActions;
    }

    /**
     * Get the maximum number of actions this individual can perform.
     *
     * @return Maximum number of actions.
     */
    int getMaxActions() {
        return this.actionMax;
    }

    int getActionCount() {
        // Calculate the action count if it's unknown
        if(this.actionCount == 0) {
            // Loop through the actions
            for(int i = 0, length = this.tradeActions.size(); i < length; i++) {
                // Get the previous city
                Stad previousCity = i > 0 ? this.tradeActions.get(i - 1).getCity() : this.algorithm.startCity;

                // Get the target city
                final Stad targetCity = this.tradeActions.get(i).getCity();

                // Determine the movement cost and add it to the action count
                actionCount += PathCacheManager.getCityPathCost(previousCity, targetCity);

                // Increase the action count by one for the trade action
                this.actionCount += 1;
            }
        }

        // Return the action count
        return this.actionCount;
    }

    /**
     * Check how many actions we've left.
     *
     * @return Amount of actions left.
     */
    int getActionsLeft() {
        return this.actionMax - getActionCount();
    }

    /**
     * Get the fitness of the individual.
     *
     * @return Fitness.
     */
    float getFitness() {
        // Loop through the list of cities, and define the fitness value, if the fitness isn't known
        if(this.fitness == 0) {
            // Get the highest known efficiency
            final float bestEfficiency = this.getAlgorithm().bestEfficiency;

            // Set the fitness to the maximum value based on the highest efficiency and the maximum number of actions used as cost
            this.fitness += bestEfficiency * getMaxActions();

            // Loop through the trade actions and determine the fitness
            for(int i = 0, length = this.tradeActions.size(); i < length; i++) {
                // Get the previous city
                Stad previousCity = i > 0 ? this.tradeActions.get(i - 1).getCity() : this.algorithm.startCity;

                // Get the target city
                final Stad targetCity = this.tradeActions.get(i).getCity();

                // Determine the movement cost
                final int cost = PathCacheManager.getCityPathCost(previousCity, targetCity);

                // Get the local efficiency value
                final float efficiency = this.tradeActions.get(i).getEfficiency();

                // Calculate the movement cost based on the best efficiency
//                this.fitness += bestEfficiency * (float) cost;

                // Subtract the movement cost base on the local efficiency
                this.fitness -= efficiency * (float) cost;

                // Add one for the trade action itself
//                this.fitness += bestEfficiency;
            }
        }

        // Return the fitness
        return this.fitness;
    }

    /**
     * Remove all actions that won't fit in the maximum number of actions.
     */
    void removeActionsOutOfRange() {
        // Keep looping until all actions that are out of range are removed
        while(getActionsLeft() < 0) {
            // Remove the last two actions
            this.tradeActions.remove(tradeActions.size() - 1);
            this.tradeActions.remove(tradeActions.size() - 1);

            // Reset the action and fitness value
            actionCount = 0;
            fitness = 0;
        }
    }

    /**
     * Generation index.
     *
     * @return Generation index.
     */
    int getGeneration() {
        return this.generation;
    }

    /**
     * Set the individual's generation index.
     *
     * @param generation Generation index.
     */
    void setGeneration(int generation) {
        this.generation = generation;
    }
}
