package com.buse.proje;
import javafx.application.Application;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.input.MouseEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import java.util.List;

public class Proje extends Application {
    private boolean isStartingNewNumber = true;
    private Text displayText = new Text("0");

    @Override
    public void start(Stage primaryStage) {
        //Create the Login screen set it on the primary stage
        LoginScreen loginScreen = new LoginScreen(primaryStage, this);
        Scene loginScene = loginScreen.createLoginScene();
        primaryStage.setTitle("Login");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }
    //Creates and returns the calculator scene after successful login
    Scene createCalculatorScene(Stage primaryStage) {
        GridPane root = new GridPane();
        root.setStyle("-fx-background-color: whitesmoke;");
        root.setHgap(10);
        root.setVgap(10);

        Rectangle longRect = new Rectangle(231.5, 80);
        longRect.setFill(Color.WHITESMOKE);
        root.add(longRect, 0, 0, 4, 1);

        displayText.setFont(Font.font(30));
        displayText.setFill(Color.BLACK);
        root.add(displayText, 0, 0, 4, 1);

        String[] number = {
                "AC", "C", "%", "/",
                "1", "2", "3", "*",
                "4", "5", "6", "-",
                "7", "8", "9", "+",
                ".", "0", "="
        };
        int numberIndex = 0;
        //Add buttons to gridpane in rows and columns
        for (int row = 1; row <= 5; row++) {
            for (int col = 0; col < 4; col++) {
                if (numberIndex >= number.length) break;

                Rectangle rectangle = new Rectangle(50, 50);
                rectangle.setFill(Color.WHITESMOKE);

                final String label = number[numberIndex++];
                Text text = new Text(label);
                text.setFont(Font.font(20));

                StackPane cell = new StackPane(rectangle, text);
                root.add(cell, col, row);
                cell.setOnMouseClicked((MouseEvent e) -> {
                    handleButtonAction(label);
                });
            }
        }
        Button historyButton = new Button("History");
        historyButton.setOnAction(e -> showHistoryWindow());

        VBox calculatorWithHistory = new VBox(10, root, historyButton);
        calculatorWithHistory.setAlignment(Pos.CENTER);

        Scene scene = new Scene(calculatorWithHistory, 250, 420);
        primaryStage.setTitle("Calculator");
        return scene;
    }
    // handle the logic for when a calculator button is clicked
    private void handleButtonAction(String buttonText) {
        switch (buttonText) {
            case "AC":
                displayText.setText("0");
                isStartingNewNumber = true;
                break;
            case "C":
                String currentText = displayText.getText();
                if (currentText.length() > 1) {
                    displayText.setText(currentText.substring(0, currentText.length() - 1));
                } else {
                    displayText.setText("0");
                }
                break;
            case "=":
                calculateResult();
                break;
            case "+":
            case "-":
            case "*":
            case "/":
                if (isStartingNewNumber) {
                    displayText.setText(buttonText);
                } else {
                    displayText.setText(displayText.getText() + buttonText);
                }
                isStartingNewNumber = false;
                break;
            case "%":
                displayText.setText(displayText.getText() + "/100");
                isStartingNewNumber = false;
                break;
            case ".":
                if (isStartingNewNumber) {
                    displayText.setText("0.");
                    isStartingNewNumber = false;
                } else if (!displayText.getText().contains(".")) {
                    displayText.setText(displayText.getText() + ".");
                }
                break;
            default:
                if (isStartingNewNumber || displayText.getText().equals("0")) {
                    displayText.setText(buttonText);
                } else {
                    displayText.setText(displayText.getText() + buttonText);
                }
                isStartingNewNumber = false;
                break;
        }
    }
    //Calculate the result of the expression on display
    public void calculateResult() {
        try {
            String expression = displayText.getText().replace("%", "/100");
            double result = evaluateExpression(expression);

            String historyEntry = expression.replace("/100", "%") + " = " + result;
            UserManager.saveHistory(historyEntry);
            displayText.setText(String.valueOf(result));
            isStartingNewNumber = true;
        } catch (Exception e) {
            displayText.setText("WRONG");
            isStartingNewNumber = true;
        }
    }
    //Evaluate expression respecting order of operations
   private double evaluateExpression(String expr) {
        return AdditionAndSubtraction(expr);
    }
// hamdle additon and subtraction (lowest precedence)
    private double AdditionAndSubtraction(String expression) {
        for (int i = expression.length() - 1; i >= 0; i--) {
            char c = expression.charAt(i);
            if ((c == '+' || c == '-') && i != 0) {
                String leftExpr = expression.substring(0, i);
                String rightExpr = expression.substring(i + 1);
                double left = AdditionAndSubtraction(leftExpr);
                double right = MultiplicationAndDivision(rightExpr);
                return (c == '+') ? left + right : left - right;
            }
        }
        return MultiplicationAndDivision(expression);
    }
    //Handle multiplication and division (higher predence)
    private double MultiplicationAndDivision(String expr) {
        for (int i = expr.length() - 1; i >= 0; i--) {
            char c = expr.charAt(i);
            if (c == '*' || c == '/') {
                String leftExpr = expr.substring(0, i);
                String rightExpr = expr.substring(i + 1);
                double left = MultiplicationAndDivision(leftExpr);
                double right = parseFactor(rightExpr);
                return (c == '*') ? left * right : left / right;
            }
        }
        return parseFactor(expr);
    }
    //convert string to double number
    private double parseFactor(String expr) {
        return Double.parseDouble(expr);
    }
//Show  a new window displaying calculation history
    private void showHistoryWindow() {
        Stage historyStage = new Stage();
        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-padding: 20;");
        List<String> history = UserManager.loadHistory();

        if (history.isEmpty()) {
            vbox.getChildren().add(new Text("There is no history to display"));
        } else {
            for (String entry : history) {
                vbox.getChildren().add(new Text(entry));
            }
        }

        Scene scene = new Scene(vbox, 300, 300);
        historyStage.setTitle("History");
        historyStage.setScene(scene);
        historyStage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}