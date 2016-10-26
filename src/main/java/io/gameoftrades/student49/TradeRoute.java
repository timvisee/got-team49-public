package io.gameoftrades.student49;

import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Stad;
import io.gameoftrades.model.markt.Handelswaar;
import io.gameoftrades.student49.util.PathChecker;

public class TradeRoute {

    /**
     * Path instance.
     */
    private Path path;

    /**
     * Buy amount.
     */
    private double buy;

    /**
     * Sell amount.
     */
    private double sell;

    /**
     * Trade goods.
     */
    private Handelswaar goods;

    /**
     * Current position.
     */
    private Coordinaat currentPos;

    /**
     * Constructor.
     *
     * @param path Path.
     * @param goods Trade goods.
     * @param buy Buy amount.
     * @param sell Sell amount.
     * @param currentPos Current position.
     */
    public TradeRoute(Path path, Handelswaar goods, double buy, double sell, Coordinaat currentPos){
        this.path = path;
        this.buy = buy;
        this.sell = sell;
        this.goods = goods;
        this.currentPos = currentPos;
    }

    /**
     * Get the profit.
     *
     * @return Profit.
     */
    public int getProfit(){
        return (int) (this.sell - this.buy);
    }

    /**
     * Set the current position.
     *
     * @param pos Position.
     */
    public void setCurrentPos(Coordinaat pos){
        this.currentPos = pos;
    }

    /**
     * Get the buy amount.
     *
     * @return Buy amount.
     */
    public int getBuy(){
        return (int) this.buy;
    }

    /**
     * Get the sell amount.
     *
     * @return Sell amount.
     */
    public int getSell(){
        return (int) this.sell;
    }

    /**
     * Get the offering city.
     *
     * @return Offering city.
     */
    public Stad getOfferCity(){
        return this.path.getStartCity();
    }

    /**
     * Get the demanding city.
     *
     * @return Demanding city.
     */
    public Stad getDemandCity(){
        return this.path.getEndCity();
    }

    /**
     * Get the efficiency value.
     *
     * @return Efficiency for 1 product.
     */
    public double getEfficiency(){
        return (this.sell - this.buy) / getPathCost();
    }

    /**
     * Get the trade goods.
     *
     * @return Goods.
     */
    public Handelswaar getGoods(){
        return this.goods;
    }

    /**
     * Get the path.
     *
     * @return Path.
     */
    public Path getPath(){
        return this.path;
    }

    /**
     * Get the path cost.
     *
     * @return Path cost.
     */
    public int getPathCost(){
        return PathChecker.checkPathCost(this.path.getStart(), this.currentPos) + this.path.getLength();
    }
}
