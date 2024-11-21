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
    private final ServiceUnit[] serviceUnits;

    /*
     * This is the place where you implement your own simulator
     *
     * Demo simulation case:
     * Simulate three service points, customer goes through all three service points to get serviced
     * 		--> SP1 --> SP2 --> SP3 -->
     */
    public MyEngine(int servicePointNum1, int servicePointNum2, int servicePointNum3) {
        serviceUnits = new ServiceUnit[3];
        Random r = new Random();
        // exponential distribution is used to model customer arrivals times, to get variability between programs runs, give a variable seed
        ContinuousGenerator arrivalTime = new Negexp(5, 5);
        // normal distribution used to model service times
        ContinuousGenerator serviceTime = new Normal(10, 6, 2);

        // Initialize the service points with the chosen service time distribution
        serviceUnits[0] = new ServiceUnit(new Normal(3, 6, 2), eventList, EventType.DEP1, servicePointNum1);
        serviceUnits[1] = new ServiceUnit(new Normal(10, 6, 2), eventList, EventType.DEP2, servicePointNum2);
        serviceUnits[2] = new ServiceUnit(new Normal(15, 6, 2), eventList, EventType.DEP3, servicePointNum3);

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
                serviceUnits[0].addQueue(new Customer());
                arrivalProcess.generateNextEvent();        // Schedule the next arrival
                break;

            case DEP1:
                // Handle departure from service point 1: move customer to service point 2
                a = serviceUnits[0].removeQueue();
                if (a.getCustomerType().equals("general")) {
                    serviceUnits[1].addQueue(a);
                } else {
                    serviceUnits[2].addQueue(a);
                }
                break;

            case DEP2:
                // Handle departure from service unit 2: remove customer from the system
                a = serviceUnits[1].removeQueue();
                a.setRemovalTime(Clock.getInstance().getClock());
                a.reportResults();
                break;

            case DEP3:
                // Handle departure from service unit 3: remove customer from the system
                a = serviceUnits[2].removeQueue();
                a.setRemovalTime(Clock.getInstance().getClock());
                a.reportResults();
                break;
        }
    }

    // Processes C-phase events, checking if any service points can begin servicing a customer
    @Override
    protected void tryCEvents() {
        for (ServiceUnit serviceUnit : serviceUnits) {
            if (!serviceUnit.isReserved() && serviceUnit.isOnQueue()) {
                int customerId = serviceUnit.beginService();       // Start servicing a customer if conditions are met
                System.out.printf("Customer %d is served at service point %d\n", customerId, serviceUnit.getSelectedServicePoint().getId());
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
