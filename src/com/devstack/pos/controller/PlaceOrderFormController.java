package com.devstack.pos.controller;

import com.devstack.pos.bo.BoFactory;
import com.devstack.pos.bo.custom.CustomerBo;
import com.devstack.pos.bo.custom.ProductDetailBo;
import com.devstack.pos.dto.CustomerDto;
import com.devstack.pos.dto.ProductDetailJoinDto;
import com.devstack.pos.enums.BoType;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class PlaceOrderFormController {

    public AnchorPane context;
    public JFXTextField txtEmail;
    public JFXTextField txtName;
    public JFXTextField txtContact;
    public JFXTextField txtSalary;
    public Hyperlink urlNewLoyalty;
    public Label lblLoyaltyType;
    public JFXTextField txtBarcode;
    public JFXTextField txtDescription;
    public JFXTextField txtSelllingPrice;
    public JFXTextField txtDiscount;
    public JFXTextField txtShowPrice;
    public JFXTextField txtQtyOnHand;
    public Label lblDiscountAvailable;
    public JFXTextField txtBuyingPrice;
    public JFXTextField txtQty;

    private final CustomerBo bo = BoFactory.getInstance().getBo(BoType.CUSTOMER);
    private final ProductDetailBo productDetailBo = BoFactory.getInstance().getBo(BoType.PRODUCT_DETAIL);


    public void btnAddNewCustomerOnAction(ActionEvent actionEvent) throws IOException {
        setUi("CustomerForm",true);
    }

    public void btnAddNewProductOnAction(ActionEvent actionEvent) throws IOException {
        setUi("ProductMainForm",true);
    }

    public void btnBackToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm",false);
    }


    private void setUi(String url,boolean state) throws IOException {
        Stage stage = null;
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../view/" + url + ".fxml")));

        if (state){
            stage = new Stage();
            stage.setScene(scene);
            stage.show();
        }else {
            stage = (Stage) context.getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
        }

    }

    public void searchCustomerOnAction(ActionEvent actionEvent) {
        try {

            CustomerDto customer = bo.findCustomer(txtEmail.getText());
            if (customer!=null){
                txtName.setText(customer.getName());
                txtContact.setText(customer.getContact());
                txtSalary.setText(String.valueOf(customer.getName()));

                fetchLoyaltyCardData(txtEmail.getText());
            }else {
                new Alert(Alert.AlertType.WARNING, "Can't Find Customer !").show();
            }

        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.WARNING, "Can't Find Customer !").show();
            throw new RuntimeException(e);

        }
    }

    private void fetchLoyaltyCardData(String text) {
        urlNewLoyalty.setText("+ New Loyalty");
        urlNewLoyalty.setVisible(true);

    }

    public void newLoyaltyOnAction(ActionEvent actionEvent) {
    }

    public void lodeProductOnAction(ActionEvent actionEvent) {

        try {
            ProductDetailJoinDto  p= productDetailBo.findProductJoinDetails(
                    txtBarcode.getText()
            );
            if (p != null){
                txtDescription.setText(p.getDescription());
                txtSelllingPrice.setText(String.valueOf(p.getDto().getSellingPrice()));
                txtDiscount.setText(String.valueOf(0));
                txtShowPrice.setText(String.valueOf(p.getDto().getSellingPrice()));
                txtQtyOnHand.setText(String.valueOf(p.getDto().getQtyOnHand()));
                txtBuyingPrice.setText(String.valueOf(p.getDto().getSellingPrice()));

                txtQty.requestFocus();

            }else {

                new Alert(Alert.AlertType.WARNING, "Can't Find the Product !").show();

            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }
}
