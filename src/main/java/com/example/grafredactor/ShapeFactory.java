package com.example.grafredactor;

import javafx.scene.paint.Color;

public class ShapeFactory {
    public static Shape createShape(String type, double x, double y, double size, Color color) {
        switch (type) {
            case "Круг":
                return new Circle(x, y, size, color);
            case "Квадрат":
                return new Rectangle(x, y, size, size, color);
            case "Треугольник":
                return new Triangle(x, y, size, color);
            case "Точка":
                return new Points(x, y, size, color);
            default:
                throw new IllegalArgumentException("Неизвестный тип фигуры: " + type);
        }
    }
}