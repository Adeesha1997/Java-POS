package com.devstack.pos.controller;

import com.devstack.pos.util.QrDataGenerator;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class NewBatchFormController {


    public ImageView barcodeImage;
    public AnchorPane context;

    public void initialize() throws WriterException {
        setQRCode();
    }

    private void setQRCode() throws WriterException {
        String uniqueData = QrDataGenerator.generate(25);

        //--------------Gen QR

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BufferedImage bufferedImage = MatrixToImageWriter
                .toBufferedImage(
                        qrCodeWriter.encode(
                                uniqueData, BarcodeFormat.QR_CODE, 200, 200

                        )
                );
        //--------------Gen QR
        Image image = SwingFXUtils.toFXImage(bufferedImage,null);
        barcodeImage.setImage(image);
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
}
