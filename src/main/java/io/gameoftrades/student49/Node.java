package io.gameoftrades.student49;

import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Terrein;

public class Node {

	private Terrein terrein;
	private Node 	parent;
	private Node 	child;
	private int 	gCost;
	private double  hCost;
	private double  fCost;
	
	public Node(Terrein terrein) {
		this.terrein = terrein;
		this.gCost = 0;
		this.hCost = 0.0;
		this.fCost = 0.0;
	}
	
	public Node(Terrein terrein, Coordinaat eind) {
		this.terrein = terrein;
		this.gCost = this.terrein.getTerreinType().getBewegingspunten();
		this.hCost = this.terrein.getCoordinaat().afstandTot(eind);
		this.fCost = this.gCost + this.hCost;
	}
	
	public Terrein getTerrein() {
		return this.terrein;
	}
	
	public int getGCost() {
		return this.gCost;
	}
	
	public double getHCost() {
		return this.hCost;
	}
	
	public double getFCost() {
		return this.fCost;
	}
	
	public void setParent(Node parent) {
		this.parent = parent;
	}
	
	public Node getParent() {
		return this.parent;
	}
	
	public void setChild(Node child) {
		this.child = child;
	}
	
	public Node getChild() {
		return this.child;
	}
}
