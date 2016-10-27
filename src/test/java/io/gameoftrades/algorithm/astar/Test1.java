package io.gameoftrades.algorithm.astar;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import io.gameoftrades.model.Handelaar;
import io.gameoftrades.model.Wereld;
import io.gameoftrades.model.algoritme.SnelstePadAlgoritme;
import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Kaart;
import io.gameoftrades.model.lader.WereldLader;
import io.gameoftrades.student49.HandelaarImpl;
import io.gameoftrades.student49.PadImpl;
import io.gameoftrades.student49.algorithm.astar.FastestPathAlgorithm;

public class Test1 {

	// Bevat het resulterende pad de start en eind coördinaten?
	
	private Handelaar handelaar;
	
	@Before
	public void init() {
		handelaar = new HandelaarImpl();
	}
	
	@Test
	public void bevatStartEnEindCoordinaten() {
		
		WereldLader lader = handelaar.nieuweWereldLader();
		
		Wereld wereld = lader.laad("/kaarten/westeros-kaart.txt");
		
		Kaart kaart 	 = wereld.getKaart();
		Coordinaat start = wereld.getSteden().get(0).getCoordinaat();
		Coordinaat eind  = wereld.getSteden().get(1).getCoordinaat();
		
		SnelstePadAlgoritme spa = new FastestPathAlgorithm();
		PadImpl pad = (PadImpl) spa.bereken(kaart, start, eind);
		
		Coordinaat assertEind = pad.volg(start);
		
		pad = (PadImpl) pad.omgekeerd();
		
		Coordinaat assertStart = pad.volg(eind);
		
		assertEquals(start, assertStart);
		assertEquals(eind, assertEind);
	}

}
