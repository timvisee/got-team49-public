package io.gameoftrades.student49;

import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Terrein;

public class Node {

    private Terrein terrain;
    private Node parent;

    private double gCost;
    private double hCost;

    private Coordinaat coordinaat;
    private Coordinaat end;

    public Node(Terrein t, Node parent, Coordinaat end){

        this.terrain = t;
        this.parent = parent;
        this.end = end;

        coordinaat = t.getCoordinaat();

        if(parent != null)
        gCost = parent.getgCost() + t.getTerreinType().getBewegingspunten();
        else
            gCost = 0;

        hCost = coordinaat.afstandTot(end);
    }

    public Node getParent(){
        return parent;
    }

    public Terrein getTerrain(){
        return terrain;
    }

    public double getgCost(){
        return gCost;
    }

    public double gethCost(){
        return hCost;
    }

    public double getfCost(){
        return hCost + gCost;
    }

    @Override
    public boolean equals(Object other) {
        // Return if the other instance is the same
        if(super.equals(other))
            return true;

        // Make sure the other object is a node instance
        if(!(other instanceof Node))
            return false;

        // Compare the coordinates, return the result
        return this.coordinaat.equals(((Node) other).getTerrain().getCoordinaat());
    }
}
