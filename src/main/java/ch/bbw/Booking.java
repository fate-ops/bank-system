package ch.bbw;

/**
 * Buchung.
 *
 * @param date   Datum der Transaktion (Unix-Millisekunden seit 1.1.1970).
 * @param amount Transaktionsbetrag (Millirappen).
 * @param text   Beschreibung bzw. Grund der Buchung (z. B. "Lohn").
 * @author Luigi Cavuoti, lro@gmx.ch, Hassan Alayan
 * @version 2.2
		*/
public record Booking(long date, long amount, String text) {
}
