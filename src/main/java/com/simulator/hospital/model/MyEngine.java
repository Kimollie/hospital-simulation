package com.simulator.hospital.model;

import com.simulator.eduni.distributions.ContinuousGenerator;
import com.simulator.eduni.distributions.Normal;
import com.simulator.eduni.distributions.Uniform;
import com.simulator.hospital.framework.*;
import com.simulator.eduni.distributions.Negexp;

import java.util.Random;

/**
 * MyEngine class extends the abstract Engine class to implement a custom simulation.
 * It models a system with three service points that customers must go through sequentially.
 */

public class MyEngine extends Engine {
	private ArrivalProcess arrivalProcess;
	private ServicePoint[] servicePoints;
	public static final boolean TEXTDEMO = true;
	public static final boolean FIXEDARRIVALTIMES = false;
	public static final boolean FXIEDSERVICETIMES = false;

	/*
	 * This is the place where you implement your own simulator
	 *
	 * Demo simulation case:
	 * Simulate three service points, customer goes through all three service points to get serviced
	 * 		--> SP1 --> SP2 --> SP3 -->
	 */
	public MyEngine() {
		servicePoints = new ServicePoint[3];

		if (TEXTDEMO) {
			/* special setup for the example in text
			 * https://github.com/jacquesbergelius/PP-CourseMaterial/blob/master/1.1_Introduction_to_Simulation.md
			 */
			Random r = new Random();

			ContinuousGenerator arrivalTime = null;
			if (FIXEDARRIVALTIMES) {
				/* version where the arrival times are constant (and greater than service times) */

				// make a special "random number distribution" which produces constant value for the customer arrival times
				arrivalTime = new ContinuousGenerator() {
					@Override
					public double sample() {
						return 10;
					}

					@Override
					public void setSeed(long seed) {
					}

					@Override
					public long getSeed() {
						return 0;
					}

					@Override
					public void reseed() {
					}
				};
			} else
				// exponential distribution is used to model customer arrivals times, to get variability between programs runs, give a variable seed
				arrivalTime = new Negexp(10, Integer.toUnsignedLong(r.nextInt()));

			ContinuousGenerator serviceTime = null;
			if (FXIEDSERVICETIMES) {
				// make a special "random number distribution" which produces constant value for the service time in service points
				serviceTime = new ContinuousGenerator() {
					@Override
					public double sample() {
						return 9;
					}

					@Override
					public void setSeed(long seed) {
					}

					@Override
					public long getSeed() {
						return 0;
					}

					@Override
					public void reseed() {
					}
				};
			} else
				// normal distribution used to model service times
				serviceTime = new Normal(10, 6, Integer.toUnsignedLong(r.nextInt()));

			// Initialize the service points with the chosen service time distribution
			servicePoints[0] = new ServicePoint(serviceTime, eventList, EventType.DEP1);
			servicePoints[1] = new ServicePoint(serviceTime, eventList, EventType.DEP2);
			servicePoints[2] = new ServicePoint(serviceTime, eventList, EventType.DEP3);

			// Initialize the arrival process
			arrivalProcess = new ArrivalProcess(arrivalTime, eventList, EventType.ARR1);
		} else {
			/* more realistic simulation case with variable customer arrival times and service times */
			servicePoints[0] = new ServicePoint(new Normal(10, 6), eventList, EventType.DEP1);
			servicePoints[1] = new ServicePoint(new Normal(10, 10), eventList, EventType.DEP2);
			servicePoints[2] = new ServicePoint(new Normal(5, 3), eventList, EventType.DEP3);

			arrivalProcess = new ArrivalProcess(new Negexp(15, 5), eventList, EventType.ARR1);
		}
	}

	// Initializes the simulation by generating the first arrival event
	@Override
	protected void initialize() {	// First arrival in the system
		arrivalProcess.generateNextEvent();
	}

	// Processes B-phase events, such as arrivals and departures
	@Override
	protected void runEvent(Event t) {  // B phase events
		Customer a;

		switch ((EventType)t.getType()) {
		case ARR1:
			// Handle a new customer arrival: add to the queue of the first service point
			servicePoints[0].addQueue(new Customer());
			arrivalProcess.generateNextEvent();		// Schedule the next arrival
			break;

		case DEP1:
			// Handle departure from service point 1: move customer to service point 2
			a = servicePoints[0].removeQueue();
			servicePoints[1].addQueue(a);
			break;

		case DEP2:
			// Handle departure from service point 2: move customer to service point 3
			a = servicePoints[1].removeQueue();
			servicePoints[2].addQueue(a);
			break;

		case DEP3:
			// Handle departure from service point 3: remove customer from the system
			a = servicePoints[2].removeQueue();
			a.setRemovalTime(Clock.getInstance().getClock());
		    a.reportResults();
			break;
		}
	}

	// Processes C-phase events, checking if any service points can begin servicing a customer
	@Override
	protected void tryCEvents() {
		for (ServicePoint p: servicePoints){
			if (!p.isReserved() && p.isOnQueue()){
				p.beginService();		// Start servicing a customer if conditions are met
			}
		}
	}

	// Outputs the results of the simulation
	@Override
	protected void results() {
		System.out.println("Simulation ended at " + Clock.getInstance().getClock());
		System.out.println("Results ... are currently missing");
	}
}
