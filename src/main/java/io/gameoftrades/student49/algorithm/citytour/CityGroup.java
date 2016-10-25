package io.gameoftrades.student49.algorithm.citytour;

import io.gameoftrades.model.kaart.Stad;

import java.util.ArrayList;

/**
 * Created by Simon on 25-9-2016.
 */
public class CityGroup {

    private ArrayList <Stad> cities;

    private int totalPathCost;

    public CityGroup(ArrayList<Stad> cities, int totalPathCost){
        this.cities = cities;
        this.totalPathCost = totalPathCost;
    }

    public int getTotalPathCost(){

        return totalPathCost;
    }

    public ArrayList<Stad> getCities(){

        return this.cities;
    }
}
