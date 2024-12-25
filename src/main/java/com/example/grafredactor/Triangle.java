package com.example.grafredactor;

import javafx.scene.paint.Color;

import javafx.scene.canvas.GraphicsContext;

public class Triangle extends Shape {
    private double size;

    public Triangle(double x, double y, double size, Color color) {
        super(x, y, color);
        this.size = size;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(color);
        double[] xPoints = {x, x - size / 2, x + size / 2};
        double[] yPoints = {y - size, y + size / 2, y + size / 2};
        gc.fillPolygon(xPoints, yPoints, 3);
    }

    // Геттеры и сеттеры
    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }
}
