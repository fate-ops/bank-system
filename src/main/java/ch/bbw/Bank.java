package ch.bbw;


import ch.bbw.accounts.Account;
import ch.bbw.exceptions.*;


import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

/**
 * Verwaltet Konten und delegiert deren Erstellung und Buchungsregeln.
 *
 * <p>Eine gemeinsame Bank bietet einen zentralen Kontobestand und einen
 * gemeinsamen ID-Zähler. Ein globaler Zugriff kann jedoch Abhängigkeiten
 * verstecken und lässt Zustand über Aufrufer hinweg bestehen. Das erschwert
 * isolierte Tests; getrennte Prozesse teilen sich diese Instanz ohnehin nicht.
 *
 * <p>Der Holder initialisiert die Standardbank erst beim ersten Zugriff.
 * Seine Initialisierung ist thread-sicher, die Kontoverwaltung selbst nicht.
 * Verhaltenstests erhalten eigene Banken mit eigener Factory. Ein temporärer
 * Reset diente als Test-Workaround, wurde aber durch diese Isolation ersetzt.
 *
 * @author luigicavuoti, lro@gmx.ch, Hassan Alayan
 * @version 2.2
 */
public class Bank {
    /**
     * Liste aller Konti.
     */
    private final TreeMap<String, Account> accounts;
    /** Erzeugt Konten und vergibt ihre Nummern. */
    private final AccountFactory accountFactory;

    /**
     * Erstellt einen leeren Kontobestand mit der übergebenen Factory.
     * @param accountFactory Factory für Kontotypen und Kontonummern
     */
    Bank(AccountFactory accountFactory) {
        this.accounts = new TreeMap<>();
        this.accountFactory = accountFactory;
    }

    /**
     * Initialisiert die gemeinsame Bank erst beim ersten Aufruf von getInstance().
     * Die JVM initialisiert eine Klasse genau einmal und thread-sicher,
     * deshalb braucht getInstance() kein synchronized (Variantenvergleich
     * eager / synchronized / holder: siehe docs/architecture.md).
     */
    private static class Holder {
        /** Standardbank mit eigener Factory. */
        private static final Bank INSTANCE = new Bank(new AccountFactory());

        /** Der Holder dient nur der Klasseninitialisierung. */
        private Holder() {
        }
    }

    /**
     * Gibt die gemeinsame Bank zurück.
     * @return gemeinsame Standardbank dieses Classloaders
     */
    public static Bank getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * Erzeugt ein neues Sparkonto
     *
     * @return die neue Kontonummer
     */
    public String createSavingsAccount() {
        var account = accountFactory.createSavingsAccount();
        var id = account.getId();

        accounts.put(id, account);

        return id;
    }

    /**
     * Erzeugt ein neues Promo-Jugendsparkonto
     *
     * @return die neue Kontonummer
     */
    public String createPromoYouthSavingsAccount() {
        var account = accountFactory.createPromoYouthSavingsAccount();
        var id = account.getId();

        accounts.put(id, account);

        return id;
    }

    /**
     * Erzeugt ein neues Lohnkonto
     *
     * @param creditLimit tiefster zulässiger Saldo (null oder negativ)
     * @throws IllegalArgumentException bei positiver Kreditlimite
     * @return String die neue Kontonummer
     */
    public String createSalaryAccount(long creditLimit) throws IllegalArgumentException {
        var account = accountFactory.createSalaryAccount(creditLimit);
        var id = account.getId();

        accounts.put(id, account);

        return id;

    }

    /**
     * Gibt den Kontostand der Bank zurück.
     *
     * @return long der Kontostand der Bank
     */
    public long getBalance() {
        return accounts
                .values()
                .stream()
                .mapToLong(Account::getBalance)
                .sum();
    }

    /**
     * Gibt den Kontostand des Kontos mit der gegebenen
     * Kontonummer zurück.
     *
     * @param id die Kontonummer
     * @return long der Kontostand des Kontos
     * @throws InvalidAmountException wenn kein Konto mit dieser Kontonummer existiert
     */
    public long getBalance(String id) throws InvalidAmountException {
        return getAccount(id).getBalance();
    }

    /**
     * Zahlt den gegebenen Betrag auf das Konto mit der gegebenen
     * Kontonummer ein (Standardtext "Einzahlung").
     *
     * @param id     die Kontonummer
     * @param date   das Transaktionsdatum
     * @param amount der einzuzahlende Betrag in Millirappen
     * @throws InvalidAmountException wenn das Konto nicht existiert oder der Betrag ungültig ist
     * @throws InvalidDateException   wenn das Datum in der Vergangenheit liegt
     */
    public void deposit(String id, Scheduled date, long amount) throws InvalidAmountException, InvalidDateException {
        getAccount(id).deposit(date, amount);
    }

    /**
     * Zahlt den gegebenen Betrag mit eigenem Buchungstext ein.
     *
     * @param id     die Kontonummer
     * @param date   das Transaktionsdatum
     * @param amount der einzuzahlende Betrag in Millirappen
     * @param text   der Buchungstext
     * @throws InvalidAmountException wenn das Konto nicht existiert oder der Betrag ungültig ist
     * @throws InvalidDateException   wenn das Datum in der Vergangenheit liegt
     */
    public void deposit(String id, Scheduled date, long amount, String text) throws InvalidAmountException, InvalidDateException {
        getAccount(id).deposit(date, amount, text);
    }

    /**
     * Hebt den gegebenen Betrag vom Konto mit der gegebenen
     * Kontonummer ab (Standardtext "Auszahlung").
     *
     * @param id     die Kontonummer
     * @param date   das Transaktionsdatum
     * @param amount der abzuhebende Betrag in Millirappen
     * @return der neue Saldo des Kontos
     * @throws InvalidAmountException wenn das Konto nicht existiert, der Betrag ungültig ist oder der Saldo nicht reicht
     * @throws InvalidDateException   wenn das Datum in der Vergangenheit liegt
     */
    public long withdraw(String id, Scheduled date, long amount) throws InvalidAmountException, InvalidDateException {
        return getAccount(id).withdraw(date, amount);
    }

    /**
     * Hebt den gegebenen Betrag mit eigenem Buchungstext ab.
     *
     * @param id     die Kontonummer
     * @param date   das Transaktionsdatum
     * @param amount der abzuhebende Betrag in Millirappen
     * @param text   der Buchungstext
     * @return der neue Saldo des Kontos
     * @throws InvalidAmountException wenn das Konto nicht existiert, der Betrag ungültig ist oder der Saldo nicht reicht
     * @throws InvalidDateException   wenn das Datum in der Vergangenheit liegt
     */
    public long withdraw(String id, Scheduled date, long amount, String text) throws InvalidAmountException, InvalidDateException {
        return getAccount(id).withdraw(date, amount, text);
    }

    /**
     * Listet die Konten mit dem höchsten Saldo.
     * @return bis zu fünf Konten, nach Saldo absteigend sortiert
     */
    public List<Account> top5HighestBalances() {
        return accounts.values()
                .stream()
                .sorted(Comparator.comparingLong(Account::getBalance).reversed())
                .limit(5)
                .toList();
    }

    /**
     * Listet die Konten mit dem tiefsten Saldo.
     * @return bis zu fünf Konten, nach Saldo aufsteigend sortiert
     */
    public List<Account> top5LowestBalances() {
        return accounts.values()
                .stream()
                .sorted(Comparator.comparingLong(Account::getBalance))
                .limit(5)
                .toList();
    }

    /**
     * Sucht ein Konto nach seiner Nummer.
     * @param id Kontonummer
     * @return das gefundene Konto
     * @throws InvalidAmountException wenn die Kontonummer unbekannt ist
     */
    public Account getAccount(String id) throws InvalidAmountException {

        var account = Optional.ofNullable(accounts.get(id));

        if (account.isEmpty()) {
            throw new InvalidAmountException("Invalid bank id");
        }

        return account.get();
    }
}