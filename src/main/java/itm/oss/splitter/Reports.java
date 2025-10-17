package itm.oss.splitter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

public class Reports {

    public SimpleMap totalsByCategory(ArrayList<Expense> xs) {
        // TODO (Issue 6): sum amounts per category
        throw new UnsupportedOperationException("totalsByCategory() not implemented yet");
    }

    public SimplePersonSummaryMap perPerson(ArrayList<Expense> xs) {
        // TODO (Issue 7): compute paidTotal, owedTotal, net per person
		SimplePersonSummaryMap result = new SimplePersonSummaryMap();
		if (xs==null) return result;
		
		// Maps
		//Used BigDecimal for accurate representation
		Map<String, BigDecimal> paid = new HashMap<>();
		Map<String, BigDecimal> owed = new HashMap<>();
		
		for (Expense x : xs) {
			if(x==null) continue;
			
			//Payer 
			String payer = x.getPayer();
			//Total paid amount
			BigDecimal amount = x.getAmount();
			if (amount == null){
				amount = BigDecimal.ZERO;
			}
			
			//Paid totals
			if(payer != null) {
				paid.merge(payer, amount, BigDecimal::add);
			}
			
			//Owed totals
			ArrayList<String> parts = x.getParticipants();
			if (parts != null && !parts.isEmpty()) {
				int totalParticipants = parts.size();
				BigDecimal share = amount.divide(BigDecimal.valueOf(totalParticipants), 2, RoundingMode.HALF_UP);
				
				//Skip payer when applying owed amounts to each part
				for (String p : parts) {
					if(p==null) continue;
					//if(payer!= null && p.equals(payer)) continue;
					owed.merge(p, share, BigDecimal::add);
				}
			}
		}
		
		//Build summary
		Set<String> names = new HashSet<>();
		names.addAll(paid.keySet());
		names.addAll(owed.keySet());
		
		for (String name : names) {
			BigDecimal paidTotal = paid.getOrDefault(name, BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
			BigDecimal owedTotal = owed.getOrDefault(name, BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
			BigDecimal net = paidTotal.subtract(owedTotal);
			result.put(name, new SimplePersonSummary(paidTotal, owedTotal, net));
		}
		return result;
    }
}
