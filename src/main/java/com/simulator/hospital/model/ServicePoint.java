package com.simulator.hospital.model;

public class ServicePoint {
    private int id;
    private static int count = 1;
    private Customer currentCustomer = null;

    public ServicePoint() {
        this.id = count++;
    }

    public boolean isAvailable() {
        return currentCustomer == null;
    }

    public int getId() {
        return id;
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }
}
