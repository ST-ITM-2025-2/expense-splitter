package itm.oss.splitter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for the Reports.totalsByCategory method")
class ReportsTest {

    private Reports reports;

    @BeforeEach
    void setUp() {
        reports = new Reports();
    }

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

        
        assertEquals(new BigDecimal("30"), totals.get("Food"), "Total for 'Food' should be 10.");
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
