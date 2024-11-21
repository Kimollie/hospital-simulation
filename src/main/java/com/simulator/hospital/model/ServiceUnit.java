package com.simulator.hospital.model;

import com.simulator.eduni.distributions.ContinuousGenerator;
import com.simulator.hospital.framework.*;

import java.util.ArrayList;
import java.util.LinkedList;
import com.simulator.hospital.framework.Trace;

/**
 * ServicePoint class models a point of service in the simulation,
 * where customers wait in a queue and receive service when available.
 */
// TODO:
// Service Point functionalities & calculations (+ variables needed) and reporting to be implemented
public class ServiceUnit {
	private LinkedList<Customer> queue = new LinkedList<>();
	private ArrayList<ServicePoint> servicePoints = new ArrayList<>();
	private ContinuousGenerator generator;
	private EventList eventList;
	private EventType eventTypeScheduled;
	private ServicePoint selectedServicePoint;
	private EventType type;

	public ServiceUnit(ContinuousGenerator generator, EventList eventList, EventType type, int servicePointNumber){
		this.eventList = eventList;
		this.generator = generator;
		this.eventTypeScheduled = type;
		for (int i = 1; i <= servicePointNumber; i++) {
			ServicePoint servicePoint = new ServicePoint();
			servicePoints.add(servicePoint);
		}
		this.type = type;
	}

	// Adds a customer to the queue. The first customer in the queue will be serviced
	public void addQueue(Customer a) {	// The first customer of the queue is always in service
		queue.add(a);
		Trace.out(Trace.Level.ERR, "Add customer" + a.getId() + " to queue type " + type );
	}

	// Removes and returns the customer who has completed their service
	public Customer removeQueue() {
		Customer a = queue.poll();
		Trace.out(Trace.Level.ERR, "Remove customer "  + a.getId() +" from queue type " + type );
		selectedServicePoint.setAvailable(true);		// Mark the service point as not reserved (available)
		return a;
	}

	// Begins servicing the first customer in the queue
	public int beginService() {
		int customerId = queue.peek().getId();
		Trace.out(Trace.Level.INFO, "Starting a new service for the customer #" + customerId);
		for (ServicePoint servicePoint : servicePoints) {
			if (servicePoint.isAvailable()) {
				selectedServicePoint = servicePoint;
				servicePoint.setAvailable(false);		// Mark the service point as reserved (not available)
				break;
			}
		}
		double serviceTime = generator.sample();
		eventList.add(new Event(eventTypeScheduled, Clock.getInstance().getClock()+serviceTime));
		return customerId;
	}

	// Checks if the service point is currently reserved
	public boolean isReserved(){
		boolean reserved = true;
		for (ServicePoint servicePoint : servicePoints) {
			if (servicePoint.isAvailable()) {
				reserved = false;
				break;
			}
		}
		return reserved;
	}

	// Checks if there are any customers waiting in the queue
	public boolean isOnQueue(){
		return !queue.isEmpty();
	}

	// get selected service point
	public ServicePoint getSelectedServicePoint() {
		return selectedServicePoint;
	}
}
