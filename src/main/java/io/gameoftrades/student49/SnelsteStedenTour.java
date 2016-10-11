package io.gameoftrades.student49;

import java.util.List;

import io.gameoftrades.debug.Debuggable;
import io.gameoftrades.debug.Debugger;
import io.gameoftrades.model.algoritme.StedenTourAlgoritme;
import io.gameoftrades.model.kaart.Kaart;
import io.gameoftrades.model.kaart.Stad;

public class SnelsteStedenTour implements StedenTourAlgoritme, Debuggable {

	@SuppressWarnings("unused")
	private Debugger debugger;
	
	@Override
	public List<Stad> bereken(Kaart kaart, List<Stad> steden) {
		// TODO Auto-generated method stub
		System.out.println("Memes.");
		return null;
	}
	
	public String toString() {
		return "Snelste StedenTour Algoritme";
	}

	@Override
	public void setDebugger(Debugger debugger) {
		this.debugger = debugger;
	}

}
