package com.example.shapeslider;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private List<Shape> shapes = new ArrayList<>();
    private int currentIndex = 0;
    private CenteredPane shapeHolder;
    private Label shapeNameLabel;
    private double orgSceneX, orgSceneY;
    private double orgTranslateX, orgTranslateY;
    private double minDimension = Double.MAX_VALUE;
    private double minSizeThreshold = 100; // Minimum size threshold
    private double triangleSizeIncrease = 20;

    @Override
    public void start(Stage stage) {

        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #E0E0E0;");

        VBox container = new VBox();

        shapeNameLabel = new Label("");
        shapeNameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 24px; -fx-text-fill: white;");

        shapeHolder = new CenteredPane();

        Button button1 = new Button("Previous");
        Button button2 = new Button("Change Background");
        Button button3 = new Button("Next");

        button1.setStyle("-fx-font-weight: bold; -fx-background-color: #424242; -fx-text-fill: white;");
        button2.setStyle("-fx-font-weight: bold; -fx-background-color: #424242; -fx-text-fill: white;");
        button3.setStyle("-fx-font-weight: bold; -fx-background-color: #424242; -fx-text-fill: white;");

        HBox buttons = new HBox(button1, button2, button3);
        buttons.setSpacing(50);
        buttons.setAlignment(Pos.BOTTOM_CENTER);

        container.setAlignment(Pos.CENTER);
        container.setSpacing(30);
        container.getChildren().addAll(shapeNameLabel, shapeHolder, buttons);

        button1.setOnAction(e -> showPreviousShape());
        button2.setOnAction(e -> changeBackground());
        button3.setOnAction(e -> showNextShape());

        root.getChildren().addAll(container);
        Scene scene = new Scene(root, 500, 500);
        stage.setTitle("MY SHAPE SLIDER");
        stage.setScene(scene);
        stage.show();

        Rectangle rectangle = new Rectangle(150, 150, Color.BLUE);
        Circle circle = new Circle(75, Color.RED);
        Polygon triangle = new Polygon();


        double triangleSideLength = minSizeThreshold + triangleSizeIncrease;
        double halfSideLength = triangleSideLength / 2;
        triangle.getPoints().addAll(new Double[]{
                halfSideLength, 0.0,
                0.0, triangleSideLength,
                triangleSideLength, triangleSideLength});

        shapes.add(rectangle);
        shapes.add(circle);
        shapes.add(triangle);

        for (Shape shape : shapes) {
            minDimension = Math.min(minDimension, Math.min(shape.getBoundsInLocal().getWidth(), shape.getBoundsInLocal().getHeight()));
            enableDragging(shape);
        }

        if (minDimension < minSizeThreshold) {
            double scaleFactor = minSizeThreshold / minDimension;
            for (Shape shape : shapes) {
                shape.setScaleX(scaleFactor);
                shape.setScaleY(scaleFactor);
            }
        }

        shapeHolder.getChildren().addAll(shapes.get(currentIndex));
        updateShapeNameLabel();
    }

    private void enableDragging(Shape shape) {
        shape.setOnMousePressed((MouseEvent event) -> {
            orgSceneX = event.getSceneX();
            orgSceneY = event.getSceneY();
            orgTranslateX = ((Shape) (event.getSource())).getTranslateX();
            orgTranslateY = ((Shape) (event.getSource())).getTranslateY();
        });

        shape.setOnMouseDragged((MouseEvent event) -> {
            double offsetX = event.getSceneX() - orgSceneX;
            double offsetY = event.getSceneY() - orgSceneY;
            double newTranslateX = orgTranslateX + offsetX;
            double newTranslateY = orgTranslateY + offsetY;

            ((Shape) (event.getSource())).setTranslateX(newTranslateX);
            ((Shape) (event.getSource())).setTranslateY(newTranslateY);
        });
    }

    private void showNextShape() {
        currentIndex = (currentIndex + 1) % shapes.size();
        shapeHolder.getChildren().clear();
        shapeHolder.getChildren().add(shapes.get(currentIndex));
        updateShapeNameLabel();
    }

    private void showPreviousShape() {
        currentIndex = (currentIndex - 1 + shapes.size()) % shapes.size();
        shapeHolder.getChildren().clear();
        shapeHolder.getChildren().add(shapes.get(currentIndex));
        updateShapeNameLabel();
    }

    private void changeBackground() {
        Shape currentShape = shapes.get(currentIndex);
        Color randomColor = Color.rgb((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
        currentShape.setFill(randomColor);
    }


    private void updateShapeNameLabel() {
        String shapeName = getShapeName(currentIndex);
        shapeNameLabel.setText(shapeName);
    }

    private String getShapeName(int index) {
        if (index == 0)
            return "Rectangle";
        else if (index == 1)
            return "Circle";
        else if (index == 2)
            return "Triangle";
        else
            return "";
    }

    public static void main(String[] args) {
        launch();
    }

    private static class CenteredPane extends Pane {
        @Override
        protected void layoutChildren() {
            super.layoutChildren();
            double width = getWidth();
            double height = getHeight();

            for (javafx.scene.Node child : getChildren()) {
                double x = (width - child.getBoundsInLocal().getWidth()) / 2;
                double y = (height - child.getBoundsInLocal().getHeight()) / 2;
                child.relocate(x, y);
            }
        }
    }
}
