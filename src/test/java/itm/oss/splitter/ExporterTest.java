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
    
}




