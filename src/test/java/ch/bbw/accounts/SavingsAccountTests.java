package ch.bbw.accounts;

import ch.bbw.Scheduled;
import ch.bbw.exceptions.InvalidAmountException;
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
}