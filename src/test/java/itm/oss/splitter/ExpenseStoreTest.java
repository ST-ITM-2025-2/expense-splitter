package itm.oss.splitter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class ExpenseStoreTest {

    public static void assertExpenseEquals(Expense e, String date, String payer, String amount, String currency,
            List<String> participants, String category, String note) {
        assertEquals(date, e.getDate());
        assertEquals(payer, e.getPayer());
        assertEquals(new BigDecimal(amount), e.getAmount());
        assertEquals(currency, e.getCurrency());
        assertEquals(participants, e.getParticipants());
        assertEquals(category, e.getCategory());
        assertEquals(note, e.getNotes());
    }

    @TempDir
    Path tempDir;

    @Test
    void loadHappyPath() throws Exception {
        // Create a temporary CSV file with test data
        String csvData = "date,payer,amount,currency,participants,category,notes\n" +
                "2025-10-01,Alice,60.00,USD,Alice;Bob;Cara,Food,Pizza night\n" +
                "2025-10-02,Bob,120.00,USD,Alice;Bob;Cara,Transport,Taxi + subway\n" +
                "2025-10-03,Cara,90.00,USD,Alice;Bob;Cara,Groceries,Market\n";

        Path csvFile = tempDir.resolve("expenses.csv");
        Files.write(csvFile, csvData.getBytes(StandardCharsets.UTF_8));

        // Call method under test
        ExpenseStore store = new ExpenseStore();
        ArrayList<Expense> list = store.load(csvFile.toString());

        // 3️⃣ Verify loaded data
        assertEquals(3, list.size());

        // expense 1
        assertExpenseEquals(
                list.get(0),
                "2025-10-01", "Alice", "60.00", "USD",
                List.of("Alice", "Bob", "Cara"), "Food", "Pizza night");

        // expense 2
        assertExpenseEquals(
                list.get(1),
                "2025-10-02", "Bob", "120.00", "USD",
                List.of("Alice", "Bob", "Cara"), "Transport", "Taxi + subway");

        // expense 3
        assertExpenseEquals(
                list.get(2),
                "2025-10-03", "Cara", "90.00", "USD",
                List.of("Alice", "Bob", "Cara"), "Groceries", "Market");
    }

    // empty list test
    @Test
    void loadEmptyList() throws Exception {
        Path emptyCsv = tempDir.resolve("empty.cvs");
        Files.write(emptyCsv, new byte[0]); // empty file

        ExpenseStore store = new ExpenseStore();
        ArrayList<Expense> list = store.load(emptyCsv.toString());

        assertEquals(0, list.size(), "No expenses because the CSV file is empty");
    }

    // problem with the list
    @Test
    void loadInvalidLine() throws Exception {
        String badData = "date,payer,amount,currency,participants,category,notes\n" +
                "invalid_line\n" + // ligne invalide
                "2025-10-05,Alice,50.00,USD,Alice;Bob,Food,Pasta\n";

        Path csvFile = tempDir.resolve("malformed.csv");
        Files.write(csvFile, badData.getBytes(StandardCharsets.UTF_8));

        ExpenseStore store = new ExpenseStore();
        ArrayList<Expense> list = store.load(csvFile.toString());

        assertEquals(1, list.size(), "Ignore malformed file");
    }

    //file doesn't exist
    @Test
    void loadFileNotFound() throws Exception {
        ExpenseStore store = new ExpenseStore();
        String nonExistentFile = tempDir.resolve("does_not_exist.csv").toString();

        ArrayList<Expense> list = store.load(nonExistentFile);

        assertEquals(0, list.size(), "Return an empty list when the file doesn't exist");
    }


}
