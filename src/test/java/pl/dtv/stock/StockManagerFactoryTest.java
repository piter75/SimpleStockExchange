package pl.dtv.stock;

import org.junit.Test;
import pl.dtv.stock.model.CommonStockManager;
import pl.dtv.stock.model.PreferredStockManager;
import pl.dtv.stock.model.StockManager;
import pl.dtv.stock.model.StockType;
import pl.dtv.stock.util.StockManagerFactory;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * StockManager Factory Tests
 *
 * @author Piotr Szczepanik
 */
public class StockManagerFactoryTest {

    @Test
    public void testShouldCreateCommonStock() throws Exception {
        StockManager stockManager = StockManagerFactory.createStockManager("ALE", StockType.COMMON, BigDecimal.ONE, null, null);

        assertTrue(stockManager instanceof CommonStockManager);
    }

    @Test
    public void testShouldCreatePreferredStock() throws Exception {
        StockManager stockManager = StockManagerFactory.createStockManager("JIN", StockType.PREFERRED, null, BigDecimal.ONE, BigDecimal.ONE);

        assertTrue(stockManager instanceof PreferredStockManager);
    }

    @Test(expected = RuntimeException.class)
    public void testFailForUndefinedStock() throws Exception {
        StockManager stockManager = StockManagerFactory.createStockManager("ALE", null, null, BigDecimal.ONE, BigDecimal.ONE);
    }

}
