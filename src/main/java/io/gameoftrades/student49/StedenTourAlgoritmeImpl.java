package io.gameoftrades.student49;

import io.gameoftrades.debug.Debuggable;
import io.gameoftrades.debug.Debugger;
import io.gameoftrades.model.algoritme.SnelstePadAlgoritme;
import io.gameoftrades.model.algoritme.StedenTourAlgoritme;
import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Kaart;
import io.gameoftrades.model.kaart.Pad;
import io.gameoftrades.model.kaart.Stad;

import java.util.ArrayList;
import java.util.List;


public class StedenTourAlgoritmeImpl implements StedenTourAlgoritme, Debuggable {

    private Debugger debugger;

    private Kaart map;

    private List <Stad> cities;
    private List <Stad> fixedCities;

    private ArrayList <Path> paths;

    private ArrayList <CityGroup> cityGroupList;

    private SnelstePadAlgoritme spa;

    @Override
    public List<Stad> bereken(Kaart kaart, List<Stad> list) {

        map = kaart;
        paths = new ArrayList<>();
        spa = new SnelstePadAlgoritmeImpl();

        cities = new ArrayList<>(list);
        fixedCities = new ArrayList<>(cities);
        cityGroupList = new ArrayList<>();

        runAlgorithm();

        int lowestPathCost = -1;
        int count = 0;
        for (int i = 0; i < cityGroupList.size(); i++) {
            if(lowestPathCost == -1 || cityGroupList.get(i).getTotalPathCost() < lowestPathCost){
                lowestPathCost = cityGroupList.get(i).getTotalPathCost();
                count = i;
            }
        }

        System.out.println("The fastest route using the 'Nearest-Neighbour Algorithm' takes " + cityGroupList.get(count).getTotalPathCost() +" moves.");
        System.out.println("Paths learned: " + paths.size() + ".");
        debugger.debugSteden(map, cityGroupList.get(count).getCities());
        return cityGroupList.get(count).getCities();
    }

    public void runAlgorithm(){

        for (int start = 0; start < fixedCities.size(); start++) {

            ArrayList<Stad> fastestRoute = new ArrayList<>();

            int totalPathCost = 0;

            // add the city you start with to the fastest route
            fastestRoute.add(cities.get(start));
            cities.remove(start);

            // variable to keep track of current city in loop
            int cityNumber = 0;


            while (!cities.isEmpty()) {
                int fastestPath = -1;
                int pathLength = 0;

                for (int i = 0; i < cities.size(); i++) {

                    // get the path-length, checks if the path is already learned
                    if(!paths.isEmpty()) {
                        pathLength = getPathLength(fastestRoute, pathLength, i);
                    }
                    // get executed once, because the paths list is empty at the start
                    // calculates the path-length from a certain city to another one.
                    else {
                        pathLength = calculateFastestPath(fastestRoute.get(fastestRoute.size() - 1).getCoordinaat(),
                                cities.get(i).getCoordinaat());
                    }

                    // if the path is faster than the current one, pick this city as the fastest option.
                    if (fastestPath == -1 || pathLength <= fastestPath) {
                        fastestPath = pathLength;
                        cityNumber = i;
                    }
                }

                totalPathCost += fastestPath;
                fastestRoute.add(cities.get(cityNumber));
                cities.remove(cityNumber);
            }

            cityGroupList.add(new CityGroup(fastestRoute, totalPathCost));

            // reset the city ArrayList.
            cities = new ArrayList<>(fixedCities);

            System.out.println(start + " - 'Nearest Neighbour' combinations calculated..");

        }

    }

    private int getPathLength(ArrayList<Stad> fastestRoute, int pathLength, int i) {

        boolean success = false;

        for (int j = 0; j < paths.size(); j++) {
            // check if a path between 2 given cities is in the paths list(from start to end, and end to start)
            if((paths.get(j).getStart().equals(fastestRoute.get(fastestRoute.size() -1).getCoordinaat()) &&
                    paths.get(j).getEnd().equals(cities.get(i).getCoordinaat())) ||
                    (paths.get(j).getEnd().equals(fastestRoute.get(fastestRoute.size() -1).getCoordinaat()) &&
                    paths.get(j).getStart().equals(cities.get(i).getCoordinaat()))) {
                pathLength = paths.get(j).getLength();
                success = true;
            }
        }
        //if the path is not in the path list, calculate it and put in in the paths list.
        if(!success) {
            pathLength = calculateFastestPath(fastestRoute.get(fastestRoute.size() - 1).getCoordinaat(),
                    cities.get(i).getCoordinaat());
        }
        return pathLength;
    }


    public int calculateFastestPath(Coordinaat c1, Coordinaat c2){

        Pad pad = spa.bereken(map ,c1, c2);
        paths.add(new Path(c1, c2, pad.getTotaleTijd()));
        return pad.getTotaleTijd();
    }

    @Override
    public void setDebugger(Debugger debugger) {
        this.debugger = debugger;
    }

    public String toString(){
        return "Nearest Neighbour Algorithm";
    }
}
