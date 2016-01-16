package pl.dtv.stock.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by piter on 13/01/16.
 */
public class Stock {

    private StockType type;

    private BigDecimal lastDividend;

    private BigDecimal fixedDividend;

    private BigDecimal parValue;

    private Queue<Trade> trades = new LinkedList<>();

    private static int PRICE_WINDOW_SECONDS = 900;

    private BigDecimal currentPrice = null;

    private long quantity = 0;

    private BigDecimal windowVolume = BigDecimal.ZERO;

    public Stock(StockType type, BigDecimal lastDividend, BigDecimal fixedDividend, BigDecimal parValue) {
        this.type = type;
        this.lastDividend = lastDividend.scaleByPowerOfTen(-2);
        this.fixedDividend = fixedDividend.scaleByPowerOfTen(-2);
        this.parValue = parValue.scaleByPowerOfTen(-2);
    }

    public StockType getType() {
        return type;
    }

    public void setType(StockType type) {
        this.type = type;
    }

    public BigDecimal getLastDividend() {
        return lastDividend;
    }

    public void setLastDividend(BigDecimal lastDividend) {
        this.lastDividend = lastDividend;
    }

    public BigDecimal getFixedDividend() {
        return fixedDividend;
    }

    public void setFixedDividend(BigDecimal fixedDividend) {
        this.fixedDividend = fixedDividend;
    }

    public BigDecimal getParValue() {
        return parValue;
    }

    public void setParValue(BigDecimal parValue) {
        this.parValue = parValue;
    }

    public boolean registerTrade(Trade trade) {
        boolean result;

        synchronized (trades) {
            result = trades.add(trade);

            quantity += trade.getQuantity();
            windowVolume = windowVolume.add(trade.getValue());
        }

        return result;
    }

    public BigDecimal calculateStockPrice() {
        Instant quarterAgo = Instant.now().minusSeconds(PRICE_WINDOW_SECONDS);

        synchronized (trades) {
            Trade trade = trades.peek();
            BigDecimal removedValue = BigDecimal.ZERO;
            long removedQuantity = 0;

            while (trade != null && trade.getTimestamp().compareTo(quarterAgo) < 0) {
                removedQuantity += trade.getQuantity();
                removedValue.add(trade.getValue());
                trades.poll();
                trade = trades.peek();
            }

            if (removedQuantity > 0) {
                quantity -= removedQuantity;
                windowVolume = windowVolume.subtract(removedValue);
            }

            currentPrice = windowVolume.divide(BigDecimal.valueOf(quantity));
        }
//        List<Trade> lastQuarterTrades = null;
//
//        synchronized (trades) {
//            lastQuarterTrades =
//                    trades.stream().filter(p -> p.getTimestamp().compareTo(quarterAgo) >= 0).collect(Collectors.toList());
//        }
//
//        BigDecimal volume = lastQuarterTrades.stream().reduce(
//                BigDecimal.ZERO, (sum, t) -> sum = sum.add(t.getPrice()), BigDecimal::add);
//
//        if (lastQuarterTrades.size() != 0) {
//            lastPrice = volume.setScale(4).divide(new BigDecimal(lastQuarterTrades.size()), RoundingMode.HALF_DOWN);
//        }

        return currentPrice;
    }

    public BigDecimal calculateDividentYield() {
        BigDecimal result = null;

        switch (type) {
            case COMMON:
                result = lastDividend.divide(calculateStockPrice(), 4, RoundingMode.HALF_DOWN);
                break;
            case PREFERRED:
                result = fixedDividend.multiply(parValue).divide(calculateStockPrice(), 4, RoundingMode.HALF_DOWN);
        }

        return result;
    }

    public BigDecimal calculatePERatio() {
        BigDecimal result = null;

        result = calculateStockPrice().divide(calculateDividentYield(), 4, BigDecimal.ROUND_HALF_DOWN);

        return result;
    }
}
