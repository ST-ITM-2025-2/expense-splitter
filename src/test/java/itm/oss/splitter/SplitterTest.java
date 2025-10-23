package itm.oss.splitter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

class SplitterTest {
    // System Under Test (SUT)
    private final Splitter splitter = new Splitter();

    @Test       
    @DisplayName("Test for issue4-compute-balance")
    void testComputeBalance() {
        // ---------- (1) Happy-path scenario ----------
        // Verify settlement when Alice, Bob, and Cara all participate in all expenses.
        ArrayList<Expense> expensesHappyCase = new ArrayList<Expense>();

        ArrayList<String> names = new ArrayList<>();
        names.add("Alice");
        names.add("Bob");
        names.add("Cara");

        // Each expense: (date, payer, amount, currency, participants, category, note)
        Expense expense01 = new Expense("2025-10-01",
                "Alice", BigDecimal.valueOf(70.00),
                "USD", names, "Food", "Pizza night");

        Expense expense02 = new Expense("2025-10-02",
                "Bob", BigDecimal.valueOf(120.00),
                "USD", names, "Transport", "Taxi + subway");

        Expense expense03 = new Expense("2025-10-03",
                "Cara", BigDecimal.valueOf(90.00),
                "USD", names, "Groceries", "Market");

        expensesHappyCase.add(expense01);
        expensesHappyCase.add(expense02);
        expensesHappyCase.add(expense03);

        Balance resultHappyCase = splitter.computeBalances(expensesHappyCase);

        System.out.println("\n[Test: Happy Case] Net per participant\n");
        for (String name : names) {
            System.out.println(name + " : " + resultHappyCase.getAmount(name));
        }

        // Expected values:
        // Total 70 + 120 + 90 = 280; 3 participants → 93.33... per person.
        // Net = (amount paid by person) - (their equal shares).
        // Round to 2 decimals using HALF_EVEN.
        assertEquals(resultHappyCase.getAmount("Alice"), BigDecimal.valueOf(-23.34));
        assertEquals(resultHappyCase.getAmount("Bob"), BigDecimal.valueOf(26.67));
        assertEquals(resultHappyCase.getAmount("Cara"), BigDecimal.valueOf(-3.33));

        // ---------- (2) Edge case: negative amount (should be skipped) ----------
        // Verify that a negative expense is ignored.
        //Only Bob’s and Cara’s expenses (80 + 90 = 170 total) are valid.
        // Each participant’s share: 170 / 3 = 56.67
        ArrayList<Expense> edgeCaseNegative = new ArrayList<Expense>();

        Expense expense04 = new Expense("2025-10-01",
                "Alice", BigDecimal.valueOf(-100.00),// ← negative: expected to be skipped
                "USD", names, "Food", "Pizza night");

        Expense expense05 = new Expense("2025-10-02",
                "Bob", BigDecimal.valueOf(80.00),
                "USD", names, "Transport", "Taxi + subway");

        Expense expense06 = new Expense("2025-10-03",
                "Cara", BigDecimal.valueOf(90.00),
                "USD", names, "Groceries", "Market");

        edgeCaseNegative.add(expense04);
        edgeCaseNegative.add(expense05);
        edgeCaseNegative.add(expense06);

        Balance resultEdgeNegative = splitter.computeBalances(edgeCaseNegative);

        System.out.println("\n[Test: Edge Case - Negative amount] Net per participant\n");
        for (String name : resultEdgeNegative.getNames()) {
            System.out.println(name + " : " + resultEdgeNegative.getAmount(name));
        }

        // Only 80 + 90 = 170 should be considered (the -100 is ignored).
        // net(Alice) = 0 - 56.67 = -56.67
        // net(Bob) = 80 - 56.67 = 23.33
        // net(Cara) = 90 - 56.67 = 33.33
        assertEquals(resultEdgeNegative.getAmount("Alice"), BigDecimal.valueOf(-56.67));
        assertEquals(resultEdgeNegative.getAmount("Bob"), BigDecimal.valueOf(23.34));
        assertEquals(resultEdgeNegative.getAmount("Cara"), BigDecimal.valueOf(33.33));

        // ---------- (3) Edge case: single participant ----------
        // verify that expenses with only one participant are handled correctly.
        // the total cost should NOT be divided — payer keeps or loses full amount.

        // In this setup, name list now contains only "Alice"
        // Bob and Cara will be removed from participants
        // however, expenses paid by Bob and Cara will still be counted in full
        // each expense is only for "Alice"

        names.remove("Bob");
        names.remove("Cara");

        ArrayList<Expense> EdgeCaseSingle = new ArrayList<Expense>();

        Expense expense07 = new Expense("2025-10-01",
                "Alice", BigDecimal.valueOf(60.00),
                "USD", names, "Food", "Pizza night");

        Expense expense08 = new Expense("2025-10-02",
                "Bob", BigDecimal.valueOf(120.00),
                "USD", names, "Transport", "Taxi + subway");

        Expense expense09 = new Expense("2025-10-03",
                "Cara", BigDecimal.valueOf(90.00),
                "USD", names, "Groceries", "Market");

        EdgeCaseSingle.add(expense07);
        EdgeCaseSingle.add(expense08);
        EdgeCaseSingle.add(expense09);

        Balance resultEdgeSingle = splitter.computeBalances(EdgeCaseSingle);

        System.out.println("\n[Test: Edge Case - Single participant] Net per participant\n");
        for (String name : resultEdgeSingle.getNames()) {
            System.out.println(name + " : " + resultEdgeSingle.getAmount(name));
        }
        // calculations:
        // net(Alice) = 60 - (60 + 120 + 90) = -210
        // net(Bob) = 0 + 120 = 120
        // net(Cara) = 0 + 90 = 90      
        assertEquals(resultEdgeSingle.getAmount("Alice").setScale(2, RoundingMode.HALF_EVEN),
                BigDecimal.valueOf(-210.00).setScale(2, RoundingMode.HALF_EVEN));
        assertEquals(resultEdgeSingle.getAmount("Bob").setScale(2, RoundingMode.HALF_EVEN),
                BigDecimal.valueOf(120.00).setScale(2, RoundingMode.HALF_EVEN));
        assertEquals(resultEdgeSingle.getAmount("Cara").setScale(2, RoundingMode.HALF_EVEN),
                BigDecimal.valueOf(90.00).setScale(2, RoundingMode.HALF_EVEN));
    }
}

