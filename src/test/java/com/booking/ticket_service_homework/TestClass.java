package com.booking.ticket_service_homework;


import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.Test;


public class TestClass {
	@Test
	public void test() throws InterruptedException {
		ArrayList<LevelInformation> input = new ArrayList<LevelInformation>();
		input.add(new LevelInformation(1,"Orchestra", 100, 25, 30));
		input.add(new LevelInformation(2,"Main", 75, 20, 100));
		input.add(new LevelInformation(3,"Balcony 1", 50, 15, 100));
		input.add(new LevelInformation(4,"Balcony 2", 40, 15, 100));
		BookingSystem system = new BookingSystem(input);
		Optional<Integer> o0 = Optional.empty();
		Optional<Integer> o1 = Optional.of(1);
		Optional<Integer> o2 = Optional.of(2);
		Optional<Integer> o3 = Optional.of(3);
		Optional<Integer> o4 = Optional.of(4);
		assertEquals(system.numSeatsAvailable(o0),5750);
		assertEquals(system.numSeatsAvailable(o1),750);
		
		
		// Holding five seats with optional level. Must be held from level 1
		SeatHold hold1 = system.findAndHoldSeats(5, o0, o0, "manisha@gmail.com");
		assertEquals(system.numSeatsAvailable(o1),745);
		
		// Check seats after Timeout 
		Thread.sleep(4000);
		assertEquals(system.numSeatsAvailable(o1),750);
		
		// Holding 10 seats at level 4
		SeatHold hold2 = system.findAndHoldSeats(10, o4, o4, "manisha@gmail.com");
		assertEquals(system.numSeatsAvailable(o4),1490);
		
		
		// Reserve booking. Must return a confirmation of code of length 10.
		String code = system.reserveSeats(hold2.seatholdID, "manisha@gmail.com");
		assertEquals(code.length(),10);
		
		// Holding 1600 seats at level 3. Must return null and not hold any seats
		SeatHold hold3 = system.findAndHoldSeats(1600, o3, o3, "manisha@gmail.com");
		assertEquals(hold3,null);
		assertEquals(system.numSeatsAvailable(o3),1500);
		
	}
		
	
	
}
