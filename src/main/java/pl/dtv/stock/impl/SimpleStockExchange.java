package pl.dtv.stock.impl;

import pl.dtv.stock.StockExchange;
import pl.dtv.stock.model.StockManager;
import pl.dtv.stock.model.Trade;
import pl.dtv.stock.model.TradeType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Super Simple Stock Exchange class
 *
 * @author Piotr Szczepanik
 */
public class SimpleStockExchange implements StockExchange {

    /**
     * StockManager repository
     */
    private final SimpleStockRepository repository = new SimpleStockRepository();

    @Override
    public BigDecimal calculateDividentYield(String stockSymbol) {
        StockManager stockManager = repository.findStockManager(stockSymbol);

        return stockManager.calculateDividentYield();
    }

    @Override
    public BigDecimal calculatePERatio(String stockSymbol) {
        StockManager stockManager = repository.findStockManager(stockSymbol);

        return stockManager.calculatePERatio();
    }

    @Override
    public boolean buy(String stockSymbol, long quantity, BigDecimal price) {
        StockManager stockManager = repository.findStockManager(stockSymbol);

        return registerTrade(TradeType.BUY, stockManager, quantity, price);
    }

    @Override
    public boolean sell(String stockSymbol, long quantity, BigDecimal price) {
        StockManager stockManager = repository.findStockManager(stockSymbol);

        return registerTrade(TradeType.SELL, stockManager, quantity, price);
    }

    /**
     * A method for registering a trade with a given stockManager
     * @param type a type of trade
     * @param stockManager a stockManager to register a trade with
     * @param quantity a quantity of trade
     * @param price a price of trade
     * @return true or false whether the trage has been successfully registered or not
     * @throws RuntimeException if the quantity is less or equal 0
     *          or price is not defined or not greated than 0 or price precision is to large
     */
    private boolean registerTrade(TradeType type, StockManager stockManager, long quantity, BigDecimal price) {
        if (quantity <= 0) {
            throw new RuntimeException("Quantity should be greater than 0!");
        }

        if (price == null || BigDecimal.ZERO.compareTo(price) >= 0) {
            throw new RuntimeException("Price should be defined and greater than 0.0!");
        }

        if (price.scale() > PRECISION) {
            throw new RuntimeException("Price should not be more precise than 4 decimal digits!");
        }

        return stockManager.registerTrade(new Trade(type, stockManager.getSymbol(), price, quantity, Instant.now()));
    }

    @Override
    public BigDecimal calculateStockPrice(String stockSymbol) {
        StockManager stockManager = repository.findStockManager(stockSymbol);

        return stockManager.calculateStockPrice();
    }

    @Override
    public BigDecimal calculateAllShareIndex() {
        Collection<StockManager> activeStockManagers = repository.getAllStockManagers().stream().filter(
                p -> p.calculateStockPrice() != null).collect(Collectors.toList());

        BigDecimal pricesMultiplied = activeStockManagers.stream().reduce(
                BigDecimal.ONE, (sum, s) -> sum = sum.multiply(s.calculateStockPrice()), BigDecimal::multiply);

        double geometricMean = Math.pow(pricesMultiplied.doubleValue(), 1d / activeStockManagers.size());

        BigDecimal result = null;

        if (!Double.isNaN(geometricMean)) {
            result = BigDecimal.valueOf(geometricMean).setScale(PRECISION, RoundingMode.HALF_DOWN);
        }

        return result;
    }

}
