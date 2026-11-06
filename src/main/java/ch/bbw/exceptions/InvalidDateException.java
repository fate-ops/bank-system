package ch.bbw.exceptions;

/** Das Datum einer Transaktion ist ungültig. */
public class InvalidDateException extends Exception {
    /**
     * Erstellt den Fehler mit einer Beschreibung.
     * @param message Grund für die Ablehnung
     */
    public InvalidDateException(String message) {
        super(message);
    }
}
