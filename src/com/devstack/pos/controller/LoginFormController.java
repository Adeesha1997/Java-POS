package com.devstack.pos.controller;

import com.devstack.pos.dao.DatabaseAccessCode;
import com.devstack.pos.dto.UserDto;
import com.devstack.pos.util.PasswordManager;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.sql.*;

public class LoginFormController {
    public AnchorPane context;
    public JFXTextField txtEmail;
    public JFXPasswordField txtPassword;

    public void btnSigningOnAction(ActionEvent actionEvent) {
        try {
            UserDto userDto = DatabaseAccessCode.findUser(txtEmail.getText());

            if (userDto!=null){
                if (PasswordManager.checkPassword(txtPassword.getText(), userDto.getPassword())){
                 setUi("DashboardForm");
                }else {
                    new Alert(Alert.AlertType.WARNING, "Check Your Password And Try Again !").show();
                }
            }else {
                new Alert(Alert.AlertType.WARNING, "User Email Not Found Try Again !").show();
            }


        } catch (ClassNotFoundException | SQLException | IOException | RuntimeException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }

    private void clearFiles() {
        txtEmail.clear();
        txtPassword.clear();
    }

    public void btnCreateAnAccountOnAction(ActionEvent actionEvent) throws IOException {
        setUi("SignupForm");
    }

    private void setUi(String url) throws IOException {
        Stage stage = (Stage) context.getScene().getWindow();
        stage.centerOnScreen();
        stage.setScene(
                new Scene(FXMLLoader.load(getClass().getResource("../view/"+url+".fxml")))
        );
        stage.centerOnScreen();
    }

}
