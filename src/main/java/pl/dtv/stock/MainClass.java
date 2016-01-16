package pl.dtv.stock;

import pl.dtv.stock.impl.SimpleStockExchange;
import pl.dtv.stock.model.TradeType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

import static pl.dtv.stock.model.TradeType.*;

/**
 * Main Class
 *
 * @author Piotr Szczepanik
 */
class MainClass {

    public static void main(String[] args) {
        StockExchange stockExchange = new SimpleStockExchange();

        String[] symbols = registerSemiRandomTrades(stockExchange);

        printStockInfo(stockExchange, symbols);
    }

    private static void printStockInfo(StockExchange stockExchange, String[] symbols) {
        System.out.printf("    %7s %7s  %9s\n", "Price", "Yield", "P/E Rat");
        for (int i = 0; i < symbols.length; i++) {
            System.out.printf("%3s %7s %7s%% %9s\n",
                    symbols[i], stockExchange.calculateStockPrice(symbols[i]),
                    stockExchange.calculateDividentYield(symbols[i]).scaleByPowerOfTen(2),
                    stockExchange.calculatePERatio(symbols[i]));
        }
        System.out.printf("\n--- All share index: %7s\n", stockExchange.calculateAllShareIndex());
    }

    private static String[] registerSemiRandomTrades(StockExchange stockExchange) {
        String[] symbols = new String[] {"TEA", "POP", "ALE", "JIN", "JOE"};
        String[] prices = new String[] {"4.0000", "6.0000", "0.5000", "1.0000", "20.0000"};
        long[] quantities = new long[] {100, 200, 300, 100, 100, 200};
        TradeType[] types = new TradeType[] {BUY, BUY, SELL, SELL, SELL, BUY};

        Random random = new Random(System.nanoTime());
        for (int i = 0; i < symbols.length; i++) {
            for (int j = 0; j < types.length; j++) {
                BigDecimal avgPrice = new BigDecimal(prices[i]);
                double var = avgPrice.doubleValue() * 0.05;
                double price = avgPrice.doubleValue() + (var * random.nextDouble() - var / 2);
                BigDecimal bdPrice = new BigDecimal(price).setScale(StockExchange.PRECISION, RoundingMode.HALF_DOWN);

                switch (types[j]) {
                    case BUY:
                        stockExchange.buy(symbols[i], quantities[i], bdPrice);
                        break;
                    case SELL:
                        stockExchange.sell(symbols[i], quantities[i], bdPrice);
                        break;
                }
            }
        }
        return symbols;
    }
}
