-- Drop the database if it already exists
DROP DATABASE IF EXISTS hospital_simulation;

-- Create the database
CREATE DATABASE hospital_simulation;

-- Use the database
USE hospital_simulation;

-- Create the ServicePointTypes table
CREATE TABLE ServicePointTypes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    type_name VARCHAR(255) NOT NULL,
    number_of_points INT NOT NULL CHECK (number_of_points >= 0)
);
-- Create the Intervals table
CREATE TABLE Intervals (
    id INT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(50) NOT NULL, -- 'arrival' or 'service'
    category VARCHAR(255) NOT NULL, -- 'Arrival', 'Registration', 'General Health Exam' or 'Specialist'
    time DOUBLE NOT NULL CHECK (time >= 0)
);
-- Create the simulation_time table
CREATE TABLE Simulation_time (
    id INT AUTO_INCREMENT PRIMARY KEY,
    time DOUBLE NOT NULL CHECK (time >= 0)
);

-- Create the delay_time table
CREATE TABLE Delay_time (
    id INT AUTO_INCREMENT PRIMARY KEY,
    time BIGINT NOT NULL CHECK (time >= 0 )
)


