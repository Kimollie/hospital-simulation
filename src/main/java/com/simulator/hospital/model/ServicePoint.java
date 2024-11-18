package com.simulator.hospital.model;

import com.simulator.eduni.distributions.ContinuousGenerator;
import com.simulator.hospital.framework.*;
import java.util.LinkedList;

/**
 * ServicePoint class models a point of service in the simulation,
 * where customers wait in a queue and receive service when available.
 */
// TODO:
// Service Point functionalities & calculations (+ variables needed) and reporting to be implemented
public class ServicePoint {
	private LinkedList<Customer> queue = new LinkedList<>(); // Data Structure used
	private ContinuousGenerator generator;
	private EventList eventList;
	private EventType eventTypeScheduled;
	//Queuestrategy strategy; // option: ordering of the customer
	private boolean reserved = false;


	public ServicePoint(ContinuousGenerator generator, EventList eventList, EventType type){
		this.eventList = eventList;
		this.generator = generator;
		this.eventTypeScheduled = type;
	}

	// Adds a customer to the queue. The first customer in the queue will be serviced
	public void addQueue(Customer a) {	// The first customer of the queue is always in service
		queue.add(a);
	}

	// Removes and returns the customer who has completed their service
	public Customer removeQueue() {
		reserved = false;		// Mark the service point as not reserved (available)
		return queue.poll();
	}

	// Begins servicing the first customer in the queue
	public void beginService() {
		Trace.out(Trace.Level.INFO, "Starting a new service for the customer #" + queue.peek().getId());
		
		reserved = true;		// Mark the service point as reserved (not available)
		double serviceTime = generator.sample();
		eventList.add(new Event(eventTypeScheduled, Clock.getInstance().getClock()+serviceTime));
	}

	// Checks if the service point is currently reserved
	public boolean isReserved(){
		return reserved;
	}

	// Checks if there are any customers waiting in the queue
	public boolean isOnQueue(){
		return queue.size() != 0;
	}
}
