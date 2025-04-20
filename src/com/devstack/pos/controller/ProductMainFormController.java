package com.devstack.pos.controller;

import com.devstack.pos.bo.custom.impl.ProductBoImpl;
import com.devstack.pos.dto.ProductDto;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

import java.sql.SQLException;

public class ProductMainFormController {
    public JFXTextField txtProductCode;
    public TextArea txtProductDescription;
    public AnchorPane context;
    public JFXButton btnSaveUpdate;
    private String searchText = "";

    public void initialize() {
        loadProductId();
    }

    private void loadProductId() {
        try {
            txtProductCode.setText(String.valueOf(new ProductBoImpl().getLastProductId()));
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnBackToHomeOnAction(ActionEvent actionEvent) {
    }

    public void btnNewProductOnAction(ActionEvent actionEvent) {
    }

    public void btnSaveUpdateOnAction(ActionEvent actionEvent) {
        try {

            if (btnSaveUpdate.getText().equals("Save Product")) {
                if (new ProductBoImpl().saveProduct(
                        new ProductDto(    Integer.parseInt(txtProductCode.getText()),
                                txtProductDescription.getText())

                )) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Product Saved !").show();
                    clearFiles();
                    loadAllProducts(searchText);
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try Again !").show();
                }
            } else {
                if (new ProductBoImpl().updateProduct(
                        new ProductDto(    Integer.parseInt(txtProductCode.getText()),
                                txtProductDescription.getText())
                )) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Product Updated !").show();
                    clearFiles();
                    loadAllProducts(searchText);

                    btnSaveUpdate.setText("Save Product");
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try Again !").show();
                }
            }


        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }

    private void loadAllProducts(String searchText) {

    }

    private void clearFiles() {
        txtProductCode.clear();
        txtProductDescription.clear();
        loadProductId();
    }
}
