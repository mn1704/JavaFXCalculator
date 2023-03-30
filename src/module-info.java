module JavaFXCalculator {

    requires javafx.fxml;
    requires javafx.controls;

    opens ro.ctrln.javafx.calculator.controller;
    opens ro.ctrln.javafx.calculator;
    opens ro.ctrln.javafx.calculator.operations;
}