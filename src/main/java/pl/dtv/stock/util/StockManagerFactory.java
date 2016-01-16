package pl.dtv.stock.util;

import pl.dtv.stock.model.CommonStockManager;
import pl.dtv.stock.model.PreferredStockManager;
import pl.dtv.stock.model.StockManager;
import pl.dtv.stock.model.StockType;

import java.math.BigDecimal;

/**
 * Stock Manager Factory
 * Creates concrete StockManager implementation instances according to passed StockType
 *
 * @author Piotr Szczepanik
 */
public class StockManagerFactory {

    public static StockManager createStockManager(
            String symbol, StockType type, BigDecimal lastDividend, BigDecimal fixedDividend, BigDecimal parValue) {

        StockManager stockManager = null;

        if (type == null) {
            throw new RuntimeException("No StockType provided!");
        }

        switch (type) {
            case COMMON:
                stockManager = new CommonStockManager(symbol, lastDividend);
                break;
            case PREFERRED:
                stockManager = new PreferredStockManager(symbol, fixedDividend, parValue);
                break;
        }

        return stockManager;
    }
}
