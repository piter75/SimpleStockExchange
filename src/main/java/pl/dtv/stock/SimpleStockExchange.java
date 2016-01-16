package pl.dtv.stock;

import pl.dtv.stock.model.Stock;
import pl.dtv.stock.model.StockType;
import pl.dtv.stock.model.Trade;
import pl.dtv.stock.model.TradeType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static pl.dtv.stock.model.StockType.*;

/**
 * Created by piter on 13/01/16.
 */
public class SimpleStockExchange implements StockExchange {

    private static Map<String, Stock> stocks = new HashMap<>();

    {
        stocks.put("TEA", new Stock(COMMON,     BigDecimal.ZERO,    BigDecimal.ZERO,    new BigDecimal(100)));
        stocks.put("POP", new Stock(COMMON,     new BigDecimal(8),  BigDecimal.ZERO,    new BigDecimal(100)));
        stocks.put("ALE", new Stock(COMMON,     new BigDecimal(23), BigDecimal.ZERO,    new BigDecimal( 60)));
        stocks.put("JIN", new Stock(PREFERRED,  new BigDecimal(8),  new BigDecimal(2),  new BigDecimal( 60)));
        stocks.put("JOE", new Stock(COMMON,     new BigDecimal(13), BigDecimal.ZERO,    new BigDecimal(250)));
    }

    public BigDecimal calculateDividentYield(String stockSymbol) {
        BigDecimal result = null;
        Stock stock = stocks.get(stockSymbol);

        if (stock != null) {
            result = stock.calculateDividentYield();
        }

        return result;
    }

    public BigDecimal calculatePERatio(String stockSymbol) {
        BigDecimal result = null;
        Stock stock = stocks.get(stockSymbol);

        if (stock != null) {
            result = stock.calculatePERatio();
        }

        return result;
    }

    @Override
    public boolean buy(String stockSymbol, long quantity, BigDecimal price) {
        boolean result = false;
        Stock stock = stocks.get(stockSymbol);

        if (stock != null) {
            result = stock.registerTrade(new Trade(TradeType.BUY, price, quantity, Instant.now()));
        }

        return result;
    }

    @Override
    public boolean sell(String stockSymbol, long quantity, BigDecimal price) {
        boolean result = false;
        Stock stock = stocks.get(stockSymbol);

        if (stock != null) {
            result = stock.registerTrade(new Trade(TradeType.SELL, price, quantity, Instant.now()));
        }

        return result;
    }

    @Override
    public BigDecimal calculateStockPrice(String stockSymbol) {
        BigDecimal result = null;
        Stock stock = stocks.get(stockSymbol);

        if (stock != null) {
            result = stock.calculateStockPrice();
        }

        return result;
    }
}
