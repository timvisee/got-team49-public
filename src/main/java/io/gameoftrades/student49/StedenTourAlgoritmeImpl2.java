package io.gameoftrades.student49;

import io.gameoftrades.debug.Debuggable;
import io.gameoftrades.debug.Debugger;
import io.gameoftrades.model.algoritme.StedenTourAlgoritme;
import io.gameoftrades.model.kaart.Kaart;
import io.gameoftrades.model.kaart.Stad;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;


public class StedenTourAlgoritmeImpl2 implements StedenTourAlgoritme, Debuggable{

    private Kaart map;

    private ArrayList <Stad> steden;

    private final int TRIES = 20;

    private Debugger debugger;

    private PathChecker pathChecker;

    @Override
    public List<Stad> bereken(Kaart kaart, List<Stad> steden) {

        map = kaart;
        this.steden = new ArrayList<>(steden);
        ArrayList<Population> populationList = new ArrayList<>();

        if(pathChecker == null)
        pathChecker = new PathChecker(this.steden, map);

        long time = System.currentTimeMillis();
        for (int i = 0; i < TRIES; i++) {

            Population pop = new Population(50, kaart, steden, true);

            int sameFitness = 0;
            int fittest = -1;
            int generationCount = 0;

            while (sameFitness <= 50) {

                generationCount++;
                pop = evolvePopulation(pop);

                if(fittest == -1 || pop.getFittest().getFitness() < fittest) {
                    fittest = pop.getFittest().getFitness();
                    sameFitness = 0;
                }

                if(fittest == pop.getFittest().getFitness()) sameFitness++;
            }
            System.out.println("Try " + (i + 1) + " --> Evolved through " + generationCount + " generations" +  " --> Best Solution: " + pop.getFittest().getFitness());
            populationList.add(pop);
        }

        System.out.println("total time passed in ms: " + ((System.currentTimeMillis() - time)) + ".");

        Population fittestPop = getFittestPop(populationList);

        System.out.println("The most efficient route is " +  fittestPop.getFittest().getFitness() + ".");
        debugger.debugSteden(map, fittestPop.getFittest().getCities());
        return fittestPop.getFittest().getCities();
    }

    public Population getFittestPop(ArrayList<Population> populationList){

        int lowest = -1;
        int count = 0;
        for (int i = 0; i < populationList.size(); i++) {
            if(lowest == -1 || populationList.get(i).getFittest().getFitness() < lowest){
                lowest =  populationList.get(i).getFittest().getFitness();
                count = i;
            }
        }
        return populationList.get(count);
    }

    public Population evolvePopulation(Population pop){

        Population newPop = new Population(pop.getPopSize(), map, steden, false);

            newPop.saveIndividual(0, pop.getFittest());

        for (int i = 1; i < pop.getPopSize(); i++) {
            Individual indiv1 = tournament(pop);
            Individual indiv2 = tournament(pop);
            Individual newIndiv = crossover(indiv1, indiv2);
            newPop.saveIndividual(i, newIndiv);
        }

        for (int i = 1; i < newPop.getPopSize(); i++) {
            mutate(newPop.getIndividual(i));
        }

        return newPop;
    }

    public Individual tournament(Population pop){

        Population tournamentPop = new Population(5, map, steden, false);

        for (int i = 0; i < tournamentPop.getPopSize(); i++) {
            int r = (int)(Math.random()*pop.getPopSize());
            tournamentPop.saveIndividual(i, pop.getPopulation()[r]);
        }

        return tournamentPop.getFittest();
    }

    @SuppressWarnings("Duplicates")
    public Individual crossover(Individual indiv1, Individual indiv2){

        Individual newSol = new Individual();

        for (int i = 0; i < steden.size(); i++) {

            if (Math.random() <= 0.500) {
                getCityFrom(newSol, indiv1, indiv2, i);
            } else {
                getCityFrom(newSol, indiv2, indiv1, i);
            }
        }
        return newSol;
    }

    public void getCityFrom(Individual newSol, Individual indiv1, Individual indiv2, int i){

        if(!newSol.listContains(indiv1.getCity(i))){
            newSol.addCity(indiv1.getCity(i));
        }
        else if(!newSol.listContains(indiv2.getCity(i))){
            newSol.addCity(indiv2.getCity(i));
        }
        else{
            steden.stream().filter(s -> !newSol.listContains(s)).forEach(newSol::addCity);
        }
    }


    public void mutate(Individual indiv) {

        for (int i = 0; i < indiv.getCities().size() -1; i++) {

            if(Math.random() < 0.015) {
                    int random = (int) (Math.random() * ((indiv.getCities().size() -1)));
                    Stad stad1 = indiv.getCities().get(random);
                    Stad stad2 = indiv.getCities().get(i);
                    indiv.setCity(i, stad1);
                    indiv.setCity(random, stad2);
                }
            if(Math.random() < 0.075) { {
                    Stad stad1 = indiv.getCities().get(i);
                    Stad stad2 = indiv.getCities().get(i + 1);
                    indiv.setCity(i, stad2);
                    indiv.setCity(i + 1, stad1);
                }
            }
            }
        }

    @Override
    public void setDebugger(Debugger debugger) {
        this.debugger = debugger;
    }

    public String toString(){
        return "Genetic Algorithm";
    }

}
