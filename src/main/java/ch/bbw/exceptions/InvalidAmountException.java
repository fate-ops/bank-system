package ch.bbw.exceptions;

/** Ein Buchungsbetrag verletzt eine Betrags- oder Kontoregel. */
public class InvalidAmountException extends Exception {
    /**
     * Erstellt den Fehler mit einer Beschreibung.
     * @param message Grund für die Ablehnung
     */
    public InvalidAmountException(String message) {
        super(message);
    }
}
