package io.gameoftrades.student49.util;

import io.gameoftrades.model.algoritme.SnelstePadAlgoritme;
import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Kaart;
import io.gameoftrades.model.kaart.Pad;
import io.gameoftrades.model.kaart.Stad;
import io.gameoftrades.student49.Path;
import io.gameoftrades.student49.algorithm.astar.SnelstePadAlgoritmeImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Path checker class.
 */
@SuppressWarnings("WeakerAccess")
public class PathChecker {

    /**
     * List of paths for cities.
     */
    private static List<Path> paths;

    /**
     * List of relevant cities.
     */
    private List<Stad> cities;

    /**
     * Map instance.
     */
    private Kaart map;

    /**
     * Fastest path algorithm.
     */
    private SnelstePadAlgoritme fastPathAlgorithm;

    /**
     * Constructor.
     *
     * @param cities List of cities.
     * @param map Map.
     */
    public PathChecker(ArrayList <Stad> cities, Kaart map){
        // Set the list of cities and the map
        this.cities = cities;
        this.map = map;

        // Instantiate the fastest path algorithm
        this.fastPathAlgorithm = new SnelstePadAlgoritmeImpl();

        // Reset the lists of paths
        paths = new ArrayList<>();

        // Fill the paths array list
        buildCityPathsArrayList();
    }

    /**
     * Fill the city paths array list.
     */
    private void buildCityPathsArrayList(){
        // Loop through all cities by index
        for(int i = 0; i < cities.size(); i++)
            // Loop through all cities
            for(Stad city : cities)
                // Make sure the cities aren't equal to each other
                if(!cities.get(i).getCoordinaat().equals(city.getCoordinaat()))
                    // Calculate the paths for the two cities
                    calculatePaths(cities.get(i), city);
    }

    /**
     * Calculate the paths between two cities.
     *
     * @param first First city.
     * @param second Second city.
     */
    private void calculatePaths(Stad first, Stad second){
        // Get the coordinates of the both cities
        final Coordinaat firstCoordinate = first.getCoordinaat();
        final Coordinaat secondCoordinate = second.getCoordinaat();

        // Check whether any paths are found
        if(!paths.isEmpty())
            // Loop through the list of paths and determine if we already know a path for these two cities
            for(Path path : paths)
                // Return if the path is for the two given cities
                if((path.getStart().equals(firstCoordinate) && path.getEnd().equals(secondCoordinate)) ||
                    (path.getEnd().equals(firstCoordinate) && path.getStart().equals(secondCoordinate)))
                    return;

        // Calculate the path between the two cities.
        final Pad pad = fastPathAlgorithm.bereken(this.map, firstCoordinate, secondCoordinate);

        // Add the path to the list
        paths.add(new Path(first, second, pad.getTotaleTijd()));
    }

    /**
     * Get the cost for the path between the given two coordinates.
     *
     * @param first First coordinate.
     * @param second Second coordinate.
     *
     * @return Path cost between the given two coordinates.
     */
    public static int checkPathCost(Coordinaat first, Coordinaat second){
        // Loop through the list of paths
        for(Path path : paths)
            // Find the path for these two coordinates
            if((first.equals(path.getStart()) && second.equals(path.getEnd())) ||
                (first.equals(path.getEnd()) && second.equals(path.getStart())))
                // Return the path length
                return path.getLength();

        // TODO: Calculate the path here, so we can return the proper cost!

        // We don't know the path cost, so return zero
        return 0;
    }

    /**
     * Get the path between the two given coordinates.
     *
     * @param first First coordinate.
     * @param second Second coordinate.
     *
     * @return Path between the two coordinates.
     */
    public Path getPath(Coordinaat first, Coordinaat second) {
        // Loop through the list of paths
        for(Path path : paths)
            // Make sure the path is for the given two coordinates, return the path in that case
            if((first.equals(path.getStart()) && second.equals(path.getEnd())))
                return path;

        // We don't have a path, return null
        return null;
    }
}
