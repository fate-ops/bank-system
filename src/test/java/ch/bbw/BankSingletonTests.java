package ch.bbw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class BankSingletonTests {

    @Test
    void getInstanceAlwaysReturnsTheSameBank() {
        assertSame(Bank.getInstance(), Bank.getInstance());
    }

    @Test
    void bankHasNoPublicConstructor() {
        assertEquals(0, Bank.class.getConstructors().length);
    }
}