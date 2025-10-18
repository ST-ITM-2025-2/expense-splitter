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
    private final Splitter splitter = new Splitter();
    @Test
    @DisplayName("Test for compute balance")
    void testComputeBalance() {
        ArrayList<Expense> expensesHappyCase = new ArrayList<Expense>();

        ArrayList<String> names = new ArrayList<>();
        names.add("Alice");
        names.add("Bob");
        names.add("Cara");


        // TODO: Implement Test case at least one happy-path
        Expense expense01 = new Expense("2025-10-01",
                "Alice", BigDecimal.valueOf(70.00),
                "USD",names,"Food","Pizza night");


        Expense expense02 = new Expense("2025-10-02",
                "Bob", BigDecimal.valueOf(120.00),
                "USD",names,"Transport","Taxi + subway");

        Expense expense03 = new Expense("2025-10-03",
                "Cara", BigDecimal.valueOf(90.00),
                "USD",names,"Groceries","Market");

        expensesHappyCase.add(expense01);
        expensesHappyCase.add(expense02);
        expensesHappyCase.add(expense03);
        Balance resultHappyCase = splitter.computeBalances(expensesHappyCase);
        System.out.println("\nTest for Happy Case : Each participant's net]\n");
        for(String name : names) {
            System.out.println(name + " : " + resultHappyCase.getAmount(name));
        }
        assertEquals(resultHappyCase.getAmount("Alice"), BigDecimal.valueOf(-23.34));
        assertEquals(resultHappyCase.getAmount("Bob"), BigDecimal.valueOf(26.67));
        assertEquals(resultHappyCase.getAmount("Cara"), BigDecimal.valueOf(-3.33));

        // TODO: Implement Test case at least one edge-case test

        // 1. Case of the amount is negative -> pass that expense

        ArrayList<Expense> edgeCaseNegative = new ArrayList<Expense>();

        Expense expense04 = new Expense("2025-10-01",
                "Alice", BigDecimal.valueOf(-100.00),
                "USD",names,"Food","Pizza night");


        Expense expense05 = new Expense("2025-10-02",
                "Bob", BigDecimal.valueOf(80.00),
                "USD",names,"Transport","Taxi + subway");

        Expense expense06 = new Expense("2025-10-03",
                "Cara", BigDecimal.valueOf(90.00),
                "USD",names,"Groceries","Market");

        edgeCaseNegative.add(expense04);
        edgeCaseNegative.add(expense05);
        edgeCaseNegative.add(expense06);

        Balance resultEdgeNegative = splitter.computeBalances(edgeCaseNegative);
        System.out.println("\nTest for Edge Case : negative amount - Each participant's net]\n");
        for(String name : resultEdgeNegative.getNames()) {
            System.out.println(name + " : " + resultEdgeNegative.getAmount(name));
        }
        assertEquals(resultEdgeNegative.getAmount("Alice"), BigDecimal.valueOf(-56.67));
        assertEquals(resultEdgeNegative.getAmount("Bob"), BigDecimal.valueOf(23.34));
        assertEquals(resultEdgeNegative.getAmount("Cara"), BigDecimal.valueOf(33.33));

        // 2. Case of single participant

        // only Alice pays all
        names.remove("Bob");
        names.remove("Cara");

        ArrayList<Expense> EdgeCaseSingle = new ArrayList<Expense>();
        Expense expense07 = new Expense("2025-10-01",
                "Alice", BigDecimal.valueOf(60.00),
                "USD",names,"Food","Pizza night");


        Expense expense08 = new Expense("2025-10-02",
                "Bob", BigDecimal.valueOf(120.00),
                "USD",names,"Transport","Taxi + subway");

        Expense expense09 = new Expense("2025-10-03",
                "Cara", BigDecimal.valueOf(90.00),
                "USD",names,"Groceries","Market");
        EdgeCaseSingle.add(expense07);
        EdgeCaseSingle.add(expense08);
        EdgeCaseSingle.add(expense09);
        Balance resultEdgeSingle = splitter.computeBalances(EdgeCaseSingle);
        System.out.println("\n[Test for Edge Case : Single participants - Each participant's net]\n");

        for(String name : resultEdgeSingle.getNames()) {
            System.out.println(name + " : " + resultEdgeSingle.getAmount(name));
        }
        assertEquals(resultEdgeSingle.getAmount("Alice").setScale(2, RoundingMode.HALF_EVEN), BigDecimal.valueOf(-210.00).setScale(2, RoundingMode.HALF_EVEN));
        assertEquals(resultEdgeSingle.getAmount("Bob").setScale(2, RoundingMode.HALF_EVEN), BigDecimal.valueOf(120.00).setScale(2, RoundingMode.HALF_EVEN));
        assertEquals(resultEdgeSingle.getAmount("Cara").setScale(2, RoundingMode.HALF_EVEN), BigDecimal.valueOf(90.00).setScale(2, RoundingMode.HALF_EVEN));
    }
}