package io.gameoftrades.student49;

import java.util.ArrayList;

import io.gameoftrades.debug.Debuggable;
import io.gameoftrades.debug.Debugger;
import io.gameoftrades.model.algoritme.SnelstePadAlgoritme;
import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Kaart;
import io.gameoftrades.model.kaart.Pad;
import io.gameoftrades.model.kaart.Richting;

public class SnelstePadAlgoritmeImpl implements SnelstePadAlgoritme, Debuggable {

	private Debugger debugger;
	
	private ArrayList<Node> open;
	private ArrayList<Node> closed;
	
	private int count = 0;
	
	@Override
	public Pad bereken(Kaart kaart, Coordinaat start, Coordinaat eind) {
		
		open   = new ArrayList<>();
		closed = new ArrayList<>();
		
		Node startNode = new Node(kaart.getTerreinOp(start));
		
		open.add(startNode);
		
		while(open.size() > 0) {
			
			Node currentNode = open.get(0);
			for(int i = 1; i < open.size(); i++) {
				Node tempNode = open.get(i);
				if(tempNode.fCost() < currentNode.fCost() || (tempNode.fCost() == currentNode.fCost() && tempNode.gCost < currentNode.gCost)) {
					currentNode = tempNode;
				}
			}
			
			closed.add(currentNode);
			open.remove(currentNode);
			
			if(currentNode.getTerrein().getCoordinaat().equals(eind)) {
				
				Node tempNode = currentNode;
				if(tempNode.getTerrein().getCoordinaat().equals(eind)) {
					
					System.out.println("---------- Possible path ----------");
					count = 0;
					while(!tempNode.getTerrein().getCoordinaat().equals(start)) {
						System.out.println((count++) + ": " + tempNode);
						tempNode = tempNode.getParent();
					}
					System.out.println(count + ": " + tempNode);
				}
				
				return null;
			}
			
			Richting[] richting = currentNode.getTerrein().getMogelijkeRichtingen();
			for(Richting r : richting) {
				Node tempNode = new Node(kaart.kijk(currentNode.getTerrein(), r), currentNode, eind);
				if(!contains(closed, tempNode) && !contains(open, tempNode))
					open.add(tempNode);
			}
		}
		
		return null;
	}
	
	private boolean contains(ArrayList<Node> list, Node node) {
		for(Node n : list) if(n.equals(node)) return true;
		return false;
	}
	
	public String toString() {
		return "Snelste Pad Algoritme";
	}

	@Override
	public void setDebugger(Debugger debugger) {
		this.debugger = debugger;
		
	}
}
