package io.gameoftrades.student49.algorithm.traderoute;

import com.timvisee.voxeltex.util.swing.ProgressDialog;
import io.gameoftrades.debug.Debuggable;
import io.gameoftrades.debug.Debugger;
import io.gameoftrades.model.Wereld;
import io.gameoftrades.model.algoritme.HandelsplanAlgoritme;
import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Kaart;
import io.gameoftrades.model.kaart.Stad;
import io.gameoftrades.model.markt.Handel;
import io.gameoftrades.model.markt.HandelType;
import io.gameoftrades.model.markt.Handelsplan;
import io.gameoftrades.model.markt.actie.*;
import io.gameoftrades.student49.Path;
import io.gameoftrades.student49.TradeRoute;
import io.gameoftrades.student49.util.PathCacheManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TradeRouteAlgorithm implements HandelsplanAlgoritme, Debuggable {

    /**
     * Number of evolutions.
     */
    private final static int EVOLUTION_COUNT = 20;

    /**
     * Population size.
     */
    private static final int POPULATION_SIZE = 75;

    /**
     * Population tournament size.
     */
    private static final int POPULATION_TOURNAMENT_SIZE = 8;

    /**
     * List of trade routes.
     */
    List<TradeRoute> tradeRoutes;

    /**
     * The city the trade is starting in.
     */
    Stad startCity;

    /**
     * Best possible efficiency value.
     */
    float bestEfficiency = 0f;

    /**
     * Random instance.
     */
    Random random;

    /**
     * Trade position.
     */
    private HandelsPositie tradePosition;

    /**
     * Goods we're buying (requesting).
     */
    private List<Handel> buying;

    /**
     * Goods we're selling (offering).
     */
    private List<Handel> selling;

    /**
     * Debugger instance.
     */
    private Debugger debugger;

    @Override
    public Handelsplan bereken(Wereld world, HandelsPositie tradePosition) {
        // Find the frame to attach the progress dialog to
        Frame mainFrame = Frame.getFrames().length > 0 ? Frame.getFrames()[0] : null;

        // Create a progress dialog instance
        final ProgressDialog progress = new ProgressDialog(mainFrame, "Processing...", false, "Starting...", true);

        // Set the random instance
        this.random = new Random();

        // Set the trade position and start city
        this.tradePosition = tradePosition;
        this.startCity = tradePosition.getStad();

        // Instantiate the list of actions and trade routes
        List<Actie> actions = new ArrayList<>();
        tradeRoutes = new ArrayList<>();

        // Get the city buying and selling offers
        buying = getRequestsAt(tradePosition.getCoordinaat(), world);
        selling = getOfferingsAt(tradePosition.getCoordinaat(), world);

        // Get the list of requested and offered goods in this world
        final ArrayList<Handel> requestGoods = new ArrayList<>(world.getMarkt().getVraag());
        final ArrayList<Handel> offerGoods = new ArrayList<>(world.getMarkt().getAanbod());

        // Create a list of trade routes, based on the offered and requested goods in all available cities
        for(Handel offer : offerGoods) {
            for(Handel request : requestGoods) {
                if(!request.getHandelswaar().equals(offer.getHandelswaar()))
                    continue;

                // Create the trade route
                final TradeRoute tradeRoute = new TradeRoute(PathCacheManager.getCityPath(offer.getStad().getCoordinaat(),
                    request.getStad().getCoordinaat()), offer.getHandelswaar(),
                    offer.getPrijs(), request.getPrijs(), tradePosition.getCoordinaat());

                // Add the trade route to the list
                tradeRoutes.add(tradeRoute);

                // Get the efficiency value
                final float efficiency = tradeRoute.getEfficiency();

                // Update the best efficiency
                this.bestEfficiency = Math.max(efficiency, this.bestEfficiency);
            }
        }

        // Get the map
        final Kaart map = world.getKaart();

        // Print the best possible efficiency
        System.out.println("Best trade route efficiency: " + bestEfficiency);

        // Print a cost table, to make debugging easier
        printTradeRoutes();

        // Create a population list
        final ArrayList<TradePopulation> populationList = new ArrayList<>();

        // Store the current time, used for profiling
        final long time = System.currentTimeMillis();

        // Setup the progress bar
        progress.setProgressMax(EVOLUTION_COUNT + 1);
        progress.setProgressValue(0);
        progress.setShowProgress(true);

        // Run the algorithm a few times
        for(int i = 0; i < EVOLUTION_COUNT; i++) {
            // Update the progress
            progress.setProgressValue(i);
            progress.setStatus("Simulating natural selection, evolution " + (i + 1) + "...");

            // Define the population
            TradePopulation population = new TradePopulation(this, tradePosition.getMaxActie(), 0, POPULATION_SIZE, true);

            // Keep track of the fittest individual and generation count
            int sameFitness = 0;
            float fittest = -1;
            int generationCount = 0;

            // Find a maximum of the population that have the same fitness
            while(sameFitness <= POPULATION_SIZE) {
                // Create a new generation
                generationCount++;
                population = evolvePopulation(population);

                // Store the individual if fitter than the current
                if(fittest < 0 || population.getFittest().getFitness() < fittest) {
                    fittest = population.getFittest().getFitness();
                    sameFitness = 0;
                }

                // Increase the sameFitness variable if we encounter the same individual
                if(fittest == population.getFittest().getFitness())
                    sameFitness++;
            }

            // Store the current population
            populationList.add(population);

            // Show a simple debug message
            System.out.println("Try " + (i + 1) + ". Evolved through " + generationCount + " generations. Best fitness: " + population.getFittest().getFitness());
        }

        // Update the progress
        progress.setProgressValue(EVOLUTION_COUNT);
        progress.setStatus("Finding most efficient individual...");

        // Show profiler information
        System.out.println("Determined optimal route, took " + (System.currentTimeMillis() - time) + " ms.");

        // Get the fittest population
        final TradePopulation fittestPopulation = getFittestPopulation(populationList);

        // Find the fittest individual
        final TradeIndividual fittest = fittestPopulation.getFittest();

        // Update the progress and dispose the dialog
        progress.setProgressValue(progress.getProgressMax());
        progress.dispose();

        // Create a variable with the previous city
        Stad prevCity = this.startCity;

        // Create a list of actions
        for(TradeAction tradeAction : fittest.getTradeActions()) {
            // Get the target city
            final Stad city = tradeAction.getCity();

            // Walk to the city if we aren't there yet
            if(!tradeAction.getCity().equals(prevCity)) {
                // Get the path between the two cities
                Path path = PathCacheManager.getCityPath(prevCity, city);

                // Add all movement actions
                actions.addAll(
                    new BeweegActie(map, prevCity, city, path.getPath())
                        .naarNavigatieActies()
                );

                // Set the previous city
                prevCity = city;
            }

            // Create the buy/sell action
            if(tradeAction.isBuy())
                actions.add(new KoopActie(new Handel(city, HandelType.BIEDT, tradeAction.getGoodType(), tradeAction.getPrice())));
            else
                actions.add(new VerkoopActie(new Handel(city, HandelType.VRAAGT, tradeAction.getGoodType(), tradeAction.getPrice())));
        }

        // Show the number of actions that are used for the determined path
        if(fittest.getActionsLeft() == 0)
            System.out.println("Number of actions in route: " + fittest.getActionCount() + " (all actions used)");
        else
            System.out.println("Number of actions in route: " + fittest.getActionCount() + " (" + fittest.getActionsLeft() + " unused actions left)");

        // Return a trade plan with the list of actions to perform
        return new Handelsplan(actions);
    }

    /**
     * Print the cost table, to make debugging easier.
     */
    private void printTradeRoutes() {
        // Print city information and the table header
        System.out.printf("\n%-5s %-5s %-5s %-5s\n\n", "City: " + tradePosition.getStad().getNaam(), "|  Money: " + tradePosition.getKapitaal(), "|  Actions: " + tradePosition.getMaxActie(), "|  Bag capacity: " + tradePosition.getRuimte());
        System.out.printf("%-6s %-30s %-15s %-15s %-5s %-7s %-9s %-15s %-8s %-11s %-10s\n", "Route", "Product", "From", "To", "Buy",
            "Profit", "Pathcost", "Efficiency (1)", "Max Buy", "Max Profit", "Efficiency (Max)");
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------");

        // Loop through the trade routes
        for(int i = 0; i < tradeRoutes.size(); i++) {
            // Determine the maximum profit
            double maxProfit;
            if(tradePosition.getKapitaal() / tradeRoutes.get(i).getBuy() > tradePosition.getRuimte())
                maxProfit = tradePosition.getRuimte() * tradeRoutes.get(i).getProfit();
            else
                maxProfit = (tradePosition.getKapitaal() / tradeRoutes.get(i).getBuy()) * tradeRoutes.get(i).getProfit();

            // Print the trade route data
            System.out.printf("%-6d %-30s %-15s %-15s %-5d %-7d %-9d %-15f %-8d %-11d %-10f\n", i,
                tradeRoutes.get(i).getGoods().getNaam(),
                tradeRoutes.get(i).getOfferCity().getNaam(),
                tradeRoutes.get(i).getDemandCity().getNaam(),
                tradeRoutes.get(i).getBuy(),
                tradeRoutes.get(i).getProfit(),
                tradeRoutes.get(i).getPathCost(),
                tradeRoutes.get(i).getEfficiency(),
                tradePosition.getKapitaal() / tradeRoutes.get(i).getBuy() > tradePosition.getRuimte() ? tradePosition.getRuimte() : tradePosition.getKapitaal() / tradeRoutes.get(i).getBuy(),
                (int) maxProfit,
                maxProfit / tradeRoutes.get(i).getPathCost());
        }

        // Finish the table
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------\n");
    }

    /**
     * Print information about the current city.
     */
    public void printCurrentCityInformation() {
        // Define the format to print in
        final String format = "%-15s %-30s %-15d\n";

        // Print the name of the current city
        System.out.println(tradePosition.getStad().getNaam());

        // Print all selling goods
        for(Handel entry : selling)
            System.out.printf(format, entry.getHandelType(), entry.getHandelswaar(), entry.getPrijs());

        // Print all buying goods
        for(Handel entry : buying)
            System.out.printf(format, entry.getHandelType(), entry.getHandelswaar(), entry.getPrijs());
    }

    /**
     * Get the offerings in the given city.
     *
     * @param city  City to get the offerings in.
     * @param world World the city is in.
     * @return List of offerings.
     */
    public ArrayList<Handel> getOfferingsAt(Stad city, Wereld world) {
        return getOfferingsAt(city.getCoordinaat(), world);
    }

    /**
     * Get the offerings at the given coordinate.
     *
     * @param coordinate Coordinate to get the offerings for.
     * @param world      World the coordinate is in.
     * @return List of offerings.
     */
    public ArrayList<Handel> getOfferingsAt(Coordinaat coordinate, Wereld world) {
        // Create a list to put the offerings in
        final ArrayList<Handel> offerings = new ArrayList<>();

        // Loop through the market offerings and add them to the offerings list if the coordinate matches the given
        for(int i = 0; i < world.getMarkt().getAanbod().size(); i++)
            if(coordinate.equals(world.getMarkt().getAanbod().get(i).getStad().getCoordinaat()))
                offerings.add(world.getMarkt().getAanbod().get(i));

        // Return the offerings
        return offerings;
    }

    /**
     * Get the requests in the given city.
     *
     * @param city  City to get the requests in.
     * @param world World the city is in.
     * @return List of requests.
     */
    public ArrayList<Handel> getRequestsAt(Stad city, Wereld world) {
        return getRequestsAt(city.getCoordinaat(), world);
    }

    /**
     * Get the requests at the given coordinate.
     *
     * @param coordinate Coordinate to get the requests for.
     * @param world      World the coordinate is in.
     * @return List of requests.
     */
    public ArrayList<Handel> getRequestsAt(Coordinaat coordinate, Wereld world) {
        // Create a list to put the requests in
        final ArrayList<Handel> requests = new ArrayList<>();

        // Loop through the market requests and add them to the requests list if the coordinate matches the given
        for(int i = 0; i < world.getMarkt().getVraag().size(); i++)
            if(coordinate.equals(world.getMarkt().getVraag().get(i).getStad().getCoordinaat()))
                requests.add(world.getMarkt().getVraag().get(i));

        // Return the requests
        return requests;
    }


    /**
     * Get the fittest population.
     *
     * @param populationList List of populations.
     * @return Fittest population.
     */
    private TradePopulation getFittestPopulation(ArrayList<TradePopulation> populationList) {
        // Keep track of the lowest fitness population
        float lowestFitness = -1;
        int index = 0;

        // Loop through the populations to find the lowest
        for(int i = 0, length = populationList.size(); i < length; i++) {
            // Get the fittest individual fitness
            final float fitness = populationList.get(i).getFittest().getFitness();

            // Store the population if it's fitter
            if(lowestFitness == -1 || fitness < lowestFitness) {
                lowestFitness = fitness;
                index = i;
            }
        }

        // Return the fittest population
        return populationList.get(index);
    }

    /**
     * Evolve the population.
     *
     * @param population Population to evolve.
     * @return Evolved population.
     */
    private TradePopulation evolvePopulation(TradePopulation population) {
        // Create a new population
        final TradePopulation evolved = new TradePopulation(this, population.getMaxActions(), population.getGeneration() + 1, population.getSize(), false);

        // Evolve the fittest individual from the current population
        evolved.saveIndividual(0, population.getFittest());

        // Cross over individuals from the given population
        for(int i = 1, length = population.getSize(); i < length; i++)
            evolved.saveIndividual(i, crossover(tournament(population), tournament(population)));

        // Mutate crossed individuals
        for(int i = 1, length = evolved.getSize(); i < length; i++)
            mutate(evolved.getIndividual(i));

        // Loop through the evolved individuals, and make sure they have fitting trade actions
        for(int i = 0, length = evolved.getSize(); i < length; i++) {
            // Get the individual
            final TradeIndividual evolvedIndividual = evolved.getIndividual(i);

            // Remove actions that are out of range
            evolvedIndividual.removeActionsOutOfRange();

            // Generate fitting actions
            evolvedIndividual.generate();
        }

        // Return the evolved population
        return evolved;
    }

    /**
     * Do a population tournament, and get the fittest individual.
     * This creates a new population with a random combination of individuals from the given population, returns the fittest individual.
     *
     * @param population Population to do a tournament for.
     * @return Fittest individual after a tournament.
     */
    private TradeIndividual tournament(TradePopulation population) {
        // Create a tournament population
        final TradePopulation tournament = new TradePopulation(this, population.getMaxActions(), population.getGeneration(), POPULATION_TOURNAMENT_SIZE, false);

        // Get the population size
        final int populationSize = population.getSize();

        // Take random individuals from the current population
        for(int i = 0, length = tournament.getSize(); i < length; i++)
            tournament.saveIndividual(i, population.getIndividual(this.random.nextInt(populationSize)));

        // Return the fittest individual from the tournament population
        return tournament.getFittest();
    }

    /**
     * Cross over the cities from the first and second individuals into a new evolved individual.
     *
     * @param first  First individual.
     * @param second Second individual.
     * @return Evolved individual.
     */
    private TradeIndividual crossover(TradeIndividual first, TradeIndividual second) {
        // Create a new individual
        final TradeIndividual evolved = new TradeIndividual(this, first.getMaxActions(), first.getGeneration(), false);

        // Determine the amount of times to loop
        int loopCount = Math.max(first.getTradeActions().size(), second.getTradeActions().size());

        // Loop through the list of cities, and randomly add a city to the evolved from the first or second individual
        boolean useFirst;
        for(int i = 0; i < loopCount; i += 2)
            useTradeRoutesFrom(evolved, (useFirst = this.random.nextFloat() <= 0.5f) ? first : second, useFirst ? second : first, i);

        // Return the evolved individual
        return evolved;
    }

    /**
     * Add a city to the evolved individual from the {@param first} or {@param second} individual at the given index.
     * Add's all cities that aren't in the evolved individual if the cities provided by {@param first} or {@param second} aren't new.
     *
     * @param evolved Evolved individual.
     * @param first   First individual.
     * @param second  Second individual.
     * @param i       Index of the trade route.
     */
    private void useTradeRoutesFrom(TradeIndividual evolved, TradeIndividual first, TradeIndividual second, int i) {
        // Determine whether to use the first or second by default
        boolean useFirst = first.getTradeActions().size() >= i + 1 && second.getTradeActions().size() < i + 1;
        boolean useSecond = first.getTradeActions().size() < i + 1 && second.getTradeActions().size() >= i + 1;

        // Generate a random number
        float random = this.random.nextFloat();

        // Evolve with a trade route from the first/second individual for 90%, evolve with a random trade action for 10%
        if(!useSecond && random < 0.45f) {
            // Evolve with a trade route from the first individual
            evolved.addTradeAction(first.getTradeAction(i));
            evolved.addTradeAction(first.getTradeAction(i + 1));

        } else if(!useFirst && random < 0.90f) {
            // Evolve with a trade route from the second individual
            evolved.addTradeAction(second.getTradeAction(i));
            evolved.addTradeAction(second.getTradeAction(i + 1));

        } else
            // Evolve with a random (fitting) trade action
            evolved.generate(1);
    }

    /**
     * Mutate the given individual.
     * This modifies the list of cities of the individual slightly.
     *
     * @param individual Individual to mutate.
     */
    private void mutate(TradeIndividual individual) {
        // Loop through the list of cities of the individual, except the last city
        for(int i = 0, length = individual.getTradeActions().size() - 3; i < length; i += 2) {
            // Flip the current and a random city with a 1.5% chance
            if(this.random.nextFloat() < 0.015f) {
                // Generate a random trade action index
                final int random = (this.random.nextInt((individual.getTradeActions().size() - 1) / 2)) * 2;

                // Get the trade actions
                final TradeAction firstBuy = individual.getTradeAction(random);
                final TradeAction firstSell = individual.getTradeAction(random + 1);
                final TradeAction secondBuy = individual.getTradeAction(i);
                final TradeAction secondSell = individual.getTradeAction(i + 1);

                // Store both trade actions flipped
                individual.setTradeAction(i, firstBuy);
                individual.setTradeAction(i + 1, firstSell);
                individual.setTradeAction(random, secondBuy);
                individual.setTradeAction(random + 1, secondSell);
            }

            // Flip the current and followed city with a 7.5% chance
            if(this.random.nextFloat() < 0.075f) {
                // Get the trade actions
                final TradeAction firstBuy = individual.getTradeAction(i);
                final TradeAction firstSell = individual.getTradeAction(i + 1);
                final TradeAction secondBuy = individual.getTradeAction(i + 2);
                final TradeAction secondSell = individual.getTradeAction(i + 3);

                // Store the trade actions flipped
                individual.setTradeAction(i, secondBuy);
                individual.setTradeAction(i + 1, secondSell);
                individual.setTradeAction(i + 2, firstBuy);
                individual.setTradeAction(i + 3, firstSell);
            }
        }
    }

    /**
     * String representation of this class, which defines the algorithm name.
     *
     * @return Algorithm name.
     */
    @Override
    public String toString() {
        return "Genetic Trade Algorithm";
    }

    @Override
    public void setDebugger(Debugger debugger) {
        this.debugger = debugger;
    }
}
