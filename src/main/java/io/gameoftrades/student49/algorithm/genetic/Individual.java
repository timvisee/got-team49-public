package io.gameoftrades.student49.algorithm.genetic;

import io.gameoftrades.model.kaart.Stad;
import io.gameoftrades.student49.util.PathCacheManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Individual class.
 */
class Individual {

    /**
     * List of cities for this individual.
     */
    private List<Stad> cities = new ArrayList<>();

    /**
     * Fitness of the individual.
     */
    private int fitness = 0;

    /**
     * Constructor.
     */
    Individual() {
    }

    /**
     * Constructor.
     *
     * @param cities List of cities to generate the individual from.
     */
    Individual(List<Stad> cities) {
        // Generate the individual based on the list of cities
        generate(cities);
    }

    /**
     * Generate a new individual based on a list of cities.
     *
     * @param cities List of cities.
     */
    private void generate(List<Stad> cities) {
        // Set the list of cities, and shuffle it
        Collections.shuffle(this.cities = new ArrayList<>(cities));
    }

    /**
     * Add a city to the individual.
     *
     * @param city City to add.
     */
    void addCity(Stad city) {
        this.cities.add(city);
    }

    /**
     * Get a city from the individual at the given index.
     *
     * @param i City index.
     * @return City.
     */
    Stad getCity(int i) {
        return this.cities.get(i);
    }

    /**
     * Set the city at the given index.
     *
     * @param i    Index to set the city at.
     * @param city City to set.
     */
    void setCity(int i, Stad city) {
        // Set the city
        this.cities.set(i, city);

        // Reset the fitness
        fitness = 0;
    }

    /**
     * Get the list of cities.
     *
     * @return List of cities.
     */
    List<Stad> getCities() {
        return this.cities;
    }

    /**
     * Check whether the individual has the given city.
     *
     * @param city City to check for.
     * @return True if the individual has the city, false if not.
     */
    boolean hasCity(Stad city) {
        // Loop through the list of cities
        //noinspection ForLoopReplaceableByForEach
        for(int i = 0, length = this.cities.size(); i < length; i++)
            if(this.cities.get(i).getCoordinaat().equals(city.getCoordinaat()))
                return true;

        // City not found, return false
        return false;
    }

    /**
     * Get the fitness of the individual.
     *
     * @return Fitness.
     */
    int getFitness() {
        // Loop through the list of cities, and define the fitness value, if the fitness isn't known
        if(this.fitness == 0)
            for(int i = 0, length = this.cities.size() - 1; i < length; i++)
                this.fitness += PathCacheManager.getCityPathCost(this.cities.get(i), this.cities.get(i + 1));

        // Return the fitness
        return this.fitness;
    }
}
