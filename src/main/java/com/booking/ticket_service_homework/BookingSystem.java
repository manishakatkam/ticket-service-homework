package com.booking.ticket_service_homework;
import java.awt.List;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CountDownLatch;


	
public class BookingSystem implements TicketService{

	ArrayList<VenueLevel> venue_list;
	final static int timeOut = 3; // timeout is set to 5 minutes
	
	int seatHoldIds = -1;
	Hashtable<Integer, SeatHold> database;
	Hashtable<String, SeatHold> reservations;
	Hashtable<String, ArrayList<String>> userBookings;

	public BookingSystem(ArrayList<LevelInformation> input) {
		venue_list = new ArrayList<VenueLevel>();
		for (int i=0;i<input.size();i++) {
			venue_list.add(new VenueLevel(input.get(i)));
		}
		database = new Hashtable<Integer, SeatHold>();
		reservations = new Hashtable<String, SeatHold>();
		userBookings = new Hashtable<String, ArrayList<String>>();
	}
	
	
	
	/* @param the venueLevel is provided optionally, otherwise entire venue is considered i.e all the levels
	 * 
	 * @return the total number of available seats in a given level or in the entire venue
	 *  
	 */
	
	
	public int numSeatsAvailable(Optional<Integer> venueLevel){
		if (venueLevel.isPresent()) {
			return getSeatsAtLevel(venueLevel.get()-1);
		}
		else {
			int res = 0;
			for (int i=0;i<venue_list.size();i++)
				res += getSeatsAtLevel(i);
			return res;
		}
	}
	
	/*
	 * @return the available number of seats in a particular level
	 * 
	 * * what's happening inside.....we iterate through the entire level and increment the counter 
	 * for all available seats and during the process if any seat is in hold state and has timeout 
	 * then we change it to available state.
	 */
	
	int getSeatsAtLevel(int venueLevel) {
		int count=0;
		long currentTime = System.currentTimeMillis()/1000;
		Seat[][] levelSeats = venue_list.get(venueLevel).seats;
		for(int i=0;i<levelSeats.length;i++){
			for(int j=0;j<levelSeats[0].length;j++){
				if (levelSeats[i][j].state==1 && currentTime - levelSeats[i][j].holdTime> timeOut) {
					levelSeats[i][j].state = 0;
					levelSeats[i][j].holdTime = 0;
				}
				if(levelSeats[i][j].state==0)
					count++;
			}
		}
		
		return count;
	}
	
	/* @param number of seats to be hold, 
	 * @param minimum level   provided conditionally
	 * @param maximum  levels provided conditionally
	 * @param customer mailID of the customer
	 * 
	 * @return the SeatHold object, hold the number of tickets requested for given customer mail ID.
	 * If seats requested is more than available seats then it returns null
	 * SeatHold has SeatHoldID,SeatInformation object which consists of seat's level,row,and column,
	 * customermailID,total cost of the tickets,time stamp at which the seat has been held.
	 * 
	 *  what's happening inside.....we iterate through each level and if the seat is available we hold it,
	 *  if the seat is already held and hold duration timed out,
	 *  the seat is considered available and used for current request 
	 *  Cost of the ticket is calculated simultaneously
	 *              
	 *              
	 *              
	 */
	
	
	public SeatHold findAndHoldSeats(int numSeats,Optional<Integer> minLevel,
										Optional<Integer> maxlevel,String customerEmail){
		ArrayList<SeatInformation> seat_numbers=new ArrayList<SeatInformation>();
		long currentTime = System.currentTimeMillis()/1000;
		double cost = 0;
		int minimumLevel = 0, maximumLevel = venue_list.size()-1;
		if (minLevel.isPresent()) {
			minimumLevel = minLevel.get()-1;
		}
		if (maxlevel.isPresent()) {
			maximumLevel = maxlevel.get()-1;
		}
		
		for(int i=minimumLevel;i<=maximumLevel && numSeats > 0;i++){
			for(int j=0;j<venue_list.get(i).seats.length && numSeats > 0;j++){
				for(int k=0;k<venue_list.get(i).seats[0].length && numSeats > 0;k++){
					if(venue_list.get(i).seats[j][k].state==0){
						venue_list.get(i).seats[j][k].state=1;
						venue_list.get(i).seats[j][k].holdTime = currentTime;
						cost += venue_list.get(i).info.cost;
						seat_numbers.add( new SeatInformation(i, j, k));
						numSeats--;
					}
					else if(venue_list.get(i).seats[j][k].state==1 && 
								currentTime - venue_list.get(i).seats[j][k].holdTime >= timeOut){
						venue_list.get(i).seats[j][k].state=1;
						venue_list.get(i).seats[j][k].holdTime = currentTime;
						cost += venue_list.get(i).info.cost;
						seat_numbers.add(new SeatInformation(i, j, k));
						numSeats--;
					}
				}
			}
		}
		if (numSeats > 0) {
			for (int i=0;i<seat_numbers.size();i++) {
				SeatInformation tmp = seat_numbers.get(i);
				venue_list.get(tmp.level).seats[tmp.row][tmp.column].state = 0;
			}
			return null;
		}
		seatHoldIds++;
		SeatHold res = new SeatHold(seatHoldIds, seat_numbers, customerEmail, cost, currentTime);
		database.put(seatHoldIds, res);
		return res;
	}
	
	
	/*
	 *@param the SeatHoldID 
	 *@param customer mail id
	 *
	 *@return  a random 10 character length string ,the confirmation code
	 *
	 *what's happening inside.....we get the SeatHold Object corresponding to SeatHoldID from database Hashmap
	 *IF the current hold request has already timed out we return error.
	 *We iterate through the list of seats and update their state to reserved 
	 *returns a random 10 character length string as confirmation code.
	 *And,the confirmation code and corresponding SeatHold Object are stored in Reservations HashMap
	 *Also, another HashTable called User Bookings is used to keep track of each users booking 
	 */
	
	
	public String reserveSeats(int seatholdID,String customerEmail){
		long currentTime = System.currentTimeMillis()/1000;
		if (!database.containsKey(seatholdID))
			return "Error: Record Not found";
		
		SeatHold hold = database.get(seatholdID);
		if (currentTime - hold.holdTime > timeOut)
			return "Error: Hold Timed out";
		
		for (int i=0;i<hold.list.size();i++) {
			int p = hold.list.get(i).level;
			int q = hold.list.get(i).row;
			int r = hold.list.get(i).column;
			venue_list.get(p).seats[q][r].state = 2;
		}
		
		String code = generateRandomChars(10);
		while(reservations.contains(code))
			code = generateRandomChars(10);
		reservations.put(code, hold);
		
		if (!userBookings.containsKey(customerEmail))
			userBookings.put(customerEmail,  new ArrayList<String>());
		
		
		userBookings.get(customerEmail).add(code);
		
		return code;
	}

	
	/*
	 * this method generates the 10 character length random string for the confirmation code
	 */
	
	public static String generateRandomChars(int length) {
		String candidateChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
		StringBuilder sb = new StringBuilder();
	    Random random = new Random();
	    for (int i = 0; i < length; i++) {
	        sb.append(candidateChars.charAt(random.nextInt(candidateChars
	                .length())));
	    }

	    return sb.toString();
	}
	
	
	
	
	
	

}
