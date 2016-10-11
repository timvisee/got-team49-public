package io.gameoftrades.student49;

import io.gameoftrades.model.kaart.Kaart;
import io.gameoftrades.model.kaart.Stad;

import java.util.List;


public class Population {

    private Individual [] population;
    private Kaart map;
    private List <Stad> cities;

    public Population(int size, Kaart map, List <Stad> cities, boolean init){

        population = new Individual[size];
        this.map = map;
        this.cities = cities;

        if (init){
            for (int i = 0; i < population.length; i++) {
                Individual newIndividual = new Individual();
                newIndividual.generateIndividual(cities);
                population[i] = newIndividual;
            }
        }
    }

    public List<Stad> getCities(){
        return cities;
    }

    public Individual[] getPopulation(){
        return population;
    }

    public Individual getIndividual(int i){
        return population[i];
    }

    public void saveIndividual(int i, Individual indiv){

        population[i] = indiv;
    }

    public Individual getFittest(){

        int min = -1;
        int city = -1;

        for (int i = 0; i < population.length; i++) {
            if(min == -1 || population[i].getFitness() < min){
                min = population[i].getFitness();
                city = i;
            }
        }
        return population[city];
    }

    public int getPopSize(){
        return population.length;
    }
}
