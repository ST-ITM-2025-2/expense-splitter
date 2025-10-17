package itm.oss.splitter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class ExpenseStore {

    public ArrayList<Expense> load(String path) throws IOException {
        // TODO (Issue 1): parse CSV file into Expense list.
        // Format: date,payer,amount,currency,participants,category,notes
        // participants are semicolon-separated.
        String line = "";
        ArrayList<Expense> expenses = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            while ((line = br.readLine()) != null) {
                String date = "";
                String payer = "";
                BigDecimal amount = null;
                String currency = "";
                String category = "";
                String notes = "";

                String[] values = line.split(",");
                if (Objects.equals(values[0].trim(), "date")){
                    continue;
                }

                date = values[0];
                payer = values[1];
                amount = new BigDecimal(values[2]);
                currency = values[3];
                ArrayList<String> participants = new ArrayList<>(Arrays.asList(values[4].split(";")));
                category = values[5];
                notes = values[6];

                Expense expense = new Expense(date, payer, amount, currency, participants, category, notes);
                expenses.add(expense);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return expenses;
  }

  public void append(String path, Expense e) throws IOException {
    // TODO (Issue 2): append a row to CSV (create file with header if missing).
    throw new UnsupportedOperationException("append() not implemented yet");
  }

  // Optional helper
  Expense parseLine(String line) {
    // split by comma (basic), then build Expense (participants split by ';')
    throw new UnsupportedOperationException("parseLine() not implemented yet");
  }
}
