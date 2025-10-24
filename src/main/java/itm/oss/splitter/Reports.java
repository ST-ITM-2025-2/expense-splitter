package itm.oss.splitter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;

public class Reports {

    public SimpleMap totalsByCategory(ArrayList<Expense> xs) {
        // TODO (Issue 6): sum amounts per category
        throw new UnsupportedOperationException("totalsByCategory() not implemented yet");
    }


    /**
     * Calculates a summary of payments and debts for each person involved in expenses.
     * The algorithm:
     * 1. For each expense, divides the amount equally among participants
     * 2. Handles rounding issues by distributing any remainder cents to participants in alphabetical order
     * 3. Tracks who paid what and who owes what in a SimplePersonSummaryMap
     * 4. For each participant: records amount paid, amount owed, and calculates net balance
     *
     */
    public SimplePersonSummaryMap perPerson(ArrayList<Expense> expenses) {
        SimplePersonSummaryMap map = new SimplePersonSummaryMap();


        for (Expense expense : expenses) {
            ArrayList<String> participants = expense.getParticipants();
            int count = participants.size();
            if (count == 0) continue;

            BigDecimal amount = expense.getAmount();

            // Credit to Kasper (github: kasperB2004) for fixing the unfair division problem
            BigDecimal baseShare = amount.divide(new BigDecimal(count), 10, BigDecimal.ROUND_DOWN);
            BigDecimal totalBase = baseShare.multiply(new BigDecimal(count));
            BigDecimal remainder = amount.subtract(totalBase);

            ArrayList<String> sortedParticipants = new ArrayList<>(participants);
            Collections.sort(sortedParticipants);

            for (int i = 0; i < sortedParticipants.size(); i++) {
                String name = sortedParticipants.get(i);

                if (map.get(name) == null) {
                    map.put(name, new PersonSummary(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
                }
                PersonSummary ps = map.get(name);

                if (name.equals(expense.getPayer())) {
                    ps.addPaid(amount);
                }

                // credit to Kasper (github: kasperB2004) for helping to fix the cent rounding problem
                BigDecimal owed = baseShare;
                if (i < remainder.multiply(new BigDecimal("100")).intValue()) {
                    owed = owed.add(new BigDecimal("0.01"));
                }

                ps.addOwed(owed);
            }
        }

        for (String name : map.keys()) {
            PersonSummary ps = map.get(name);
            ps.addOwed(BigDecimal.ZERO);
        }

        return map;
    }

}
