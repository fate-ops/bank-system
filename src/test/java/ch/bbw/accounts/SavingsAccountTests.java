package ch.bbw.accounts;

import ch.bbw.Scheduled;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SavingsAccountTests {

	@Test
	void withdrawingEntireBalanceIsAllowed() throws Exception {
		SavingsAccount account = new SavingsAccount("S-1");
		account.deposit(Scheduled.now(), 100_000);

		long balance = account.withdraw(Scheduled.now(), 100_000);

		assertEquals(0, balance);
		assertEquals(0, account.getBalance());
		assertEquals(2, account.getBookings().size());
	}

	@Test
	void overdraftIsRejectedWithAndWithoutText() throws Exception {
		SavingsAccount account = new SavingsAccount("S-1");
		account.deposit(Scheduled.now(), 100_000);

		assertThrows(InvalidAmountException.class,
				() -> account.withdraw(Scheduled.now(), 100_001));

		assertThrows(InvalidAmountException.class,
				() -> account.withdraw(
						Scheduled.now(), 100_001, "Bancomat"));

		assertEquals(100_000, account.getBalance());
		assertEquals(1, account.getBookings().size());
	}

    @Test
    void zeroWithdrawalDoesNotCreateABooking() {
        var account = new SavingsAccount("S-1");

        assertThrows(InvalidAmountException.class,
                () -> account.withdraw(Scheduled.now(), 0));
        assertTrue(account.getBookings().isEmpty());
    }

    @Test
    void pastWithdrawalDoesNotChangeTheBalance() throws Exception {
        var account = new SavingsAccount("S-1");
        account.deposit(Scheduled.now(), 100_000);

        assertThrows(InvalidDateException.class,
                () -> account.withdraw(new Scheduled(-1), 30_000));
        assertEquals(100_000, account.getBalance());
        assertEquals(1, account.getBookings().size());
    }

    @Test
    void withdrawalKeepsTheCustomBookingText() throws Exception {
        var account = new SavingsAccount("S-1");
        account.deposit(Scheduled.now(), 100_000);

        assertEquals(70_000, account.withdraw(Scheduled.now(), 30_000, "Bancomat"));
        var withdrawal = account.getBookings().getLast();
        assertEquals(-30_000, withdrawal.amount());
        assertEquals("Bancomat", withdrawal.text());
    }
}
