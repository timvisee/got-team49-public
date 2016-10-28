package io.gameoftrades.student49.algorithm.traderoute;

import io.gameoftrades.model.Handelaar;
import io.gameoftrades.model.Wereld;
import io.gameoftrades.model.kaart.Stad;
import io.gameoftrades.model.markt.Handel;
import io.gameoftrades.model.markt.Handelsplan;
import io.gameoftrades.model.markt.actie.HandelsPositie;
import io.gameoftrades.student49.HandelaarImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class TradeRouteAlgorithmTest {

    private Handelaar handelaar;

    /**
     * World.
     */
    private Wereld world;

    @Before
    public void init() {
        handelaar = new HandelaarImpl();

        // Load the world
        world = handelaar.nieuweWereldLader().laad("/kaarten/westeros-kaart.txt");
    }

    @Test
    public void hasAnyTradeOffers() {
        // Load the trade route algorithm
        final TradeRouteAlgorithm tradeRouteAlgorithm = new TradeRouteAlgorithm();

        // Calculate a trade route
        final Handelsplan handelsplan = tradeRouteAlgorithm.bereken(world, new HandelsPositie(world, world.getSteden().get(0), 150, 10, 100));

        // Get the offers
        final List<Handel> offers = new ArrayList<>();

        // Loop through the cities and get the offerings
        for(Stad city : world.getSteden())
            offers.addAll(tradeRouteAlgorithm.getOfferingsAt(city, world));

        // Make sure we've enough offers
        assertTrue(offers.size() > 0);
    }

    @Test
    public void hasAnyTradeDemands() {
        // Load the trade route algorithm
        final TradeRouteAlgorithm tradeRouteAlgorithm = new TradeRouteAlgorithm();

        // Calculate a trade route
        final Handelsplan handelsplan = tradeRouteAlgorithm.bereken(world, new HandelsPositie(world, world.getSteden().get(0), 150, 10, 100));

        // Get the demands
        final List<Handel> demands = new ArrayList<>();

        // Loop through the cities and get the offerings
        for(Stad city : world.getSteden())
            demands.addAll(tradeRouteAlgorithm.getRequestsAt(city, world));

        // Make sure we've enough offers
        assertTrue(demands.size() > 0);
    }

    @Test
    public void hasProperTradePlan() {
        // Load the trade route algorithm
        final TradeRouteAlgorithm tradeRouteAlgorithm = new TradeRouteAlgorithm();

        // Calculate a trade route
        Handelsplan handelsplan = tradeRouteAlgorithm.bereken(world, new HandelsPositie(world, world.getSteden().get(0), 150, 10, 100));

        // Make sure we've enough actions in the trade plan
        assertTrue(handelsplan.getActies().size() > 10);
    }
}
