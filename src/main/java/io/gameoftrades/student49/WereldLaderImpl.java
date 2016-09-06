package io.gameoftrades.student49;

import io.gameoftrades.model.Wereld;
import io.gameoftrades.model.kaart.*;
import io.gameoftrades.model.lader.WereldLader;
import io.gameoftrades.model.markt.Markt;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WereldLaderImpl implements WereldLader {

    @Override
    public Wereld laad(String resource) {
        // Get the input stream for the given resource
        final InputStream in = this.getClass().getResourceAsStream(resource);

        // Make sure a valid resource is loaded
        if(in == null)
            throw new RuntimeException("The given resource file does not exist, the program will be terminated");

        // Create a scanner for the loaded resource
        final Scanner scanner = new Scanner(in);

        // Load a map
        final Kaart map = loadMap(scanner);

        // Load the towns
        final List<Stad> towns = loadTowns(scanner);

        // TODO: Load market
        final Markt market = loadMarket(scanner);

        // Return the world instance
        return new Wereld(map, towns, market);
    }

    /**
     * Load a map from a scanner instance.
     *
     * @param scanner Scanner.
     * @return Map instance.
     */
    private Kaart loadMap(Scanner scanner) {
        // Read the map size
        final String rawMapSize = scanner.next();

        // Make sure the raw map size has a valid format
        final Pattern mapSizePattern = Pattern.compile("^([0-9]+),([0-9]+)$");
        final Matcher mapSizeMatcher = mapSizePattern.matcher(rawMapSize);

        // Find the pattern and make sure it exists
        if(!mapSizeMatcher.find())
            throw new RuntimeException("The world being loaded has a malformed map size");

        // Get the map width and height
        final int mapWidth = Integer.parseInt(mapSizeMatcher.group(0));
        final int mapHeight = Integer.parseInt(mapSizeMatcher.group(1));

        // Create a map
        final Kaart map = new Kaart(mapWidth, mapHeight);

        // TODO: Load the map tiles (terrain), and add them to the map object

        // Return the loaded map
        return map;
    }

    /**
     * Load the list of towns from the given scanner.
     *
     * @param scanner Scanner.
     * @return List of towns.
     */
    private List<Stad> loadTowns(Scanner scanner) {
        // TODO: Load a list of towns here, return the list
        return null;
    }

    /**
     * Load the market from the given scanner.
     *
     * @param scanner Scanner.
     * @return Market.
     */
    private Markt loadMarket(Scanner scanner) {
        // TODO: Load the market and return it
        return null;
    }
}
