package io.gameoftrades.student49;

import io.gameoftrades.model.kaart.Stad;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Individual {

    private List <Stad> cities;
    private int fitness = 0;

    public Individual(){

        cities = new ArrayList<>();
        fitness = 0;

    }

    public void addCity(Stad stad){
        this.cities.add(stad);
    }

    public Stad getCity(int i){
        return cities.get(i);
    }

    public void setCity(int i,Stad stad){
        this.cities.set(i, stad);
        fitness = 0;
    }

    public void generateIndividual(List<Stad> steden) {
        this.cities = new ArrayList<>(steden);
        Collections.shuffle(this.cities);
    }

    public List<Stad> getCities(){
        return this.cities;
    }

    public boolean listContains(Stad city) {

        for (int i = 0; i < cities.size(); i++) {
            if (cities.get(i).getCoordinaat().equals(city.getCoordinaat()))
                return true;
        }
        return false;
    }

    public int getFitness(){

        if(fitness == 0) {
            for (int i = 0; i < cities.size() - 1; i++) {
                fitness += PathChecker.checkPathCost(this.cities.get(i).getCoordinaat(), this.cities.get(i + 1).getCoordinaat());
            }
        }
        return this.fitness;
    }


}
