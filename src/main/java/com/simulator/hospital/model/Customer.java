package com.simulator.hospital.model;

import com.simulator.hospital.framework.*;

/**
 * Customer class represents a customer in the simulation.
 * It tracks the customer's arrival and removal times and assigns a unique ID to each customer.
 * It also reports results and calculates the mean service time across all customers.
 */
// TODO:
// Customer to be implemented according to the requirements of the simulation model (data!)
public class Customer {
	private double arrivalTime;
	private double removalTime;
	private int id;
	private static int customerCount = 1;		// Counter for generating unique IDs
	private static long sum = 0;				// Sum of all customer service times
	
	public Customer(){
	    id = customerCount++;
	    
		arrivalTime = Clock.getInstance().getClock();		// Set the arrival time to the current clock time
		Trace.out(Trace.Level.INFO, "New customer #" + id + " arrived at  " + arrivalTime);
	}

	public double getRemovalTime() {
		return removalTime;
	}

	public void setRemovalTime(double removalTime) {
		this.removalTime = removalTime;
	}

	public double getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(double arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public int getId() {
		return id;
	}
	
	public void reportResults(){
		Trace.out(Trace.Level.INFO, "\nCustomer " + id + " ready! ");
		Trace.out(Trace.Level.INFO, "Customer "   + id + " arrived: " + arrivalTime);
		Trace.out(Trace.Level.INFO,"Customer "    + id + " removed: " + removalTime);
		Trace.out(Trace.Level.INFO,"Customer "    + id + " stayed: "  + (removalTime - arrivalTime));

		sum += (removalTime - arrivalTime);
		double mean = sum/id;
		System.out.println("Current mean of the customer service times " + mean);
	}
}
