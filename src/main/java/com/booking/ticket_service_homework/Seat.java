
package com.booking.ticket_service_homework;
/*
 * seat class has two properties of the Seat
 * state: whether the state is on hold or available or reserved,
 *        0 for available
 *        1 for hold
 *        2 for reserved
 * holdTime: the time at which the seat is on hold
 *        
 */
public class Seat {
	int state; // 0 for Available, 1 for hold, 2 for reserved
	long holdTime;
	public Seat(){
		state = 0;
		holdTime = 0;
	}

}
