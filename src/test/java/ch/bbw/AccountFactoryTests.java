package ch.bbw;

import ch.bbw.accounts.PromoYouthSavingsAccount;
import ch.bbw.accounts.SalaryAccount;
import ch.bbw.accounts.SavingsAccount;
import ch.bbw.exceptions.InvalidAmountException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountFactoryTests {
    private final AccountFactory factory = new AccountFactory();

    @Test
    void createsSavingsAccount() {
        assertInstanceOf(SavingsAccount.class, factory.createSavingsAccount());
        assertEquals("Sparkonto", factory.createSavingsAccount().getAccountType());
    }

    @Test
    void createsYouthAccountWithTheYouthRule() {
        assertInstanceOf(PromoYouthSavingsAccount.class,
                factory.createPromoYouthSavingsAccount());
    }

    @Test
    void genericCreateDispatchesToSalaryAccount() {
        assertInstanceOf(SalaryAccount.class, factory.create(AccountType.SALARY, 0));
    }

    @Test
    void sharesIdSequenceAcrossAccountTypes() {
        assertEquals("S-1000", factory.createSavingsAccount().getId());
        assertEquals("Y-1001", factory.createPromoYouthSavingsAccount().getId());
        assertEquals("P-1002", factory.createSalaryAccount(0).getId());
    }

    @Test
    void salaryAccountUsesTheSuppliedCreditLimit() throws Exception {
        var salary = assertInstanceOf(SalaryAccount.class,
                factory.createSalaryAccount(-100_000));
        assertEquals(-100_000, salary.getCreditLimit());
        assertEquals(-100_000, salary.withdraw(Scheduled.now(), 100_000));
        assertThrows(InvalidAmountException.class,
                () -> salary.withdraw(Scheduled.now(), 1));
    }

    @Test
    void positiveSalaryLimitIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> factory.createSalaryAccount(1));
    }
}
