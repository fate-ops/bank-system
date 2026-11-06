package ch.bbw.accounts;

import ch.bbw.Scheduled;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;

/**
 * Jugendkonto mit einem Mindestguthaben von CHF 10 nach Auszahlungen.
 * Erbt vom Sparkonto, um dessen Überziehungsschutz zu übernehmen.
 * Neue Konten starten bei null; die Regel gilt nur für Auszahlungen.
 */
public class PromoYouthSavingsAccount extends SavingsAccount {

    /** CHF 10 in Millirappen. */
    private static final long MINIMUM_BALANCE = 1_000_000;

    /**
     * Erstellt ein leeres Jugendkonto.
     * @param id Kontonummer
     */
    public PromoYouthSavingsAccount(String id) {
        super(id);
    }

    /**
     * {@inheritDoc}
     * @throws InvalidAmountException wenn der Betrag ungültig ist oder
     *         weniger als CHF 10 verbleiben würden
     */
    @Override
    public long withdraw(Scheduled date, long amount, String text)
            throws InvalidAmountException, InvalidDateException {

        if (amount > 0 && getBalance() - amount < MINIMUM_BALANCE) {
            throw new InvalidAmountException(
                    "At least CHF 10 must remain after withdrawal");
        }

        return super.withdraw(date, amount, text);
    }

    /** {@inheritDoc} */
    @Override
    public String getAccountType() {
        return "Promo-Jugendsparkonto";
    }
}