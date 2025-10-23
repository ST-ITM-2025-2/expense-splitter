package itm.oss.splitter;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class SettlementTest {

    @Test
    void simpleTest() {
        Balance b = new Balance();
        b.put("Alice", new BigDecimal("-30"));
        b.put("Bob", new BigDecimal("30"));

        ArrayList<Payment> result = Settlement.suggest(b);

        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).getFrom());
        assertEquals("Bob", result.get(0).getTo());
        assertEquals(0, result.get(0).getAmount().compareTo(new BigDecimal("30")));
    }

    @Test
    void tinyAmountTest() {
        Balance b = new Balance();
        b.put("Alice", new BigDecimal("-0.004"));
        b.put("Bob", new BigDecimal("0.004"));

        ArrayList<Payment> result = Settlement.suggest(b);
        assertTrue(result.isEmpty());
    }
}