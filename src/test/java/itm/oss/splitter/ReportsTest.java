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
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ReportsTest {
    private Reports reports;

    @BeforeEach
    void setUp() {
        reports = new Reports();
    }
    // Issue 1 integration testing
    @Test
    @DisplayName("Per-person summary loads correctly from sample CSV")
    void perPerson_usesCsvLoadedByExpenseStore() throws IOException, URISyntaxException {

        Path path = getSampleCsvPath();

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
        Path path = getSampleCsvPath();

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

    // Helper for getting the sample CSV path from classpath (src/test/resources) or repo root (./data).
    private Path getSampleCsvPath() throws URISyntaxException {
        var resourceUrl = getClass().getClassLoader().getResource("data/expenses.sample.csv");
        if (resourceUrl != null) {
            return Path.of(resourceUrl.toURI());
        }
        Path p = Path.of("data/expenses.sample.csv");
        assertTrue(Files.exists(p), "Could not find data/expenses.sample.csv on classpath or in project root ./data");
        return p;
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
            // If later a function for distributing remainder cents, the assertions will have to be updated.
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

    // Tests for the Reports.totalsByCategory method

    /**
     * Tests totals by category with normal inputs, including 'Uncategorized' category handling.
     */
    @Test
    @DisplayName("should calculate totals correctly for multiple categories")
    void totalsByCategory_HappyPath() {
        ArrayList<String> participants = new ArrayList<>(Arrays.asList("Alice", "Bob"));
        ArrayList<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense("2024-01-01", "Alice", new BigDecimal("10.00"), "USD", participants, "Food", "Lunch"));
        expenses.add(new Expense("2024-01-02", "Bob", new BigDecimal("20.00"), "USD", participants, "Travel", "Taxi"));
        expenses.add(new Expense("2024-01-03", "Alice", new BigDecimal("5.50"), "USD", participants, "Food", "Snacks"));
        expenses.add(new Expense("2024-01-04", "Cara", new BigDecimal("7.25"), "USD", participants, null, "Misc"));

        SimpleMap totals = reports.totalsByCategory(expenses);

        assertArrayEquals(new String[]{"Food", "Travel", "Uncategorized"}, totals.keys());
        assertEquals(new BigDecimal("15.50"), totals.get("Food"));
        assertEquals(new BigDecimal("20.00"), totals.get("Travel"));
        assertEquals(new BigDecimal("7.25"), totals.get("Uncategorized"));
    }

    /**
     * Tests behavior when the expense list is empty.
     */
    @Test
    @DisplayName("should return an empty map for an empty input list")
    void totalsByCategory_WhenInputIsEmpty() {
        ArrayList<Expense> emptyExpenses = new ArrayList<>();
        SimpleMap totals = reports.totalsByCategory(emptyExpenses);
        assertEquals(0, totals.keys().length);
    }

    /**
     * Tests correct handling when all expenses belong to a single category.
     */
    @Test
    @DisplayName("should calculate total correctly when all expenses are in one category")
    void totalsByCategory_WhenAllInOneCategory() {
        ArrayList<String> participants = new ArrayList<>(Arrays.asList("Alice", "Bob"));
        ArrayList<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense("2024-01-01", "Alice", new BigDecimal("10"), "USD", participants, "Entertainment", "Movie"));
        expenses.add(new Expense("2024-01-02", "Bob", new BigDecimal("30"), "USD", participants, "Entertainment", "Concert"));

        SimpleMap totals = reports.totalsByCategory(expenses);

        assertEquals(1, totals.keys().length);
        assertEquals(new BigDecimal("40"), totals.get("Entertainment"));
    }

    /**
     * Tests handling of zero and negative amounts (refunds).
     */
    @Test
    @DisplayName("should handle zero and negative amounts correctly")
    void totalsByCategory_WithZeroAndNegativeAmounts() {
        ArrayList<String> participants = new ArrayList<>(Arrays.asList("Alice"));
        ArrayList<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense("2024-01-01", "Alice", new BigDecimal("50.00"), "USD", participants, "Shopping", "Jacket"));
        expenses.add(new Expense("2024-01-02", "Alice", new BigDecimal("-10.00"), "USD", participants, "Shopping", "Jacket Refund"));
        expenses.add(new Expense("2024-01-03", "Alice", new BigDecimal("0.00"), "USD", participants, "Shopping", "Freebie"));

        SimpleMap totals = reports.totalsByCategory(expenses);

        assertEquals(new BigDecimal("40.00"), totals.get("Shopping"));
    }

    /**
     * Tests if categories with different capitalization (e.g., "Food" and "food")
     * are treated as the same category.
     */
    @Test
    @DisplayName("should treat different case categories as same category")
    void totalsByCategory_WhenCategoriesHaveDifferentCase() {
        ArrayList<String> participants = new ArrayList<>(Arrays.asList("Alice"));
        ArrayList<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense("2024-01-01", "Alice", new BigDecimal("10"), "USD", participants, "Food", ""));
        expenses.add(new Expense("2024-01-02", "Alice", new BigDecimal("20"), "USD", participants, "fOoD", ""));
        SimpleMap totals = reports.totalsByCategory(expenses);

        assertEquals(new BigDecimal("30"), totals.get("Food"), "Total for 'Food' should be 30.");
        assertEquals(1, totals.keys().length);
    }

    /**
     * Tests if 'null' categories and empty string "" categories and "Uncategorized"
     * are correctly grouped together into a single "Uncategorized" category.
     */
    @Test
    @DisplayName("should treat 'null' and 'Uncategorized' and 'empty string categories' as same category 'Uncategorized'")
    void totalsByCategory_WithNullAndEmptyStringCategories() {
        ArrayList<String> participants = new ArrayList<>(Arrays.asList("Alice"));
        ArrayList<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense("2024-01-01", "Alice", new BigDecimal("100"), "USD", participants, null, "Null category"));
        expenses.add(new Expense("2024-01-02", "Alice", new BigDecimal("50"), "USD", participants, "Uncategorized", "Uncategorized category"));
        expenses.add(new Expense("2024-01-03", "Alice", new BigDecimal("20"), "USD", participants, "", "Empty string category"));

        SimpleMap totals = reports.totalsByCategory(expenses);
        assertEquals(1, totals.keys().length, "There should be only one category for null, 'Uncategorized' and empty string.");
        assertEquals(new BigDecimal("170"), totals.get("Uncategorized"), "Total for 'Uncategorized' should be 170.");
        assertEquals(1, totals.keys().length);
    }
}
