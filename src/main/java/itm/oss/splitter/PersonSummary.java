package itm.oss.splitter;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PersonSummary {
    private BigDecimal paidTotal = BigDecimal.ZERO;
    private BigDecimal owedTotal =  BigDecimal.ZERO;
    private BigDecimal net =  BigDecimal.ZERO;

    public PersonSummary(BigDecimal paidTotal, BigDecimal owedTotal, BigDecimal net) {
        this.paidTotal = paidTotal;
        this.owedTotal = owedTotal;
        this.net = net;
    }

    public BigDecimal getPaidTotal() { return paidTotal; }
    public BigDecimal getOwedTotal() { return owedTotal; }
    public BigDecimal getNet() { return net; }


    public void addPaid(BigDecimal amount) {
        if (amount != null)
            paidTotal = paidTotal.add(amount);

        /*
        might be redundant to call recalculateNet() each time the
        the addPaid is called, but at the moment not
        certain if having it off would give wrong values

        for future optimization, check whether this is needed or not
         */
        recalculateNet();
    }

    public void addOwed(BigDecimal amount) {
        if (amount != null)
            owedTotal = owedTotal.add(amount);
        recalculateNet();
    }

    private void recalculateNet() {
        net = paidTotal.subtract(owedTotal).setScale(2, RoundingMode.HALF_UP);
    }

}
