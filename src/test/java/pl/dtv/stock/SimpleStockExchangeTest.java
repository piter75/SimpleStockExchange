package pl.dtv.stock;

import org.junit.Before;
import org.junit.Test;
import pl.dtv.stock.impl.SimpleStockExchange;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * Simple StockManager Exchange Tests
 *
 * @author Piotr Szczepanik
 */
public class SimpleStockExchangeTest {
    StockExchange stock = null;

    @Before
    public void setUp() throws Exception {
        stock = new SimpleStockExchange();
    }

    @Test
    public void testShouldAllowToBuyStock() throws Exception {
        boolean result = stock.buy("ALE", 1, BigDecimal.ONE);

        assertTrue(result);
    }

    @Test(expected = RuntimeException.class)
    public void testShouldNotAllowToBuyStockWithUndefinedPrice() throws Exception {
        stock.buy("ALE", 1, null);
    }

    @Test(expected = RuntimeException.class)
    public void testShouldNotAllowToBuyStockWithZeroPrice() throws Exception {
        stock.buy("ALE", 1, BigDecimal.ZERO);
    }

    @Test(expected = RuntimeException.class)
    public void testShouldNotAllowToBuyStockWithAnotherZeroPrice() throws Exception {
        stock.buy("ALE", 1, new BigDecimal("0.0000"));
    }

    @Test(expected = RuntimeException.class)
    public void testShouldNotAllowToBuyStockWithNegativePrice() throws Exception {
        stock.buy("ALE", 1, new BigDecimal(-1));
    }

    @Test(expected = RuntimeException.class)
    public void testShouldNotAllowToBuyStockWithTooPrecisePrice() throws Exception {
        stock.buy("ALE", 1, new BigDecimal("0.00001"));
    }

    @Test(expected = RuntimeException.class)
    public void testShouldNotAllowToBuyStockOfZeroQuantity() throws Exception {
        stock.buy("ALE", 0, BigDecimal.ONE);
    }

    @Test(expected = RuntimeException.class)
    public void testShouldNotAllowToBuyStockOfNegativeQuantity() throws Exception {
        stock.buy("ALE", -1, BigDecimal.ONE);
    }

    @Test
    public void testShouldAllowToSellStock() throws Exception {
        boolean result = stock.buy("ALE", 1, BigDecimal.ONE);

        assertTrue(result);
    }

    @Test(expected = RuntimeException.class)
    public void testShouldNotAllowToSellStockWithUndefinedPrice() throws Exception {
        stock.buy("ALE", 1, null);
    }

    @Test(expected = RuntimeException.class)
    public void testShouldNotAllowToSellStockWithZeroPrice() throws Exception {
        stock.buy("ALE", 1, BigDecimal.ZERO);
    }

    @Test(expected = RuntimeException.class)
    public void testShouldNotAllowToSellStockWithAnotherZeroPrice() throws Exception {
        stock.buy("ALE", 1, new BigDecimal("0.0000"));
    }

    @Test(expected = RuntimeException.class)
    public void testShouldNotAllowToSellStockWithNegativePrice() throws Exception {
        stock.buy("ALE", 1, new BigDecimal(-1));
    }

    @Test(expected = RuntimeException.class)
    public void testShouldNotAllowToSellStockWithTooPrecisePrice() throws Exception {
        stock.buy("ALE", 1, new BigDecimal("0.00001"));
    }

    @Test(expected = RuntimeException.class)
    public void testShouldNotAllowToSellStockOfZeroQuantity() throws Exception {
        stock.buy("ALE", 0, BigDecimal.ONE);
    }

    @Test(expected = RuntimeException.class)
    public void testShouldNotAllowToSellStockOfNegativeQuantity() throws Exception {
        stock.buy("ALE", -1, BigDecimal.ONE);
    }

    @Test
    public void testStockPriceShouldBeUndefinedInitiallyButDefinedAfterFirstTrade() throws Exception {
        BigDecimal stockPrice = stock.calculateStockPrice("ALE");
        assertNull(stockPrice);

        boolean result = stock.buy("ALE", 100, new BigDecimal("4.00"));
        assertTrue(result);

        stockPrice = stock.calculateStockPrice("ALE");
        assertNotNull(stockPrice);
        assertEquals(new BigDecimal("4.0000"), stockPrice);
    }

    @Test
    public void testCommonStockDividendShouldBeUndefinedInitiallyButDefinedAfterFirstTrade() throws Exception {
        BigDecimal dividentYield = stock.calculateDividentYield("ALE");
        assertNull(dividentYield);

        boolean result = stock.buy("ALE", 100, new BigDecimal("4.00"));
        assertTrue(result);

        dividentYield = stock.calculateDividentYield("ALE");
        assertNotNull(dividentYield);
        assertEquals(new BigDecimal("0.0575"), dividentYield);
    }


    @Test
    public void testPreferredStockDividendShouldBeUndefinedInitiallyButDefinedAfterFirstTrade() throws Exception {
        BigDecimal dividentYield = stock.calculateDividentYield("JIN");
        assertNull(dividentYield);

        boolean result = stock.buy("JIN", 100, new BigDecimal("4.00"));
        assertTrue(result);

        dividentYield = stock.calculateDividentYield("JIN");
        assertNotNull(dividentYield);
        assertEquals(new BigDecimal("0.0030"), dividentYield);
    }

    @Test
    public void testStockPERatioShouldBeUndefinedInitiallyButDefinedAfterFirstTrade() throws Exception {
        BigDecimal peRatio = stock.calculatePERatio("JIN");
        assertNull(peRatio);

        boolean result = stock.buy("JIN", 100, new BigDecimal("4.00"));
        assertTrue(result);

        peRatio = stock.calculatePERatio("JIN");
        assertNotNull(peRatio);
        assertEquals(new BigDecimal("1333.3333"), peRatio);
    }

    @Test
    public void testStockPERatioShouldBeUndefinedWhenDividendYieldIsZero() throws Exception {
        boolean result = stock.buy("TEA", 100, new BigDecimal("4.00"));
        assertTrue(result);

        BigDecimal peRatio = stock.calculatePERatio("JIN");
        assertNull(peRatio);
    }

    @Test
    public void testShareIndexShouldBeUndefinedInitiallyButThenCalculatedProperly() throws Exception {
        BigDecimal shareIndex = stock.calculateAllShareIndex();
        assertNull(shareIndex);

        boolean resultJin = stock.buy("JIN", 100, new BigDecimal("4.00"));
        assertTrue(resultJin);

        boolean resultAle = stock.sell("ALE", 100, new BigDecimal("16.00"));
        assertTrue(resultAle);

        boolean resultPop = stock.buy("POP", 100, new BigDecimal("3.375"));
        assertTrue(resultPop);

        shareIndex = stock.calculateAllShareIndex();
        assertNotNull(shareIndex);
        assertEquals(new BigDecimal("6.0000"), shareIndex);
    }
}
