package io.gameoftrades.student49.algorithm.traderoute;

import io.gameoftrades.model.Wereld;
import io.gameoftrades.model.algoritme.HandelsplanAlgoritme;
import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Richting;
import io.gameoftrades.model.kaart.Stad;
import io.gameoftrades.model.markt.Handel;
import io.gameoftrades.model.markt.Handelsplan;
import io.gameoftrades.model.markt.actie.Actie;
import io.gameoftrades.model.markt.actie.HandelsPositie;
import io.gameoftrades.model.markt.actie.NavigeerActie;
import io.gameoftrades.student49.TradeRoute;
import io.gameoftrades.student49.util.PathCacheManager;

import java.util.ArrayList;
import java.util.List;

public class TradeRouteAlgorithm implements HandelsplanAlgoritme {

    /**
     * List of actions that define the trade plan.
     */
    private List<Actie> actions;

    /**
     * Trade position.
     */
    private HandelsPositie tradePosition;

    /**
     * Goods we're buying (requesting).
     */
    private ArrayList<Handel> buying;

    /**
     * Goods we're selling (offering).
     */
    private ArrayList<Handel> selling;

    /**
     * List of goods for cities.
     */
    private ArrayList<Handel> cityGoods;

    /**
     * List of trade routes.
     */
    private ArrayList<TradeRoute> tradeRoutes;

    @Override
    public Handelsplan bereken(Wereld world, HandelsPositie tradePosition) {
        // Set the trade position
        this.tradePosition = tradePosition;

        // Instantiate the list of actions and trade routes
        actions = new ArrayList<>();
        tradeRoutes = new ArrayList<>();

        // Get the city goods and buying and selling offers
        cityGoods = getGoodsAt(tradePosition.getCoordinaat(), world);
        buying = getRequestsAt(tradePosition.getCoordinaat(), world);
        selling = getOfferingsAt(tradePosition.getCoordinaat(), world);

        // Get the list of requested and offered goods in this world
        final ArrayList<Handel> requestGoods = new ArrayList<>(world.getMarkt().getVraag());
        final ArrayList<Handel> offerGoods = new ArrayList<>(world.getMarkt().getAanbod());

        // Get a list of cities in this world
        final ArrayList<Stad> cities = new ArrayList<>(world.getSteden());


        Actie actie = new NavigeerActie(tradePosition.getCoordinaat(), Richting.WEST);
        actions.add(actie);

        for(Handel offer : offerGoods) {
            for(Handel request : requestGoods) {
                if(request.getHandelswaar().equals(offer.getHandelswaar())) {
                    tradeRoutes.add(new TradeRoute(PathCacheManager.getCityPath(offer.getStad().getCoordinaat(),
                        request.getStad().getCoordinaat()), offer.getHandelswaar(),
                        offer.getPrijs(), request.getPrijs(), tradePosition.getCoordinaat()));
                }
            }
        }

        // Als de kosten van het pad hoger zijn dan de totale bewegingspunten is die handelsroute sowieso geen optie.

        // Print a cost table, to make debugging easier
        printCostTable();

        // Return a trade plan with the list of actions to perform
        return new Handelsplan(actions);
    }

    /**
     * Print the cost table, to make debugging easier.
     */
    public void printCostTable() {
        // Print city information and the table header
        System.out.printf("%-5s %-5s %-5s %-5s", "City: " + tradePosition.getStad().getNaam(), "|  Money: " + tradePosition.getKapitaal(), "|  Actions: " + tradePosition.getMaxActie(), "|  Bag capacity: " + tradePosition.getRuimte());
        System.out.println(" ");
        System.out.println(" ");
        System.out.printf("%-10s %-15s %-30s %-15s %-10s %-10s %-10s %-20s %-10s %-15s %-10s", "Route:", "From", "Product:", "To", "Buy",
            "Profit", "Pathcost", "efficiency(1)", "Max-Buy", "Max Profit", "Efficiency(MAX)");
        System.out.println("");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------");

        // Loop through the trade routes
        for(int i = 0; i < tradeRoutes.size(); i++) {
            // Determine the maximum profit
            double maxProfit;
            if(tradePosition.getKapitaal() / tradeRoutes.get(i).getBuy() > tradePosition.getRuimte())
                maxProfit = tradePosition.getRuimte() * tradeRoutes.get(i).getProfit();
            else
                maxProfit = (tradePosition.getKapitaal() / tradeRoutes.get(i).getBuy()) * tradeRoutes.get(i).getProfit();

            // Print the trade route data
            System.out.printf("%-10d %-15s %-30s %-15s %-10d %-10d %-10d %-20f %-10d %-15d %-10f\n", i,
                tradeRoutes.get(i).getOfferCity().getNaam(), tradeRoutes.get(i).getGoods().getNaam(),
                tradeRoutes.get(i).getDemandCity().getNaam(), tradeRoutes.get(i).getBuy(), tradeRoutes.get(i).getProfit(), tradeRoutes.get(i).getPathCost(),
                tradeRoutes.get(i).getEfficiency(),
                tradePosition.getKapitaal() / tradeRoutes.get(i).getBuy() > tradePosition.getRuimte()
                    ? tradePosition.getRuimte() : tradePosition.getKapitaal() / tradeRoutes.get(i).getBuy(),
                (int) maxProfit, maxProfit / tradeRoutes.get(i).getPathCost());
        }
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
     * Get the goods in the given city.
     *
     * @param city  City to get the goods in.
     * @param world World the city is in.
     * @return List of goods.
     */
    public ArrayList<Handel> getGoodsAt(Stad city, Wereld world) {
        return getGoodsAt(city.getCoordinaat(), world);
    }

    /**
     * Get the goods at the given coordinate.
     *
     * @param coordinate Coordinate to get the goods for.
     * @param world      World the coordinate is in.
     * @return List of goods.
     */
    public ArrayList<Handel> getGoodsAt(Coordinaat coordinate, Wereld world) {
        // Create a list to put the goods in
        final ArrayList<Handel> goods = new ArrayList<>();

        // Loop through the market goods and add them to the goods list if the coordinate matches the given
        for(int i = 0; i < world.getMarkt().getHandel().size(); i++)
            if(coordinate.equals(world.getMarkt().getHandel().get(i).getStad().getCoordinaat()))
                goods.add(world.getMarkt().getHandel().get(i));

        // Return the goods
        return goods;
    }
}
