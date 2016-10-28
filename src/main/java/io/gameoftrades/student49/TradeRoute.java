package io.gameoftrades.student49;

import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Stad;
import io.gameoftrades.model.markt.Handelswaar;
import io.gameoftrades.student49.util.PathCacheManager;

public class TradeRoute {

    /**
     * CityPath instance.
     */
    private CityPath cityPath;

    /**
     * Buy amount.
     */
    private int buy;

    /**
     * Sell amount.
     */
    private int sell;

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
     * @param cityPath   CityPath.
     * @param goods      Trade goods.
     * @param buy        Buy amount.
     * @param sell       Sell amount.
     * @param currentPos Current position.
     */
    public TradeRoute(CityPath cityPath, Handelswaar goods, int buy, int sell, Coordinaat currentPos) {
        this.cityPath = cityPath;
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
    public int getProfit() {
        return (int) (this.sell - this.buy);
    }

    /**
     * Set the current position.
     *
     * @param pos Position.
     */
    public void setCurrentPos(Coordinaat pos) {
        this.currentPos = pos;
    }

    /**
     * Get the buy amount.
     *
     * @return Buy amount.
     */
    public int getBuy() {
        return (int) this.buy;
    }

    /**
     * Get the sell amount.
     *
     * @return Sell amount.
     */
    public int getSell() {
        return (int) this.sell;
    }

    /**
     * Get the offering city.
     *
     * @return Offering city.
     */
    public Stad getOfferCity() {
        return this.cityPath.getStartCity();
    }

    /**
     * Get the demanding city.
     *
     * @return Demanding city.
     */
    public Stad getDemandCity() {
        return this.cityPath.getEndCity();
    }

    /**
     * Get the efficiency value. This represents the possible profit per movement.
     *
     * @return Efficiency for 1 product.
     */
    public float getEfficiency() {
        return (float) (this.sell - this.buy) / getPathCost();
    }

    /**
     * Get the trade goods.
     *
     * @return Goods.
     */
    public Handelswaar getGoods() {
        return this.goods;
    }

    /**
     * Get the cityPath.
     *
     * @return CityPath.
     */
    public CityPath getCityPath() {
        return this.cityPath;
    }

    /**
     * Get the cityPath cost.
     *
     * @return CityPath cost.
     */
    public int getPathCost() {
        return PathCacheManager.getPathCost(this.cityPath.getStart(), this.currentPos) + this.cityPath.getLength();
    }
}
