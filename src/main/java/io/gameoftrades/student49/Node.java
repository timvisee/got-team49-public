package io.gameoftrades.student49;

import java.text.DecimalFormat;

import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Terrein;

public class Node {
	
	public Terrein terrein;
	public Node parent;
	public double gCost;
	public double hCost;
	
	public Node(Terrein terrein, Node parent) {
		this.terrein = terrein;
		this.parent = parent;
		this.gCost = 0; // value of distance to starting node
		this.hCost = 0;
	}
	
	public Node(Terrein terrein, Node parent, Coordinaat start, Coordinaat eind) {

		this.terrein = terrein;
		this.parent = parent;
		this.gCost = 0; // value of distance to starting node
		this.hCost = this.terrein.getCoordinaat().afstandTot(eind);

		if(parent != null) {
			this.gCost  = this.parent.gCost;
			//this.gCost += this.terrein.getCoordinaat().afstandTot(start);
			this.gCost += this.terrein.getTerreinType().getBewegingspunten();
		}
	}
	
	public Terrein getTerrein() { return this.terrein; }
	public Node    getParent()  { return this.parent; }
	public double  fCost() 		{ return this.gCost + this.hCost; }
	
	public boolean equals(Node node) {
		return this.terrein.getCoordinaat() == node.getTerrein().getCoordinaat();
	}
	
	public String toString() {
		DecimalFormat df = new DecimalFormat("#0.00");
		return this.terrein.toString() +
				" g: " + df.format(this.gCost) +
				" h: " + df.format(this.hCost) +
				" f: " + df.format(this.gCost + this.hCost);
	}
}
