package itm.oss.splitter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Random;

public class Splitter {
    /**
 * Computes each participant’s net balance by equally splitting all expenses.
 * <p>
 * Rules:
 * <ul>
 *   <li>Share = amount / number_of_participants (BigDecimal)</li>
 *   <li>Each participant owes their share (negative in their net).</li>
 *   <li>Payer’s net increases by the full amount (common model).</li>
 *   <li>Sum of all nets ≈ 0 (within rounding error).</li>
 * </ul>
 * <p>
 * Works for the provided sample and handles single-participant cases.
 *
 * @return Balance object containing each participant’s final net
 */

  public Balance computeBalances(ArrayList<Expense> xs) {

      Balance result = new Balance();
      ArrayList<String> names = new ArrayList<>();
      BigDecimal total = new BigDecimal(0);

      for(Expense item : xs) {

          String payer = item.getPayer();
          BigDecimal amount = item.getAmount().setScale(2, RoundingMode.HALF_EVEN);
          if(amount.compareTo(BigDecimal.ZERO) < 0) {
              // pass the case of negative amount
              continue;
          }
          ArrayList<String> participants = item.getParticipants();
          BigDecimal share = amount.divide(BigDecimal.valueOf(participants.size()),2, RoundingMode.HALF_EVEN);

          if(names.contains(payer)) {
              // just in case if some payer pays more than two times.
              result.put(payer, result.getAmount(payer).add(amount));
          } else {
              result.put(payer, amount);
              names.add(payer);
          }

          // Distribute the amount of expense to the participants
          for(String participant : participants) {
              if(names.contains(participant)) {
                  // subtract share of existing person including payer
                  result.put(participant, result.getAmount(participant).subtract(share));
              } else {
                  // subtract share of new person
                  result.put(participant, share.multiply(BigDecimal.valueOf(-1)));
                  names.add(participant);
              }
          }

          for(String person : names) {
              total = total.add(result.getAmount(person));
          }

          if(total.compareTo(BigDecimal.valueOf(0.00)) != 0) {
              // current total net
              result.put(payer, result.getAmount(payer).subtract(total));
              total = BigDecimal.valueOf(0.00);
          }

      }
      //Sum nets so total = 0; scale(2) rounding HALF_EVEN when needed.

      for(String person : names) {
          total = total.add(result.getAmount(person));
      }
      if(total.compareTo(BigDecimal.valueOf(0.00)) != 0) {
          // decide who will receive the leftover
          Random random = new Random();
          String ranName = names.get(random.nextInt(names.size()));
          result.put(ranName, result.getAmount(ranName).subtract(total));
      }
      return result;

  }
}
