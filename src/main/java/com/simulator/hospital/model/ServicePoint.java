package com.simulator.hospital.model;

public class ServicePoint {
    private int id;
    private static int count = 1;
    private boolean available;

    public ServicePoint() {
        this.available = true;
        this.id = count++;
    }

    public void setAvailable(boolean value) {
        this.available = value;
    }

    public boolean isAvailable() {
        return this.available;
    }

    public int getId() {
        return id;
    }
}
