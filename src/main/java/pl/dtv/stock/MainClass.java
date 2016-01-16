package pl.dtv.stock;

import java.math.BigDecimal;

/**
 * Created by piter on 16/01/16.
 */
public class MainClass {
    public static void main(String[] args) {
        SimpleStockExchange stockExchange = new SimpleStockExchange();

        stockExchange.buy("ALE", 100, new BigDecimal("4.20"));
        stockExchange.buy("ALE", 300, new BigDecimal("4.10"));
        stockExchange.buy("ALE", 500, new BigDecimal("4.15"));
        stockExchange.buy("ALE", 400, new BigDecimal("4.20"));
        stockExchange.buy("ALE", 200, new BigDecimal("4.25"));

        System.out.printf("Dividend yield: %d\\n", stockExchange.calculateDividentYield("ALE"));
    }
}
