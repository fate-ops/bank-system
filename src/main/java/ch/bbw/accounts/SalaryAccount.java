package ch.bbw.accounts;

import ch.bbw.Scheduled;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;

public class SalaryAccount extends Account {

    private final long creditLimit;

    public SalaryAccount(String id, long creditLimit) {
        super(id);

        if (creditLimit > 0) {
            throw new IllegalArgumentException(
                    "Credit limit must be zero or negative");
        }

        this.creditLimit = creditLimit;
    }

    public long getCreditLimit() {
        return creditLimit;
    }

    @Override
    public long withdraw(Scheduled date, long amount, String text)
            throws InvalidAmountException, InvalidDateException {
        if (amount > 0 && getBalance() - amount < creditLimit) {
            throw new InvalidAmountException(
                    "Withdrawal would exceed credit limit " + creditLimit);
        }

        return super.withdraw(date, amount, text);
    }
}