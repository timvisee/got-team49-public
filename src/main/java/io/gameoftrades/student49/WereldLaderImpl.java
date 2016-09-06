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

    /**
     * Regex for the map size.
     */
    private static final String REGEX_MAP_SIZE = "^([0-9]+),([0-9]+)$";

    @Override
    public Wereld laad(String resource) {
        // Get the input stream for the given resource
        final InputStream in = this.getClass().getResourceAsStream(resource);

        // Make sure a valid resource is loaded
        if(in == null)
            throw new RuntimeException("The given resource file does not exist, the program will be terminated");

        // Create a scanner to load the world from
        final Scanner scanner = new Scanner(in);

        // Load the map
        final Kaart map = loadMap(scanner);

        // Load the towns
        final List<Stad> towns = loadTowns(scanner);

        // Load market
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
        final String rawMapSize = scanner.nextLine();

        // Make sure the raw map size has a valid format
        final Pattern mapSizePattern = Pattern.compile(REGEX_MAP_SIZE);
        final Matcher mapSizeMatcher = mapSizePattern.matcher(rawMapSize);

        // Find the pattern and make sure it exists
        if(!mapSizeMatcher.find())
            throw new RuntimeException("The world being loaded has a malformed map size");

        // Get the map width and height
        final int mapWidth = Integer.parseInt(mapSizeMatcher.group(1));
        final int mapHeight = Integer.parseInt(mapSizeMatcher.group(2));

        // Instantiate a map
        final Kaart map = new Kaart(mapWidth, mapHeight);

        // Loop through the map line by line
        for(int x = 0; x < mapHeight; x++) {
            // Read the current line
            final String mapLine = scanner.nextLine();

            // Loop through the characters
            for(int y = 0; y < mapWidth; y++)
                new Terrein(map, Coordinaat.op(x, y), TerreinType.fromLetter(mapLine.charAt(y)));
        }

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
