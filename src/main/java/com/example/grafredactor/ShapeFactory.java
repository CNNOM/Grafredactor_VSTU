package com.example.grafredactor;

import javafx.scene.paint.Color;

public class ShapeFactory {
    public static Shape createShape(String type, double x, double y, double size, Color color) {
        switch (type) {
            case "Круг":
            case "Circle":
                return new Circle(x, y, size, color);
            case "Квадрат":
            case "Square":
                return new Rectangle(x, y, size, size, color);
            case "Треугольник":
            case "Triangle":
                return new Triangle(x, y, size, color);
            case "Point":
            case "Точка":
                return new Points(x, y, size, color);
            default:
                throw new IllegalArgumentException("Неизвестный тип фигуры: " + type);
        }
    }
}