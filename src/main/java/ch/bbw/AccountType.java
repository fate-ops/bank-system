package ch.bbw;

/**
 * Die Kontoarten, die die Bank anbietet, jeweils mit ihrem Präfix für die Kontonummer.
 *
 * @author Hassan Alayan
 * @version 1.0
 */
public enum AccountType {
    /** Sparkonto mit Präfix S. */
    SAVINGS("S"),
    /** Jugendkonto mit Präfix Y. */
    PROMO_YOUTH("Y"),
    /** Lohnkonto mit Präfix P. */
    SALARY("P");

    /**
     * Präfix der Kontonummer, z. B. "S" für S-1000.
     */
    private final String prefix;

    /**
     * Legt das Präfix für diesen Typ fest.
     * @param prefix Präfix der Kontonummer
     */
    AccountType(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Gibt das Präfix der Kontonummer zurück.
     *
     * @return das Präfix, z. B. "S"
     */
    public String getPrefix() {
        return prefix;
    }
}