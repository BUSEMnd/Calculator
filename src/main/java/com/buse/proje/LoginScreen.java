package com.buse.proje;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.stage.Stage;

public class LoginScreen {
    private final Stage primaryStage;
    private final Proje calculatorApp;
    //constructor  to initialize with the stage and main app instannce
    public LoginScreen(Stage stage, Proje calculatorApp) {
        this.primaryStage = stage;
        this.calculatorApp = calculatorApp;
    }
//this method creates and return the login cene with username and password file
    public Scene createLoginScene() {
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Log in");
        Button registerButton = new Button("Register");

        loginButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (UserManager.authenticate(username, password)) {
                System.out.println("Login successful!");
                Scene calculatorScene = calculatorApp.createCalculatorScene(primaryStage);
                primaryStage.setScene(calculatorScene);
            } else {
                System.out.println("Invalid username or password.");
            }
        });

        registerButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (!username.isEmpty() && !password.isEmpty()) {
                UserManager.saveUser(username, password);
                System.out.println("Registration successful!");
            } else {
                System.out.println("Fields cannot be empty!");
            }
        });

        VBox vbox = new VBox(10, usernameField, passwordField, loginButton, registerButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: brown;");
        return new Scene(vbox, 300, 250);
    }

}
