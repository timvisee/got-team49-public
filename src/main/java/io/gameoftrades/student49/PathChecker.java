package io.gameoftrades.student49;

import io.gameoftrades.model.algoritme.SnelstePadAlgoritme;
import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Kaart;
import io.gameoftrades.model.kaart.Pad;
import io.gameoftrades.model.kaart.Stad;

import java.util.ArrayList;

public class PathChecker {

    private static ArrayList <Path> paths;
    private ArrayList <Stad> cities;

    private SnelstePadAlgoritme spa;
    private Kaart map;

    public PathChecker(ArrayList <Stad> cities, Kaart map){

        this.cities = cities;
        this.map = map;
        this.spa = new SnelstePadAlgoritmeImpl();
        this.paths = new ArrayList<>();

        FillPathsArrayList();
    }

    private void FillPathsArrayList(){

        for (int i = 0; i < cities.size(); i++) {
            for (int j = 0; j < cities.size(); j++) {
                if(!cities.get(i).getCoordinaat().equals(cities.get(j).getCoordinaat()))
                    calculatePaths(cities.get(i).getCoordinaat(), cities.get(j).getCoordinaat());
            }
        }
    }

    private void calculatePaths(Coordinaat c1, Coordinaat c2){

        boolean foundPath = false;

        if(paths.isEmpty()){
            Pad pad = spa.bereken(map, c1, c2);
            paths.add(new Path(c1, c2, pad.getTotaleTijd()));
            return;
        }

        if(!paths.isEmpty()){
            for (int i = 0; i < paths.size(); i++) {
                if ((paths.get(i).getStart().equals(c1) && paths.get(i).getEnd().equals(c2)) ||
                        (paths.get(i).getEnd().equals(c1) && paths.get(i).getStart().equals(c2))) {
                    foundPath = true;
                }
            }
        }
        if(!foundPath && !paths.isEmpty()){
            Pad pad = spa.bereken(map, c1, c2);
            paths.add(new Path(c1, c2, pad.getTotaleTijd()));
        }
    }

    public static int checkPathCost(Coordinaat c1, Coordinaat c2){

        int pathCost = 0;

            for (int j = 0; j < paths.size(); j++) {
                if((c1.equals(paths.get(j).getStart()) && c2.equals(paths.get(j).getEnd())) ||
                        (c1.equals(paths.get(j).getEnd()) && c2.equals(paths.get(j).getStart()))){
                    return paths.get(j).getLength();
            }
        }
        return pathCost;
    }

    public Path getPath(Coordinaat c1, Coordinaat c2) {
        for (int i = 0; i < paths.size(); i++) {
            if ((c1.equals(paths.get(i).getStart()) && c2.equals(paths.get(i).getEnd())) ||
                    (c1.equals(paths.get(i).getEnd()) && c2.equals(paths.get(i).getStart()))) {
                return paths.get(i);
            }
        }
        return null;
    }

}
