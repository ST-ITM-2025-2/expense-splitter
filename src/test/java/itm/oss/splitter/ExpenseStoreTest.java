package itm.oss.splitter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class ExpenseStoreTest {

    @TempDir
    Path tempDir;

    @Test
    void load_happyPath() throws Exception {
        // Create a temporary CSV file with test data
        String csvData =
            "date,payer,amount,currency,participants,category,notes\n" +
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

        // Expense 1
        Expense e1 = list.get(0);
        assertEquals("2025-10-01", e1.getDate());
        assertEquals("Alice", e1.getPayer());
        assertEquals(new BigDecimal("60.00"), e1.getAmount());
        assertEquals("USD", e1.getCurrency());
        assertEquals(List.of("Alice", "Bob", "Cara"), e1.getParticipants());
        assertEquals("Food", e1.getCategory());
        assertEquals("Pizza night", e1.getNotes());

        // Expense 2
        Expense e2 = list.get(1);
        assertEquals("2025-10-02", e2.getDate());
        assertEquals("Bob", e2.getPayer());
        assertEquals(new BigDecimal("120.00"), e2.getAmount());
        assertEquals("USD", e2.getCurrency());
        assertEquals(List.of("Alice", "Bob", "Cara"), e2.getParticipants());
        assertEquals("Transport", e2.getCategory());
        assertEquals("Taxi + subway", e2.getNotes());

        // Expense 3e
        Expense e3 = list.get(2);
        assertEquals("2025-10-03", e3.getDate());
        assertEquals("Cara", e3.getPayer());
        assertEquals(new BigDecimal("90.00"), e3.getAmount());
        assertEquals("USD", e3.getCurrency());
        assertEquals(List.of("Alice", "Bob", "Cara"), e3.getParticipants());
        assertEquals("Groceries", e3.getCategory());
        assertEquals("Market", e3.getNotes());
    }

    //empty list test
    @Test
    void loadEmptyList()throws Exception{
        Path emptyCsv = tempDir.resolve("empty.cvs");
        Files.write(emptyCsv, new  byte[0]); //empty file

        ExpenseStore store = new ExpenseStore();
        ArrayList<Expense> list = store.load(emptyCsv.toString());

        assertEquals(0, list.size(), "No expenses because the CSV file is empty");
    }

    //problem with the list
    @Test
    void loadInvalidLine() throws Exception {
        String badData =
                "date,payer,amount,currency,participants,category,notes\n" +
                        "invalid_line\n" + // ligne invalide
                        "2025-10-05,Alice,50.00,USD,Alice;Bob,Food,Pasta\n";

        Path csvFile = tempDir.resolve("malformed.csv");
        Files.write(csvFile, badData.getBytes(StandardCharsets.UTF_8));

        ExpenseStore store = new ExpenseStore();
        ArrayList<Expense> list = store.load(csvFile.toString());

        assertEquals(1, list.size(), "Ignore malformed file");
    }

}
