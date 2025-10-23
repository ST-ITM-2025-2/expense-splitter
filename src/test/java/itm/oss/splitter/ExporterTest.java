package itm.oss.splitter;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import java.math.*;

public class ExporterTest {

    @Test
    public void testWritePaymentsCsv_createsFileWithHeader() throws Exception {
        
        ArrayList<Payment> pays = new ArrayList<>();
        pays.add(new Payment("Alice", "Bob",  new BigDecimal("30.0")));
        pays.add(new Payment("Bob", "Charlie",  new BigDecimal("20.0")));
        pays.add(new Payment("Charlie", "Alice",  new BigDecimal("33.3333333")));
        String path = "data/expense.sample.csv";

        Exporter.writePaymentsCsv(path, pays);

        File file = new File(path);
        assertTrue(file.exists(), "csv file needs to be created");

        String content = Files.readString(file.toPath());
        assertTrue(content.contains("from,to,amount"), "needs to include csv header");
        assertTrue(content.contains("Alice,Bob,30"), "needs to include payment record");
    }

    @Test
    public void testWritePaymentsCsv_handlesEmptyList() throws Exception {
        
        ArrayList<Payment> pays = new ArrayList<>();
        String path = "data/test_empty.csv";

        Exporter.writePaymentsCsv(path, pays);

        File file = new File(path);
        assertTrue(file.exists(), "Though empty, file needs to be created");

        String content = Files.readString(file.toPath()).trim();
        assertEquals("from,to,amount", content, "When the list is empty, only header should be present");
    }
}


