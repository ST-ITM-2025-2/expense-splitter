package itm.oss.splitter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ExpenseStore {

  public static final String HEADER = "date,payer,amount,currency,participants,category,notes";

  public List<Expense> load(String path) throws IOException {
    // TODO (Issue 1): parse CSV file into Expense list.
    // Format: date,payer,amount,currency,participants,category,notes
    // participants are semicolon-separated.

    // path = "data/expenses.csv"
    ArrayList<Expense> returnList = new ArrayList<>() ; // the list that we will return

    try (Scanner scanner = new Scanner(Paths.get(path))) {
      if(scanner.hasNextLine()){ // because the first row of the csv file is headers, so we have to get rid of it 
        scanner.nextLine(); // read one line and throw it (do not store it anywhere)
      }
      // we read the file until all lines have been read
      int row_num = 1;

     
      while (scanner.hasNextLine()) {

        try{ // try block that handles the error in single line

          // we read one line
          String expenseString = scanner.nextLine();
          String[] expenseArray = expenseString.split(",", -1);

          // possible errors in single line :
          // 1. a line has less or more than 7 elements. 
          // 2. in String amount(expenseArray[2]), the String has some characters that are not either number or point(.)

          if(expenseArray.length != 7){
            throw new IllegalArgumentException("The number of argument in this line is not 7");
          }

          String date = expenseArray[0].trim();
          String payer = expenseArray[1].trim();
          BigDecimal amount = new BigDecimal(expenseArray[2].trim()); // if this is unable, NumberFormatException occurs
          String currency = expenseArray[3].trim();

          ArrayList<String> participants = new ArrayList<>(); // expenseArray[4]
          String[] participantArray = expenseArray[4].split(";");
          for(String participant : participantArray){
            participants.add(participant.trim());
          }

          String category = expenseArray[5].trim();
          String notes = expenseArray[6].trim();

          Expense e = new Expense(date, payer, amount, currency, participants, category, notes);
          returnList.add(e);

        }catch(NumberFormatException e){
          System.out.println("Error : Cannot change the String into BigDecimal, so skip row "+row_num);
        }catch(IllegalArgumentException e){
          System.out.println("Error : The given argument does not match the format, so skip row "+row_num);
        }catch(Exception e){
          System.out.println("Error: " + e.getMessage());
        }finally{
          row_num++ ;
        }

      } // the end of while loop

    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }

    return returnList ;
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
    
    try (FileWriter fw = new FileWriter(file, StandardCharsets.UTF_8, true); // append mode
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
