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
        // Valid expense object creation
        Expense e = new Expense("2025-10-23", "Juyoung", new BigDecimal("100.00"),
                "USD", new ArrayList<>(Arrays.asList("Obregon", "Soyeon")), "Food", "Lunch");
        // Verify that validation runs successfully without throwing any exception
        assertDoesNotThrow(() -> ExpenseValidator.validate(e), "Valid expense should pass validation");
    }

    /**
     * Happy Path test
     * Checks valid expense case with multiple participants
     */
    @Test
    @DisplayName("Valid case with multiple participants")
    void testValidMultipleParticipants() {
        // Expense with multiple valid participants
        Expense e = new Expense("2025-10-23", "Soyeon", new BigDecimal("250.00"),
                "EUR", new ArrayList<>(Arrays.asList("Juyoung", "Gyeongyoon", "Sihwan")),
                "OSS Team Project", "Dinner together");
        // Verify that validation runs successfully
        assertDoesNotThrow(() -> ExpenseValidator.validate(e), "Valid expense with multiple participants should pass validation");
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

        // Verify that IllegalArgumentException is thrown
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> ExpenseValidator.validate(e));
        // Verify the specific error message content
        assertEquals("Payer is Empty! Please add payer", thrown.getMessage());
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

        // Verify that IllegalArgumentException is thrown
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> ExpenseValidator.validate(e));
        // Verify the specific error message content
        assertEquals("Payer is Empty! Please add payer", thrown.getMessage());
    }

    /**
     * Edge Case test
     * Checks zero or negative expense amount
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

        // Verify exception and message for zero amount
        IllegalArgumentException thrown1 = assertThrows(IllegalArgumentException.class,
                () -> ExpenseValidator.validate(e1));
        assertEquals("Amount should be larger than 0", thrown1.getMessage());

        // Verify exception and message for negative amount
        IllegalArgumentException thrown2 = assertThrows(IllegalArgumentException.class,
                () -> ExpenseValidator.validate(e2));
        assertEquals("Amount should be larger than 0", thrown2.getMessage());
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

        // Verify exception and message
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> ExpenseValidator.validate(e));
        assertEquals("Amount should be larger than 0", thrown.getMessage());
    }

    /**
     * Edge Case test
     * Checks null participant name within the list
     */
    @Test
    @DisplayName("Null participant name should fail")
    void testNullParticipantName() {
        ArrayList<String> participants = new ArrayList<>();
        participants.add(null); // Contains null
        participants.add("Soyeon");
        Expense e = new Expense("2025-10-23", "Lee Yongil", new BigDecimal("100.00"),
                "USD", participants, "Study", "CS Project");

        // Verify exception and message
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> ExpenseValidator.validate(e));
        assertEquals("Participant name cannot be blank.", thrown.getMessage());
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

        // Verify exception and message
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> ExpenseValidator.validate(e));
        assertEquals("Currency is Empty! Please add currency", thrown.getMessage());
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

        // Verify exception and message
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> ExpenseValidator.validate(e));
        assertEquals("Currency is Empty! Please add currency", thrown.getMessage());
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

        // Verify exception and message
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> ExpenseValidator.validate(e));
        assertEquals("Participants list cannot be empty.", thrown.getMessage());
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

        // Verify exception and message
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> ExpenseValidator.validate(e));
        assertEquals("Participant name cannot be blank.", thrown.getMessage());
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

        // Verify exception and message
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> ExpenseValidator.validate(e));
        assertEquals("Date is Empty! Please add date", thrown.getMessage());
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

        // Verify exception and message
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> ExpenseValidator.validate(e));
        assertEquals("Date is Empty! Please add date", thrown.getMessage());
    }

    /**
     * Edge Case test
     * Checks null expense object
     */
    @Test
    @DisplayName("Null expense object should fail")
    void testNullExpenseObject() {
        Expense e = null;

        // Verify exception and message
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> ExpenseValidator.validate(e));
        assertEquals("Expense cannot be null.", thrown.getMessage());
    }
}