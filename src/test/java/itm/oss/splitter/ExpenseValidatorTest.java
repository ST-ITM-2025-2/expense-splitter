package itm.oss.splitter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This is a test class for ExpenseValidator.
 */
public class ExpenseValidatorTest {

    /**
     * Happy Path test
     * Checks valid expense pass validation
     */
    @Test
    @DisplayName("Valid expense should be valid")
    void validExpense() {
        // Valid expense
        Expense e = new Expense("2025-10-23", "Juyoung", new BigDecimal("100.00"),
                "USD", new ArrayList<>(Arrays.asList("Obregon", "Soyeon")), "Food", "Lunch");
        boolean result = isValid(e); //used isValid method to simplify code , result should be true
        assertTrue(result, "Valid expense should pass validation");
    }

    /**
     * Happy Path test
     * Checks validvexpense case with multiple participants
     */
    @Test
    @DisplayName("Valid case with multiple participants")
    void testValidMultipleParticipants() {
        Expense e = new Expense("2025-10-23", "Soyeon", new BigDecimal("250.00"),
                "EUR", new ArrayList<>(Arrays.asList("Juyoung", "Gyeongyoon", "Sihwan")),
                "OSS Team Project", "Dinner together");
        boolean result = isValid(e); //result should be true
        assertTrue(result, "Valid expense with multiple participants should pass validation");
    }



    /**
     * Edge Case test
     * Checks blank payer
     */
    @Test
    @DisplayName("Blank payer should fail")
    void testBlankPayer() {
        Expense e = new Expense("2025-10-23", "", new BigDecimal("100.00"),
                "USD", new ArrayList<>(Arrays.asList("Winter")), "Food", "fresh food");
        boolean result = isValid(e); //result should be false
        assertFalse(result, "Payer is Empty! Please add payer");
    }

    /**
     * Edge Case test
     * Checks null payer
     */
    @Test
    @DisplayName("Null payer should fail")
    void testNullPayer() {
        Expense e = new Expense("2025-10-23", null, new BigDecimal("100.00"),
                "USD", new ArrayList<>(Arrays.asList("Moon Hyun Bin")), "Food", "nongmin sundae");
        boolean result = isValid(e); //result should be false
        assertFalse(result, "Payer is Empty! Please add payer");
    }


    /**
     * Edge Case test
     * Checks zero or negative expense 
     */
    @Test
    @DisplayName("Zero or negative amount should fail")
    void testZeroOrNegativeAmount() {
        // 1. zero amount
        Expense e1 = new Expense("2025-10-23", "Gyeongyoon", BigDecimal.ZERO,
                "USD", new ArrayList<>(Arrays.asList("Karina")), "Food", "fresh food");
        // 2. negative amount
        Expense e2 = new Expense("2025-10-23", "RM", new BigDecimal("-10.00"),
                "USD", new ArrayList<>(Arrays.asList("Moon Dong Ju")), "Food", "Snack");

        // isValid() should be false
        assertFalse(isValid(e1), "Amount should be larger than 0");
        assertFalse(isValid(e2), "Amount should be larger than 0");
    }


    /**
     * Edge Case test
     * Checks null amount
     */
    @Test
    @DisplayName("Null amount should fail")
    void testNullAmount() {
        Expense e = new Expense("2025-10-23", "Gyeongyoon", null,
                "USD", new ArrayList<>(Arrays.asList("Jung Woo Joo")), "Food", "Lunch");
        boolean result = isValid(e); //result should be false
        assertFalse(result, "Amount should be larger than 0");
    }

    /**
     * Edge Case test
     * Checks null participant name
     */
    @Test
    @DisplayName("Null participant name should fail")
    void testNullParticipantName() {
        ArrayList<String> participants = new ArrayList<>();
        participants.add(null);
        participants.add("Soyeon");
        Expense e = new Expense("2025-10-23", "Lee Yongil", new BigDecimal("100.00"),
                "USD", participants, "Study", "CS Project");
        boolean result = isValid(e); //result should be false
        assertFalse(result, "Participant name cannot be blank.");
    }


    /**
     * Edge Case test
     * Checks blank currency
     */
    @Test
    @DisplayName("Blank currency should fail")
    void testBlankCurrency() {
        Expense e = new Expense("2025-10-23", "Obregon", new BigDecimal("100.00"),
                "", new ArrayList<>(Arrays.asList("Bob")), "IT", "notebook");
        boolean result = isValid(e); // result should be false
        assertFalse(result, "Currency is Empty! Please add currency");
    }

    /**
     * Edge Case test
     * Checks null currency
     */
    @Test
    @DisplayName("Null currency should fail")
    void testNullCurrency() {
        Expense e = new Expense("2025-10-23", "Juyoung", new BigDecimal("100.00"),
                null, new ArrayList<>(Arrays.asList("Gyeongyoon")), "Travel", "Ticket");
        boolean result = isValid(e); //result should be false
        assertFalse(result, "Currency is Empty! Please add currency");
    }

    /**
     * Edge Case test
     * Checks empty participants list
     */
    @Test
    @DisplayName("Empty participants list should fail")
    void testEmptyParticipants() {
        Expense e = new Expense("2025-10-23", "kwon hyeok yoon", new BigDecimal("100.00"),
                "USD", new ArrayList<>(), "IT", "PC");
        boolean result = isValid(e); // result should be false.
        assertFalse(result, "Participants list cannot be empty.");
    }

    /**
     * Edge Case test
     * Checks blank name inside the participants list
     */
    @Test
    @DisplayName("Blank participant name should fail")
    void testBlankParticipantName() {
        Expense e = new Expense("2025-10-23", "G-Dragon", new BigDecimal("100.00"),
                "USD", new ArrayList<>(Arrays.asList("Bob", "")), "Music", "headphone");
        boolean result = isValid(e); // result should be false.
        assertFalse(result, "Participant name cannot be blank.");
    }

    /**
     * Edge Case test
     * checks blank date
     */
    @Test
    @DisplayName("Blank date should fail")
    void testBlankDate() {
        Expense e = new Expense("", "Soyeon", new BigDecimal("100.00"),
                "USD", new ArrayList<>(Arrays.asList("Gyeong yoon")), "Cosmetic", "BB cream");
        boolean result = isValid(e); // result should be false.
        assertFalse(result, "Date cannot be blank.");
    }
    
    /**
     * Edge Case test
     * Checks null date
     */
    @Test
    @DisplayName("Null date should fail")
    void testNullDate() {
        Expense e = new Expense(null, "RM", new BigDecimal("100.00"),
                "USD", new ArrayList<>(Arrays.asList("Jin")), "Music", "Concert");
        boolean result = isValid(e); //result should be false
        assertFalse(result, "Date cannot be blank.");
    }

    /**
     * <<Helper method isValid()>>
     * 
     * Checks if an expense is valid.
     * Calls ExpenseValidator.validate() inside a try-catch block.
     *
     * @param e the Expense object to check
     * @return true if valid (no error), false if invalid (an error was thrown).
     */
    private boolean isValid(Expense e) {
        try {
            ExpenseValidator.validate(e); // calling validation method
            return true; // if no exception, return true
        } catch (Exception ex) {
            return false; // if exception (IllegalArgumentException) happens, return false
        }
    }
}