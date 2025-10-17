package itm.oss.splitter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class Reports {

    public SimpleMap totalsByCategory(ArrayList<Expense> xs) {
        // TODO (Issue 6): sum amounts per category
        throw new UnsupportedOperationException("totalsByCategory() not implemented yet");
    }

    public SimplePersonSummaryMap perPerson(ArrayList<Expense> xs) {
        SimplePersonSummaryMap map = new SimplePersonSummaryMap();

        for (Expense x : xs) {
            int count = x.getParticipants().size();
            if (count == 0) continue;

            BigDecimal owedPerPerson = x.getAmount()
                    .divide(new BigDecimal(count), 2, RoundingMode.HALF_UP);

            for (String name : x.getParticipants()) {
                if (map.get(name) == null) {
                    map.put(name, new PersonSummary(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO ));
                }
                PersonSummary ps = map.get(name);

                if (name.equals(x.getPayer())) {
                    ps.addPaid(x.getAmount());
                }

                ps.addOwed(owedPerPerson);
            }
        }

        return map;
    }

}
