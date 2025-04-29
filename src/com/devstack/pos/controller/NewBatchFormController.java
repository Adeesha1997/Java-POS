package com.devstack.pos.controller;

import com.devstack.pos.bo.BoFactory;
import com.devstack.pos.bo.custom.ProductDetailBo;
import com.devstack.pos.dto.CustomerDto;
import com.devstack.pos.dto.ProductDetailDto;
import com.devstack.pos.enums.BoType;
import com.devstack.pos.util.QrDataGenerator;
import com.devstack.pos.view.tm.ProductDetailTm;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.jfoenix.controls.JFXTextField;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;

public class NewBatchFormController {


    public ImageView barcodeImage;
    public AnchorPane context;
    public JFXTextField txtProductQty;
    public JFXTextField txtProductBuyingPrice;
    public JFXTextField txtProductShowPrice;
    public JFXTextField txtProductSellingPrice;
    public JFXTextField txtProductCode;
    public TextArea txtProductDescription;
    public RadioButton rbtnYes;
    public RadioButton rbtnNo;
    String uniqueData = null;
    BufferedImage bufferedImage = null;

    Stage stage = null;

    private final ProductDetailBo productDetailBo = BoFactory.getInstance().getBo(BoType.PRODUCT_DETAIL);

    public void initialize() throws WriterException {


    }

    private void setQRCode() throws WriterException {
        uniqueData = QrDataGenerator.generate(25);

        //--------------Gen QR

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        bufferedImage = MatrixToImageWriter
                .toBufferedImage(
                        qrCodeWriter.encode(
                                uniqueData, BarcodeFormat.QR_CODE, 200, 200

                        )
                );

        //--------------Gen QR
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
        barcodeImage.setImage(image);
    }

    public void setDetails(int code, String description, Stage stage, boolean state, ProductDetailTm tm) {
        this.stage = stage;

        if (state) {
            try {
                ProductDetailDto productDetails = productDetailBo.findProductDetails(tm.getCode());
                if (productDetails != null) {
                    txtProductQty.setText(String.valueOf(productDetails.getQtyOnHand()));
                    txtProductBuyingPrice.setText(String.valueOf(productDetails.getBuyingPrice()));
                    txtProductSellingPrice.setText(String.valueOf(productDetails.getSellingPrice()));
                    txtProductShowPrice.setText(String.valueOf(productDetails.getShowPrice()));
                    txtProductQty.setText(String.valueOf(productDetails.getQtyOnHand()));
                    rbtnYes.setSelected(productDetails.isDiscountAvailability());


                } else {
                    stage.close();
                }

            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        } else {
            try {
                setQRCode();
            } catch (WriterException e) {
                throw new RuntimeException(e);
            }
        }
        txtProductCode.setText(String.valueOf(code));
        txtProductDescription.setText(description);

    }


    public void btnBackToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("ProductMainForm");
    }

    private void setUi(String url) throws IOException {
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(
                new Scene(FXMLLoader.load(getClass().getResource("../view/" + url + ".fxml")))
        );
        stage.centerOnScreen();
    }

    public void btnSaveOnAction(ActionEvent actionEvent) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        javax.imageio.ImageIO.write(bufferedImage, "png", baos);
        byte[] arr = baos.toByteArray();
        boolean isYesSelected = rbtnYes.isSelected();


        ProductDetailDto dto = new ProductDetailDto(
                uniqueData, Base64.getEncoder().encodeToString(arr), Integer.parseInt(txtProductQty.getText()),
                Double.parseDouble(txtProductSellingPrice.getText()), Double.parseDouble(txtProductShowPrice.getText()),
                Double.parseDouble(txtProductBuyingPrice.getText()), Integer.parseInt(txtProductCode.getText()), rbtnYes.isSelected() ? true : false


        );
        try {

            if (productDetailBo.saveProductDetail(dto)) {
                new Alert(Alert.AlertType.CONFIRMATION, "Batch Saved !").show();
                Thread.sleep(3000);
                this.stage.close();

            } else {
                new Alert(Alert.AlertType.WARNING, "Try Again !").show();
            }

        } catch (SQLException | ClassNotFoundException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
