package pl.dtv.stock.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static pl.dtv.stock.StockExchange.PRECISION;

/**
 * Preferred Stock Manager
 * Describes stock for which dividend is calculated in a preferred way
 *
 * @author Piotr Szczepanik
 */
public class PreferredStockManager extends AbstractStockManager implements StockManager {

    /**
     * Fixed dividend value
     */
    private BigDecimal fixedDividend;

    /**
     * Par value
     */
    private BigDecimal parValue;

    public PreferredStockManager(String symbol, BigDecimal fixedDividend, BigDecimal parValue) {
        super(symbol, StockType.PREFERRED);

        if (fixedDividend == null || parValue == null) {
            throw new RuntimeException("Wrong stock definition. One of the required values is null!");
        }

        this.fixedDividend = fixedDividend.scaleByPowerOfTen(-2);
        this.parValue = parValue.scaleByPowerOfTen(-2);
    }

    /**
     * Calculates the dividend yield for preferred stock according to the following equation:<br />
     * dividendYield = round(fixedDividend * parValue / stockPrice, 4)
     *
     * @return calculated dividend yield value (rounded to 4 decimal digits)
     *          or null if there is no current price available for stock
     */

    @Override
    public BigDecimal calculateDividentYield() {
        BigDecimal result = null;

        BigDecimal stockPrice = calculateStockPrice();

        if (stockPrice != null) {
            result = fixedDividend.multiply(parValue).divide(stockPrice, PRECISION, RoundingMode.HALF_DOWN);
        }

        return result;
    }

}
