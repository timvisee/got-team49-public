package io.gameoftrades.student49.algorithm.astarfix;

import io.gameoftrades.model.Handelaar;
import io.gameoftrades.model.Wereld;
import io.gameoftrades.model.algoritme.SnelstePadAlgoritme;
import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Kaart;
import io.gameoftrades.model.kaart.Richting;
import io.gameoftrades.model.lader.WereldLader;
import io.gameoftrades.student49.HandelaarImpl;
import io.gameoftrades.student49.PadImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class AStarAlgorithmTest {

    private Handelaar handelaar;

    @Before
    public void init() {
        handelaar = new HandelaarImpl();
    }

    // Bevat het resulterende pad de start en eind coördinaten?
    @Test
    public void bevatStartEnEindCoordinaten() {

        WereldLader lader = handelaar.nieuweWereldLader();

        Wereld wereld = lader.laad("/kaarten/westeros-kaart.txt");

        Kaart kaart = wereld.getKaart();
        Coordinaat start = wereld.getSteden().get(0).getCoordinaat();
        Coordinaat eind = wereld.getSteden().get(1).getCoordinaat();

        SnelstePadAlgoritme spa = new FastestPathAlgorithm();
        PadImpl pad = (PadImpl) spa.bereken(kaart, start, eind);

        Coordinaat assertEind = pad.volg(start);

        pad = (PadImpl) pad.omgekeerd();

        Coordinaat assertStart = pad.volg(eind);

        assertEquals(start, assertStart);
        assertEquals(eind, assertEind);
    }

    // Bevat het resulterende pad geen dubbele coördinaten?
    // Liggen alle coördinaten in het resulterende pad naast elkaar?
    @Test
    public void geenDubbeleCoordinatenEnGeenOvergeslagenTerrein() {

        WereldLader lader = handelaar.nieuweWereldLader();

        Wereld wereld = lader.laad("/kaarten/westeros-kaart.txt");

        Kaart kaart = wereld.getKaart();
        Coordinaat start = wereld.getSteden().get(0).getCoordinaat();
        Coordinaat eind = wereld.getSteden().get(1).getCoordinaat();

        SnelstePadAlgoritme spa = new FastestPathAlgorithm();
        PadImpl pad = (PadImpl) spa.bereken(kaart, start, eind);

        Richting[] directions = pad.getBewegingen();

        ArrayList<Coordinaat> list = new ArrayList<>();
        list.add(start);

        Coordinaat current = start;

        for(Richting r : directions) {

            current = current.naar(r);

            if(list.contains(current))
                fail("List already contains this coordinate. Cannot be double");

            list.add(current);
        }
    }

    // Word er geen pad gevonden als twee steden worden gescheiden door zee?
    @Test
    public void geenPadDoorZeeGevonden() {

        WereldLader lader = handelaar.nieuweWereldLader();

        Wereld wereld = lader.laad("/kaarten/testcases/route-door-zee.txt");

        Kaart kaart = wereld.getKaart();
        Coordinaat start = wereld.getSteden().get(0).getCoordinaat();
        Coordinaat eind = wereld.getSteden().get(1).getCoordinaat();

        SnelstePadAlgoritme spa = new FastestPathAlgorithm();
        PadImpl pad = (PadImpl) spa.bereken(kaart, start, eind);

        assertNull(pad);
    }

    // Is het resulterende pad werkelijk het snelste pad vs het kortste pad?
    @Test
    public void isWerkelijkHetSnelstePad() {

        WereldLader lader = handelaar.nieuweWereldLader();

        Wereld wereld = lader.laad("/kaarten/testcases/duidelijk-snelste-route.txt");

        Kaart kaart = wereld.getKaart();
        Coordinaat start = wereld.getSteden().get(0).getCoordinaat();
        Coordinaat eind = wereld.getSteden().get(1).getCoordinaat();

        SnelstePadAlgoritme spa = new FastestPathAlgorithm();
        PadImpl pad = (PadImpl) spa.bereken(kaart, start, eind);

        assertEquals(4, pad.getTotaleTijd());
    }
}
