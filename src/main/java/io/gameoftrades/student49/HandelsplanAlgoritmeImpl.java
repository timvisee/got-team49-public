package io.gameoftrades.student49;

import io.gameoftrades.model.Wereld;
import io.gameoftrades.model.algoritme.HandelsplanAlgoritme;
import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.markt.Handel;
import io.gameoftrades.model.markt.Handelsplan;
import io.gameoftrades.model.markt.actie.Actie;
import io.gameoftrades.model.markt.actie.HandelsPositie;

import java.util.ArrayList;
import java.util.List;

public class HandelsplanAlgoritmeImpl implements HandelsplanAlgoritme {

    private List <Actie> actions;
    private HandelsPositie handelsPositie;
    private ArrayList<Handel> vraag;
    private ArrayList<Handel> aanbod;
    private ArrayList<Handel> cityMarkets;

    @Override
    public Handelsplan bereken(Wereld wereld, HandelsPositie handelsPositie) {

        actions             = new ArrayList<>();
        this.handelsPositie = handelsPositie;
        cityMarkets         = getHandel(handelsPositie.getCoordinaat(), wereld);
        vraag               = getVraag(handelsPositie.getCoordinaat(), wereld);
        aanbod              = getAanbod(handelsPositie.getCoordinaat(), wereld);

        printCurrentCityInformation();

        Handelsplan plan = new Handelsplan(actions);

        return plan;
    }

    public void printCurrentCityInformation(){

        String format = "%-15s %-30s %-15d";
        System.out.println(handelsPositie.getStad().getNaam());

        for (int i = 0; i < aanbod.size(); i++) {
            System.out.printf(format, aanbod.get(i).getHandelType(), aanbod.get(i).getHandelswaar(), aanbod.get(i).getPrijs());
            System.out.println("");
        }

        for (int i = 0; i < vraag.size(); i++) {
            System.out.printf(format, vraag.get(i).getHandelType(), vraag.get(i).getHandelswaar(), vraag.get(i).getPrijs());
            System.out.println("");
        }
    }


    public ArrayList<Handel> getAanbod(Coordinaat c1, Wereld wereld){

        ArrayList <Handel> temp = new ArrayList<>();

        for (int i = 0; i < wereld.getMarkt().getAanbod().size(); i++) {
            if(c1.equals(wereld.getMarkt().getAanbod().get(i).getStad().getCoordinaat())){
                temp.add(wereld.getMarkt().getAanbod().get(i));
            }
        }
        return temp;
    }
    
    public ArrayList<Handel> getVraag(Coordinaat c1, Wereld wereld){
        
        ArrayList<Handel> temp = new ArrayList<>();

        for (int i = 0; i < wereld.getMarkt().getVraag().size(); i++) {
            if(c1.equals(wereld.getMarkt().getVraag().get(i).getStad().getCoordinaat())){
                temp.add(wereld.getMarkt().getVraag().get(i));
            }
        }
        return temp;
    }

    public ArrayList<Handel> getHandel(Coordinaat c1, Wereld wereld){

        ArrayList<Handel> handels = new ArrayList<>();

        for (int i = 0; i < wereld.getMarkt().getHandel().size() ; i++) {
            if(c1.equals(wereld.getMarkt().getHandel().get(i).getStad().getCoordinaat())){
                handels.add(wereld.getMarkt().getHandel().get(i));
            }
        }
        return handels;
    }
}
