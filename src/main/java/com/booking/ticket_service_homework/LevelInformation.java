package com.booking.ticket_service_homework;

/*
 * this class stores the information of a level
 * levelId : the unique number for a level
 * name: the name of the level
 * cost: the cost of the ticket in this level
 * rows: the number of rows in the level
 * seats: the number of seats in each row of the level
 */
public class LevelInformation {
	int levelId;
	String name;
	double cost;
	int rows;
	int seats;
	public LevelInformation(int levelId, String name, double cost, int rows, int seats) {
		this.levelId = levelId;
		this.name = name;
		this.cost = cost;
		this.rows = rows;
		this.seats = seats;
	}
	
	public LevelInformation(LevelInformation input) {
		levelId = input.levelId;
		name = input.name;
		cost = input.cost;
		rows = input.rows;
		seats = input.seats;
	}
}
