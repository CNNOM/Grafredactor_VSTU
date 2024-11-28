package com.example.grafredactor;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class HelloController {
    @FXML
    private ColorPicker cp;
    @FXML
    private Slider sl;
    @FXML
    private Canvas canvas;
    private Model model;
    private Points points;
    private CanvasStateManager canvasStateManager;

    private Image bgImage;
    private double bgX, bgY, bgW = 300.0, bgH = 300.0;
    private String flag;
    @FXML
    private Button NewLine;

    public void initialize() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        model = new Model();
        canvasStateManager = new CanvasStateManager(canvas);
        SliderTol();
    }

    public void SliderTol() { // толщина линии
        sl.setMin(3);
        sl.setMax(10);
        sl.setValue(3);
        flag = NewLine.getId();
    }

    public void clik_canvas(MouseEvent mouseEvent) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        model = new Model();
        model.addPoint(new Points((int) mouseEvent.getX(), (int) mouseEvent.getY()));
        for (int i = 0; i < model.getPointCount(); i++) {
            gc.fillOval(model.getPoint(i).getX(), model.getPoint(i).getY(), model.getPoint(i).getwP(), model.getPoint(i).gethP());
        }
        canvasStateManager.saveState();
    }

    public void open(ActionEvent actionEvent) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        FileChooser fileChooser = new FileChooser(); // класс работы с диалоговым окном
        fileChooser.setTitle("Выберите изображение..."); // заголовок диалога
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Изображение", "*.jpg", "*.png"),
                new FileChooser.ExtensionFilter("Изображение", "*.bmp")
        );

        File loadImageFile = fileChooser.showOpenDialog(canvas.getScene().getWindow());
        if (loadImageFile != null) {
            System.out.println("Процесс открытия файла");
            initDraw(gc, loadImageFile);
            canvasStateManager.saveState();
        }
    }

    private void initDraw(GraphicsContext gc, File file) {
        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();

        bgImage = new Image(file.toURI().toString());
        bgX = canvasWidth / 4;
        bgY = canvasHeight / 7;
        gc.drawImage(bgImage, bgX, bgY, bgW, bgH);
    }

    public void update(Model model) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        for (int i = 0; i < model.getPointCount(); i++) {
            gc.fillOval(model.getPoint(i).getX(), model.getPoint(i).getY(), model.getPoint(i).getwP(), model.getPoint(i).gethP());
        }
        canvasStateManager.saveState();
    }

    public void print(MouseEvent mouseEvent) { // для непрерывной линии
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Points points = new Points((int) mouseEvent.getX(), (int) mouseEvent.getY());
        if (flag.equals(NewLine.getId())) {
            points.setSizePoint(sl.getValue(), sl.getValue());
            model.addPoint(points);
        } else {
            model.removePoint(points);
        }
        update(model);
    }

    public void save(ActionEvent actionEvent) throws IOException {
        WritableImage wim = new WritableImage(700, 700);
        SnapshotParameters spa = new SnapshotParameters();
        spa.setTransform(Transform.scale(2, 2));
        canvas.snapshot(spa, wim);
        File file = new File("Результат.png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void lastik(ActionEvent actionEvent) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        model = new Model();
        gc.setFill(Color.WHITESMOKE);
        for (int i = 0; i < model.getPointCount(); i++) {
            gc.clearRect(model.getPoint(i).getX(), model.getPoint(i).getY(), model.getPoint(i).getwP(), model.getPoint(i).gethP());
        }
        canvasStateManager.saveState();
    }

    public void kar(ActionEvent actionEvent) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        model = new Model();
        gc.setFill(Color.BLACK);
        for (int i = 0; i < model.getPointCount(); i++) {
            gc.clearRect(model.getPoint(i).getX(), model.getPoint(i).getY(), model.getPoint(i).getwP(), model.getPoint(i).gethP());
        }
        canvasStateManager.saveState();
    }

    public void act(ActionEvent actionEvent) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(cp.getValue());
    }

    public void click2(MouseEvent mouseEvent) {
        // Дополнительная логика для второго клика, если необходимо
    }

    public void clearCanvas(ActionEvent actionEvent) {
        canvasStateManager.clearCanvas(); // Очистка холста
    }

    public void undo(ActionEvent actionEvent) {
        canvasStateManager.undo(); // Возврат к предыдущему состоянию
    }
}