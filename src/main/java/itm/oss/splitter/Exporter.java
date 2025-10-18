package itm.oss.splitter;

import java.io.*;
import java.math.RoundingMode;
import java.util.ArrayList;

public class Exporter {

  public static void writePaymentsCsv(String path, ArrayList<Payment> pays) throws IOException {
    // TODO (Issue 9): write CSV with header: from,to,amount

    FileWriter writer = new FileWriter(path);

    // CSV header
    writer.write("from,to,amount\n");

    // Write each payment line

    for(int i = 0; i < pays.size(); i++) { // this can be expressed as (Payment p : pays) also... feedback? >.<
      Payment p = pays.get(i);
      String from = p.getFrom();
      String to = p.getTo();
      String amount = p.getAmount().setScale(2, RoundingMode.HALF_EVEN).toString();
      writer.write(from + "," + to + "," + amount + "\n");
    }

    writer.close();
  }
  
}
