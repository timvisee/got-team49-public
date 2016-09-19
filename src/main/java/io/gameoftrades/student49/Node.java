package io.gameoftrades.student49;

import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Terrein;

public class Node {

	private Terrein terrein;
	private int 	gCost;
	private double  hCost;
	private double  fCost;
	
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
}
