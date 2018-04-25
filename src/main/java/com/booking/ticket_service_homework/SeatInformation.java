
package com.booking.ticket_service_homework;

/*
 * this class has the information of each seat
 * level: level of the seat
 * row: row number in the level
 * column : seat number in the row
 */
public class SeatInformation {
	int level;
	int row;
	int column;
	public SeatInformation(int level, int row, int column) {
		this.level = level;
		this.column = column;
		this.row = row;
	}
}
