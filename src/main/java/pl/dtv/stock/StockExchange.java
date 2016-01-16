package pl.dtv.stock;

import java.math.BigDecimal;

/**
 * Stock Exchange interface
 *
 * @author Piotr Szczepanik
 */
public interface StockExchange {

    /**
     * Default precision for decimal numbers
     */
    int PRECISION = 4;

    /**
     * Calculates the dividend yield according to a stock type for common and preferred stock
     *
     * @param stockSymbol stock symbol for which to calculate the dividend yield
     * @return calculated dividend yield value or null if there is no current price available for stock
     */
    BigDecimal calculateDividentYield(String stockSymbol);

    /**
     * Calculates the P/E ratio
     *
     * @param stockSymbol stock symbol for which to calculate the P/E ratio
     * @return calculated P/E ratio or null if there is no current price available for stock
     */
    BigDecimal calculatePERatio(String stockSymbol);

    /**
     * Registers a buying trade on stock for given quantity and price
     *
     * @param stockSymbol stock symbol on which to register the trade
     * @param quantity a quantity of the trade
     * @param price a price of the trade
     * @return true or false whether trade has been successfully registered or not
     */
    boolean buy(String stockSymbol, long quantity, BigDecimal price);

    /**
     * Registers a selling trade on stock for given quantity and price
     *
     * @param stockSymbol stock symbol on which to register the trade
     * @param quantity a quantity of the trade
     * @param price a price of the trade
     * @return true or false whether trade has been successfully registered or not
     * @throws RuntimeException if the quantity is less or equal 0 or price is not defined or not greated than 0
     */
    boolean sell(String stockSymbol, long quantity, BigDecimal price);

    /**
     * Calculates the stock price during the last 15 minute trade window
     *
     * @param stockSymbol stock symbol for which to calculate the price
     * @return calculated price <br />
     *          the last calculated price if there were no trades on the stock during last 15 minutes<br />
     *          null if there were not trades at all on the stock
     */
    BigDecimal calculateStockPrice(String stockSymbol);

    /**
     * Calculates all stock index based on the geometric mean of all stock which price is defined (@see this#calculateStockPrice())
     *
     * @return calculated all share index
     * Note: the value is returned as BigDecimal although it is converted to double for rooting.
     * The decision has been made to keep the interface consistent.
     */
    BigDecimal calculateAllShareIndex();
}
