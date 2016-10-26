package io.gameoftrades.student49;

import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Stad;
import io.gameoftrades.model.markt.Handelswaar;
import io.gameoftrades.student49.util.PathChecker;

public class TradeRoute {

    private Path path;

    private double buy;

    private double sell;

    private Handelswaar handelswaar;

    private Coordinaat currentPos;

    public TradeRoute(Path path, Handelswaar handelswaar, double buy, double sell, Coordinaat currentPos){
        this.path = path;
        this.buy = buy;
        this.sell = sell;
        this.handelswaar = handelswaar;
        this.currentPos = currentPos;
    }

    public int getProfit(){
        return (int) (sell-buy);
    }

    public void setCurrentPos(Coordinaat c1){
        currentPos = c1;
    }

    public int getBuy(){
        return (int) buy;
    }

    public int getSell(){
        return (int)sell;
    }

    public Stad getOfferCity(){
        return path.getStartCity();
    }

    public Stad getDemandCity(){
        return path.getEndCity();
    }

    /**
     *
     * @return efficiency for 1 product.
     */
    public double getEfficiency(){
        return (sell-buy)/getPathCost();
    }

    /**
     *
     * @return a Handelswaar.
     */
    public Handelswaar getHandelswaar(){
        return this.handelswaar;
    }

    /**
     *
     * @return a Path.
     */
    public Path getPath(){
        return this.path;
    }

    public int getPathCost(){
        //return path.getLength();
        return PathChecker.checkPathCost(path.getStart(), currentPos) + path.getLength();
    }
}
