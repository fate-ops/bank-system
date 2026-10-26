import ch.bbw.Booking;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * Tests für die Klasse Booking.
 *
 * @author Luigi Cavuoti
 * @version 1.1
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
	 * Experimente mit print().
	 */
	@Test
	@DisplayName("Not a real test, it prints a list of bookings")
	public void testPrint()
	{
		fail("ToDo");
	}
}