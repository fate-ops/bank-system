package ch.bbw.accounts;


import ch.bbw.Scheduled;
import ch.bbw.exceptions.InvalidAmountException;
import ch.bbw.exceptions.InvalidDateException;

/**
 * Sparkonto.
 *
 * <p>
 * Ein Sparkonto darf nicht überzogen werden: Der Saldo darf nie unter 0 fallen.
 * </p>
 *
 * @author Luigi Cavuoti, lro@gmx.ch, Hassan Alayan
 * @version 1.2
 */
public class SavingsAccount extends Account
{
	/**
	 * Initialisiert ein Sparkonto
	 *
	 * @param id die Kontonummer
	 */
	public SavingsAccount(String id)
	{
		super(id);
	}

	/**
	 * Hebt einen Betrag ab, aber nur, wenn genug Saldo vorhanden ist.
	 *
	 * <p>
	 * Überschrieben wird die Variante mit Text, weil auch die Variante ohne Text
	 * ({@code withdraw(date, amount)}) intern diese Methode aufruft. So gilt die
	 * Prüfung für beide.
	 * </p>
	 *
	 * @param date   das Transaktionsdatum
	 * @param amount der abzuhebende Betrag in Millirappen
	 * @param text   der Buchungstext
	 * @return der neue Saldo
	 * @throws InvalidAmountException wenn der Betrag grösser als der Saldo ist (genau der Saldo ist erlaubt)
	 * @throws InvalidDateException   wenn das Datum in der Vergangenheit liegt
	 */
	@Override
	public long withdraw(Scheduled date, long amount, String text) throws InvalidAmountException, InvalidDateException {
		if (getBalance() < amount)
			throw new InvalidAmountException("Amount is greater than balance");

		return super.withdraw(date, amount, text);
	}

	@Override
	public String getAccountType() {
		return "Sparkonto";
	}
}