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

  String expenseToCSV(Expense e) {
        // participants are semicolon-separated
        String participantsStr = String.join(";", e.getParticipants());
        
        // CSV format
        return String.join(",",
                e.getDate(),
                e.getPayer(),
                String.valueOf(e.getAmount()),
                e.getCurrency(),
                participantsStr,
                e.getCategory(),
                e.getNotes()
        );
    }

  // Optional helper
  Expense parseLine(String line) {
    // split by comma (basic), then build Expense (participants split by ';')
    throw new UnsupportedOperationException("parseLine() not implemented yet");
  }
  
}
