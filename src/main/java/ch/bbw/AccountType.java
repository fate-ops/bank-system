package ch.bbw;

/**
 * Die Kontoarten, die die Bank anbietet, jeweils mit ihrem Präfix für die Kontonummer.
 *
 * @author Hassan Alayan
 * @version 1.0
 */
public enum AccountType {
    SAVINGS("S"),
    PROMO_YOUTH("Y"),
    SALARY("P");

    /**
     * Präfix der Kontonummer, z. B. "S" für S-1000.
     */
    private final String prefix;

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