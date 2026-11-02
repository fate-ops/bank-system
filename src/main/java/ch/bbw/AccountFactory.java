package ch.bbw;

import java.util.concurrent.atomic.AtomicLong;
import ch.bbw.accounts.*;

/**
 * Erzeugt alle Konten der Bank und vergibt ihre Kontonummern.
 *
 * <p>
 * Die Factory ist die einzige Stelle, an der {@code new} für ein Konto steht.
 * Der Zähler ist für alle Kontoarten gemeinsam (S-1000, Y-1001, P-1002), damit
 * die Nummer allein schon eindeutig ist, auch ohne Präfix.
 * </p>
 *
 * @author Luigi Cavuoti, Hassan Alayan
 * @version 2.1
 */
public class AccountFactory {
    /**
     * Next account id.
     */
    /**
     * Nächste Kontonummer. AtomicLong, damit zwei gleichzeitige Aufrufe
     * nie dieselbe Nummer bekommen (wichtig, sobald Spring HTTP-Requests parallel bedient).
     */
    private final AtomicLong nextAccountId;

    public AccountFactory() {
        this.nextAccountId = new AtomicLong(1000);
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
        String id = type.getPrefix() + "-" + nextAccountId.getAndIncrement();

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