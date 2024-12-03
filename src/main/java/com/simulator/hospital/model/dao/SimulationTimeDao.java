package com.simulator.hospital.model.dao;

import com.simulator.hospital.model.datasource.MariaDbJpaConnection;
import com.simulator.hospital.model.entity.SimulationTime;
import jakarta.persistence.EntityManager;

import java.util.List;

public class SimulationTimeDao {

    // method to insert a new simulation time
    public void persist(SimulationTime simulationTime) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.persist(simulationTime);
        em.getTransaction().commit();
    }

    // method to fetch the simulation time
    public List<SimulationTime> getSimulationTime() {
        EntityManager em = MariaDbJpaConnection.getInstance();
        return em.createQuery("select t from SimulationTime t").getResultList();
    }

    // methods to update simulation time
    public void update(SimulationTime simulationTime) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.merge(simulationTime);
        em.getTransaction().commit();
    }

    public void persistOrUpdate(SimulationTime simulationTime) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        SimulationTime existingSimulationTime = em.find(SimulationTime.class, 1);
        if (existingSimulationTime != null) {
            existingSimulationTime.setTime(simulationTime.getTime());
            em.merge(existingSimulationTime);
        } else {
            em.persist(simulationTime);
        }
        em.getTransaction().commit();
    }
}
