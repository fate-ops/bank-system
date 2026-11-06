package ch.bbw;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BankTests {

    private Bank bank;

    @BeforeEach
    void setUp() {
        bank = new Bank(new AccountFactory());
    }

    @Test
    void testCreate() throws Exception {
        String first = bank.createSavingsAccount();
        String second = bank.createSavingsAccount();

        assertNotEquals(first, second);
        assertEquals("Sparkonto", bank.getAccount(first).getAccountType());
        assertEquals("Sparkonto", bank.getAccount(second).getAccountType());
        assertEquals(0, bank.getBalance(first));
        assertEquals(0, bank.getBalance(second));
    }

    @Test
    void testDeposit() throws Exception {
        String id = bank.createSavingsAccount();
        bank.deposit(id, Scheduled.now(), 100_000, "Lohn");

        assertEquals(100_000, bank.getBalance(id));
        assertEquals("Lohn", bank.getAccount(id).getBookings().getFirst().text());
    }

    @Test
    void testWithdraw() throws Exception {
        String id = bank.createSavingsAccount();
        bank.deposit(id, Scheduled.now(), 100_000);

        long balance = bank.withdraw(id, Scheduled.now(), 30_000, "Bancomat");

        assertEquals(70_000, balance);
        assertEquals("Bancomat", bank.getAccount(id).getBookings().get(1).text());
    }

    @Test
    void depositStartsWithAnEmptyBank() throws Exception {
        assertEquals(0, bank.getBalance());

        String id = bank.createSavingsAccount();
        bank.deposit(id, Scheduled.now(), 100_000);

        assertEquals(100_000, bank.getBalance());
    }

    @Test
    void anotherTestAlsoStartsEmptyAndCanDeposit() throws Exception {
        assertEquals(0, bank.getBalance());

        String id = bank.createSavingsAccount();
        bank.deposit(id, Scheduled.now(), 200_000);

        assertEquals(200_000, bank.getBalance());
    }

    @Test
    void testPrint() throws Exception {
        String id = bank.createSavingsAccount();
        bank.deposit(id, Scheduled.now(), 100_000, "Einzahlung Test");

        String statement = bank.getAccount(id).printStatement();

        assertTrue(statement.contains("Einzahlung Test"));
        assertTrue(statement.contains("Saldo: CHF 1.00"));
    }

    @Test
    void testMonthlyPrint() throws Exception {
        String id = bank.createSavingsAccount();
        bank.deposit(id, Scheduled.now(), 100_000, "Monatsbuchung");

        var account = bank.getAccount(id);
        long bookingDate = account.getBookings().getFirst().date();
        YearMonth month = YearMonth.from(
                Instant.ofEpochMilli(bookingDate).atZone(ZoneId.of("Europe/Zurich"))
        );
        YearMonth previousMonth = month.minusMonths(1);

        assertTrue(account.printStatement(month.getYear(), month.getMonthValue())
                .contains("Monatsbuchung"));
        assertFalse(account.printStatement(previousMonth.getYear(), previousMonth.getMonthValue())
                .contains("Monatsbuchung"));
    }

    @Test
    void testBalance() throws Exception {
        String savings = bank.createSavingsAccount();
        String youth = bank.createPromoYouthSavingsAccount();
        String salary = bank.createSalaryAccount(-100_000);

        bank.deposit(savings, Scheduled.now(), 200_000);
        bank.deposit(youth, Scheduled.now(), 100_000);
        bank.withdraw(salary, Scheduled.now(), 50_000);

        assertEquals(-50_000, bank.getBalance(salary));
        assertEquals(250_000, bank.getBalance());
    }

    @Test
    void testTop5() throws Exception {
        createAccountsForRanking();

        var balances = bank.top5HighestBalances().stream()
                .map(account -> account.getBalance())
                .toList();

        assertEquals(
                List.of(500_000L, 400_000L, 300_000L, 200_000L, 100_000L),
                balances
        );
    }

    @Test
    void testBottom5() throws Exception {
        String salary = createAccountsForRanking();
        var lowest = bank.top5LowestBalances();

        assertEquals(salary, lowest.getFirst().getId());
        assertEquals(
                List.of(-50_000L, 100_000L, 200_000L, 300_000L, 400_000L),
                lowest.stream().map(account -> account.getBalance()).toList()
        );
    }

    private String createAccountsForRanking() throws Exception {
        String salary = bank.createSalaryAccount(-100_000);
        bank.withdraw(salary, Scheduled.now(), 50_000);

        for (long balance = 100_000; balance <= 400_000; balance += 100_000) {
            String id = bank.createSavingsAccount();
            bank.deposit(id, Scheduled.now(), balance);
        }

        String youth = bank.createPromoYouthSavingsAccount();
        bank.deposit(youth, Scheduled.now(), 500_000);

        return salary;
    }
}