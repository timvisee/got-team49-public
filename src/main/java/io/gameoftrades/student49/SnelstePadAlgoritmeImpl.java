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

	private ArrayList <Node> closedList;
	private ArrayList <Node> openList;
	private ArrayList <Node> route;

    private Richting[]richting;

	private Kaart map;

	private Coordinaat eind;
	private Coordinaat start;

	private Debugger debugger;
	private Comparator <Node> sorter;

	private PadImpl pad;
	private int totalCost = 0;

	@Override
	public Pad bereken(Kaart kaart, Coordinaat start, Coordinaat eind) {

		this.closedList = new ArrayList<>();
		this.openList = new ArrayList<>();
		this.route = new ArrayList<>();

		this.map = kaart;
		this.eind = eind;
		this.start = start;

		sorter = (o1, o2) -> {
            if(o1.getfCost() > o2.getfCost())
            	return 1;
			else if(o1.getfCost() < o2.getfCost())
				return -1;
			else
				return 0;
        };

        //starting node, has no parent
		Node initial = new Node(kaart.getTerreinOp(start), null, eind);
		openList.add(initial);

		// run algorithm until it has found the end
		while(openList.size() >= 1) {
            runAlgorithm(true);
		}

        //clear all the lists and variables
        openList.clear();
		closedList.clear();
		route.clear();
		totalCost = 0;

		return pad;
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

		// get the lowest cost node
		Node current = checkLowest();

		openList.remove(0);
		closedList.add(current);

		// check for possible richtingen and throw them in an array
		Richting [] r = current.getTerrain().getMogelijkeRichtingen();

		// create nodes and put them in the list if they're not already in it
		for (int i = 0; i < r.length; i++) {
			Node node = new Node(map.kijk(current.getTerrain(), r[i]), current, eind);

			//if the node isnt already in the lists, add it to the open list.
			if(checkOpenList(node) && checkClosedList(node)) {
					openList.add(node);
			}

			//Only needed when its possible to go diagonal
			if(checkClosedList(node) && !checkOpenList(node)){}
		}

		//end code, you made it to a city!
		if(current.getTerrain().getCoordinaat().equals(eind)) {

			// check the total cost
			totalCost = (int) closedList.get(closedList.size() - 1).getgCost();

			// add current node, which was the last one, to the list
            route.add(current);

			// add all following nodes to the list, this will give you the shortest route in an array
			while(current.getParent() != null){
                Node parent = current.getParent();
                route.add(parent);
				current = parent;
			}

			richting = new Richting[route.size() - 1];
            Collections.reverse(route);

            for (int i = 0; i < route.size() - 1; i++) {
				richting[i] = Richting.tussen(route.get(i).getTerrain().getCoordinaat(), route.get(i + 1).getTerrain().getCoordinaat());
			}

			// show the path in the GUI upon starting the algorithm
			pad = new PadImpl(richting, totalCost);

			// Debug the path
			if(debug)
                debugger.debugPad(map, start, pad);
		}
	}

	/**
	 *
	 * @param node
	 * @return false if it is in the last, true if its not
	 */
	public boolean checkOpenList(Node node){

		for (int i = 0; i < openList.size(); i++) {
			if(openList.get(i).getTerrain().getCoordinaat().equals(node.getTerrain().getCoordinaat())){
				return false;
			}
		}
		return true;
	}

	public boolean checkClosedList(Node node){

		for (int i = 0; i < closedList.size(); i++) {
			if(closedList.get(i).getTerrain().getCoordinaat().equals(node.getTerrain().getCoordinaat())){
				return false;
			}
		}
		return true;
	}

	public Node checkLowest(){

		Collections.sort(openList, sorter);

		return openList.get(0);
	}

	public String toString(){
		return "A-Star Algorithm";
	}


	@Override
	public void setDebugger(Debugger debugger) {
		this.debugger = debugger;
	}
}
