package ch.bbw.accounts;

import ch.bbw.Booking;
import ch.bbw.Scheduled;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Konto.
 *
 * <p>
 * Ein Konto hält nur seine Buchungen. Der Saldo wird nicht separat
 * gespeichert, sondern bei jedem Aufruf aus den Buchungen berechnet
 * (eine einzige Quelle der Wahrheit).
 * </p>
 *
 * @author Luigi Cavuoti, lro@gmx.ch, Hassan Alayan
 * @version 2.2
 */
public abstract class Account {
    /**
     * Die Kontonummer (kann auch Buchstaben und Sonderzeichen enthalten).
     */
    private final String id;
    /**
     * Die Buchungen.
     */
    private final List<Booking> bookings;

    /**
     * Erzeugt ein neues Konto.
     *
     * @param id die Kontonummer
     */
    public Account(String id) {
        this.id = id;
        this.bookings = new ArrayList<Booking>();
    }

    /**
     * Gibt die Kontonummer zurück.
     *
     * @return die Kontonummer
     */
    public String getId() {
        return id;
    }

    public abstract String getAccountType();

    /**
     * Gibt alle Buchungen des Kontos zurück.
     *
     * @return eine nicht veränderbare Sicht auf die Buchungen;
     * {@code add} oder {@code remove} wirft eine {@link UnsupportedOperationException}
     */
    public List<Booking> getBookings() {
        return Collections.unmodifiableList(bookings);
    }

    /**
     * Gibt den Saldo zurück, berechnet als Summe aller Buchungsbeträge.
     *
     * @return Saldo in Millirappen (0 bei einem Konto ohne Buchungen)
     */
    public long getBalance() {
        return bookings.stream()
                .mapToLong(Booking::amount)
                .sum();
    }

    /**
     * Checks if the transaction date is not in the past.
     *
     * @param date the transaction date
     * @return whether the transaction date is valid
     */
    public boolean isInvalidTransaction(Scheduled date) {
        return date.isPastDue();
    }

    /**
     * Zahlt einen Betrag mit dem Standardtext "Einzahlung" ein.
     *
     * @param date   das Transaktionsdatum
     * @param amount der einzuzahlende Betrag in Millirappen
     * @return der neue Saldo
     * @throws InvalidAmountException wenn der Betrag 0 oder negativ ist
     * @throws InvalidDateException   wenn das Datum in der Vergangenheit liegt
     */
    public long deposit(Scheduled date, long amount) throws InvalidAmountException, InvalidDateException {
        return deposit(date, amount, "Einzahlung");
    }

    /**
     * Zahlt einen Betrag mit eigenem Buchungstext ein.
     *
     * @param date   das Transaktionsdatum
     * @param amount der einzuzahlende Betrag in Millirappen
     * @param text   der Buchungstext
     * @return der neue Saldo
     * @throws InvalidAmountException wenn der Betrag 0 oder negativ ist (keine Buchung wird angelegt)
     * @throws InvalidDateException   wenn das Datum in der Vergangenheit liegt (keine Buchung wird angelegt)
     */
    public long deposit(Scheduled date, long amount, String text) throws InvalidAmountException, InvalidDateException {
        if (amount <= 0)
            throw new InvalidAmountException("amount must be greater than 0");

        if (isInvalidTransaction(date))
            throw new InvalidDateException("date mustn't be in the past");

        bookings.add(new Booking(date.toUnix(), amount, text));

        return getBalance();
    }

    /**
     * Hebt einen Betrag mit dem Standardtext "Auszahlung" ab.
     *
     * @param date   das Transaktionsdatum
     * @param amount der abzuhebende Betrag in Millirappen
     * @return der neue Saldo
     * @throws InvalidAmountException wenn der Betrag 0 oder negativ ist
     * @throws InvalidDateException   wenn das Datum in der Vergangenheit liegt
     */
    public long withdraw(Scheduled date, long amount) throws InvalidAmountException, InvalidDateException {
        return withdraw(date, amount, "Auszahlung");
    }

    /**
     * Hebt einen Betrag mit eigenem Buchungstext ab.
     * Die Buchung wird mit negativem Betrag gespeichert.
     *
     * @param date   das Transaktionsdatum
     * @param amount der abzuhebende Betrag in Millirappen (positiv angeben)
     * @param text   der Buchungstext
     * @return der neue Saldo
     * @throws InvalidAmountException wenn der Betrag 0 oder negativ ist (keine Buchung wird angelegt)
     * @throws InvalidDateException   wenn das Datum in der Vergangenheit liegt (keine Buchung wird angelegt)
     */
    public long withdraw(Scheduled date, long amount, String text) throws InvalidAmountException, InvalidDateException {
        if (amount <= 0)
            throw new InvalidAmountException("amount must be greater than 0");

        if (isInvalidTransaction(date))
            throw new InvalidDateException("date mustn't be in the past");

        // Achtung: hier Buchung mit negativem Betrag!
        bookings.add(new Booking(date.toUnix(), -amount, text));

        return getBalance();
    }

    /**
     * Erstellt einen Kontoauszug mit allen Buchungen und dem Schlusssaldo.
     *
     * @return der Kontoauszug als Text, Beträge in CHF
     */
    public String printStatement() {
        return statement(bookings, "Kontoauszug " + id);
    }

    /**
     * Erstellt einen Kontoauszug nur für die Buchungen eines Monats
     * (Zeitzone Europe/Zurich).
     *
     * @param year  das Jahr, z. B. 2026
     * @param month der Monat, 1 = Januar bis 12 = Dezember
     * @return der Kontoauszug des Monats; der Saldo ist die Summe der Buchungen dieses Monats
     */
    public String printStatement(int year, int month) {
        ZoneId zone = ZoneId.of("Europe/Zurich");
        List<Booking> monthBookings = bookings.stream()
                .filter(b -> {
                    LocalDate day = Instant.ofEpochMilli(b.date()).atZone(zone).toLocalDate();
                    return day.getYear() == year && day.getMonthValue() == month;
                })
                .toList();
        return statement(monthBookings, "Kontoauszug " + id + " " + month + "/" + year);
    }

    /**
     * Baut den Text eines Kontoauszugs: eine Zeile pro Buchung und der Saldo.
     *
     * @param list  die Buchungen, die auf den Auszug kommen
     * @param title die Überschrift
     * @return der Kontoauszug als Text
     */
    private String statement(List<Booking> list, String title) {
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
        long abs = Math.abs(millirappen);
        return String.format("CHF %s%d.%02d", sign, abs / 100_000, (abs % 100_000) / 1_000);
    }
}