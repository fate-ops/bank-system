package ch.bbw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

class BankSingletonTests {

    @BeforeEach
    void resetSharedBank() {
        Bank.resetForTests();
    }

    @Test
    void getInstanceAlwaysReturnsTheSameBank() {
        assertSame(Bank.getInstance(), Bank.getInstance());
    }

    @Test
    void bankHasNoPublicConstructor() {
        assertThrows(NoSuchMethodException.class,
                () -> Bank.class.getConstructor());
    }

    @Test
    void depositStartsWithAnEmptyBank() throws Exception {
        Bank bank = Bank.getInstance();
        assertEquals(0, bank.getBalance());

        String id = bank.createSavingsAccount();
        bank.deposit(id, Scheduled.now(), 100_000);

        assertEquals(100_000, bank.getBalance());
    }

    @Test
    void anotherTestAlsoStartsEmptyAndCanDeposit() throws Exception {
        Bank bank = Bank.getInstance();
        assertEquals(0, bank.getBalance());

        String id = bank.createSavingsAccount();
        bank.deposit(id, Scheduled.now(), 200_000);

        assertEquals(200_000, bank.getBalance());
    }
}