package pl.dtv.stock;

import org.junit.Before;
import org.junit.Test;
import pl.dtv.stock.exceptions.StockNotFound;
import pl.dtv.stock.impl.SimpleStockRepository;
import pl.dtv.stock.model.StockManager;

import static junit.framework.TestCase.*;

/**
 * @author Piotr Szczepanik
 */
public class SimpleStockRepositoryTest {
    SimpleStockRepository repository;

    @Before
    public void setUp() throws Exception {
        repository = new SimpleStockRepository();
    }

    @Test
    public void testShouldFindExistingStock() throws Exception {
        StockManager stockManager = repository.findStockManager("ALE");
        assertNotNull(stockManager);
    }

    @Test(expected = StockNotFound.class)
    public void testShouldThrowStockNotFoundForNotExistingStock() throws Exception {
        StockManager stockManager = repository.findStockManager("ALE1");
    }

}
