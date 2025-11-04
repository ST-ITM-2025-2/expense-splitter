package itm.oss.splitter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ExpenseStore {

  public static final String HEADER = "date,payer,amount,currency,participants,category,notes";

  public ArrayList<Expense> load(String path) throws IOException {
    // TODO (Issue 1): parse CSV file into Expense list.
    // Format: date,payer,amount,currency,participants,category,notes
    // participants are semicolon-separated.
    throw new UnsupportedOperationException("load() not implemented yet");
  }

  /**
   * Append a new expense to CSV file.
   * Creates file with header if it doesn't exist.
   */
  public void append(String path, Expense e) throws IOException {
    
    File file = new File(path);
    
    // Create parent directories if they don't exist
    File parentDir = file.getParentFile();
    if (parentDir != null && !parentDir.exists()) {
      parentDir.mkdirs();
    }
    
    boolean fileExists = file.exists() && file.length() > 0;
    
    try (FileWriter fw = new FileWriter(file, true); // append mode
         PrintWriter pw = new PrintWriter(fw)) {
      
      // Write header if file is new or empty
      if (!fileExists) {
        pw.println(HEADER);
      }
      
      // Append the expense row
      pw.println(expenseToCSV(e));
    }
  }

  /**
   * (수정됨)
   * Expense 객체를 CSV 형식의 문자열로 변환합니다.
   * 쉼표(,)나 큰따옴표(")가 포함된 필드는 escapeCsvField를 사용하여 처리합니다.
   */
  String expenseToCSV(Expense e) {
        // participants 목록을 먼저 세미콜론(;)으로 연결
        String participants = String.join(";", e.getParticipants());

        // 각 필드를 헬퍼 메소드를 이용해 이스케이프 처리
        String data = String.join(",", 
            escapeCsvField(e.getDate()), 
            escapeCsvField(e.getPayer()), 
            e.getAmount().toString(), // 숫자는 이스케이프 필요 없음
            escapeCsvField(e.getCurrency()), 
            escapeCsvField(participants), 
            escapeCsvField(e.getCategory()), 
            escapeCsvField(e.getNotes())
        );
        
        return data;
    }

  // Optional helper
  Expense parseLine(String line) {
    // split by comma (basic), then build Expense (participants split by ';')
    throw new UnsupportedOperationException("parseLine() not implemented yet");
  }

  /**
   * CSV 필드에 특수 문자(쉼표, 큰따옴표, 줄바꿈)가 포함된 경우
   * RFC 4180 표준에 맞게 이스케이프 처리합니다.
   * 1. 큰따옴표(")를 두 개의 큰따옴표("")로 치환합니다.
   * 2. 필드 전체를 큰따옴표(")로 감쌉니다.
   */
  private String escapeCsvField(String field) {
    if (field == null || field.isEmpty()) {
        return "";
    }

    // 특수 문자를 포함하고 있는지 확인
    boolean needsQuotes = field.contains(",") || field.contains("\"") || field.contains("\n");

    if (needsQuotes) {
        // 1. 기존 큰따옴표를 두 개로 치환
        String escapedField = field.replace("\"", "\"\"");
        // 2. 필드 전체를 큰따옴표로 감쌈
        return "\"" + escapedField + "\"";
    } else {
        return field;
    }
  }
}