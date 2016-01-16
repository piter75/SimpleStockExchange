package pl.dtv.stock;

import java.math.BigDecimal;

/**
 * Created by piter on 13/01/16.
 */
public interface StockExchange {

    BigDecimal calculateDividentYield(String stockSymbol);

    BigDecimal calculatePERatio(String stockSymbol);

    boolean buy(String stockSymbol, long quantity, BigDecimal price);

    boolean sell(String stockSymbol, long quantity, BigDecimal price);

    BigDecimal calculateStockPrice(String stockSymbol);

}
