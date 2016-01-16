package pl.dtv.stock.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.LinkedList;
import java.util.Queue;

import static pl.dtv.stock.StockExchange.PRECISION;

/**
 * Abstract StockManager
 * Implements common properties and calculations for all stock types
 *
 * @author Piotr Szczepanik
 */
public abstract class AbstractStockManager implements StockManager {

    private String symbol;

    private StockType type;

    private final Queue<Trade> trades = new LinkedList<>();

    private static final int PRICE_WINDOW_SECONDS = 900;

    private BigDecimal currentPrice = null;

    private long quantity = 0;

    private BigDecimal windowVolume = BigDecimal.ZERO;

    AbstractStockManager(String symbol, StockType type) {
        if (symbol == null) {
            throw new RuntimeException("Wrong stock definition. Stock symbol is undefined!");
        }

        if (type == null) {
            throw new RuntimeException("Wrong stock definition. Stock type is undefined!");
        }

        this.symbol = symbol;
        this.type = type;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    public StockType getType() {
        return type;
    }

    @Override
    public boolean registerTrade(Trade trade) {
        boolean result;

        synchronized (trades) {
            result = trades.add(trade);

            quantity += trade.getQuantity();
            windowVolume = windowVolume.add(trade.getValue());
        }

        return result;
    }

    @Override
    public BigDecimal calculateStockPrice() {
        Instant quarterAgo = Instant.now().minusSeconds(PRICE_WINDOW_SECONDS);

        synchronized (trades) {
            Trade trade = trades.peek();
            BigDecimal removedValue = BigDecimal.ZERO;
            long removedQuantity = 0;

            while (trade != null && trade.getTimestamp().compareTo(quarterAgo) < 0) {
                removedQuantity += trade.getQuantity();
                removedValue = removedValue.add(trade.getValue());
                trades.poll();
                trade = trades.peek();
            }

            if (removedQuantity > 0) {
                quantity -= removedQuantity;
                windowVolume = windowVolume.subtract(removedValue);
            }

            if (quantity > 0) {
                currentPrice = windowVolume.divide(BigDecimal.valueOf(quantity), PRECISION, RoundingMode.HALF_DOWN);
            }
        }

        return currentPrice;
    }

    @Override
    public BigDecimal calculatePERatio() {
        BigDecimal result = null;

        BigDecimal stockPrice = calculateStockPrice();
        BigDecimal dividendYield = calculateDividentYield();

        if (stockPrice != null && dividendYield != null && dividendYield.compareTo(BigDecimal.ZERO) != 0) {
            result = stockPrice.divide(dividendYield, PRECISION, BigDecimal.ROUND_HALF_DOWN);
        }

        return result;
    }
}
