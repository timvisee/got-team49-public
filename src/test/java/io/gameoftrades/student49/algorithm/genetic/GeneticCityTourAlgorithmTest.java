package io.gameoftrades.student49.algorithm.genetic;


import io.gameoftrades.model.Handelaar;
import io.gameoftrades.model.Wereld;
import io.gameoftrades.model.kaart.Stad;
import io.gameoftrades.student49.HandelaarImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class GeneticCityTourAlgorithmTest {

    private Handelaar handelaar;

    @Before
    public void init() {
        handelaar = new HandelaarImpl();
    }

    @Test
    public void stedenlijstMagGeenNullWaardenBevatten() {

        Wereld wereld = handelaar.nieuweWereldLader().laad("/kaarten/westeros-kaart.txt");

        boolean isNull = false;
        for(int i = 0; i < wereld.getSteden().size(); i++) {
            if(wereld.getSteden().get(i) == null) {
                isNull = true;
            }
        }
        assertFalse(isNull);
    }

    @Test
    public void stedenlijstMagGeenDuplicatenBevatten() {

        Wereld wereld = handelaar.nieuweWereldLader().laad("/kaarten/westeros-kaart.txt");

        ArrayList<Stad> cities = new ArrayList<>(wereld.getSteden());

        boolean duplicate = false;
        for(int i = 1; i < cities.size(); i++) {
            for(int j = i - 1; j >= 0; j--)
                if(cities.get(i).equals(cities.get(j)))
                    duplicate = true;
        }

        assertFalse(duplicate);
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
