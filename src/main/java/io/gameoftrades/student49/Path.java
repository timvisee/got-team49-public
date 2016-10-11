package io.gameoftrades.student49;

import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Pad;
import io.gameoftrades.model.kaart.Richting;

public class Path implements Pad {

	private Richting[] directions;
	private int routeCost;
	
	public Path(Richting[] directions, int routeCost) {
		this.directions = directions;
		this.routeCost  = routeCost;
	}
	
	@Override
	public int getTotaleTijd() {
		return this.routeCost;
	}

	@Override
	public Richting[] getBewegingen() {
		return this.directions;
	}

	@Override
	public Pad omgekeerd() {
		Richting[] reverse = new Richting[directions.length];
		for(int i = 0; i < directions.length; i++) {
			reverse[i] = directions[directions.length - (i + 1)].omgekeerd();
		}
		
		return new Path(reverse, this.routeCost);
	}

	@Override
	public Coordinaat volg(Coordinaat start) {
		Coordinaat c = start;
		for(Richting direction : this.directions) {
			c = c.naar(direction);
		}
		
		return c;
	}

}
