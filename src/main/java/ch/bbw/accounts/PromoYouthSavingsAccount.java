package ch.bbw.accounts;

import ch.bbw.Scheduled;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;

public class PromoYouthSavingsAccount extends SavingsAccount {

    private static final long MINIMUM_BALANCE = 1_000_000;

    public PromoYouthSavingsAccount(String id) {
        super(id);
    }

    @Override
    public long withdraw(Scheduled date, long amount, String text)
            throws InvalidAmountException, InvalidDateException {

        if (amount > 0 && getBalance() - amount < MINIMUM_BALANCE) {
            throw new InvalidAmountException(
                    "At least CHF 10 must remain after withdrawal");
        }

        return super.withdraw(date, amount, text);
    }

    @Override
    public String getAccountType() {
        return "Promo-Jugendsparkonto";
    }
}