package com.devstack.pos.controller;

import com.devstack.pos.bo.BoFactory;
import com.devstack.pos.bo.custom.CustomerBo;
import com.devstack.pos.bo.custom.LoyaltyCardBo;
import com.devstack.pos.bo.custom.OrderDetailBo;
import com.devstack.pos.bo.custom.ProductDetailBo;
import com.devstack.pos.dto.*;
import com.devstack.pos.enums.BoType;
import com.devstack.pos.enums.CardType;
import com.devstack.pos.util.QrDataGenerator;
import com.devstack.pos.util.UserSessionData;
import com.devstack.pos.view.tm.CartTm;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Random;

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
    private final OrderDetailBo orderDetailBo = BoFactory.getInstance().getBo(BoType.ORDER_DETAIL);
    private final LoyaltyCardBo loyaltyCardBo = BoFactory.getInstance().getBo(BoType.LOYALTY_CARD);
    public TableView<CartTm> tblCart;
    public TableColumn colCode;
    public TableColumn colDescription;
    public TableColumn colSellingPrice;
    public TableColumn colDiscount;
    public TableColumn colShowPrice;
    public TableColumn colQty;
    public TableColumn colTotalPrice;
    public TableColumn colOperation;
    public Text lblTotalCost;


    public void initialize() {
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colSellingPrice.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colShowPrice.setCellValueFactory(new PropertyValueFactory<>("showPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        colOperation.setCellValueFactory(new PropertyValueFactory<>("btn"));
    }

    public void btnAddNewCustomerOnAction(ActionEvent actionEvent) throws IOException {
        setUi("CustomerForm", true);
    }

    public void btnAddNewProductOnAction(ActionEvent actionEvent) throws IOException {
        setUi("ProductMainForm", true);
    }

    public void btnBackToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm", false);
    }


    private void setUi(String url, boolean state) throws IOException {
        Stage stage = null;
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../view/" + url + ".fxml")));

        if (state) {
            stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } else {
            stage = (Stage) context.getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
        }

    }

    public void searchCustomerOnAction(ActionEvent actionEvent) {
        try {

            CustomerDto customer = bo.findCustomer(txtEmail.getText());
            if (customer != null) {
                txtName.setText(customer.getName());
                txtContact.setText(customer.getContact());
                txtSalary.setText(String.valueOf(customer.getSalary()));

                fetchLoyaltyCardData(txtEmail.getText());
            } else {
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
        try {
            double salary = Double.parseDouble(txtSalary.getText());
            CardType type = CardType.SILVER;
            if (salary >= 100000) {
                type = CardType.PLATINUM;
            } else if (salary >= 50000) {
                type = CardType.GOLD;
            }else {
                type = CardType.SILVER;
            }


            String uniqueData = QrDataGenerator.generate(25);
            //--------------Gen QR

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BufferedImage bufferedImage = MatrixToImageWriter
                    .toBufferedImage(
                            qrCodeWriter.encode(
                                    uniqueData, BarcodeFormat.QR_CODE, 200, 200

                            )
                    );

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            javax.imageio.ImageIO.write(bufferedImage, "png", baos);
            byte[] arr = baos.toByteArray();

            if (urlNewLoyalty.getText().equals("+ New Loyalty")) {
                boolean isLoyaltyCardAssign = loyaltyCardBo.saveLoyaltyData(

                        new LoyaltyCardDto(new Random().nextInt(1001), type, Base64.getEncoder().encodeToString(arr), txtEmail.getText())
                );

                if (isLoyaltyCardAssign){
                    new Alert(Alert.AlertType.CONFIRMATION, "Data Saved!").show();
                    urlNewLoyalty.setText("Show Loyalty Card Info");
                }else {
                    new Alert(Alert.AlertType.WARNING, "Try Again!").show();
                }

            } else {

            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.WARNING, "Try Again!").show();
        } catch (WriterException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public void lodeProductOnAction(ActionEvent actionEvent) {

        try {
            ProductDetailJoinDto p = productDetailBo.findProductJoinDetails(
                    txtBarcode.getText()
            );
            if (p != null) {
                txtDescription.setText(p.getDescription());
                txtSelllingPrice.setText(String.valueOf(p.getDto().getSellingPrice()));
                txtDiscount.setText(String.valueOf(250));
                txtShowPrice.setText(String.valueOf(p.getDto().getSellingPrice()));
                txtQtyOnHand.setText(String.valueOf(p.getDto().getQtyOnHand()));
                txtBuyingPrice.setText(String.valueOf(p.getDto().getSellingPrice()));

                txtQty.requestFocus();

            } else {

                new Alert(Alert.AlertType.WARNING, "Can't Find the Product !").show();

            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }

    ObservableList<CartTm> tms = FXCollections.observableArrayList();

    public void addToCartOnAction(ActionEvent actionEvent) {


        int qty = Integer.parseInt(txtQty.getText());
/* if (customer.cardType.equals(CardType.GOLD.name())){

      }*/
        double discount = 250;


        double sellingPrice = (Double.parseDouble(txtSelllingPrice.getText())-discount);
        double totalCost = qty * sellingPrice;




        CartTm selectedCartTm = isExists(txtBarcode.getText());
        if (selectedCartTm != null) {
            selectedCartTm.setQty(qty + selectedCartTm.getQty());
            selectedCartTm.setTotalCost(qty + selectedCartTm.getTotalCost());
            tblCart.refresh();
        } else {
            Button btn = new Button("Remove");

            CartTm tm = new CartTm(
                    txtBarcode.getText(),
                    txtDescription.getText(),
                    sellingPrice,
                    Double.parseDouble(txtDiscount.getText()),
                    Double.parseDouble(txtShowPrice.getText()),
                    qty,
                    totalCost,
                    btn);

            btn.setOnAction((e) -> {
                tms.remove(tm);
                tblCart.refresh();
                setTotal();
            });

            tms.add(tm);
            clear();
            tblCart.setItems(tms);
            setTotal();
        }


    }

    private void clear() {
        txtBarcode.clear();
        txtDescription.clear();
        txtSelllingPrice.clear();
        txtDiscount.clear();
        txtShowPrice.clear();
        txtQtyOnHand.clear();
        txtBuyingPrice.clear();
        txtQty.clear();
        txtBarcode.requestFocus();

    }


    private CartTm isExists(String code) {
        for (CartTm tm : tms
        ) {
            if (tm.getCode().equals(code)) {
                return tm;
            }

        }
        return null;
    }

    private void setTotal() {
        double total = 0;
        for (CartTm tm : tms
        ) {
            total += tm.getTotalCost();
        }
        lblTotalCost.setText(total + "/=");
    }

    public void btnCompleteOrderOnAction(ActionEvent actionEvent) {
        ArrayList<ItemDetailDto> dtoList = new ArrayList<>();
        double discount = 0;
        for (CartTm tm : tms
        ) {
            dtoList.add(new ItemDetailDto(
                    tm.getCode(), tm.getQty(), tm.getDiscount(), tm.getTotalCost()
            ));

            discount += tm.getDiscount();
        }

        OrderDetailDto dto = new OrderDetailDto(
                new Random().nextInt(100001),
                new Date(),
                Double.parseDouble(lblTotalCost.getText().split("/=")[0]),
                txtEmail.getText(),
                0,
                UserSessionData.email,
                dtoList
        );

        try {
            if (orderDetailBo.makeOrder(dto)) {
                new Alert(Alert.AlertType.CONFIRMATION, "Order Placed!").show();
                clearFields();
            } else {
                new Alert(Alert.AlertType.CONFIRMATION, "Try Again!").show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.CONFIRMATION, "Try Again!").show();
        }
    }

    private void clearFields() {

    }
}
