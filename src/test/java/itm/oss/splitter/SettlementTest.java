package itm.oss.splitter;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for Settlement.suggest()
 */
public class SettlementTest {

    @Test
    void simpleTest() {
        // One debtor (-30) and one creditor (+30)
        Balance b = new Balance();
        b.put("Alice", new BigDecimal("-30"));
        b.put("Bob", new BigDecimal("30"));

        ArrayList<Payment> result = Settlement.suggest(b);

        // Expect one payment: Alice → Bob: 30
        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).getFrom());
        assertEquals("Bob", result.get(0).getTo());
        assertEquals(0, result.get(0).getAmount().compareTo(new BigDecimal("30")));
    }

    @Test
    void tinyAmountTest() {
        // Small values should be ignored (less than 0.005)
        Balance b = new Balance();
        b.put("Alice", new BigDecimal("-0.004"));
        b.put("Bob", new BigDecimal("0.004"));

        ArrayList<Payment> result = Settlement.suggest(b);

        // Expect no payments
        assertTrue(result.isEmpty());
    }
}