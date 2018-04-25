
package com.booking.ticket_service_homework;
import java.awt.List;
import java.util.ArrayList;

/*
 * SeatHold class has information related to the seats that are on hold
 * seatholdId :unique id to identify each SeatHold
 * ArrayList<SeatInformation> : the level, the row and the column of a seat
 * email: the customer's email
 * cost: cost of the ticket
 * holdtime: the time at which the seat is held
 * 
 */

public class SeatHold {
   int seatholdID;
   ArrayList<SeatInformation> list;
   String email;
   double cost;
   long holdTime;
    
      public SeatHold(int seatholdID,ArrayList<SeatInformation> list,String email, double cost, long holdTime){
    	  this.seatholdID=seatholdID;
    	  this.list= new ArrayList<SeatInformation>(list);
    	  this.email=email;
    	  this.cost = cost;
    	  this.holdTime = holdTime;
      }
   
}
