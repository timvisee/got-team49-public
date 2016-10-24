package io.gameoftrades.student49;

import io.gameoftrades.debug.Debuggable;
import io.gameoftrades.debug.Debugger;
import io.gameoftrades.model.algoritme.SnelstePadAlgoritme;
import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Kaart;
import io.gameoftrades.model.kaart.Pad;
import io.gameoftrades.model.kaart.Richting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SnelstePadAlgoritmeImpl implements SnelstePadAlgoritme, Debuggable {

    /**
     * List of open nodes for A*.
     */
    private ArrayList <Node> openList;

    /**
     * List of closed nodes for A*.
     */
	private ArrayList <Node> closedList;

    /**
     * Preferred route calculated by A*.
     */
    private ArrayList <Node> route;

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
     * Sorter comparator.
     */
    private Comparator<Node> sorter;

    /**
     * Path instance.
     */
	private PadImpl path;

    /**
     * Total cost to traverse the path that is found.
     */
    private int totalCost = 0;

	@Override
	public Pad bereken(Kaart kaart, Coordinaat start, Coordinaat eind) {
	    // Initialize the open, closed and route lists
        this.openList = new ArrayList<>();
		this.closedList = new ArrayList<>();
		this.route = new ArrayList<>();

        // Set the map, start and end coordinates
		this.map = kaart;
		this.start = start;
        this.end = eind;

        // Define the sorter (closure)
		sorter = (o1, o2) -> {
            if(o1.getfCost() > o2.getfCost())
            	return 1;
			else if(o1.getfCost() < o2.getfCost())
				return -1;
			else
				return 0;
        };

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
	public void runAlgorithm(boolean debug){
	    // Do not debug if no debugger instance is found
        if(debugger == null)
            debug = false;

		// Get the lowest cost node
		Node current = getLowest();

        // Remove the first entry from the open list, and add it to the closed list, since we're processing it
		openList.remove(0);
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
        while(current.getParent() != null){
            // Get the parent node
            Node parent = current.getParent();

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
     * @return Lowest node, or null if there is none.
     */
    // TODO: Don't sort the list (just get the lowest) sorting is slow
	public Node getLowest(){
	    // Sort the open list from lowest to highest
		Collections.sort(openList, sorter);

        // TODO: Return null if there's nothing in the open list

        // Return the first entry
		return openList.get(0);
	}

    /**
     * String representation of this class, which defines the algorithm name.
     *
     * @return Algorithm name.
     */
	public String toString(){
		return "A* Algorithm";
	}

	@Override
	public void setDebugger(Debugger debugger) {
		this.debugger = debugger;
	}
}
