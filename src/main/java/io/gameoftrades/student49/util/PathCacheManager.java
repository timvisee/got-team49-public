package io.gameoftrades.student49.util;

import io.gameoftrades.model.algoritme.SnelstePadAlgoritme;
import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Kaart;
import io.gameoftrades.model.kaart.Pad;
import io.gameoftrades.model.kaart.Stad;
import io.gameoftrades.student49.CityPath;
import io.gameoftrades.student49.Path;
import io.gameoftrades.student49.algorithm.astar.FastestPathAlgorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * CityPath checker class.
 */
public class PathCacheManager {

    /**
     * List of paths between two coordinates.
     */
    private static List<Path> paths;

    /**
     * List of cityPaths for cities.
     */
    private static List<CityPath> cityPaths;

    /**
     * List of cities.
     */
    private static List<Stad> cities;

    /**
     * Map instance.
     */
    private static Kaart map;

    /**
     * Fastest path algorithm.
     */
    private static SnelstePadAlgoritme fastPathAlgorithm;

    // Static initializations
    static {
        // Initialize the fastest path algorithm
        fastPathAlgorithm = new FastestPathAlgorithm();

        // Initialize the lists of paths and city paths
        paths = new ArrayList<>();
        cityPaths = new ArrayList<>();
    }

    /**
     * Constructor.
     *
     * @param cities List of cities.
     * @param map    Map.
     */
    public static void init(List<Stad> cities, Kaart map) {
        // Set the list of cities and the map
        PathCacheManager.cities = cities;
        PathCacheManager.map = map;

        // Generate all common city paths in the background
        generateCityPaths();
    }

    /**
     * Generate common city paths in the background.
     */
    private static void generateCityPaths() {
        // Create a new thread to generate city paths in the background
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Show a debug message
                System.out.println("Generating common city paths in the background...");

                // Generate paths for all cities
                for(Stad startCity : PathCacheManager.cities)
                    PathCacheManager.cities.stream()
                        .filter(endCity -> !startCity.getCoordinaat().equals(endCity.getCoordinaat()))
                        .forEach(endCity -> getCityPath(startCity, endCity, false));

                // Show a debug message
                System.out.println(PathCacheManager.cityPaths.size() + " city paths generated!");
            }
        }).start();
    }

    /**
     * Calculate and get the path between the given two coordinates.
     * This will be skipped automatically if a path is already known.
     * <p>
     * This method allows you to specify whether reversed paths may be returned. This usually is less performance
     * intensive and may be useful to determine the length between two coordinates.
     *
     * @param start         Start coordinate.
     * @param end           End coordinate.
     * @param allowReversed True to allow reversed paths to be returned, false if not.
     */
    public static Path getPath(Coordinaat start, Coordinaat end, boolean allowReversed) {
        // Check whether any paths are known
        if(!paths.isEmpty()) {
            // Variable to store the reversed path if available
            Path reversed = null;

            // Loop through the list of paths and determine if we already know the path between the two given coordinates
            for(Path path : paths) {
                // Return the path if it's valid
                if(path.isFor(start, end))
                    return path;

                // Remember the path if it's a reversed version of the one we're trying to fetch
                if(reversed == null && path.isFor(end, start)) {
                    // Return the reversed if allowed
                    if(allowReversed)
                        return path;

                    // Set the reversed
                    reversed = path;
                }
            }

            // Correct the reversed path if we have any, to get the correct result
            if(reversed != null) {
                // Create a corrected path, and add it to the list
                final Path corrected = reversed.reverse();
                paths.add(corrected);

                // Return the corrected path
                return corrected;
            }
        }

        // Calculate the fastest path between the two given coordinates
        final Pad path = fastPathAlgorithm.bereken(PathCacheManager.map, start, end);

        // Create a path instance which defines the route, and add it to the list
        final Path route = new Path(start, end, path);
        paths.add(route);

        // Return the route/path
        return route;
    }

    /**
     * Calculate and get the path between the given two coordinates.
     * This will be skipped automatically if a path is already known.
     * <p>
     * This method won't return reversed paths.
     *
     * @param start Start coordinate.
     * @param end   End coordinate.
     */
    public static Path getPath(Coordinaat start, Coordinaat end) {
        return getPath(start, end);
    }

    /**
     * Calculate the path between two cities.
     * This will be skipped automatically if a path is already known.
     * <p>
     * This method allows you to specify whether reversed paths may be returned. This usually is less performance
     * intensive and may be useful to determine the length between two coordinates.
     *
     * @param start         Starting city.
     * @param end           Ending city.
     * @param allowReversed True to allow reversed paths to be returned, false if not.
     */
    public static CityPath getCityPath(Stad start, Stad end, boolean allowReversed) {
        // Check whether any paths are known
        if(!cityPaths.isEmpty()) {
            // Variable to store the reversed path if available
            CityPath reversed = null;

            // Loop through the list of paths and determine if we already know the path between the two given coordinates
            for(CityPath cityPath : cityPaths) {
                // Return the path if it's valid
                if(cityPath.isFor(start, end))
                    return cityPath;

                // Remember the path if it's a reversed version of the one we're trying to fetch
                if(reversed == null && cityPath.isFor(end, start)) {
                    // Return the path is reversed are allowed
                    if(allowReversed)
                        return cityPath;

                    // Set the reversed path
                    reversed = cityPath;
                }
            }

            // Correct the reversed path if we have any, to get the correct result
            if(reversed != null) {
                // Create a corrected path, and add it to the list
                final CityPath corrected = reversed.reverse();
                cityPaths.add(corrected);

                // Return the corrected path
                return corrected;
            }
        }

        // Get the normal (non-city) path instead
        final Path path = getPath(start.getCoordinaat(), end.getCoordinaat(), false);

        // Create a city path instance
        final CityPath cityPath = new CityPath(start, end, path);

        // Add the city path to the list of city paths
        cityPaths.add(cityPath);

        // Return the city path
        return cityPath;
    }

    /**
     * Calculate the path between two cities.
     * This will be skipped automatically if a path is already known.
     * <p>
     * This method won't return reversed paths.
     *
     * @param start Starting city.
     * @param end   Ending city.
     */
    public static CityPath getCityPath(Stad start, Stad end) {
        return getCityPath(start, end, false);
    }

    /**
     * Calculate the path between two cities.
     * This will be skipped automatically if a path is already known.
     * <p>
     * This method allows you to specify whether reversed paths may be returned. This usually is less performance
     * intensive and may be useful to determine the length between two coordinates.
     * <p>
     * Warning: This method is more expensive to run than {@see PathCacheManager#getCityPath(Stad, Stad)}, because this
     * method has to find the cities associated with the given coordinates. Thus, it's recommended to use
     * {@see PathCacheManager#getCityPath(Stad, Stad)} instead when possible.
     *
     * @param start         Coordinate of starting city.
     * @param end           Coordinate of ending city.
     * @param allowReversed True to allow reversed paths to be returned, false if not.
     * @throws RuntimeException Throws if no city was found for the given start and/or end coordinates.
     */
    public static CityPath getCityPath(Coordinaat start, Coordinaat end, boolean allowReversed) {
        // Get the cities at the given coordinates
        final Stad startCity = getCityAt(start);
        final Stad endCity = getCityAt(end);

        // Make sure any cities are found
        if(startCity == null || endCity == null)
            throw new RuntimeException("Invalid start or end coordinate, city could not be found.");

        // Find and return the city path
        return getCityPath(startCity, endCity, allowReversed);
    }

    /**
     * Calculate the path between two cities.
     * This will be skipped automatically if a path is already known.
     * <p>
     * This method won't return reversed paths.
     * <p>
     * Warning: This method is more expensive to run than {@see PathCacheManager#getCityPath(Stad, Stad)}, because this
     * method has to find the cities associated with the given coordinates. Thus, it's recommended to use
     * {@see PathCacheManager#getCityPath(Stad, Stad)} instead when possible.
     *
     * @param start Coordinate of starting city.
     * @param end   Coordinate of ending city.
     * @throws RuntimeException Throws if no city was found for the given start and/or end coordinates.
     */
    public static CityPath getCityPath(Coordinaat start, Coordinaat end) {
        return getCityPath(start, end, false);
    }

    /**
     * Calculate and get the path cost between the two given coordinates.
     *
     * @param first  First coordinate.
     * @param second Second coordinate.
     * @return Path cost.
     */
    public static int getPathCost(Coordinaat first, Coordinaat second) {
        return getPath(first, second, true).getLength();
    }

    /**
     * Calculate and get the path cost between the two given cities.
     *
     * @param first  First city.
     * @param second Second city.
     * @return Path cost.
     */
    public static int getCityPathCost(Stad first, Stad second) {
        return getPathCost(first.getCoordinaat(), second.getCoordinaat());
    }

    /**
     * Find a city at the given coordinate.
     *
     * @param coordinate Coordinate of the city to find.
     * @return City that was found, or null if no city was found.
     */
    private static Stad getCityAt(Coordinaat coordinate) {
        // Try to find the city in the list of cities
        for(Stad city : PathCacheManager.cities)
            if(city.getCoordinaat().equals(coordinate))
                return city;

        // Return null because we didn't find a city
        return null;
    }
}
