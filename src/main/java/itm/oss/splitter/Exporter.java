package itm.oss.splitter;

import java.io.*;
import java.math.RoundingMode;
import java.util.ArrayList;

public class Exporter {

  public static void writePaymentsCsv(String path, ArrayList<Payment> pays) throws IOException {
    // TODO (Issue 9): write CSV with header: from,to,amount

    File file = new File(path);

    // try-with-resources로 자동 close
    try (FileWriter writer = new FileWriter(file)) {

      // CSV header
      writer.write("from,to,amount");

      // 리스트가 비어 있지 않을 때만 개행 및 내용 작성
      if (pays != null && !pays.isEmpty()) {
        writer.write("\n");

        for (int i = 0; i < pays.size(); i++) {
          Payment p = pays.get(i);
          String from = p.getFrom();
          String to = p.getTo();
          String amount = p.getAmount().setScale(2, RoundingMode.HALF_EVEN).toString();
          writer.write(from + "," + to + "," + amount + "\n");
        }
      }
    }
  }

}
