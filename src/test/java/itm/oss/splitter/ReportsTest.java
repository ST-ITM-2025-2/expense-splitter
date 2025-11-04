package itm.oss.splitter;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ReportsTest {

// simple csv file test


// complex csv file test


    // Issue 1 integration testing
    @Test
    void perPerson_usesCsvLoadedByExpenseStore() throws IOException, URISyntaxException {

        // Load the CSV from test/resources (classpath)
        var resourceUrl = getClass().getClassLoader().getResource("Expenses_Simple.csv");
        assertNotNull(resourceUrl, "Resource Expenses_Simple.csv not found on classpath");

        Path path = Path.of(resourceUrl.toURI());

        ExpenseStore store = new ExpenseStore();
        ArrayList<Expense> xs = store.load(path.toString());

        // sanity check
        assertFalse(xs.isEmpty(), "Expenses_Simple.csv should not be empty");

        Reports reports = new Reports();
        SimplePersonSummaryMap map = reports.perPerson(xs);

        // Sanity on loader
        assertEquals(3, xs.size(), "Should load three expense rows (header skipped)");
        assertEquals("Alice", xs.get(0).getPayer(), "the payer should be alice");
        assertEquals(new BigDecimal("60.00"), xs.get(0).getAmount(), "the amount should be 60");
        assertEquals(3, xs.get(0).getParticipants().size(), "There should be three participants");
    }

// Unit testing

        private static final RoundingMode RM = RoundingMode.HALF_UP;

        private static void assertMoney(String expected, BigDecimal actual, String msg) {
            assertEquals(new BigDecimal(expected).setScale(2, RM),
                        actual.setScale(2, RM),
                        msg);
        }

        // Helper from Expense
    private static Expense exp(String payer, String amount, 
                            List<String> participants, String category) {
        return new Expense(
            "2025-01-01",
            payer,
            new BigDecimal(amount),
            "USD",
            new ArrayList<>(participants),
            category,
            ""
        );
    }

        @Test
        @DisplayName("Rounding: 10.00 split by 3 uses HALF_UP in code")
        void perPerson_roundingTenByThree() {
            Reports reports = new Reports();
            ArrayList<Expense> xs = new ArrayList<>();

            xs.add(exp("Alice", "10.00", List.of("Alice", "Bob", "Cara"), "snacks"));

            SimplePersonSummaryMap m = reports.perPerson(xs);
            assertMoney("10.00", m.get("Alice").getPaidTotal(), "payer paid");
            assertMoney("3.33",  m.get("Alice").getOwedTotal(), "Alice owes");
            assertMoney("3.33",  m.get("Bob").getOwedTotal(),   "Bob owes");
            assertMoney("3.33",  m.get("Cara").getOwedTotal(),  "Cara owes");
            // If later a function for descributing remainder cents, the assertions will have to be updated.
        }

        @Test
        @DisplayName("Expense with zero participants is ignored")
        void perPerson_ignoresZeroParticipants() {
            Reports reports = new Reports();
            ArrayList<Expense> xs = new ArrayList<>();

            xs.add(exp("Alice", "100.00", List.of(), "misc"));

            SimplePersonSummaryMap m = reports.perPerson(xs);
            assertEquals(0, m.keys().length, "No entries should be created");
        }

        @Test
        @DisplayName("Zero-amount expense has no effect")
        void perPerson_zeroAmount() {
            Reports reports = new Reports();
            ArrayList<Expense> xs = new ArrayList<>();

            xs.add(exp("Alice", "0.00", List.of("Alice","Bob"), "snacks"));

            SimplePersonSummaryMap m = reports.perPerson(xs);
            assertMoney("0.00", m.get("Alice").getPaidTotal(), "Alice paid");
            assertMoney("0.00", m.get("Bob").getOwedTotal(), "Bob owes");
        }
}
