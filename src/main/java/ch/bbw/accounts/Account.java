package ch.bbw.accounts;

import ch.bbw.Booking;
import ch.bbw.Scheduled;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;

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

    /**
     * Liefert die Bezeichnung der Kontoart.
     * @return lesbare Bezeichnung der Kontoart
     */
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
     * Prüft, ob das Transaktionsdatum in der Vergangenheit liegt.
     *
     * @param date the transaction date
     * @return true, wenn das Datum in der Vergangenheit liegt
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
        return AccountStatement.render(bookings, "Kontoauszug " + id);
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
        return AccountStatement.renderMonth(bookings, id, year, month);
    }
}
