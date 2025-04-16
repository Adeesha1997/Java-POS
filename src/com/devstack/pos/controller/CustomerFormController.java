package com.devstack.pos.controller;

import com.devstack.pos.dao.DatabaseAccessCode;
import com.devstack.pos.dto.dto.CustomerDto;
import com.devstack.pos.view.tm.CustomerTm;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class CustomerFormController {

    public AnchorPane context;
    public JFXTextField txtEmail;
    public JFXTextField txtName;
    public JFXTextField txtContact;
    public JFXTextField txtSalary;
    public JFXButton btnSaveUpdate;
    public TextField txtSearch;
    public TableView<CustomerTm> tblCustomer;
    public TableColumn colId;
    public TableColumn colEmail;
    public TableColumn colName;
    public TableColumn colContact;
    public TableColumn colSalary;
    public TableColumn colOperator;


    private String searchText = "";

    public void initialize() throws SQLException, ClassNotFoundException {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colOperator.setCellValueFactory(new PropertyValueFactory<>("deleteButton"));
        loadAllCustomers(searchText);
    }

    private void loadAllCustomers(String searchText) throws SQLException, ClassNotFoundException {
        ObservableList<CustomerTm> observableList = FXCollections.observableArrayList();
        int counter = 1;
        for (CustomerDto dto : DatabaseAccessCode.searchCustomer(searchText)
        ) {
            Button btn = new Button("Delete");
            CustomerTm tm = new CustomerTm(
                    counter, dto.getEmail(), dto.getName(), dto.getContact(), dto.getSalary(), btn
            );
            observableList.add(tm);
            counter++;
        }

        tblCustomer.setItems(observableList);
    }

    public void btnBackToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm");

    }

    public void btnManageLoyalityCardsOnAction(ActionEvent actionEvent) {
    }

    public void btnNewCustomerOnAction(ActionEvent actionEvent) {
    }

    public void btnSaveUpdateOnAction(ActionEvent actionEvent) {
        try {
            if (DatabaseAccessCode.createCustomer(
                    txtEmail.getText(),
                    txtName.getText(),
                    txtContact.getText(),
                    Double.parseDouble(txtSalary.getText())
            )) {
                new Alert(Alert.AlertType.CONFIRMATION, "Customer Saved !").show();
                clearFiles();
                loadAllCustomers(searchText);
            } else {
                new Alert(Alert.AlertType.WARNING, "Try Again !").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }


    private void clearFiles() {
        txtEmail.clear();
        txtName.clear();
        txtContact.clear();
        txtSalary.clear();
    }

    private void setUi(String url) throws IOException {
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(
                new Scene(FXMLLoader.load(getClass().getResource("../view/" + url + ".fxml")))
        );
        stage.centerOnScreen();
    }


}
