package ro.ctrln.javafx.calculator.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import ro.ctrln.javafx.calculator.operations.Operation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

public class CalculatorController {
    @FXML
    public TextArea calculatorOperationsArea;
    @FXML
    public Label errorsLabel;

    // Aceasta metoda verifica daca !un! 0 a fost deja scris
    private boolean replaceZero(String replacement) {
        boolean zeroReplaced = false;
        if (calculatorOperationsArea.getText().equalsIgnoreCase("0")) {
            calculatorOperationsArea.setText(replacement);
            zeroReplaced = true;
        }
        return !zeroReplaced;
    }

    // Aceasta metoda seteaza pozitia cursorului la limita din dreapta a textului
    private void setPositionCaret() {
        calculatorOperationsArea.positionCaret(calculatorOperationsArea.getText().length());
    }

    // Medotele write(...) afiseaza pe calculatorOperationsArea cifra respectiva
    @FXML
    public void writeZero(ActionEvent actionEvent) {
        checkNewOperation();
        if (!calculatorOperationsArea.getText().equalsIgnoreCase("0")) {
            calculatorOperationsArea.setText(calculatorOperationsArea.getText().concat("0"));
        }
        setPositionCaret();
    }

    private void writeDigit(String digit) {
        checkNewOperation();
        if (replaceZero(digit)) {
            calculatorOperationsArea.setText(calculatorOperationsArea.getText().concat(digit));
        }
        setPositionCaret();
    }

    @FXML
    public void writeOne(ActionEvent actionEvent) {
        writeDigit("1");
    }

    @FXML
    public void writeTwo(ActionEvent actionEvent) {
        writeDigit("2");
    }

    @FXML
    public void writeThree(ActionEvent actionEvent) {
        writeDigit("3");
    }

    @FXML
    public void writeFour(ActionEvent actionEvent) {
        writeDigit("4");
    }

    @FXML
    public void writeFive(ActionEvent actionEvent) {
        writeDigit("5");
    }

    @FXML
    public void writeSix(ActionEvent actionEvent) {
        writeDigit("6");
    }

    @FXML
    public void writeSeven(ActionEvent actionEvent) {
        writeDigit("7");
    }

    @FXML
    public void writeEight(ActionEvent actionEvent) {
        writeDigit("8");
    }

    @FXML
    public void writeNine(ActionEvent actionEvent) {
        writeDigit("9");
    }

    @FXML
    public void writeComma(ActionEvent actionEvent) {
        if (!commaAlreadyPresentOnOperand(calculatorOperationsArea.getText())) {
            calculatorOperationsArea.setText(calculatorOperationsArea.getText().concat("."));
        }
    }

    // Metoda aceasta reseteaza calculatorOperationsArea
    private void checkNewOperation() {
        if (calculatorOperationsArea.getText().contains("=")) {
            calculatorOperationsArea.setText("");
        }
    }

    private boolean commaAlreadyPresentOnOperand(String text) {
        if (mathOperationsNotPresentOnCalculatorTextArea()) {
            return text.contains("."); // verificam operandul din partea stanga a operatorului
        } else { // verificam operatorul din partea dreapta a operatorului

            String[] operands = {};
            for (String mathOperation : operationsCharacters) {
                if (operands.length == 2) {
                    break;
                }
                operands = splitOperation(text, mathOperation);
            }
            return operands[1].contains(".");
        }
    }

    // Executia celor 4 operatii matematice implementate
    @FXML
    public void addition(ActionEvent actionEvent) {
        if (mathOperationsNotPresentOnCalculatorTextArea())
            calculatorOperationsArea.setText(calculatorOperationsArea.getText().concat("+"));
    }

    @FXML
    public void subtraction(ActionEvent actionEvent) {
        if (mathOperationsNotPresentOnCalculatorTextArea()) {
            calculatorOperationsArea.setText(calculatorOperationsArea.getText().concat("-"));
        }
    }

    @FXML
    public void multiplication(ActionEvent actionEvent) {
        if (mathOperationsNotPresentOnCalculatorTextArea()) {
            calculatorOperationsArea.setText(calculatorOperationsArea.getText().concat("*"));
        }
    }

    @FXML
    public void division(ActionEvent actionEvent) {
        if (mathOperationsNotPresentOnCalculatorTextArea()) {
            calculatorOperationsArea.setText(calculatorOperationsArea.getText().concat("/"));
        }
    }

    private boolean mathOperationsNotPresentOnCalculatorTextArea() {
        return !calculatorOperationsArea.getText().contains("+") &&
                !calculatorOperationsArea.getText().contains("-") &&
                !calculatorOperationsArea.getText().contains("/") &&
                !calculatorOperationsArea.getText().contains("*");
    }

    // Resetam spatiul de lucru
    @FXML
    public void clearCalculatorOperationsArea(ActionEvent actionEvent) {
        calculatorOperationsArea.setText("");
        errorsLabel.setText("CtrlN JavaFX Calculator");
    }

    @FXML
    public void evaluate(ActionEvent actionEvent) {
        String operation = calculatorOperationsArea.getText();
        if (!operation.isEmpty()) {
            if (operation.contains("+")) {
                performAddition(operation);
            } else if (operation.contains("-")) {
                performSubtraction(operation);
            } else if (operation.contains("*")) {
                performMultiplication(operation);
            } else if (operation.contains("/")) {
                performDivision(operation);
            } else {
                errorsLabel.setText("Avem o operatie necunoscuta!");
            }
        }
    }

    private void performAddition(String operation) {
        String[] operands = splitOperation(operation, "+");
        if (operands.length == 2) {
            doOperation(operands, Operation.ADDITION);
        }
    }

    private void performSubtraction(String operation) {
        String[] operands = splitOperation(operation, "-");
        if (operands.length == 2) {
            doOperation(operands, Operation.SUBTRACTION);
        }
    }

    private void performMultiplication(String operation) {
        String[] operands = splitOperation(operation, "*");
        if (operands.length == 2) {
            doOperation(operands, Operation.MULTIPLICATION);
        }
    }

    private void performDivision(String operation) {
        String[] operands = splitOperation(operation, "/");
        if (operands.length == 2) {
            doOperation(operands, Operation.DIVISION);
        }
    }

    private String[] splitOperation(String operation, String splitter) {
        String[] operands = {};
        try {
            if (Arrays.asList("+", "-", "/", "*").contains(splitter)) {
                operation = operation.replace(splitter, "----");
            }
            operands = operation.split("----");
        } catch (Exception ex) {
            errorsLabel.setText("Operanzi nedectati!");
            ex.printStackTrace();
        }
        return operands;
    }


    private void doOperation(String[] operands, Operation operation) {
        try {
            BigDecimal firstOperand = new BigDecimal(cleanOperand(operands[0]));
            BigDecimal secondOperand = new BigDecimal(cleanOperand(operands[1]));

            switch (operation) {
                case ADDITION:
                    writeResult(firstOperand.add(secondOperand));
                    break;
                case SUBTRACTION:
                    writeResult(firstOperand.subtract(secondOperand));
                    break;
                case DIVISION:
                    writeResult(firstOperand.divide(secondOperand, RoundingMode.HALF_DOWN));
                    break;
                case MULTIPLICATION:
                    writeResult(firstOperand.multiply(secondOperand));
                    break;
            }
        } catch (NumberFormatException nfe) {
            errorsLabel.setText("Operanzii sunt gresiti!");
        }
    }

    // curata termenul expresiei matematice de "\n"
    private String cleanOperand(String operand) {
        return operand.replaceAll("\n", "");
    }

    // afisam rezultatul final pe textArea
    private void writeResult(BigDecimal result) {
        calculatorOperationsArea.setText(calculatorOperationsArea.getText()
                .replaceAll("\n", "")
                .replaceAll("\r", "")
                .concat("=").concat(result.toString()));
    }

    //ce se intampla la tastatura
    @FXML
    public void handleKeyTyped(KeyEvent keyEvent) {
        if (allowedCharacters(keyEvent.getCharacter())) {

            if (keyEvent.getCharacter().equalsIgnoreCase(".")) {
                writeComma(new ActionEvent());
                keyEvent.consume();
            }

            if (operationCharacter(keyEvent.getCharacter())) {
                if (!mathOperationsNotPresentOnCalculatorTextArea()) {
                    keyEvent.consume();
                }
            }

            if (keyEvent.getCharacter().equalsIgnoreCase("=") || keyEvent.getCharacter().equalsIgnoreCase("\r")) {
                keyEvent.consume();
                evaluate(new ActionEvent());
            }
        } else {
            keyEvent.consume();
        }
    }

    // Sir de caractere valide
    private List<String> allowedCharacters = Arrays
            .asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ".", "=", "+", "-", "/", "*", "\r", "\n");

    // Verific daca caracterul introdus este valid
    private boolean allowedCharacters(String character) {
        return allowedCharacters.contains(character);
    }

    // Sir de operanzi valizi
    private List<String> operationsCharacters = Arrays.asList("+", "-", "/", "*");

    // Verific daca operandul este valid
    private boolean operationCharacter(String character) {
        return operationsCharacters.contains(character);
    }

    // Lista de cifre
    private List<String> digitCharacters = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");

    // Verific daca caracterul este valid
    private boolean isDigitCharacter(String character) {
        return digitCharacters.contains(character);
    }
}