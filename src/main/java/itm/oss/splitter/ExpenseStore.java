package itm.oss.splitter;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;
import java.nio.file.Paths;

public class ExpenseStore {

  public ArrayList<Expense> load(String path) throws IOException {
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
