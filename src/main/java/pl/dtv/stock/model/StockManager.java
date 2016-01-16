package pl.dtv.stock.model;

import java.math.BigDecimal;

/**
 * Stock Manager interface
 *
 * @author Piotr Szczepanik
 */
public interface StockManager {
    String getSymbol();

    StockType getType();

    boolean registerTrade(Trade trade);

    BigDecimal calculateStockPrice();

    BigDecimal calculateDividentYield();

    BigDecimal calculatePERatio();
}
