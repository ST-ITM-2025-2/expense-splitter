package itm.oss.splitter;

import java.math.BigDecimal;

public class ExpenseValidator {
  private static void checkStringIsBlank(String value, String errorMessage) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(errorMessage);
    }
  }

  public static void validate(Expense e) {
    // null check for expense
    if (e == null) {
      throw new IllegalArgumentException("Expense cannot be null.");
    }

    // check if payer is nonblank and not null
    checkStringIsBlank(e.getPayer(), "Payer is Empty! Please add payer");

    // check if amount is larger than 0 and not null
    if (e.getAmount() == null || e.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Amount should be larger than 0");
    }

    // check if currency is nonblank and not null
    checkStringIsBlank(e.getCurrency(), "Currency is Empty! Please add currency");

    // check if participants list is not null or empty
    if (e.getParticipants() == null || e.getParticipants().isEmpty()) {
      throw new IllegalArgumentException("Participants list cannot be empty.");
    }

    // check if each participant name is nonblank and not null
    for (String participant : e.getParticipants()) {
      checkStringIsBlank(participant, "Participant name cannot be blank.");
    }

    // check if date is nonblank and not null
    checkStringIsBlank(e.getDate(), "Date is Empty! Please add date");
  }
}
