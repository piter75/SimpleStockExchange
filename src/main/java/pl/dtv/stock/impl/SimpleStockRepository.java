package pl.dtv.stock.impl;

import pl.dtv.stock.exceptions.StockNotFound;
import pl.dtv.stock.model.StockManager;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static pl.dtv.stock.model.StockType.COMMON;
import static pl.dtv.stock.model.StockType.PREFERRED;
import static pl.dtv.stock.util.StockManagerFactory.createStockManager;

/**
 * Simple StockManager Repository holding all available StockManager definitions
 *
 * @author Piotr Szczepanik
 */
public class SimpleStockRepository {

    /**
     * Map holding all stock definitions
     */
    private final Map<String, StockManager> stocks = new HashMap<>();

    /**
     * Map initialization
     */
    {
        stocks.put("TEA",
                createStockManager("TEA", COMMON,     BigDecimal.ZERO,    BigDecimal.ZERO,    new BigDecimal(100)));
        stocks.put("POP",
                createStockManager("POP", COMMON,     new BigDecimal(8),  BigDecimal.ZERO,    new BigDecimal(100)));
        stocks.put("ALE",
                createStockManager("ALE", COMMON,     new BigDecimal(23), BigDecimal.ZERO,    new BigDecimal( 60)));
        stocks.put("JIN",
                createStockManager("JIN", PREFERRED,  new BigDecimal(8),  new BigDecimal(2),  new BigDecimal( 60)));
        stocks.put("JOE",
                createStockManager("JOE", COMMON,     new BigDecimal(13), BigDecimal.ZERO,    new BigDecimal(250)));
    }

    /**
     * A method for finding a stock instance based on stock symbol
     *
     * @param stockSymbol a symbol of stock
     * @return stock instance if one is found otherwise throws StockNotFound exception
     *
     * @throws StockNotFound if the stock for specified symbol is not found
     */
    public StockManager findStockManager(String stockSymbol) {
        StockManager stockManager = stocks.get(stockSymbol);

        if (stockManager == null) {
            throw new StockNotFound(String.format("StockManager with the symbol \"%s\" has not been found", stockSymbol));
        }

        return stockManager;
    }

    Collection<StockManager> getAllStockManagers() {
        return stocks.values();
    }
}
