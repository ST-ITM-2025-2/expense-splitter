package itm.oss.splitter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Reports {

  public SimpleMap totalsByCategory(ArrayList<Expense> xs) {
    LinkedHashMap<String, BigDecimal> totals = new LinkedHashMap<>();

    for (int i = 0; i < xs.size(); i++) {
      Expense e = xs.get(i);
      String category = e.getCategory();

      if (category == null || category.trim().isEmpty()) {
        category = "Uncategorized";
      } else {
        category = category.substring(0, 1).toUpperCase() + category.substring(1).toLowerCase();
      }
      
      BigDecimal amount = e.getAmount();
      BigDecimal current = totals.get(category);
      totals.put(category, current == null ? amount : current.add(amount));
    }

    SimpleMap out = new SimpleMap();
    for (String category : totals.keySet())
      out.put(category, totals.get(category));
    return out;
  }

  public SimplePersonSummaryMap perPerson(ArrayList<Expense> xs) {
    // TODO (Issue 7): compute paidTotal, owedTotal, net per person
    throw new UnsupportedOperationException("perPerson() not implemented yet");
  }
}
