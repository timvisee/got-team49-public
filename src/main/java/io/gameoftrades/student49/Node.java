package io.gameoftrades.student49;

import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Terrein;

@SuppressWarnings("WeakerAccess")
public class Node {

    /**
     * Terrain tile.
     */
    private Terrein terrain;

    /**
     * Parent node.
     */
    private Node parent;

    /**
     * G cost value.
     */
    private double gCost = 0;

    /**
     * H cost value.
     */
    private double hCost;

    /**
     * Node coordinate.
     */
    private Coordinaat coordinaat;

    /**
     * Constructor.
     *
     * @param terrain Terrain.
     * @param parent Parent node.
     */
    public Node(Terrein terrain, Node parent, Coordinaat end){
        // Set the properties
        this.terrain = terrain;
        this.parent = parent;

        // Get the coordinate
        this.coordinaat = terrain.getCoordinaat();

        // Calculate the g cost
        if(parent != null)
            this.gCost = parent.getgCost() + terrain.getTerreinType().getBewegingspunten();

        // Calculate the h cost
        this.hCost = coordinaat.afstandTot(end);
    }

    /**
     * Get the parent node if there is any.
     *
     * @return Parent node or null.
     */
    public Node getParent(){
        return parent;
    }

    /**
     * Get the terrain type.
     *
     * @return Terrain.
     */
    public Terrein getTerrain(){
        return terrain;
    }

    /**
     * Get the g cost.
     *
     * @return G cost value.
     */
    public double getgCost(){
        return this.gCost;
    }

    /**
     * Get the h cost.
     *
     * @return H cost value.
     */
    public double gethCost(){
        return this.hCost;
    }

    /**
     * Get the f cost.
     *
     * @return F cost value.
     */
    public double getfCost(){
        return gethCost() + getgCost();
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
