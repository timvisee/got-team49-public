package io.gameoftrades.student49;

import java.util.ArrayList;

import io.gameoftrades.model.algoritme.SnelstePadAlgoritme;
import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Kaart;
import io.gameoftrades.model.kaart.Pad;

public class SnelstePadAlgoritmeImpl implements SnelstePadAlgoritme {

	private ArrayList<Node> open;
	private ArrayList<Node> closed;
	
	@Override
	public Pad bereken(Kaart kaart, Coordinaat start, Coordinaat eind) {
		
		open   = new ArrayList<>();
		closed = new ArrayList<>();
		
		return null;
	}

}
