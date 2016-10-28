package io.gameoftrades.student49.algorithm.genetic;

import io.gameoftrades.model.Handelaar;
import io.gameoftrades.model.Wereld;
import io.gameoftrades.model.kaart.Stad;
import io.gameoftrades.student49.HandelaarImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class GeneticCityTourAlgorithmTest {

    private Handelaar handelaar;

    @Before
    public void init() {
        handelaar = new HandelaarImpl();
    }

    @Test
    public void stedenlijstMagGeenNullWaardenBevatten() {
        Wereld wereld = handelaar.nieuweWereldLader().laad("/kaarten/westeros-kaart.txt");

        for(int i = 0; i < wereld.getSteden().size(); i++)
            assertNotNull(wereld.getSteden().get(i));
    }

    @Test
    public void stedenlijstMagGeenDuplicatenBevatten() {
        Wereld wereld = handelaar.nieuweWereldLader().laad("/kaarten/westeros-kaart.txt");

        ArrayList<Stad> cities = new ArrayList<>(wereld.getSteden());

        for(int i = 1; i < cities.size(); i++)
            for(int j = i - 1; j >= 0; j--)
                assertTrue(cities.get(i).equals(cities.get(j)));
    }

    @Test
    public void stedenlijstIsEvenGrootAlsIngeladenSteden() {
        Wereld wereld = handelaar.nieuweWereldLader().laad("/kaarten/westeros-kaart.txt");
        assertEquals(wereld.getSteden().size(), 21);

        Wereld wereld2 = handelaar.nieuweWereldLader().laad("/kaarten/voorbeeld-kaart.txt");
        assertEquals(wereld2.getSteden().size(), 4);

        Wereld wereld3 = handelaar.nieuweWereldLader().laad("/kaarten/testcases/route-door-zee.txt");
        assertEquals(wereld3.getSteden().size(), 2);
    }
}
