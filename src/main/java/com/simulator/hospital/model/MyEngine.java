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
    private final ArrivalProcess arrivalProcess;
    private final ServicePoint[] servicePoints;

    /*
     * This is the place where you implement your own simulator
     *
     * Demo simulation case:
     * Simulate three service points, customer goes through all three service points to get serviced
     * 		--> SP1 --> SP2 --> SP3 -->
     */
    public MyEngine() {
        servicePoints = new ServicePoint[3];
        Random r = new Random();
        // exponential distribution is used to model customer arrivals times, to get variability between programs runs, give a variable seed
        ContinuousGenerator arrivalTime = new Negexp(10, 5);
        // normal distribution used to model service times
        ContinuousGenerator serviceTime = new Normal(10, 6, Integer.toUnsignedLong(r.nextInt()));

        // Initialize the service points with the chosen service time distribution
        servicePoints[0] = new ServicePoint(serviceTime, eventList, EventType.DEP1);
        servicePoints[1] = new ServicePoint(serviceTime, eventList, EventType.DEP2);
        servicePoints[2] = new ServicePoint(serviceTime, eventList, EventType.DEP3);

        // Initialize the arrival process
        arrivalProcess = new ArrivalProcess(arrivalTime, eventList, EventType.ARR1);
    }

    // Initializes the simulation by generating the first arrival event
    @Override
    protected void initialize() {    // First arrival in the system
        arrivalProcess.generateNextEvent();
    }

    // Processes B-phase events, such as arrivals and departures
    @Override
    protected void runEvent(Event t) {  // B phase events
        Customer a;

        switch ((EventType) t.getType()) {
            case ARR1:
                // Handle a new customer arrival: add to the queue of the first service point
                servicePoints[0].addQueue(new Customer());
                arrivalProcess.generateNextEvent();        // Schedule the next arrival
                break;

            case DEP1:
                // Handle departure from service point 1: move customer to service point 2
                a = servicePoints[0].removeQueue();
                if (a.getCustomerType().equals("general")) {
                    servicePoints[1].addQueue(a);
                } else {
                    servicePoints[2].addQueue(a);
                }
                break;

            case DEP2:
                // Handle departure from service point 2: move customer to service point 3
                a = servicePoints[1].removeQueue();
                a.setRemovalTime(Clock.getInstance().getClock());
                a.reportResults();
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
        for (ServicePoint p : servicePoints) {
            if (!p.isReserved() && p.isOnQueue()) {
                p.beginService();        // Start servicing a customer if conditions are met
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
