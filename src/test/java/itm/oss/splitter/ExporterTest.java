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
        // 준비 (Arrange)
        ArrayList<Payment> pays = new ArrayList<>();
        pays.add(new Payment("Alice", "Bob",  new BigDecimal("30.0")));
        String path = "data/expense.sample.csv";

        // 실행 (Act)
        Exporter.writePaymentsCsv(path, pays);

        // 검증 (Assert)
        File file = new File(path);
        assertTrue(file.exists(), "CSV 파일이 생성되어야 한다");

        // 파일 내용 확인
        String content = Files.readString(file.toPath());
        assertTrue(content.contains("from,to,amount"), "CSV 헤더가 포함되어야 한다");
        assertTrue(content.contains("Alice,Bob,30"), "결제 내역이 포함되어야 한다");
    }

    @Test
    public void testWritePaymentsCsv_handlesEmptyList() throws Exception {
        // 준비 (Arrange)
        ArrayList<Payment> pays = new ArrayList<>();
        String path = "data/test_empty.csv";

        // 실행 (Act)
        Exporter.writePaymentsCsv(path, pays);

        // 검증 (Assert)
        File file = new File(path);
        assertTrue(file.exists(), "빈 리스트여도 파일은 생성되어야 한다");

        // 내용이 헤더만 있는지 확인
        String content = Files.readString(file.toPath()).trim();
        assertEquals("from,to,amount", content, "빈 리스트일 때는 헤더만 있어야 한다");
    }
}
