package itm.oss.splitter;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal; // (추가) 테스트 후 정리를 위해 import
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals; // (추가)
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach; // (추가) 파일을 쉽게 읽기 위해 import
import org.junit.jupiter.api.Test;
import java.nio.charset.StandardCharsets;

class ExpenseStoreTest {

    private ExpenseStore store;
    private Expense sampleExpense1;
    private Expense sampleExpense2;

    // 테스트용 파일 이름을 정해둡니다. (프로젝트 폴더 최상위에 생깁니다)
    private static final String TEST_FILE_PATH = "my_test_expenses.csv";

@BeforeEach
    void setUp() {
        store = new ExpenseStore();

        // (수정 1) participants를 ArrayList로 만들기
        ArrayList<String> p1 = new ArrayList<>(Arrays.asList("Bob", "Charlie"));
        
        // (수정 2) participants를 ArrayList로 만들기
        ArrayList<String> p2 = new ArrayList<>(Arrays.asList("Alice"));

        // 테스트에 사용할 샘플 데이터
        sampleExpense1 = new Expense(
                "2025-10-22", "Alice", 
                new BigDecimal("10000.0"), // (수정 3) double 대신 BigDecimal 사용
                "KRW",
                p1, // (수정 1) 만들어둔 ArrayList 전달
                "Food", "Lunch"
        );
        
        sampleExpense2 = new Expense(
                "2025-10-23", "Bob", 
                new BigDecimal("5000.0"), // (수정 3) double 대신 BigDecimal 사용
                "KRW",
                p2, // (수정 2) 만들어둔 ArrayList 전달
                "Coffee", "Morning coffee"
        );
        
        // (중요) 각 테스트 시작 전에 항상 깨끗한 상태로 만들기 위해
        // 테스트 파일을 미리 삭제합니다.
        File testFile = new File(TEST_FILE_PATH);
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    // (추가) @AfterEach : 각 테스트 메서드가 끝날 때마다 실행됩니다.
    // 테스트하면서 만든 파일을 깔끔하게 지워줍니다.
    @AfterEach
    void tearDown() {
        File testFile = new File(TEST_FILE_PATH);
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    /**
     * [테스트 1] append() : 새 파일에 데이터를 처음 추가하는 경우
     * -> 헤더와 데이터가 올바르게 쓰여야 한다.
     */
    @Test
    void append_shouldWriteHeadAndData_whenFileIsNew() throws IOException {
        // 1. Given (준비)
        // (setUp에서 파일은 이미 지워진 상태)
        File testFile = new File(TEST_FILE_PATH);

        // 2. When (실행)
        store.append(TEST_FILE_PATH, sampleExpense1);

        // 3. Then (검증)
        // 파일이 실제로 생겼는지 확인
        assertTrue(testFile.exists(), "append() 호출 후 파일이 생성되어야 합니다.");

        // (선택) 파일 내용을 간단히 읽어서 확인해보기
        try (Scanner scanner = new Scanner(testFile, StandardCharsets.UTF_8)) {
            // 첫 번째 줄 (헤더) 검증
            assertTrue(scanner.hasNextLine(), "헤더 라인이 있어야 합니다.");
            assertEquals(ExpenseStore.HEADER, scanner.nextLine());

            // 두 번째 줄 (데이터) 검증
            String expectedData = "2025-10-22,Alice,10000.0,KRW,Bob;Charlie,Food,Lunch";
            assertTrue(scanner.hasNextLine(), "데이터 라인이 있어야 합니다.");
            assertEquals(expectedData, scanner.nextLine());

            // 더 이상 내용이 없는지 검증
            assertFalse(scanner.hasNextLine(), "헤더와 데이터 1줄 외에 다른 내용이 없어야 합니다.");
        }
    }

    /**
     * [테스트 2] append() : 이미 내용이 있는 파일에 데이터를 추가하는 경우
     * -> 헤더 없이 데이터만 추가되어야 한다.
     */
    @Test
    void append_shouldAppendDataOnly_whenFileExists() throws IOException {
        // 1. Given (준비)
        // 첫 번째 데이터를 미리 추가해둠 (헤더 + 데이터1)
        store.append(TEST_FILE_PATH, sampleExpense1);

        // 2. When (실행)
        // 두 번째 데이터를 추가
        store.append(TEST_FILE_PATH, sampleExpense2);

        // 3. Then (검증)
        File testFile = new File(TEST_FILE_PATH);
        try (Scanner scanner = new Scanner(testFile, StandardCharsets.UTF_8)) {
            // (1) 헤더 검증
            assertEquals(ExpenseStore.HEADER, scanner.nextLine());
            
            // (2) 데이터 1 검증
            String expectedData1 = "2025-10-22,Alice,10000.0,KRW,Bob;Charlie,Food,Lunch";
            assertEquals(expectedData1, scanner.nextLine());
            
            // (3) 데이터 2 검증 (새로 추가된 것)
            String expectedData2 = "2025-10-23,Bob,5000.0,KRW,Alice,Coffee,Morning coffee";
            assertTrue(scanner.hasNextLine(), "두 번째 데이터 라인이 있어야 합니다.");
            assertEquals(expectedData2, scanner.nextLine());

            // (4) 끝
            assertFalse(scanner.hasNextLine(), "총 3줄(헤더, 데이터1, 데이터2)이어야 합니다.");
        }
    }

    /**
     * [테스트 3] append() - Edge Case: 참가자 목록이 비어 있는 경우
     * -> participants 필드가 비어있는 문자열로 올바르게 변환되어야 한다.
     */
    @Test
    void append_edgeCase_zeroParticipants() throws IOException {
        // 1. Given (준비)
        ArrayList<String> emptyParticipants = new ArrayList<>();
        Expense expenseNoParticipants = new Expense(
                "2025-10-24", "Charlie",
                new BigDecimal("1000.0"),
                "USD",
                emptyParticipants, // 참가자 목록이 비어 있음
                "Parking", ""
        );

        // 2. When (실행)
        store.append(TEST_FILE_PATH, expenseNoParticipants);

        // 3. Then (검증)
        File testFile = new File(TEST_FILE_PATH);
        try (Scanner scanner = new Scanner(testFile, StandardCharsets.UTF_8)) {
            // 헤더 건너뛰기
            scanner.nextLine();

            // 데이터 라인 검증: 참가자 필드가 비어 있어야 합니다.
            String expectedData = "2025-10-24,Charlie,1000.0,USD,,Parking,"; // 참가자 필드가 비어 있음 (쉼표 사이에 아무것도 없음)
            
            assertTrue(scanner.hasNextLine());
            assertEquals(expectedData, scanner.nextLine(), "참가자 필드가 비어 있어야 합니다.");
        }
    }
}