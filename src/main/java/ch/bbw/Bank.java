package ch.bbw;


import ch.bbw.accounts.Account;
import ch.bbw.exceptions.*;


import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

/**
 * Die Bank.
 *
 * @author luigicavuoti, lro@gmx.ch, Hassan Alayan
 * @version 2.2
 */
public class Bank {
    /**
     * Liste aller Konti.
     */
    private final TreeMap<String, Account> accounts;
    private final AccountFactory accountFactory;
    /**
     * Initialisiert eine neue Bank.
     */
    public Bank() {
        this.accounts = new TreeMap<String, Account>();
        accountFactory = new AccountFactory();
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
     * @param creditLimit Kreditlimite (negative Zahl)
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

    public List<Account> top5HighestBalances() {
        return accounts.values()
                .stream()
                .sorted(Comparator.comparingLong(Account::getBalance).reversed())
                .limit(5)
                .toList();
    }

    public List<Account> top5LowestBalances() {
        return accounts.values()
                .stream()
                .sorted(Comparator.comparingLong(Account::getBalance))
                .limit(5)
                .toList();
    }

    /**
     * Getter of the property <tt>account</tt>
     *
     * @return Returns the account.
     */
    public Account getAccount(String id) throws InvalidAmountException {

        var account = Optional.ofNullable(accounts.get(id));

        if (account.isEmpty()) {
            throw new InvalidAmountException("Invalid bank id");
        }

        return account.get();
    }
}