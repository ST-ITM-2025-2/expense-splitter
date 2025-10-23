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
        pays.add(new Payment("Hong", "Lee",  new BigDecimal("2500.0")));
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
    @Test
    public void testWritePaymentsCsv_withDecimalAmounts() throws Exception {
    // 소수점 금액 테스트
        ArrayList<Payment> pays = new ArrayList<>();
        pays.add(new Payment("Alice", "Bob", new BigDecimal("33.3333")));

        String path = "data/expense.decimal.csv";
        Exporter.writePaymentsCsv(path, pays);

    // 파일 생성 확인
        File file = new File(path);
        assertTrue(file.exists(), "CSV file should be created");

    // 소수점 처리 검증
        String content = Files.readString(file.toPath());
        assertTrue(content.contains("33.3333") || content.contains("33.33"),
                   "Should properly handle decimal amounts");
}

}


