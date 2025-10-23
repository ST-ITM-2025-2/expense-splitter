package itm.oss.splitter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Settlement {

    public static ArrayList<Payment> suggest(Balance b) {
        ArrayList<Payment> pay = new ArrayList<>();

        Map<String, BigDecimal> map = b.asMap();
        List<Map.Entry<String, BigDecimal>> neg = new ArrayList<>();
        List<Map.Entry<String, BigDecimal>> pos = new ArrayList<>();
        BigDecimal eps = new BigDecimal("0.005");

        for (Map.Entry<String, BigDecimal> e : map.entrySet()) {
            BigDecimal v = e.getValue();
            if (v == null) continue;
            if (v.compareTo(BigDecimal.ZERO) < 0 && v.abs().compareTo(eps) > 0)
                neg.add(e);
            else if (v.compareTo(BigDecimal.ZERO) > 0 && v.abs().compareTo(eps) > 0)
                pos.add(e);
        }

        neg.sort(Comparator.comparing(Map.Entry::getValue));
        pos.sort((a, b2) -> b2.getValue().compareTo(a.getValue()));

        int i = 0, j = 0;
        while (i < neg.size() && j < pos.size()) {
            Map.Entry<String, BigDecimal> d = neg.get(i);
            Map.Entry<String, BigDecimal> c = pos.get(j);

            BigDecimal debt = d.getValue().abs();
            BigDecimal cred = c.getValue();
            BigDecimal amt = debt.min(cred);

            if (amt.compareTo(eps) <= 0) break;

            pay.add(new Payment(d.getKey(), c.getKey(), amt));

            d.setValue(d.getValue().add(amt));
            c.setValue(c.getValue().subtract(amt));

            if (d.getValue().abs().compareTo(eps) <= 0) i++;
            if (c.getValue().abs().compareTo(eps) <= 0) j++;
        }

        return pay;
    }
}