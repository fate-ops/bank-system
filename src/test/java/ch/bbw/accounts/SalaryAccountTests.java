package ch.bbw.accounts;

import ch.bbw.Scheduled;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SalaryAccountTests {

	@Test
	void positiveCreditLimitIsRejected() {
		assertThrows(IllegalArgumentException.class,
				() -> new SalaryAccount("P-1", 1));
	}

	@Test
	void withdrawingExactlyToCreditLimitIsAllowed() throws Exception {
		SalaryAccount account = new SalaryAccount("P-1", -100_000);

		long balance = account.withdraw(Scheduled.now(), 100_000);

		assertEquals(-100_000, balance);
		assertEquals(-100_000, account.getBalance());
		assertEquals(-100_000, account.getCreditLimit());
		assertEquals(1, account.getBookings().size());
		assertEquals(-100_000, account.getBookings().get(0).amount());
	}

	@Test
	void withdrawingBeyondCreditLimitIsRejected() throws Exception {
		SalaryAccount account = new SalaryAccount("P-1", -100_000);
		account.withdraw(Scheduled.now(), 100_000, "Bancomat");

		assertThrows(InvalidAmountException.class,
				() -> account.withdraw(Scheduled.now(), 1, "Noch mehr"));

		assertEquals(-100_000, account.getBalance());
		assertEquals(1, account.getBookings().size());
	}

	@Test
	void zeroCreditLimitPreventsOverdraft() throws Exception {
		SalaryAccount account = new SalaryAccount("P-1", 0);
		account.deposit(Scheduled.now(), 100_000);

		account.withdraw(Scheduled.now(), 100_000);

		assertThrows(InvalidAmountException.class,
				() -> account.withdraw(Scheduled.now(), 1));

		assertEquals(0, account.getBalance());
		assertEquals(2, account.getBookings().size());
	}

	@Test
	void invalidTransactionsDoNotCreateBookings() {
		SalaryAccount account = new SalaryAccount("P-1", -100_000);

		assertThrows(InvalidAmountException.class,
				() -> account.withdraw(Scheduled.now(), 0));

		assertThrows(InvalidAmountException.class,
				() -> account.withdraw(Scheduled.now(), -1));

		assertThrows(InvalidDateException.class,
				() -> account.withdraw(new Scheduled(-1), 1));

		assertEquals(0, account.getBalance());
		assertTrue(account.getBookings().isEmpty());
	}
}