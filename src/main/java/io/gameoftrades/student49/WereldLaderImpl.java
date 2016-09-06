package io.gameoftrades.student49;

import io.gameoftrades.model.Wereld;
import io.gameoftrades.model.lader.WereldLader;

import java.io.InputStream;
import java.util.Scanner;
import java.util.regex.Pattern;

public class WereldLaderImpl implements WereldLader {

    @Override
    public Wereld laad(String resource) {
        // Get the input stream for the given resource
        InputStream in = this.getClass().getResourceAsStream(resource);

        // Make sure a valid resource is loaded
        if(in == null)
            throw new RuntimeException("The given resource file does not exist, the program will be terminated");

        // Create a scanner for the loaded resource
        Scanner scan = new Scanner(in);

        // Read the map size
        String rawMapSize = scan.next();

        // Make sure the raw map size has a valid format
        Pattern mapSizePattern = Pattern.compile("");

        // Split the raw map size
        String[] splitMapSize = rawMapSize.split(",");

        // Make sure we've two split parts
        if(splitMapSize.length != 2)
            throw new RuntimeException("The world being loaded has a malformed map size");

        // TODO: Make sure the raw map dimensions contain numbers, possibly use a regex

        // Get the map width and height
        final int mapWidth = Integer.parseInt(splitMapSize[0]);
        final int mapHeight = Integer.parseInt(splitMapSize[1]);

        // TODO: Load map

        // TODO: Load towns

        // TODO: Load market

        // Return the world instance
        return new Wereld();
}
