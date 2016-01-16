package pl.dtv.stock.model;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Trade
 * Describes trade on a stock
 *
 * @author Piotr Szczepanik
 */
public class Trade {
    private final TradeType type;

    private final BigDecimal price;

    private final long quantity;

    private final Instant timestamp;

    private final BigDecimal value;

    public Trade(TradeType type, String symbol, BigDecimal price, long quantity, Instant timestamp) {
        this.type = type;
        this.price = price;
        this.quantity = quantity;
        this.timestamp = timestamp;
        this.value = price.multiply(new BigDecimal(quantity));
    }

    public TradeType getType() {
        return type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public BigDecimal getValue() {
        return value;
    }

}
