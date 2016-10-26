package io.gameoftrades.student49;

import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Pad;
import io.gameoftrades.model.kaart.Richting;

public class PadImpl implements Pad {

    /**
     * Array of directions defining the path.
     */
	private Richting[] directions;

    /**
     * Path instance with the reversed path.
     */
    private PadImpl reversed = null;

    /**
     * Total cost of the path.
     */
	private int totalCost;

    /**
     * Path implementation.
     *
     * @param directions Array of directions.
     * @param totalCost Total cost of the path.
     */
	public PadImpl(Richting[] directions, int totalCost) {
        // Set the directions and total cost
		this.directions = directions;
        this.totalCost = totalCost;
	}

	@Override
	public int getTotaleTijd() {
		return this.totalCost;
	}

	@Override
	public Richting[] getBewegingen() {
		return this.directions;
	}

	@Override
	public Pad omgekeerd() {
        // Return the reversed instance if there is any
        if(this.reversed != null)
            return this.reversed;

        // Create a new array to store the reversed path in
        Richting[] directionsReversed = new Richting[this.directions.length];

        // Fill the array with the reversed directions
		for(int i = 0; i < directions.length; i++)
			directionsReversed[i] = directions[directions.length - i - 1].omgekeerd();

        // Create a new path instance with the reversed directions
		this.reversed = new PadImpl(directionsReversed, this.totalCost);

        // Return the reversed path
        return this.reversed;
	}

	@Override
	public Coordinaat volg(Coordinaat start) {
        // Follow the directions
        for(Richting direction : directions)
            start = start.naar(direction);

		// Return the new coordinate
		return start;
	}
}
