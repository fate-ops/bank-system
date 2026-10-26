package ch.bbw.accounts;

import ch.bbw.Scheduled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountPolymorphismTests {

    @Test
    void mixedAccountsExposeTheirOwnRulesAndTypes() throws Exception {
        Account savings = new SavingsAccount("S-1");
        Account salary = new SalaryAccount("P-1", -1_000_000);
        Account youth = new PromoYouthSavingsAccount("Y-1");

        savings.deposit(Scheduled.now(), 2_000_000);
        salary.withdraw(Scheduled.now(), 500_000);
        youth.deposit(Scheduled.now(), 1_000_000);

        List<Account> accounts = List.of(savings, salary, youth);

        long total = accounts.stream()
                .mapToLong(Account::getBalance)
                .sum();

        assertEquals(2_500_000, total);

        List<String> types = accounts.stream()
                .map(Account::getAccountType)
                .toList();

        assertEquals(
                List.of("Sparkonto", "Lohnkonto", "Promo-Jugendsparkonto"),
                types);
    }
}