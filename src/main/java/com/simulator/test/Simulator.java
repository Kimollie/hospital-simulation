package com.simulator.test;

import com.simulator.eduni.distributions.*;
import com.simulator.hospital.framework.Engine;
import com.simulator.hospital.framework.Trace;
import com.simulator.hospital.framework.Trace.Level;
import com.simulator.hospital.model.MyEngine;

/* Command-line type User Interface */
public class Simulator {
	public static void main(String[] args) {
		Trace.setTraceLevel(Level.INFO);

		Engine m = new MyEngine();
		m.setSimulationTime(1000);
		m.run();
	}
}
