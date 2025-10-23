package itm.oss.splitter;

import java.math.BigDecimal;

public class ExpenseValidator {
  public static void validate(Expense e) {
    // TODO (Issue 3): implement checks (payer nonblank, amount > 0, participants
    // nonempty, currency nonblank)

    // check if payer is nonblank and not null
    if (e == null || e.getPayer().isBlank()) {
      throw new IllegalArgumentException("Payer is Empty! Please add payer");

    }

    // check if amount is bigger than zero and amount is not null
    if (e == null || e.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Amount should be larger than 0");
    }

    // check if currency is nonblank and not null
    if (e == null || e.getCurrency().isBlank()) {
      throw new IllegalArgumentException("Currency is Empty! Please add currency");
    }

    // check if participants size is bigger or same than 1 and if names are nonblank
    // and not null
    if (e == null || e.getParticipants().isEmpty()) {
      throw new IllegalArgumentException("Participants list cannot be empty.");
    }
    for (String participant : e.getParticipants()) {
      if (e == null || participant.isBlank()) {
        throw new IllegalArgumentException("Participant name cannot be blank.");
      }
    }

    // check if date is nonblank and not null
    if (e == null || e.getDate().isBlank()) {
      throw new IllegalArgumentException("Date cannot be blank.");
    }

  }
}
