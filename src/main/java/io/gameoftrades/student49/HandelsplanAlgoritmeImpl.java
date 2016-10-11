package io.gameoftrades.student49;

import io.gameoftrades.model.Wereld;
import io.gameoftrades.model.algoritme.HandelsplanAlgoritme;
import io.gameoftrades.model.algoritme.SnelstePadAlgoritme;
import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Stad;
import io.gameoftrades.model.markt.Handel;
import io.gameoftrades.model.markt.Handelsplan;
import io.gameoftrades.model.markt.actie.Actie;
import io.gameoftrades.model.markt.actie.BeweegActie;
import io.gameoftrades.model.markt.actie.HandelsPositie;
import io.gameoftrades.model.markt.actie.NavigeerActie;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class HandelsplanAlgoritmeImpl implements HandelsplanAlgoritme {

    private List <Actie> actions;
    private HandelsPositie handelsPositie;
    private ArrayList<Handel> vraag;
    private ArrayList<Handel> aanbod;
    private ArrayList<Handel> cityMarkets;

    private ArrayList<TradeRoute> tradeRoutes;

    @Override
    public Handelsplan bereken(Wereld wereld, HandelsPositie handelsPositie) {

        actions = new ArrayList<>();
        this.handelsPositie = handelsPositie;

        cityMarkets = getHandel(handelsPositie.getCoordinaat(), wereld);
        vraag = getVraag(handelsPositie.getCoordinaat(), wereld);
        aanbod = getAanbod(handelsPositie.getCoordinaat(), wereld);

        ArrayList<Handel> vraagTest = new ArrayList<>(wereld.getMarkt().getVraag());
        ArrayList<Handel> aanbodTest = new ArrayList<>(wereld.getMarkt().getAanbod());
        ArrayList<Stad> stedenTest = new ArrayList<>(wereld.getSteden());
        tradeRoutes = new ArrayList<>();

        PathChecker pc = new PathChecker(stedenTest, wereld.getKaart());

        for (int i = 0; i < aanbodTest.size(); i++) {
            for (int j = 0; j < vraagTest.size(); j++) {
                if(vraagTest.get(j).getHandelswaar().equals(aanbodTest.get(i).getHandelswaar())){
                    tradeRoutes.add(new TradeRoute(pc.getPath(aanbodTest.get(i).getStad().getCoordinaat(),
                            vraagTest.get(j).getStad().getCoordinaat()), aanbodTest.get(i).getHandelswaar(),
                            aanbodTest.get(i).getPrijs(), vraagTest.get(j).getPrijs(), handelsPositie.getCoordinaat()));
                }
            }
        }

        //printCurrentCityInformation();

        printAllRoutes();
//        actions.add(new BeweegActie(wereld.getKaart(), handelsPositie.getStad(),
//                stedenTest.get(5),
//                new SnelstePadAlgoritmeImpl().bereken(wereld.getKaart(),
//                        handelsPositie.getCoordinaat(), stedenTest.get(5).getCoordinaat())));
        for (int i = 0; i < tradeRoutes.size(); i++) {
            tradeRoutes.get(i).setCurrentPos(stedenTest.get(5).getCoordinaat());
        }
        System.out.println("");
        printAllRoutes();

        Handelsplan plan = new Handelsplan(actions);

        return plan;
    }

    public void printAllRoutes(){
        System.out.printf("%-10s %-30s %-10s %-10s %-20s %-10s %-10s", "Route:", "Product:",
                "Profit", "Pathcost", "Walk-efficiency", "Max-Buy", "Max Profit");
        System.out.println("");
        for (int i = 0; i < tradeRoutes.size(); i++) {
            System.out.printf("%-10d %-30s %-10d %-10d %-20f %-10d %-10d", i,
                    tradeRoutes.get(i).getHandelswaar().getNaam(),
                    tradeRoutes.get(i).getProfit(),
                    tradeRoutes.get(i).getPathCost(),
                    tradeRoutes.get(i).getEfficiency(),
                    handelsPositie.getKapitaal()/tradeRoutes.get(i).getBuy() > handelsPositie.getRuimte() ?
                            handelsPositie.getRuimte() :handelsPositie.getKapitaal()/tradeRoutes.get(i).getBuy(),
                    handelsPositie.getKapitaal()/tradeRoutes.get(i).getBuy() > handelsPositie.getRuimte() ?
                            handelsPositie.getRuimte()*tradeRoutes.get(i).getProfit() :
                            (handelsPositie.getKapitaal()/tradeRoutes.get(i).getBuy())*tradeRoutes.get(i).getProfit()
            );
            System.out.println("");
        }
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
