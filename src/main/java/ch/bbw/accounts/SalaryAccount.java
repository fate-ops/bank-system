package ch.bbw.accounts;

import ch.bbw.Scheduled;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;

/**
 * Lohnkonto mit erlaubter Überziehung bis zu einer festen Kreditlimite.
 * Eine Limite von null verbietet die Überziehung. Buchungen verwaltet die
 * Basisklasse; dieses Konto entscheidet nur über seine Auszahlungsgrenze.
 */
public class SalaryAccount extends Account {

    /** Niedrigster erlaubter Saldo in Millirappen. */
    private final long creditLimit;

    /**
     * Erstellt ein leeres Lohnkonto.
     * @param id Kontonummer
     * @param creditLimit niedrigster zulässiger Saldo in Millirappen
     * @throws IllegalArgumentException bei einer positiven Kreditlimite
     */
    public SalaryAccount(String id, long creditLimit) {
        super(id);

        if (creditLimit > 0) {
            throw new IllegalArgumentException(
                    "Credit limit must be zero or negative");
        }

        this.creditLimit = creditLimit;
    }

    /**
     * Liefert die konfigurierte Kreditlimite.
     * @return tiefster erlaubter Saldo in Millirappen
     */
    public long getCreditLimit() {
        return creditLimit;
    }

    /**
     * {@inheritDoc}
     * @throws InvalidAmountException wenn der Betrag ungültig ist oder der
     *         neue Saldo die Kreditlimite unterschreiten würde
     */
    @Override
    public long withdraw(Scheduled date, long amount, String text)
            throws InvalidAmountException, InvalidDateException {
        // creditLimit <= 0 and amount > 0: their sum cannot overflow.
        if (amount > 0 && getBalance() < creditLimit + amount) {
            throw new InvalidAmountException(
                    "Withdrawal would exceed credit limit " + creditLimit);
        }

        return super.withdraw(date, amount, text);
    }

    /** {@inheritDoc} */
    @Override
    public String getAccountType() {
        return "Lohnkonto";
    }
}