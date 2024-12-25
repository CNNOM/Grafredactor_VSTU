package com.example.grafredactor;

import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

public class Action {
    private List<Points> points;
    private Color color;

    public Action(Color color) {
        this.points = new ArrayList<>();
        this.color = color;
    }

    public void addPoint(Points point) {
        points.add(point);
    }

    public List<Points> getPoints() {
        return points;
    }

    public Color getColor() {
        return color;
    }
}