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
	
	private Coordinaat start;
	private Coordinaat eind;
	
	private int count = 0;
	
	@Override
	public Pad bereken(Kaart kaart, Coordinaat start, Coordinaat eind) {
		
		this.kaart = kaart;
		this.start = start;
		this.eind  = eind;
		
		open   = new ArrayList<>();
		closed = new ArrayList<>();
		
		Node startNode = new Node(kaart.getTerreinOp(start), null, start, eind);
		
		open.add(startNode);
		
		while(open.size() > 0) {
			
			//System.out.println("Loop count: " + (count++));
			
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
				
				System.out.println("---------- Path found ----------");
				
				System.out.println("Open list:");
				for(int i = 0; i < open.size(); i++) {
					System.out.println(open.get(i));
				}
				
				System.out.println("Closed list:");
				for(int i = 0; i < closed.size(); i++) {
					System.out.println(closed.get(i));
				}
				
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
				Node tempNode = new Node(kaart.kijk(currentNode.getTerrein(), r), currentNode, start, eind);
				if(!closedContains(tempNode) && !openContains(tempNode))
					open.add(tempNode);
			}
		}
		
		return null;
	}
	
	private boolean openContains(Node node) {
		for(int i = 0; i < open.size(); i++) {
			if(open.get(i).equals(node))
				return true;
		}
		return false;
	}
	
	private boolean closedContains(Node node) {
		for(int i = 0; i < closed.size(); i++) {
			if(closed.get(i).equals(node))
				return true;
		}
		return false;
	}
}
