package io.gameoftrades.student49.algorithm.traderoute;

import io.gameoftrades.model.kaart.Stad;
import io.gameoftrades.model.markt.Handelswaar;

class TradeAction {

    /**
     * City to make the trade in.
     */
    private Stad city;

    /**
     * Define whether the action is to buy or sell. True to buy, false to sell.
     */
    private boolean buy;

    /**
     * Type of the goods to trade.
     */
    private Handelswaar goodType;

    /**
     * Price of goods to trade.
     */
    private int price;

    /**
     * Efficiency value.
     */
    private float efficiency;

    /**
     * Constructor.
     *
     * @param city       City to make the trade in.
     * @param buy        True to buy for this action, false to sell.
     * @param goodType   Type of goods to trade.
     * @param price      Price of the goods.
     * @param efficiency Efficiency value determined by the trade route.
     */
    TradeAction(Stad city, boolean buy, Handelswaar goodType, int price, float efficiency) {
        this.city = city;
        this.buy = buy;
        this.goodType = goodType;
        this.price = price;
        this.efficiency = efficiency;
    }

    /**
     * Get the city to make the trade in.
     *
     * @return City to make the trade in.
     */
    Stad getCity() {
        return this.city;
    }

    /**
     * Check whether this action is to buy.
     *
     * @return True to buy, false to sell.
     */
    boolean isBuy() {
        return this.buy;
    }

    /**
     * Get the type of goods to trade.
     *
     * @return Type of goods.
     */
    Handelswaar getGoodType() {
        return this.goodType;
    }

    /**
     * Get the price of goods to trade.
     *
     * @return Price of goods.
     */
    int getPrice() {
        return this.price;
    }

    /**
     * Efficiency value determined by the trade route.
     *
     * @return Efficiency value.
     */
    float getEfficiency() {
        return this.efficiency;
    }

    @Override
    public boolean equals(Object other) {
        // Return true if the super equals
        if(super.equals(other))
            return true;

        // Make sure the other is an instance of this class
        if(!(other instanceof TradeAction))
            return false;

        // Get the other as trade action instance
        final TradeAction otherTradeAction = (TradeAction) other;

        // Compare the fields
        return this.city.getCoordinaat().equals(otherTradeAction.getCity().getCoordinaat()) &&
            this.buy == otherTradeAction.isBuy() &&
            this.goodType.equals(otherTradeAction.getGoodType()) &&
            this.price == otherTradeAction.getPrice();
    }
}
