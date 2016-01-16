package pl.dtv.stock;

import org.junit.Test;
import pl.dtv.stock.model.*;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.Assert.*;

/**
 * StockManager Test
 * @author Piotr Szczepanik
 */
public class StockManagerTest {

    @Test
    public void testCommonStockShouldAllowToGetItsStockSymbol() {
        StockManager stockManager = new CommonStockManager("ALE", BigDecimal.ONE);
        assertEquals("ALE", stockManager.getSymbol());
    }

    @Test
    public void testCommonStockShouldAllowToGetItsStockType() {
        StockManager stockManager = new CommonStockManager("ALE", BigDecimal.ONE);
        assertEquals(StockType.COMMON, stockManager.getType());
    }

    @Test(expected = RuntimeException.class)
    public void testCommonStockShouldNotAllowUndefinedSymbol() {
        StockManager stockManager = new CommonStockManager(null, BigDecimal.ONE);
    }

    @Test(expected = RuntimeException.class)
    public void testCommonStockShouldNotAllowUndefinedLastDividend() {
        StockManager stockManager = new CommonStockManager("ALE", null);
    }

    @Test
    public void testPreferredStockShouldAllowToGetItsStockSymbol() {
        StockManager stockManager = new PreferredStockManager("GIN", BigDecimal.ONE, BigDecimal.ONE);
        assertEquals("GIN", stockManager.getSymbol());
    }

    @Test
    public void testPreferredStockShouldAllowToGetItsStockType() {
        StockManager stockManager = new PreferredStockManager("GIN", BigDecimal.ONE, BigDecimal.ONE);
        assertEquals(StockType.PREFERRED, stockManager.getType());
    }

    @Test(expected = RuntimeException.class)
    public void testPreferredStockShouldNotAllowUndefinedSymbol() throws Exception {
        StockManager stockManager = new PreferredStockManager(null, BigDecimal.ONE, BigDecimal.ONE);
    }

    @Test(expected = RuntimeException.class)
    public void testPreferredStockShouldNotAllowUndefinedFixedDividend() throws Exception {
        StockManager stockManager = new PreferredStockManager("JIN", null, BigDecimal.ONE);
    }

    @Test(expected = RuntimeException.class)
    public void testPreferredStockShouldNotAllowUndefinedParValue() throws Exception {
        StockManager stockManager = new PreferredStockManager("JIN", BigDecimal.ONE, null);
    }

    @Test
    public void testOldTradesShouldBeDiscarded() {
        StockManager stockManager = new CommonStockManager("ALE", BigDecimal.TEN);
        stockManager.registerTrade(new Trade(TradeType.BUY, "ALE", new BigDecimal("12.0000"), 100, Instant.now().minusSeconds(905)));

        BigDecimal stockPrice = stockManager.calculateStockPrice();
        assertNull(stockPrice);
    }

    @Test
    public void testOnlyOldTradesShouldBeDiscarded() {
        StockManager stockManager = new CommonStockManager("ALE", BigDecimal.TEN);
        stockManager.registerTrade(new Trade(TradeType.BUY, "ALE", new BigDecimal("12.0000"), 100, Instant.now().minusSeconds(905)));
        stockManager.registerTrade(new Trade(TradeType.BUY, "ALE", new BigDecimal("11.0000"), 100, Instant.now().minusSeconds(800)));

        BigDecimal stockPrice = stockManager.calculateStockPrice();
        assertEquals(new BigDecimal("11.0000"), stockPrice);
    }
}
