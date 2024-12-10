package com.example.grafredactor;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Points extends Shape {
    private double size; // Размер точки

    public Points(double x, double y, double size, Color color) {
        super(x, y, color);
        this.size = size;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(color);
        // Рисуем точку как круг с минимальным радиусом
        gc.fillOval(x - size / 2, y - size / 2, size, size);
    }

    // Геттеры и сеттеры
    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }
}