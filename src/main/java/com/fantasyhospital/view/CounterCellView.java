package com.fantasyhospital.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * CounterCellView is a utility class that provides a method to create a circular counter view.
 * It displays a number inside a circle with a specified radius and background color.
 */
public class CounterCellView {

    /**
     * Creates a circular counter with a centered number
     *
     * @param number the number to display
     * @param radius the radius of the circle
     * @param color  the background color of the circle
     * @return a StackPane containing the circle and the centered number
     */
    public static StackPane create(int number, double radius, Color color) {
        Circle circle = new Circle(radius);
        circle.setFill(color);

        Label numberLabel = new Label(String.valueOf(number));
        numberLabel.setTextFill(Color.WHITE);
        numberLabel.setFont(Font.font("Arial", FontWeight.BOLD, radius));

        StackPane stack = new StackPane();
        stack.getChildren().addAll(circle, numberLabel);
        stack.setAlignment(Pos.CENTER);

        return stack;
    }
}
