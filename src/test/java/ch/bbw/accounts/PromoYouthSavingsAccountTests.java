package ch.bbw.accounts;

import ch.bbw.Scheduled;
import ch.bbw.exceptions.InvalidAmountException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PromoYouthSavingsAccountTests {

    @Test
    void withdrawingExactlyToMinimumBalanceIsAllowed() throws Exception {
        PromoYouthSavingsAccount account =
                new PromoYouthSavingsAccount("Y-1");

        account.deposit(Scheduled.now(), 1_500_000);

        long balance = account.withdraw(Scheduled.now(), 500_000);

        assertEquals(1_000_000, balance);
        assertEquals(2, account.getBookings().size());
    }

    @Test
    void withdrawingBelowMinimumIsRejected() throws Exception {
        PromoYouthSavingsAccount account =
                new PromoYouthSavingsAccount("Y-1");

        account.deposit(Scheduled.now(), 1_500_000);

        assertThrows(InvalidAmountException.class,
                () -> account.withdraw(Scheduled.now(), 500_001));

        assertThrows(InvalidAmountException.class,
                () -> account.withdraw(
                        Scheduled.now(), 500_001, "Bancomat"));

        assertEquals(1_500_000, account.getBalance());
        assertEquals(1, account.getBookings().size());
    }

    @Test
    void emptyAccountCannotWithdraw() {
        PromoYouthSavingsAccount account =
                new PromoYouthSavingsAccount("Y-1");

        assertThrows(InvalidAmountException.class,
                () -> account.withdraw(Scheduled.now(), 1));

        assertEquals(0, account.getBalance());
        assertTrue(account.getBookings().isEmpty());
    }

    @Test
    void depositWithTextIsBookedNormally() throws Exception {
        PromoYouthSavingsAccount account =
                new PromoYouthSavingsAccount("Y-1");

        long balance = account.deposit(Scheduled.now(), 2_000_000, "Taschengeld");

        assertEquals(2_000_000, balance);
        assertEquals(1, account.getBookings().size());
        assertEquals("Taschengeld", account.getBookings().getFirst().text());
    }

    @Test
    void isASavingsAccountWithItsOwnType() {
        PromoYouthSavingsAccount account =
                new PromoYouthSavingsAccount("Y-1");

        assertInstanceOf(SavingsAccount.class, account);
        assertEquals("Promo-Jugendsparkonto", account.getAccountType());
        assertEquals("Y-1", account.getId());
    }
}