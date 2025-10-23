import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import itm.oss.splitter.Expense;
import itm.oss.splitter.Reports;
import itm.oss.splitter.SimpleMap;

@DisplayName("test for Reports.totalsByCategory")
class ReportsTest {

    @Test
    void totalsByCategoryTest() {
        ArrayList<String> participants = new ArrayList<>(Arrays.asList("Alice", "Bob"));
        ArrayList<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense(
                "2024-01-01", "Alice", new BigDecimal("10.00"), "USD", participants, "Food", "Lunch"));
        expenses.add(new Expense(
                "2024-01-02", "Bob", new BigDecimal("20.00"), "USD", participants, "Travel", "Taxi"));
        expenses.add(new Expense(
                "2024-01-03", "Alice", new BigDecimal("5.50"), "USD", participants, "Food", "Snacks"));
        expenses.add(new Expense(
                "2024-01-04", "Cara", new BigDecimal("7.25"), "USD", participants, null, "Misc"));

        Reports reports = new Reports();
        SimpleMap totals = reports.totalsByCategory(expenses);

        assertArrayEquals(new String[] { "Food", "Travel", "" }, totals.keys());
        assertEquals(new BigDecimal("15.50"), totals.get("Food"));
        assertEquals(new BigDecimal("20.00"), totals.get("Travel"));
        assertEquals(new BigDecimal("7.25"), totals.get(""));
    }
}
