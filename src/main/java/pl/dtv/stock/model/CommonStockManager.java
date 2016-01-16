package pl.dtv.stock.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static pl.dtv.stock.StockExchange.PRECISION;

/**
 * Common Stock Manager
 * Describes stock for which dividend is calculated in a common way
 *
 * @author Piotr Szczepanik
 */
public class CommonStockManager extends AbstractStockManager implements StockManager {

    /**
     * Last dividend value
     */
    private BigDecimal lastDividend;

    public CommonStockManager(String symbol, BigDecimal lastDividend) {
        super(symbol, StockType.COMMON);

        if (lastDividend == null) {
            throw new RuntimeException("Wrong stock definition. One of the required values is null!");
        }

        this.lastDividend = lastDividend.scaleByPowerOfTen(-2);
    }

    /**
     * Calculates the dividend yield for common stock according to the following equation:<br />
     * dividendYield = round(lastDividend / stockPrice, 4)
     *
     * @return calculated dividend yield value (rounded to 4 decimal digits)
     *          or null if there is no current price available for stock
     */
    @Override
    public BigDecimal calculateDividentYield() {
        BigDecimal result = null;

        BigDecimal stockPrice = calculateStockPrice();

        if (stockPrice != null) {
            result = lastDividend.divide(stockPrice, PRECISION, RoundingMode.HALF_DOWN);
        }

        return result;
    }

}
