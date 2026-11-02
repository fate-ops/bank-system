package ch.bbw;

import ch.bbw.accounts.*;

/**
 * Erzeugt alle Konten der Bank und vergibt ihre Kontonummern.
 *
 * @author Luigi Cavuoti, Hassan Alayan
 * @version 2.0
 */
public class AccountFactory {
    /**
     * Next account id.
     */
    private long nextAccountId;

    public AccountFactory() {
        this.nextAccountId = 1000;
    }

    /**
     * Erzeugt ein Konto der gegebenen Art (Simple Factory mit switch).
     *
     * <p>
     * Für Bank behalten wir die drei benannten Methoden: Sie sind klarer, und
     * der Parameter creditLimit wird nur dort verlangt, wo er Sinn macht.
     * Bei create(type, creditLimit) ist creditLimit für Sparkonten nutzlos und
     * wird ignoriert. Das zeigt die Schwäche einer einzigen Methode; vermeiden
     * könnte man das mit Überladungen oder einem kleinen Parameter-Objekt.
     * Das hier ist eine Simple Factory (eine Klasse mit switch), nicht das
     * Factory-Method-Pattern (eine Creator-Subklasse pro Produkt).
     * </p>
     *
     * @param type        die Kontoart
     * @param creditLimit Kreditlimite (nur für SALARY, sonst ignoriert)
     * @return das neue Konto
     */
    public Account create(AccountType type, long creditLimit) {
        String id = type.getPrefix() + "-" + nextAccountId++;

        return switch (type) {
            case SAVINGS -> new SavingsAccount(id);
            case PROMO_YOUTH -> new PromoYouthSavingsAccount(id);
            case SALARY -> new SalaryAccount(id, creditLimit);
        };
    }

    public Account createSavingsAccount() {
        return create(AccountType.SAVINGS, 0);
    }

    public Account createPromoYouthSavingsAccount() {
        return create(AccountType.PROMO_YOUTH, 0);
    }

    public Account createSalaryAccount(long creditLimit) {
        return create(AccountType.SALARY, creditLimit);
    }
}