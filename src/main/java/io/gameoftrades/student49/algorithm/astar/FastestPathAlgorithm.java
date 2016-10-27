package io.gameoftrades.student49.algorithm.astar;

import io.gameoftrades.debug.Debuggable;
import io.gameoftrades.debug.Debugger;
import io.gameoftrades.model.algoritme.SnelstePadAlgoritme;
import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Kaart;
import io.gameoftrades.model.kaart.Pad;
import io.gameoftrades.model.kaart.Richting;
import io.gameoftrades.student49.PadImpl;

import java.util.ArrayList;
import java.util.Collections;

public class FastestPathAlgorithm implements SnelstePadAlgoritme, Debuggable {

    /**
     * List of open nodes for A*.
     */
    private ArrayList<Node> openList;

    /**
     * List of closed nodes for A*.
     */
    private ArrayList<Node> closedList;

    /**
     * Preferred route calculated by A*.
     */
    private ArrayList<Node> route;

    /**
     * Current map that is used for A*.
     */
    private Kaart map;

    /**
     * Start coordinate.
     */
    private Coordinaat start;

    /**
     * End coordinate.
     */
    private Coordinaat end;

    /**
     * Debugger instance.
     */
    private Debugger debugger;

    /**
     * CityPath instance.
     */
    private PadImpl path;

    /**
     * Total cost to traverse the path that is found.
     */
    private int totalCost = 0;

    @Override
    public synchronized Pad bereken(Kaart kaart, Coordinaat start, Coordinaat eind) {
        // Initialize the open, closed and route lists
        this.openList = new ArrayList<>();
        this.closedList = new ArrayList<>();
        this.route = new ArrayList<>();

        // Set the map, start and end coordinates
        this.map = kaart;
        this.start = start;
        this.end = eind;

        // Starting node (has no parent), add it to the open list
        Node initial = new Node(kaart.getTerreinOp(start), null, eind);
        openList.add(initial);

        // Run algorithm until it has reached the end coordinate
        while(openList.size() >= 1)
            // Run the algorithm
            runAlgorithm(true);

        // Clear the open, closed and route list
        openList.clear();
        closedList.clear();
        route.clear();

        // Reset the total route cost
        totalCost = 0;

        // Return the path that was found
        return path;
    }

    /**
     * Run the A* algorithm.
     *
     * @param debug True to debug the path, false otherwise.
     */
    public synchronized void runAlgorithm(boolean debug) {
        // Do not debug if no debugger instance is found
        if(debugger == null)
            debug = false;

        // Get the lowest cost node and consume it from the open list, add it to the closed list
        Node current = getLowestOpen(true);
        closedList.add(current);

        // Check for possible directions and throw them in an array
        Richting[] dirs = current.getTerrain().getMogelijkeRichtingen();

        // Create nodes and put them in the list if they're not already in it
        for(Richting dir : dirs) {
            // Get the current node
            final Node node = new Node(map.kijk(current.getTerrain(), dir), current, end);

            // If the node isn't already in the lists, add it to the open list.
            if(!openList.contains(node) && !closedList.contains(node))
                openList.add(node);
        }

        // Return if we didn't reach the target yet
        if(!current.getTerrain().getCoordinaat().equals(end))
            return;

        // Calculate the total cost
        totalCost = (int) closedList.get(closedList.size() - 1).getgCost();

        // Add current node (which was the last one) to the list
        route.add(current);

        // Add all following nodes to the list, this will give you the shortest route in an array
        while(current.getParent() != null) {
            // Get the parent node
            final Node parent = current.getParent();

            // Add the parent
            route.add(parent);

            // Set the parent as current
            current = parent;
        }

        // Create a new directions array with the proper size
        Richting[] directions = new Richting[route.size() - 1];

        // Reverse the route list
        Collections.reverse(route);

        // Construct the directions array based on the route
        for(int i = 0; i < route.size() - 1; i++)
            directions[i] = Richting.tussen(route.get(i).getTerrain().getCoordinaat(), route.get(i + 1).getTerrain().getCoordinaat());

        // Show the path in the GUI upon starting the algorithm
        path = new PadImpl(directions, totalCost);

        // Debug the path
        if(debug)
            debugger.debugPad(map, start, path);
    }

    /**
     * Get the lowest (cost) node from the open list.
     *
     * @param consume True to consume the lowest node, which removes the node from the open list. False to not consume.
     * @return Lowest node, or null if there is none.
     */
    private Node getLowestOpen(boolean consume) {
        // Make sure there's anything in the open list
        if(openList.isEmpty())
            return null;

        // Define a variable for the lowest node and it's cost
        Node lowest = null;
        double cost = -1;

        // Loop through the list of nodes to find the lowest one
        for(final Node current : this.openList) {
            // Continue if the cost isn't lower than the current selected
            if(cost != -1 && current.getfCost() >= cost)
                continue;

            // Set the lowest and it's cost
            lowest = current;
            cost = current.getfCost();
        }

        // Consume the node
        if(consume)
            this.openList.remove(lowest);

        // Return the lowest node
        return lowest;
    }

    /**
     * String representation of this class, which defines the algorithm name.
     *
     * @return Algorithm name.
     */
    @Override
    public String toString() {
        return "A* Algorithm (Cached)";
    }

    @Override
    public void setDebugger(Debugger debugger) {
        this.debugger = debugger;
    }
}
