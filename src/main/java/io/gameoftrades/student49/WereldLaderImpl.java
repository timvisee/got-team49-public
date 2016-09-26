package io.gameoftrades.student49;

import io.gameoftrades.model.Wereld;
import io.gameoftrades.model.kaart.*;
import io.gameoftrades.model.lader.WereldLader;
import io.gameoftrades.model.markt.Handel;
import io.gameoftrades.model.markt.HandelType;
import io.gameoftrades.model.markt.Handelswaar;
import io.gameoftrades.model.markt.Markt;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WereldLaderImpl implements WereldLader {

    /**
     * Regex for the map size.
     */
    private static final String REGEX_MAP_SIZE = "^([0-9]+),([0-9 ]+)$";

    /**
     * Regex for the market.
     */
    private static final String REGEX_MARKET = "^([^,]+),(BIEDT|VRAAGT),([^,]+),([0-9. ]+)";

    /**
     * List of cities.
     */
    private List<Stad> cities = new ArrayList<>();

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

        // Load the cities
        loadCities(scanner, map);

        // Load market
        final Markt market = loadMarket(scanner);

        // Return the world instance
        return new Wereld(map, this.cities, market);
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
        final int mapHeight = Integer.parseInt(mapSizeMatcher.group(2).replaceAll("\\s+", ""));

        // Instantiate a map
        final Kaart map = new Kaart(mapWidth, mapHeight);

        // Loop through the map line by line
        for(int y = 0; y < mapHeight; y++) {
            // Read the current line
            final String mapLine = scanner.nextLine().replaceAll("\\s+", "");

            if(mapLine.length() != mapWidth)
                throw new IllegalArgumentException("The amount of characters exceeds the mapWidth");

            // Loop through the characters
            for(int x = 0; x < mapWidth; x++)
                new Terrein(map, Coordinaat.op(x, y), TerreinType.fromLetter(mapLine.charAt(x)));
        }

        // Return the loaded map
        return map;
    }

    /**
     * Load the list of cities from the given scanner.
     *
     * @param scanner Scanner.
     */
    private void loadCities(Scanner scanner, Kaart map) {
        // Get the city count
        final int cityCount = Integer.parseInt(scanner.nextLine().replaceAll("\\s+", ""));

        // Clear the list of cities
        cities.clear();

        // Read the cities
        for(int i = 0; i < cityCount; i++) {
            // Read the city coordinates and name
            String line = scanner.nextLine();

            // Split the data
            String[] cityData = line.split(",");

            // Get the X and Y coordinate
            int x = Integer.parseInt(cityData[0]);
            int y = Integer.parseInt(cityData[1]);

            // Make sure the coordinates are in bound
            if((x < 0 || x >= map.getBreedte()) || (y < 0 || y >= map.getHoogte()))
                throw new IllegalArgumentException("City coordinates exceed the mapWidth or mapHeight");

            // Create a new city object
            Stad city = new Stad(Coordinaat.op(x, y), cityData[2]);

            // Add the city object to the list
            cities.add(city);
        }
    }

    /**
     * Load the market from the given scanner.
     *
     * @param scanner Scanner.
     * @return Market.
     */
    private Markt loadMarket(Scanner scanner) {
        // Create a list of trades
        ArrayList<Handel> trades = new ArrayList<>();

        // Read market count
        final int marketCount = Integer.parseInt(scanner.nextLine().replaceAll("\\s+", ""));

        // Look for x amount of markets
        for(int i = 0; i < marketCount; i++) {
            // Read the current line
            String line = scanner.nextLine();

            // Make sure the market has a valid format
            final Pattern marketPattern = Pattern.compile(REGEX_MARKET);
            final Matcher marketMatcher = marketPattern.matcher(line);

            // Find the pattern and make sure it exists
            if(!marketMatcher.find()) {
                System.out.println(line);
                throw new RuntimeException("The market being loaded has a invalid format");
            }

            // Create a new trade and market
            Handel trade = new Handel(
                    findCity(marketMatcher.group(1)),
                    HandelType.valueOf(marketMatcher.group(2)),
                    new Handelswaar(marketMatcher.group(3)),
                    Integer.parseInt(marketMatcher.group(4).replaceAll("\\s+", "")));
            trades.add(trade);
        }

        // Return a new market instance
        return new Markt(trades);
    }

    /**
     * Helper method to find a city with the given name.
     *
     * @param name City name.
     * @return City instance, or null if no city was found.
     */
    private Stad findCity(String name) {
        // Loop through the list of cities
        for(Stad city : this.cities)
            if(city.getNaam().equals(name))
                return city;

        // No city found, return null
        return null;
    }
}
