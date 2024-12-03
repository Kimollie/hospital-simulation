package com.simulator.hospital.controller;

import com.simulator.hospital.framework.Clock;
import com.simulator.hospital.framework.Trace;
import com.simulator.hospital.model.entity.DelayTime;
import com.simulator.hospital.model.entity.Intervals;
import com.simulator.hospital.model.entity.ServicePointTypes;
import com.simulator.hospital.model.entity.SimulationTime;
import com.simulator.hospital.model.logic.Customer;
import com.simulator.hospital.model.logic.ServicePoint;
import com.simulator.hospital.model.logic.ServiceUnit;
import com.simulator.hospital.model.logic.SimulatorModel;
import com.simulator.hospital.view.MainMenuViewControl;
import com.simulator.hospital.view.ResultViewControl;
import com.simulator.hospital.view.SimuViewControl;
import com.simulator.hospital.model.entity.*;
import com.simulator.hospital.model.dao.*;
import javafx.application.Platform;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;

public class SimuController implements Runnable {
    private SimulatorModel simuModel;
    private final MainMenuViewControl menuView;
    private final SimuViewControl simuView;
    private final ResultViewControl resultView;
    private long delayTime;
    private final Clock clock;
    private IntervalsDao intervalsDao;
    private ServicePointTypesDao spTypesDao;
    private SimulationTimeDao simuTimeDao;
    private DelayTimeDao delayTimeDao;
    private int numberRegister;
    private int numberGeneral ;
    private int numberSpecialist ;
    private double avgRegisterTime;
    private double avgGeneralTime ;
    private double avgSpecialistTime;

    public SimuController(MainMenuViewControl menuView, SimuViewControl simuView, ResultViewControl resultView) {
        this.menuView = menuView;
        this.simuView = simuView;
        this.clock = Clock.getInstance();
        this.delayTime = menuView.getDelayTime(); //initialize with initial delay
        this.resultView = resultView;
    }

    public void initializeModel() {
        numberRegister = menuView.getNumberRegister();
        numberGeneral = menuView.getNumberGeneral();
        numberSpecialist = menuView.getNumberSpecialist();
        avgRegisterTime = menuView.getRegisterTime();
        avgGeneralTime = menuView.getGeneralTime();
        avgSpecialistTime = menuView.getSpecialistTime();
        double avgArrivalTime = menuView.getArrivalTime();
        double simulationTime = menuView.getSimulationTime();
        this.simuModel = new SimulatorModel(numberRegister, avgRegisterTime, numberGeneral, avgGeneralTime, numberSpecialist, avgSpecialistTime, avgArrivalTime);
        this.simuModel.setSimulationTime(simulationTime);
    }

    //method to set new delay according to speed adjustment
    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }

    //method to get initial delay time and set to Simulator View
    public long getDelayTime() {
        return delayTime;
    }

    // set the interval value to the database
    public void setIntervalsDb(String typeName, String category, double time) {
        Intervals interval = new Intervals(typeName, category, time);
        intervalsDao.update(interval);
    }

    // get the interval values from the database
    public HashMap<Integer, Double> getIntervals() {
        List<Intervals> intervals = intervalsDao.getAllIntervals();
        HashMap<Integer, Double> timeIntervals = new HashMap<>();
        for (Intervals interval : intervals) {
            timeIntervals.put(interval.getId(), interval.getTime());
        }
        return timeIntervals;
    }

    // set the number of specified service point type to the database
    public void setNumberOfPointDb(String typeName, int numberPoint) {
        ServicePointTypes servicePointType = new ServicePointTypes(typeName,numberPoint);
        spTypesDao.update(servicePointType);
    }

    // get the number of point values from the database
    public HashMap<Integer, Integer> getNumberOfPoints() {
        List<ServicePointTypes> servicePointTypes = spTypesDao.getAllServicePointTypes();
        HashMap<Integer, Integer> numberOfPoints = new HashMap<>();
        for (ServicePointTypes servicePointType : servicePointTypes) {
            numberOfPoints.put(servicePointType.getId(), servicePointType.getNumberPoints());
        }
        return numberOfPoints;
    }

    // set the simulation time to the database
    public void setSimulationTimeDb(double time) {
        SimulationTime simulationTime = new SimulationTime(time);
        simuTimeDao.update(simulationTime);
    }

    // get the simulation time from the database
    public double getSimulationTimeDb() {
        List<SimulationTime> simulationTimes = simuTimeDao.getSimulationTime();
        return simulationTimes.get(0).getTime();
    }

    // set the delay time to the database
    public void setDelayTimeDb(long time) {
        DelayTime delayTime = new DelayTime(time);
        delayTimeDao.update(delayTime);
    }

    // get the delay time from the database
    public long getDelayTimeDb() {
        List<DelayTime> delayTimes = delayTimeDao.getDelayTime();
        return delayTimes.get(0).getTime();
    }


    public SimulatorModel getSimuModel(){
        return this.simuModel;
    }

    @Override
    public void run() {
        Trace.setTraceLevel(Trace.Level.INFO);
        if (simuModel == null) {
            System.err.println("SimulatorModel is not initialized. Please set up the parameters first.");
            return;
        }
        simuModel.initialize();

        while (simuModel.simulate()) {

                // set clock
                clock.setClock(simuModel.currentTime());
                // display clock
                Platform.runLater(() -> simuView.displayClock(clock.getClock()));

            // Processes all B-events scheduled for the current time
            while (simuModel.currentTime() == clock.getClock()) {
                // process each B-event and display result
                AbstractMap.SimpleEntry<Customer, ServiceUnit> result = simuModel.runEvent(simuModel.processEvent());        // Execute and remove the event from the list
                // get necessary value from result
                int customerId = result.getKey().getId();
                int serviceUnitNumber = result.getValue() != null ? result.getValue().getIndex() : 0;

                Customer customer = result.getKey();
                ServiceUnit serviceUnit = result.getValue(); // might return null


                // call display method from view
                Platform.runLater(() -> {

                    simuView.displayBEvent(customer, serviceUnit);
                });
            }

            // add some delay so here there is delay between 2 phase, wait for animation to complete in phase B in UI
            try {
                System.out.println("Delay time: " + delayTime);

                Thread.sleep(delayTime/2);
            } catch (InterruptedException e) {
//                System.err.println(e);
                System.err.println("Simulation thread interrupted.");
                Thread.currentThread().interrupt(); // Reset the interrupted status
                break; // Exit the loop
            }

            // Processes C-phase events, checking if any service points can begin servicing a customer
            for (ServiceUnit serviceUnit : simuModel.getServiceUnits()) {
                // check in the service unit if any service point is available and customer is on queue
                if (!serviceUnit.isReserved() && serviceUnit.isOnQueue()) {
                    // start servicing a customer if conditions are met
                    ServicePoint servicePoint = serviceUnit.beginService();
                    Customer customer = servicePoint.getCurrentCustomer();
                    // get necessary value from result and display in view
                    Platform.runLater(() -> {
                        simuView.displayCEvent(customer, servicePoint);

                    });
                }
            }
            // add some delay so here there is delay between 2 phase, wait for animation to complete in phase B in UI
            try {
                System.out.println("Delay time: " + delayTime);

                Thread.sleep(delayTime/2);
            } catch (InterruptedException e) {
//                System.err.println(e);
                System.err.println("Simulation thread interrupted.");
                Thread.currentThread().interrupt(); // Reset the interrupted status
                break; // Exit the loop
            }


        }
        // Ensure results are printed only if the simulation time is completed
        if (isSimulationTimeCompleted()) {
            Platform.runLater(() -> {
                simuModel.results();
                // Get the results from the model
                double avgWaitingTime = simuModel.getAvgWaitingTime();
                List<Integer> customerCount = simuModel.getCustomerCount();
                List<Double> utilization = simuModel.getUtilization();

                // Display the results to ResultViewControl
                resultView.setTable(numberRegister, numberGeneral, numberSpecialist, avgRegisterTime, avgGeneralTime, avgSpecialistTime);
                resultView.display(avgWaitingTime, customerCount, utilization, simuView.getStage());
            });
        }
    }

    public boolean isSimulationTimeCompleted() {
        return clock.getClock() >= menuView.getSimulationTime();
    }
}

