package itm.oss.splitter;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Files;
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

        // Load the CSV. Prefer classpath (src/test/resources), fallback to repo file (./data/expenses.sample.csv)
        Path path;
        var resourceUrl = getClass().getClassLoader().getResource("data/expenses.sample.csv");
        if (resourceUrl != null) {
            path = Path.of(resourceUrl.toURI());
        } else {
            path = Path.of("data/expenses.sample.csv");
            assertTrue(Files.exists(path), "Could not find data/expenses.sample.csv on classpath or in project root ./data");
        }

        ExpenseStore store = new ExpenseStore();
        ArrayList<Expense> xs = store.load(path.toString());

        // sanity check
        assertFalse(xs.isEmpty(), "expenses.sample.csv should not be empty");

        Reports reports = new Reports();
        SimplePersonSummaryMap map = reports.perPerson(xs);

        // Sanity on loader
        assertEquals(3, xs.size(), "Should load three expense rows (header skipped)");
        assertEquals("Alice", xs.get(0).getPayer(), "the payer should be alice");
        assertEquals(new BigDecimal("60.00"), xs.get(0).getAmount(), "the amount should be 60");
        assertEquals(3, xs.get(0).getParticipants().size(), "There should be three participants");
    }

    @Test
    @DisplayName("getNet() reflects paid - owed for CSV sample (recalculateNet)")
    void perPerson_netFromSimpleCsv() throws IOException, URISyntaxException {
        // Load the CSV. Prefer classpath (src/test/resources), fallback to repo file (./data/expenses.sample.csv)
        Path path;
        var resourceUrl = getClass().getClassLoader().getResource("data/expenses.sample.csv");
        if (resourceUrl != null) {
            path = Path.of(resourceUrl.toURI());
        } else {
            path = Path.of("data/expenses.sample.csv");
            assertTrue(Files.exists(path), "Could not find data/expenses.sample.csv on classpath or in project root ./data");
        }

        ExpenseStore store = new ExpenseStore();
        ArrayList<Expense> xs = store.load(path.toString());

        Reports reports = new Reports();
        SimplePersonSummaryMap map = reports.perPerson(xs);

        // Expected from the CSV: each owes 90; paid: Alice 60, Bob 120, Cara 90 -> net: -30, +30, 0
        assertMoney("-30.00", map.get("Alice").getNet(), "Alice net should be paid-owed");
        assertMoney("30.00",  map.get("Bob").getNet(),   "Bob net should be paid-owed");
        assertMoney("0.00",   map.get("Cara").getNet(),  "Cara net should be zero");
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
        @DisplayName("getNet() updates correctly across multiple expenses")
        void perPerson_netTwoExpenses() {
            Reports reports = new Reports();
            ArrayList<Expense> xs = new ArrayList<>();

            // Expense 1: Alice pays 10 split by Alice,Bob -> each owes 5
            xs.add(exp("Alice", "10.00", List.of("Alice", "Bob"), "snacks"));
            // Expense 2: Bob pays 20 split by Alice,Bob -> each owes 10
            xs.add(exp("Bob",   "20.00", List.of("Alice", "Bob"), "meal"));

            SimplePersonSummaryMap m = reports.perPerson(xs);

            // Totals: Alice paid 10, owed 15 -> net -5; Bob paid 20, owed 15 -> net +5
            assertMoney("-5.00", m.get("Alice").getNet(), "Alice net across two expenses");
            assertMoney("5.00",  m.get("Bob").getNet(),   "Bob net across two expenses");
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

        @Test
        @DisplayName("Single-participant expense (payer included)")
        void perPerson_singleParticipantExpense() {
            Reports reports = new Reports();
            ArrayList<Expense> xs = new ArrayList<>();

            xs.add(exp("Alice", "100.00", List.of("Alice"), "coffee"));

            SimplePersonSummaryMap m = reports.perPerson(xs);
            assertMoney( "100.00", m.get("Alice").getPaidTotal(), "Alice paid total");
            assertMoney("100.00", m.get("Alice").getOwedTotal(), "Alice owed total");
            assertMoney("0.00", m.get("Alice").getNet(), "Paying yourself shouldn't create debt");
        }

        @Test
        @DisplayName("Payer not in participants splits owed without auto-including payer")
        void perPerson_payerNotInParticipants() {
            Reports reports = new Reports();
            ArrayList<Expense> xs = new ArrayList<>();

            // Alice pays 90 but only Bob and Cara participate
            xs.add(exp("Alice", "90.00", List.of("Bob", "Cara"), "meal"));

            SimplePersonSummaryMap m = reports.perPerson(xs);

            // Alice should be +90 net; Bob and Cara -45 each
            assertMoney("90.00",  m.get("Alice").getNet(), "Alice net should be +90");
            assertMoney("-45.00", m.get("Bob").getNet(),   "Bob net should be -45");
            assertMoney("-45.00", m.get("Cara").getNet(),  "Cara net should be -45");

            // Invariant: sum of net == 0
            BigDecimal sum = m.get("Alice").getNet()
                .add(m.get("Bob").getNet())
                .add(m.get("Cara").getNet());
            assertEquals(0, sum.compareTo(BigDecimal.ZERO), "Sum of net must be zero");
        }
}
