package com.devstack.pos.controller;

import com.devstack.pos.bo.BoFactory;
import com.devstack.pos.bo.custom.ProductBo;
import com.devstack.pos.bo.custom.ProductDetailBo;
import com.devstack.pos.bo.custom.impl.ProductBoImpl;
import com.devstack.pos.dto.CustomerDto;
import com.devstack.pos.dto.ProductDetailDto;
import com.devstack.pos.dto.ProductDto;
import com.devstack.pos.enums.BoType;
import com.devstack.pos.view.tm.ProductDetailTm;
import com.devstack.pos.view.tm.ProductTm;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class ProductMainFormController {
    public JFXTextField txtProductCode;
    public TextArea txtProductDescription;
    public AnchorPane context;
    public JFXButton btnSaveUpdate;
    public TableView<ProductTm> tbl;
    public TableColumn colProductId;
    public TableColumn colProductDesc;
    public TableColumn colProductShowMore;
    public TableColumn colProductDelete;
    public JFXTextField txtSelectedProId;
    public TextArea txtSelectedProDesc;
    public JFXButton btnNewBatch;
    public TableView<ProductDetailTm> tblDetail;
    public TableColumn colPDId;
    public TableColumn colPDQty;
    public TableColumn colPDSellingPrice;
    public TableColumn colPDBuyingPrice;
    public TableColumn colPDDisAvailability;
    public TableColumn colPDShowPrice;
    public TableColumn colPDDelete;
    private String searchText = "";

    ProductBo bo = BoFactory.getInstance().getBo(BoType.PRODUCT);
    ProductDetailBo detailBo = BoFactory.getInstance().getBo(BoType.PRODUCT_DETAIL);

    public void initialize() throws SQLException, ClassNotFoundException {

        colProductId.setCellValueFactory(new PropertyValueFactory<>("code"));
        colProductDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colProductShowMore.setCellValueFactory(new PropertyValueFactory<>("showMore"));
        colProductDelete.setCellValueFactory(new PropertyValueFactory<>("delete"));

        colPDId.setCellValueFactory(new PropertyValueFactory<>("code"));
        colPDQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colPDSellingPrice.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        colPDBuyingPrice.setCellValueFactory(new PropertyValueFactory<>("buyingPrice"));
        colPDDisAvailability.setCellValueFactory(new PropertyValueFactory<>("discountAvailability"));
        colPDShowPrice.setCellValueFactory(new PropertyValueFactory<>("showPrice"));
        colPDDelete.setCellValueFactory(new PropertyValueFactory<>("delete"));


        loadProductId();
        loadAllProducts(searchText);

        tbl.getSelectionModel().selectedItemProperty().addListener((
                observable,
                oldValue,
                newValue) -> {

            setData(newValue);

        });
    }

    private void setData(ProductTm newValue) {
        txtSelectedProId.setText(String.valueOf(newValue.getCode()));
        txtSelectedProDesc.setText(newValue.getDescription());
        btnNewBatch.setDisable(false);
        try {
            loadBatchData(newValue.getCode());
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    private void loadProductId() {
        try {
            txtProductCode.setText(String.valueOf(bo.getLastProductId()));
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnBackToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm");
    }

    public void btnNewProductOnAction(ActionEvent actionEvent) {
    }

    public void btnSaveUpdateOnAction(ActionEvent actionEvent) {
        try {

            if (btnSaveUpdate.getText().equals("Save Product")) {
                if (bo.saveProduct(
                        new ProductDto(Integer.parseInt(txtProductCode.getText()),
                                txtProductDescription.getText())

                )) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Product Saved !").show();
                    clearFiles();
                    loadAllProducts(searchText);
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try Again !").show();
                }
            } else {
                if (bo.updateProduct(
                        new ProductDto(Integer.parseInt(txtProductCode.getText()),
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

    private void loadAllProducts(String searchText) throws SQLException, ClassNotFoundException {
        ObservableList<ProductTm> tms = FXCollections.observableArrayList();
        for (ProductDto dto : bo.findAllProduct()
        ) {
            Button showMore = new Button("Show More");
            Button delete = new Button("Delete");
            ProductTm tm = new ProductTm(dto.getCode(), dto.getDescription(), showMore, delete);
            tms.add(tm);
        }
        tbl.setItems(tms);
    }

    private void clearFiles() {
        txtProductCode.clear();
        txtProductDescription.clear();
        loadProductId();
    }

    private void setUi(String url) throws IOException {
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(
                new Scene(FXMLLoader.load(getClass().getResource("../view/" + url + ".fxml")))
        );
        stage.centerOnScreen();
    }

    public void newBatchOnAction(ActionEvent actionEvent) throws IOException {
        if (!txtSelectedProId.getText().trim().isEmpty()) {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/NewBatchForm.fxml"));
            Parent parent = fxmlLoader.load();
            NewBatchFormController controller = fxmlLoader.getController();
            controller.setDetails(Integer.parseInt(txtSelectedProId.getText()), txtProductDescription.getText(), stage);

            stage.setScene(new Scene(parent));
            stage.show();
            stage.centerOnScreen();
        } else {
            new Alert(Alert.AlertType.WARNING, "Please Select a Valid One !");
        }
    }

    private void loadBatchData(int code) throws SQLException, ClassNotFoundException {
        ObservableList<ProductDetailTm> obList = FXCollections.observableArrayList();
        for (ProductDetailDto pd : detailBo.findAllProductDetails(code)
        ) {
            Button btn = new Button("Delete");
            ProductDetailTm tm = new ProductDetailTm(
                    pd.getCode(), pd.getQtyOnHand(), pd.getSellingPrice(), pd.getBuyingPrice(), pd.isDiscountAvailability(),pd.getShowPrice(),
                    btn);
            obList.add(tm);
        }
        tblDetail.setItems(obList);
    }

}
