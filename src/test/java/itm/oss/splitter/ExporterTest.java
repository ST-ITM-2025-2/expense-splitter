package itm.oss.splitter;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import java.math.*;

public class ExporterTest {

    @Test
    public void writePaymentsCsv_createsFileWithHeader() throws Exception {
        
        ArrayList<Payment> pays = new ArrayList<>();
        pays.add(new Payment("Alice", "Bob",  new BigDecimal("30.0")));
        
        pays.add(new Payment("Charlie", "Alice",  new BigDecimal("33.3333333")));

        pays.add(new Payment("Hong", "Lee",  new BigDecimal("2500.0")));

        pays.add(new Payment(null, null, null));

        String path = "data/expense.sample.csv";

        Exporter.writePaymentsCsv(path, pays);

        File createdFile = new File(path);
        assertTrue(createdFile.exists(), "csv file needs to be created");

        String content = Files.readString(createdFile.toPath());
        assertTrue(content.contains("from,to,amount"), "needs to include csv header");
        assertTrue(content.contains("Alice,Bob,30"), "needs to include payment record");
    }

    @Test
    public void writePaymentsCsv_handlesEmptyList() throws Exception {
        
        ArrayList<Payment> pays = new ArrayList<>();
        String path = "data/test_empty.csv";

        Exporter.writePaymentsCsv(path, pays);

        File createdFile = new File(path);
        assertTrue(createdFile.exists(), "Though empty, file needs to be created");

        String content = Files.readString(createdFile.toPath()).trim();
        assertEquals("from,to,amount", content, "When the list is empty, only header should be present");
    }
    	@Test
	public void writePaymentsCsv_handlesNegativeAmount() throws Exception {
		// Prepare a Payment list with a negative amount
		ArrayList<Payment> pays = new ArrayList<>();
		pays.add(new Payment("Bob", "Charlie", new BigDecimal("-15.5")));

		// Specify CSV file path (for edge case testing)
		String path = "data/edgecases/negative.csv";

		// Create parent directory if it does not exist
		File file = new File(path);
		File parent = file.getParentFile();
		if (parent != null && !parent.exists()) {
			parent.mkdirs();
		}

		// Delete the existing file before running the test
		if (file.exists()) {
			file.delete();
		}

		// Execute the Exporter functionality
		Exporter.writePaymentsCsv(path, pays);

		// Check if the file was created
		assertTrue(file.exists(), "CSV file should be created.");

		// Read the file and verify its content
		java.util.List<String> lines = Files.readAllLines(file.toPath());

		// The CSV should contain 2 lines (header + 1 data row)
		assertEquals(2, lines.size(), "CSV should contain header and one data row.");

		// The first line should be the header
		assertEquals("from,to,amount", lines.get(0));

		// The second line should correctly show the negative amount (-15.50)
		assertEquals("Bob,Charlie,-15.50", lines.get(1));
	}

}




