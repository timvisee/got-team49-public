package io.gameoftrades.student49;

import io.gameoftrades.model.Wereld;
import io.gameoftrades.model.kaart.Kaart;
import io.gameoftrades.model.lader.WereldLader;

import java.io.InputStream;
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
        final Scanner scan = new Scanner(in);

        // Read the map size
        final String rawMapSize = scan.next();

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

        // TODO: Load towns

        // TODO: Load market

        // Return the world instance
        return new Wereld();
    }
}
