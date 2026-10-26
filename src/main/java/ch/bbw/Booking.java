package ch.bbw;

/**
 * Buchung.
 *
 * @param date   Datum der Transaktion (Unix-Millisekunden seit 1.1.1970).
 * @param amount Transaktionsbetrag (Millirappen), Abhebungen sind negativ.
 * @param text   Beschreibung bzw. Grund der Buchung (z. B. "Lohn"), darf nicht leer sein.
 * @author Luigi Cavuoti, lro@gmx.ch, Hassan Alayan
 * @version 2.2
 */
public record Booking(long date, long amount, String text) {

	/**
	 * Prüft die Werte beim Erzeugen einer Buchung.
	 *
	 * @throws IllegalArgumentException wenn {@code text} null, leer oder nur Leerzeichen ist
	 */
	public Booking {
		if (text == null || text.isBlank()) {
			throw new IllegalArgumentException("text must not be empty");
		}
	}
}