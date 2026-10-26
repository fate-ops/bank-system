package ch.bbw.accounts;


import ch.bbw.Booking;
import ch.bbw.Scheduled;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Tests für die Klasse Account.
 *
 * @author luigicavuoti, Hassan Alayan
 * @version 2.1
 */
public class AccountTests {
    private Account account;

    /**
     * Erstellt vor jedem Test ein neues, leeres Sparkonto.
     */
    @BeforeEach
    public void setUp() {
        account = new SavingsAccount("1234567890");
    }

    /**
     * Tested die Initialisierung eines Kontos.
     */
    @Test
    @DisplayName("Simple constructor-Test for SavingsAccount should work")
    public void testInit() {
        assertEquals("1234567890", account.getId());
        assertTrue(account.getBookings().isEmpty());
        assertEquals(0, account.getBalance());
    }

    /**
     * Testet das Einzahlen auf ein Konto.
     */
    @Test
    @DisplayName("Simple deposit on SavingsAccount should work")
    public void testDeposit() throws Exception {
        long balance = account.deposit(Scheduled.now(), 100_000, "Lohn");

        assertEquals(1, account.getBookings().size());
        Booking booking = account.getBookings().get(0);
        assertEquals(100_000, booking.amount());
        assertEquals("Lohn", booking.text());
        assertEquals(100_000, balance);
        assertEquals(100_000, account.getBalance());
    }

    /**
     * Testet das Abheben von einem Konto.
     */
    @Test
    @DisplayName("Simple withdraw from SavingsAccount should work")
    public void testWithdraw() throws Exception {
        account.deposit(Scheduled.now(), 100_000);
        long balance = account.withdraw(Scheduled.now(), 30_000, "Bancomat");

        assertEquals(2, account.getBookings().size());
        assertEquals(-30_000, account.getBookings().get(1).amount());
        assertEquals(70_000, balance);
    }

    /**
     * Ein Sparkonto darf nicht überzogen werden.
     */
    @Test
    @DisplayName("Withdraw more than balance is rejected and adds no booking")
    public void testWithdrawTooMuch() throws Exception {
        account.deposit(Scheduled.now(), 100_000);

        assertThrows(InvalidAmountException.class, () -> account.withdraw(Scheduled.now(), 100_001));
        assertEquals(1, account.getBookings().size());
        assertEquals(100_000, account.getBalance());
    }

    /**
     * Grenzfall: Genau den ganzen Saldo abheben ist erlaubt.
     */
    @Test
    @DisplayName("Withdraw exactly the balance is allowed")
    public void testWithdrawExactBalance() throws Exception {
        account.deposit(Scheduled.now(), 100_000);
        account.withdraw(Scheduled.now(), 100_000);

        assertEquals(0, account.getBalance());
    }

    /**
     * Der Saldo ist immer die Summe aller Buchungen.
     */
    @Test
    @DisplayName("Balance equals the sum of all booking amounts")
    public void testBalanceIsSumOfBookings() throws Exception {
        account.deposit(Scheduled.now(), 500_000);
        account.withdraw(Scheduled.now(), 120_000);
        account.deposit(Scheduled.now(), 30_000);

        long sum = 0;
        for (Booking b : account.getBookings()) {
            sum += b.amount();
        }
        assertEquals(sum, account.getBalance());
        assertEquals(410_000, account.getBalance());
    }

    /**
     * Ein ungültiger Betrag erzeugt keine Buchung.
     */
    @Test
    @DisplayName("Deposit with amount <= 0 is rejected and adds no booking")
    public void testDepositInvalidAmount() {
        assertThrows(InvalidAmountException.class, () -> account.deposit(Scheduled.now(), 0));
        assertThrows(InvalidAmountException.class, () -> account.deposit(Scheduled.now(), -5));
        assertTrue(account.getBookings().isEmpty());
        assertEquals(0, account.getBalance());
    }

    /**
     * Ein Datum in der Vergangenheit erzeugt keine Buchung.
     */
    @Test
    @DisplayName("Deposit with past date is rejected and adds no booking")
    public void testDepositPastDate() {
        assertThrows(InvalidDateException.class, () -> account.deposit(new Scheduled(-1000), 100_000));
        assertTrue(account.getBookings().isEmpty());
    }

    /**
     * Die Buchungsliste kann von aussen nicht verändert werden.
     */
    @Test
    @DisplayName("getBookings() is read-only")
    public void testBookingsAreReadOnly() throws Exception {
        account.deposit(Scheduled.now(), 100_000);

        assertThrows(UnsupportedOperationException.class,
                () -> account.getBookings().add(new Booking(0, 1, "Hack")));
        assertEquals(1, account.getBookings().size());
    }

    /**
     * Das Buchungsdatum ist in Unix-Millisekunden gespeichert.
     */
    @Test
    @DisplayName("Booking date is stored as Unix milliseconds")
    public void testBookingDateIsUnixMillis() throws Exception {
        long before = System.currentTimeMillis();
        account.deposit(Scheduled.now(), 100_000);
        long after = System.currentTimeMillis();

        long date = account.getBookings().get(0).date();
        assertTrue(date >= before - 1000 && date <= after + 1000);
    }

    @Test
    @DisplayName("TreeMap of two Account-References should work")
    public void testReferences() {
        Account other = new SavingsAccount("S-2000");
        TreeMap<String, Account> accounts = new TreeMap<>();
        accounts.put(account.getId(), account);
        accounts.put(other.getId(), other);

        assertEquals(2, accounts.size());
        assertSame(account, accounts.get("1234567890"));
        assertSame(other, accounts.get("S-2000"));
    }

    @Test
    @DisplayName("Transactions in the past are invalid, now is valid")
    public void testCanTransact() {
        assertTrue(account.isInvalidTransaction(new Scheduled(-1000)));
        assertFalse(account.isInvalidTransaction(Scheduled.now()));
    }

    /**
     * Experimente mit print().
     */
    @Test
    @DisplayName("Statement shows texts, readable amounts and balance")
    public void testPrint() throws Exception {
        account.deposit(Scheduled.now(), 100_000, "Lohn");
        account.withdraw(Scheduled.now(), 50_000, "Bancomat");

        String statement = account.printStatement();
        System.out.println(statement);

        assertTrue(statement.contains("Lohn"));
        assertTrue(statement.contains("Bancomat"));
        assertTrue(statement.contains("CHF 1.00"));
        assertTrue(statement.contains("CHF -0.50"));
        assertTrue(statement.contains("Saldo: CHF 0.50"));
    }

    /**
     * Experimente mit print(year,month).
     */
    @Test
    @DisplayName("Monthly statement only contains bookings of that month")
    public void testMonthlyPrint() throws Exception {
        account.deposit(Scheduled.now(), 100_000, "Lohn");
        LocalDate today = LocalDate.now(ZoneId.of("Europe/Zurich"));

        String thisMonth = account.printStatement(today.getYear(), today.getMonthValue());
        String otherMonth = account.printStatement(today.getYear() - 1, today.getMonthValue());
        System.out.println(thisMonth);

        assertTrue(thisMonth.contains("Lohn"));
        assertFalse(otherMonth.contains("Lohn"));
    }

}