package pl.dtv.stock.exceptions;

/**
 * StockNotFound exception
 * Thrown when there is no known StockManager for requested symbol
 *
 * @author Piotr Szczepanik
 */
public class StockNotFound extends RuntimeException {

    public StockNotFound(String message) {
        super(message);
    }

}
