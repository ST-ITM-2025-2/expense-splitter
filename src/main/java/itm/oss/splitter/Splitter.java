package itm.oss.splitter;

import java.util.ArrayList;

public class Splitter {

  public Balance computeBalances(ArrayList<Expense> xs) {
      /**
       * Goal: For each expense, split equally among participants and compute net per person.
       *
       * Rules:
       *
       * Share = amount / number_of_participants (BigDecimal)
       * Each participant owes their share (negative in their net).
       * Payer’s net increaseby the full amount (common model).
       * Sum of all nets ~ 0 (within cents).
       * Acceptance Criteria:
       *
       * Works for the provided 3-row sample.
       * Handles single-participant case.
       * Nets retrievable via Balance.getNames() / getAmount(name)
       */
      // TODO (Issue 4): equal split math.
    // Sum nets so total = 0; scale(2) rounding HALF_EVEN when needed.
    throw new UnsupportedOperationException("computeBalances() not implemented yet");
  }
}
