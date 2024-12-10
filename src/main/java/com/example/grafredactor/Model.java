package com.example.grafredactor;

import java.util.ArrayList;

public class Model {
    private ArrayList<Shape> shapes; // Список фигур

    public Model() {
        this.shapes = new ArrayList<>();
    }

    public int getShapeCount() {
        return shapes.size(); // Возвращает количество фигур
    }

    public void addShape(Shape shape) {
        shapes.add(shape); // Добавляет фигуру в список
    }

    public void removeShape(Shape shape) {
        shapes.remove(shape); // Удаляет фигуру из списка
    }

    public Shape getShape(int index) {
        return shapes.get(index); // Возвращает фигуру по индексу
    }

    public void clearShapes() {
        shapes.clear(); // Очищает список фигур
    }
    public ArrayList<Shape> getShapes() {
        return shapes; // Возвращает список всех фигур
    }
}
