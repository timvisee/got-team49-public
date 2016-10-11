package io.gameoftrades.student49;

import java.util.ArrayList;
import java.util.List;

import io.gameoftrades.debug.Debuggable;
import io.gameoftrades.debug.Debugger;
import io.gameoftrades.model.algoritme.StedenTourAlgoritme;
import io.gameoftrades.model.kaart.Kaart;
import io.gameoftrades.model.kaart.Stad;

public class SnelsteStedenTour implements StedenTourAlgoritme, Debuggable {

	@SuppressWarnings("unused")
	private Debugger debugger;
	
	private ArrayList<Stad> open;
	private ArrayList<Stad> closed;
	private ArrayList<Stad> route;
	
	private double cost;
	
	@Override
	public List<Stad> bereken(Kaart kaart, List<Stad> steden) {

		/*for(int i = 0; i < 10000; i++) {
			if(i % 50 == 0 && i > 0)
				System.out.print('\n');
			System.out.print((int) (Math.random() * steden.size()) + " ");
		}
		System.out.println('\n' + "steden.size(): " + steden.size());*/
		
		// Instanciate the ArrayLists.
		open   = new ArrayList<>();
		closed = new ArrayList<>();
		route  = new ArrayList<>();
		
		// Set the initial cost.
		cost = 0.0;
		
		// Add all cities to the `open` ArrayList.
		for(Stad stad : steden)
			open.add(stad);
		
		// Get a random city from the `open` ArrayList.
		Stad city = open.get((int) (Math.random() * open.size()));
		
		// Add the random city to the `closed` ArrayList.
		closed.add(city);

		// Remove the random city from the `open` ArrayList.
		open.remove(city);
		
		// As long as there are unvisited cities..
		while(open.size() > 0) {
			
			// Get the first city in line.
			Stad nearest = open.get(0);
			double cost  = nearest.getCoordinaat().afstandTot(city.getCoordinaat());
			
			// Loop trough all the remaining cities.
			for(int i = 1; i < open.size(); i++) {
				double temp = open.get(i).getCoordinaat().afstandTot(city.getCoordinaat());
				
				// If the cost of the current city is nearer, set this city to the nearest city.
				if(temp < cost) {
					nearest = open.get(i);
					cost 	= temp;
				}
			}
			
			// Add the nearest city to the `closed` ArrayList.
			closed.add(nearest);
			
			// Add the cost to the total cost.
			this.cost += cost;
			
			// Remove the nearest city from the `open` ArrayList.
			open.remove(nearest);
		}
		
		System.out.println("Route cost: " + this.cost);
		debugger.debugSteden(kaart, closed);
		
		return closed;
	}
	
	public String toString() {
		return "Nearest Neighbour Algoritme";
	}

	@Override
	public void setDebugger(Debugger debugger) {
		this.debugger = debugger;
	}

}
