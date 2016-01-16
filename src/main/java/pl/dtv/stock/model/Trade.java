package pl.dtv.stock.model;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Created by piter on 13/01/16.
 */
public class Trade {
    private TradeType type;

    private BigDecimal price;

    private long quantity;

    private Instant timestamp;

    private BigDecimal value;

    public Trade(TradeType type, BigDecimal price, long quantity, Instant timestamp) {
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
