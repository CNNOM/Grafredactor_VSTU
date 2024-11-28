package com.example.grafredactor;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;

import java.util.HashMap;
import java.util.Map;

public class CanvasStateManager {
    private Map<Integer, WritableImage> canvasStates;
    private int currentStateIndex;
    private Canvas canvas;

    public CanvasStateManager(Canvas canvas) {
        this.canvas = canvas;
        this.canvasStates = new HashMap<>();
        this.currentStateIndex = 0;
        saveState();
    }

    public void saveState() {
        WritableImage image = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        canvas.snapshot(null, image);
        canvasStates.put(currentStateIndex, image);
        currentStateIndex++;
    }

    public void restoreState(int index) {
        if (canvasStates.containsKey(index)) {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.drawImage(canvasStates.get(index), 0, 0);
        }
    }

    public void clearCanvas() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        saveState();
    }

    public void undo() {
        if (currentStateIndex > 0) {
            currentStateIndex--;
            restoreState(currentStateIndex);
        }
    }//111
}