package io.gameoftrades.student49;

import java.util.ArrayList;

import io.gameoftrades.model.algoritme.SnelstePadAlgoritme;
import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Kaart;
import io.gameoftrades.model.kaart.Pad;
import io.gameoftrades.model.kaart.Richting;

public class SnelstePadAlgoritmeImpl implements SnelstePadAlgoritme {

	private ArrayList<Node> open;
	private ArrayList<Node> closed;
	
	private Kaart kaart;
	
	@Override
	public Pad bereken(Kaart kaart, Coordinaat start, Coordinaat eind) {
		
		this.kaart = kaart;
		
		// Instantiate the Open and Closed lists.
		open   = new ArrayList<>();
		closed = new ArrayList<>();
		
		// Create a new start Node.
		Node initial = new Node(kaart.getTerreinOp(start));
		
		// Add the initial Node to the Open list.
		open.add(initial);
		
		// Begin recursion.
		calculate();
		
		return null;
	}
	
	private void calculate() {
		
		// Get the first node by default.
		Node current = open.get(0);
		
		// Loop trough the Open list.
		for(int i = 1; i < open.size(); i++) {
			// If there is a Node with a lower F cost, make it the the current Node.
			if(open.get(i).getFCost() < current.getFCost()) {
				current = open.get(i);
			}
		}
		
		// Switch the current Node to the Closed list.
		closed.add(current);
		open.remove(current);
		
		// For each of the 4 squares adjacent to the current square,
		Richting[] r = current.getTerrein().getMogelijkeRichtingen();
		
		for(int i = 0; i < r.length; i++) {
			
			Node temp = new Node(kaart.kijk(current.getTerrein(), r[i]));
			
			// If it is not walkable or is on the Closed list, ignore it.
			if(!closed.contains(temp)) {
				
				// If it isn't on the Open list, add it to the Open list.
				if(!open.contains(temp)) {
					open.add(temp);
					
					// Make the current square the parent of this square.
					temp.setParent(current);
					current.setChild(temp);
					
					// If the Open list contains the temp square already,
				} else if(open.contains(temp)) {
					// Check if temp is a better path, using G cost.
					
					if(temp.getGCost() < current.getGCost()) {
						//
					}
					
					// If it is on the open list already, check to see if this path to that square is better,
					// using G cost as the measure. A lower G cost means that this is a better path. If so,
					// change the parent of the square to the current square, and recalculate the G and F
					// scores of the square. If you are keeping your open list sorted by F score, you may
					// need to resort the list to account for the change.
				}
			}
		}
	}
}
