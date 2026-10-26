import ch.bbw.Booking;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Tests für die Klasse Booking.
 *
 * @author Luigi Cavuoti, Hassan Alayan
 * @version 1.2
 */
public class BookingTests
{
	/**
	 * Tests für die Erzeugung von Buchungen.
	 */
	@Test
	@DisplayName("Construct Simple Booking, should work ")
	public void testInitialization()
	{
		Booking booking = new Booking(1000, 500_000, "Lohn");
		assertEquals(1000, booking.date());
		assertEquals(500_000, booking.amount());
		assertEquals("Lohn", booking.text());
	}

	/**
	 * Eine Abhebung wird als negativer Betrag gespeichert.
	 */
	@Test
	@DisplayName("Negative amount (withdrawal) is allowed")
	public void testNegativeAmount()
	{
		Booking booking = new Booking(1000, -200_000, "Bancomat");
		assertEquals(-200_000, booking.amount());
	}

	/**
	 * Eine Buchung ohne Text wird abgelehnt.
	 */
	@Test
	@DisplayName("Text null is rejected")
	public void testNullText()
	{
		assertThrows(IllegalArgumentException.class, () -> new Booking(1000, 500_000, null));
	}

	/**
	 * Ein leerer Text oder nur Leerzeichen wird abgelehnt.
	 */
	@Test
	@DisplayName("Empty or blank text is rejected")
	public void testBlankText()
	{
		assertThrows(IllegalArgumentException.class, () -> new Booking(1000, 500_000, ""));
		assertThrows(IllegalArgumentException.class, () -> new Booking(1000, 500_000, "   "));
	}

	/**
	 * Experimente mit print().
	 */
	@Test
	@DisplayName("toString of a booking contains the text")
	public void testPrint()
	{
		Booking booking = new Booking(1000, 500_000, "Lohn");
		System.out.println(booking);
		assertTrue(booking.toString().contains("Lohn"));
	}
}