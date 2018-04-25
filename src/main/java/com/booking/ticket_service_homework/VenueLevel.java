
package com.booking.ticket_service_homework;
import java.util.ArrayList;


/*
 * VenueLevel has the LevelInformation Object 
 * the 2 dimensional array to represent the seats in each level
 */

public class VenueLevel {
	LevelInformation info;
	Seat[][] seats;
	
	
	public VenueLevel(LevelInformation input){
		info = new LevelInformation(input);
		this.seats=new Seat[info.rows][info.seats];
		for (int i=0;i<info.rows;i++) {
			for (int j=0;j<info.seats;j++) {
				seats[i][j] = new Seat();
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}



