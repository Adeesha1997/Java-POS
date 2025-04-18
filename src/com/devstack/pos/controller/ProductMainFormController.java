package com.devstack.pos.controller;

import com.devstack.pos.dao.DatabaseAccessCode;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

import java.sql.SQLException;

public class ProductMainFormController {
    public JFXTextField txtProductCode;
    public TextArea txtProductDescription;
    public AnchorPane context;

    public void initialize(){
        loadProductId();
    }

    private void loadProductId() {
        try {
            txtProductCode.setText(String.valueOf(DatabaseAccessCode.getLastProductId()));
        }  catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } 
    }

    public void btnBackToHomeOnAction(ActionEvent actionEvent) {
    }

    public void btnNewProductOnAction(ActionEvent actionEvent) {
    }

    public void btnSaveUpdateOnAction(ActionEvent actionEvent) {
    }
}
