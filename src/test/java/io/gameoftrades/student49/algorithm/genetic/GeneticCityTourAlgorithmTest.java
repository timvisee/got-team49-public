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
    public void StedenlijstMagGeenDuplicatenBevatten(){

        Wereld wereld = handelaar.nieuweWereldLader().laad("/kaarten/westeros-kaart.txt");

        StedenTourAlgoritme algorithm = handelaar.nieuwStedenTourAlgoritme();

        List<Stad> result = algorithm.bereken(wereld.getKaart(), wereld.getSteden());
        assertNotNull(result);
    }
}
