package ch.bbw.accounts;

import ch.bbw.Booking;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;

/** Formatiert Buchungen und deren Summe als lesbaren Kontoauszug. */
final class AccountStatement {

    /** Diese Klasse stellt nur Formatierungsfunktionen bereit. */
    private AccountStatement() {
    }

    /**
     * Rendert die Buchungen im angegebenen Kalendermonat (Europe/Zurich).
     * @param bookings zu filternde Buchungen
     * @param id Kontonummer
     * @param year Jahr des Auszugs
     * @param month Monat des Auszugs (1 bis 12)
     * @return Auszug mit der Summe der Buchungen dieses Monats
     */
    static String renderMonth(List<Booking> bookings, String id, int year, int month) {
        ZoneId zone = ZoneId.of("Europe/Zurich");
        List<Booking> monthBookings = bookings.stream()
                .filter(b -> {
                    LocalDate day = Instant.ofEpochMilli(b.date()).atZone(zone).toLocalDate();
                    return day.getYear() == year && day.getMonthValue() == month;
                })
                .toList();
        return render(monthBookings, "Kontoauszug " + id + " " + month + "/" + year);
    }

    /**
     * Baut den Text eines Kontoauszugs: eine Zeile pro Buchung und der Saldo.
     *
     * @param list  die Buchungen, die auf den Auszug kommen
     * @param title die Überschrift
     * @return der Kontoauszug als Text
     */
    static String render(List<Booking> list, String title) {
        StringBuilder sb = new StringBuilder(title).append("\n");
        long sum = 0;
        for (Booking b : list) {
            sb.append(Instant.ofEpochMilli(b.date()))
                    .append("  ")
                    .append(formatAmount(b.amount()))
                    .append("  ")
                    .append(b.text())
                    .append("\n");
            sum += b.amount();
        }
        sb.append("Saldo: ").append(formatAmount(sum));
        return sb.toString();
    }

    /**
     * Formatiert Millirappen als Frankenbetrag mit Rappen,
     * z. B. 123_456 als "CHF 1.23" und -50_000 als "CHF -0.50".
     *
     * @param millirappen der Betrag in Millirappen (100_000 = 1 Franken)
     * @return der lesbare Betrag
     */
    private static String formatAmount(long millirappen) {
        String sign = millirappen < 0 ? "-" : "";
        long francs = Math.abs(millirappen / 100_000);
        long rappen = Math.abs(millirappen % 100_000) / 1_000;
        return String.format(Locale.ROOT, "CHF %s%d.%02d", sign, francs, rappen);
    }
}
