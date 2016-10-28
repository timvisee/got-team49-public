package io.gameoftrades.student49.algorithm.genetic;


import io.gameoftrades.model.Handelaar;
import io.gameoftrades.model.Wereld;
import io.gameoftrades.model.algoritme.StedenTourAlgoritme;
import io.gameoftrades.model.kaart.Stad;
import io.gameoftrades.student49.HandelaarImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GeneticCityTourAlgorithmTest {

    private Handelaar handelaar;

@Before
    public void init(){
        handelaar = new HandelaarImpl();}

    @Test
    public void stedenlijstMagGeenNullWaardenBevatten(){

        Wereld wereld = handelaar.nieuweWereldLader().laad("/kaarten/westeros-kaart.txt");

        StedenTourAlgoritme algorithm = handelaar.nieuwStedenTourAlgoritme();

        ArrayList<Stad> testList = new ArrayList<>(wereld.getSteden());

        testList.set(5, null);

        List<Stad> result = algorithm.bereken(wereld.getKaart(), testList);

        assertNotNull(result);
    }

    @Test
    public void stedenlijstMagGeenDuplicatenBevatten(){

        Wereld wereld = handelaar.nieuweWereldLader().laad("/kaarten/westeros-kaart.txt");

        StedenTourAlgoritme algorithm = handelaar.nieuwStedenTourAlgoritme();

        ArrayList<Stad> testList = new ArrayList<>(wereld.getSteden());
        Stad test = testList.get(3);
        testList.set(4, test);

        List<Stad> result = algorithm.bereken(wereld.getKaart(), testList);
    }

    @Test
    public void stedenlijstIsEvenGrootAlsIngeladenSteden(){

        Wereld wereld = handelaar.nieuweWereldLader().laad("/kaarten/westeros-kaart.txt");
        assertEquals(wereld.getSteden().size(), 21);

        Wereld wereld2 = handelaar.nieuweWereldLader().laad("/kaarten/voorbeeld-kaart.txt");
        assertEquals(wereld2.getSteden().size(), 4);

        Wereld wereld3 = handelaar.nieuweWereldLader().laad("/kaarten/route-door-zee.txt");
        assertEquals(wereld3.getSteden().size(), 2);
    }
}
