package itm.oss.splitter;

import java.io.*;
import java.math.RoundingMode;
import java.util.ArrayList;

public class Exporter {

  public static void writePaymentsCsv(String path, ArrayList<Payment> pays) throws IOException {
    // TODO (Issue 9): write CSV with header: from,to,amount

    File file = new File(path);

    // Automatically close using try-with-resources
    try (FileWriter writer = new FileWriter(file)) {

      // CSV header
      writer.write("from,to,amount");
      if( pays == null || pays.isEmpty()) {
        return;
      }

      // Write data only when the list is not null or empty
      // if value is null, write empty string
        for (int i = 0; i < pays.size(); i++) {
          Payment p = pays.get(i);
          String from = (p.getFrom() == null) ? "" : p.getFrom();
          String to = (p.getTo() == null) ? "" : p.getTo();
          String amount = "";

          if(p.getAmount() != null) {
            amount = p.getAmount().setScale(2, RoundingMode.HALF_EVEN).toString();
          }
          writer.write(from + "," + to + "," + amount + "\n");
        }

    }
  }

}
