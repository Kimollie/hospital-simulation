package com.simulator.hospital.controller;

import com.simulator.hospital.view.ResultView;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ResultController {

    // FXML UI components
    @FXML
    private TableView<ServiceData> tableView;

    @FXML
    private TableColumn<ServiceData, String> serviceColumn;

    @FXML
    private TableColumn<ServiceData, Integer> servicePointNumbersColumn;

    @FXML
    private TableColumn<ServiceData, Double> serviceTimeColumn;

    @FXML
    private PieChart totalCustomers;

    @FXML
    private BarChart<String, Number> utilization;

    @FXML
    private CategoryAxis utilizationXAxis;

    @FXML
    private BarChart<String, Number> meanTime;

    @FXML
    private CategoryAxis meanTimeXAxis;

    @FXML
    private Label initialSetupLabel;

    @FXML
    private Label totalCustomerCount;

    @FXML
    private Label avgWaitingTime;


    @FXML
    public void initialize() {
        // Set up table columns
        serviceColumn.setCellValueFactory(new PropertyValueFactory<>("service"));
        servicePointNumbersColumn.setCellValueFactory(new PropertyValueFactory<>("servicePointNumbers"));
        serviceTimeColumn.setCellValueFactory(new PropertyValueFactory<>("serviceTime"));

        // Populate table with sample data
        tableView.getItems().addAll(
                //TODO: Retrieve user input from main menu, from SimuViewControl
                new ServiceData("Register Desk", 2, 3.5),
                new ServiceData("General Examination", 1, 8.5),
                new ServiceData("Specialist Treatment", 1, 2.7)
        );

        // Populate PieChart with total customers data
        totalCustomers.getData().addAll(
                //TODO: Retrieve result from simulation, from SimulatorModel result() method
                new PieChart.Data("RegisterDesk - SP1", 12),
                new PieChart.Data("RegisterDesk - SP2", 10),
                new PieChart.Data("GeneralExamination - SP1", 9),
                new PieChart.Data("GeneralExamination - SP2", 4),
                new PieChart.Data("SpecialistExamination - SP1", 5),
                new PieChart.Data("SpecialistExamination - SP2", 0)
        );

        // Utilization Efficiency Data
        utilization.setTitle("Utilization Efficiency (%)");
        utilization.getXAxis().setLabel("Service Units");
        utilization.getYAxis().setLabel("Utilization");

        XYChart.Series<String, Number> registerDeskUtilization = new XYChart.Series<>();
        registerDeskUtilization.setName("RegisterDesk");
        //TODO: Retrieve result from simulation, from SimulatorModel result() method
        registerDeskUtilization.getData().add(new XYChart.Data<>("Service Point 1", 0.26));
        registerDeskUtilization.getData().add(new XYChart.Data<>("Service Point 2", 0.61));

        XYChart.Series<String, Number> generalExamUtilization = new XYChart.Series<>();
        generalExamUtilization.setName("General Examination");
        //TODO: Retrieve result from simulation, from SimulatorModel result() method
        generalExamUtilization.getData().add(new XYChart.Data<>("Service Point 1", 0.67));
        generalExamUtilization.getData().add(new XYChart.Data<>("Service Point 2", 0.02));


        XYChart.Series<String, Number> specialistExamUtilization = new XYChart.Series<>();
        specialistExamUtilization.setName("Specialist Examination");
        //TODO: Retrieve result from simulation, from SimulatorModel result() method
        specialistExamUtilization.getData().add(new XYChart.Data<>("Service Point 1", 0.46));
        specialistExamUtilization.getData().add(new XYChart.Data<>("Service Point 2", 0.0));

        utilization.getData().addAll(registerDeskUtilization, generalExamUtilization, specialistExamUtilization);

        totalCustomerCount.setText("40");
        avgWaitingTime.setText("5.2");
    }


    // Inner class for table data
    public static class ServiceData {
        private final String service;
        private final int servicePointNumbers;
        private final double serviceTime;

        public ServiceData(String service, int servicePointNumbers, double serviceTime) {
            this.service = service;
            this.servicePointNumbers = servicePointNumbers;
            this.serviceTime = serviceTime;
        }

        public String getService() {
            return service;
        }

        public int getServicePointNumbers() {
            return servicePointNumbers;
        }

        public double getServiceTime() {
            return serviceTime;
        }
    }

    //Test result scene
    public static void main(String[] args) {
        ResultView.launch(ResultView.class);
    }
}
