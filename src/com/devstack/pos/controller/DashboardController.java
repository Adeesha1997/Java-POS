package com.devstack.pos.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    public AnchorPane context;

    public void btnCustomerOnAction(ActionEvent actionEvent) throws IOException {
        setUi("CustomerForm");
    }

    public void btnProductOnAction(ActionEvent actionEvent) {
    }

    public void btnPlaceOrderOnAction(ActionEvent actionEvent) {
    }

    public void btnIncomeReportOnActin(ActionEvent actionEvent) {
    }

    public void btnOrderDetailOnAction(ActionEvent actionEvent) {
    }


    private void setUi(String url) throws IOException {
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(
                new Scene(FXMLLoader.load(getClass().getResource("../view/" + url + ".fxml")))
        );
        stage.centerOnScreen();
    }
}
