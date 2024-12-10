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
import java.util.Stack;

public class HelloController {
    @FXML
    private ColorPicker cp;
    @FXML
    private Button toggleButton;
    @FXML
    private Button NewLine;
    @FXML
    private Button Eraser;
    @FXML
    private Button Save;
    @FXML
    private Button Download;
    @FXML
    private Button Undo;
    @FXML
    private Slider sl;
    @FXML
    private Canvas canvas;
    private Model model;
    private Points points;

    private Image bgImage;
    private double bgX, bgY, bgW = 300.0, bgH = 300.0;
    private String flag;

    private Stack<Model> history = new Stack<>();

    public void initialize() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        model = new Model();
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
        Points point = new Points((int) mouseEvent.getX(), (int) mouseEvent.getY());
        point.setSizePoint(sl.getValue(), sl.getValue());
        Action action = new Action((Color) gc.getFill());
        action.addPoint(point);
        history.push(new Model(model)); // Сохраняем текущее состояние модели с помощью конструктора копирования
        model.addAction(action);
        gc.fillOval(point.getX(), point.getY(), point.getwP(), point.gethP()); // Рисуем новую точку
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
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // Очищаем холст
        for (int i = 0; i < model.getActionCount(); i++) {
            Action action = model.getAction(i);
            gc.setFill(action.getColor());
            for (Points point : action.getPoints()) {
                gc.fillOval(point.getX(), point.getY(), point.getwP(), point.gethP());
            }
        }
    }

    public void print(MouseEvent mouseEvent) { // для непрерывной линии
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Points points = new Points((int) mouseEvent.getX(), (int) mouseEvent.getY());
        if (flag.equals(NewLine.getId())) {
            points.setSizePoint(sl.getValue(), sl.getValue());
            if (model.getActionCount() > 0 && model.getAction(model.getActionCount() - 1).getColor().equals(gc.getFill())) {
                model.getAction(model.getActionCount() - 1).addPoint(points);
            } else {
                Action action = new Action((Color) gc.getFill());
                action.addPoint(points);
                history.push(new Model(model)); // Сохраняем текущее состояние модели с помощью конструктора копирования
                model.addAction(action);
            }
            gc.fillOval(points.getX(), points.getY(), points.getwP(), points.gethP()); // Рисуем новую точку
        } else {
            // Удаляем все точки в области действия ластика
            boolean pointsRemoved = false;
            for (int i = model.getActionCount() - 1; i >= 0; i--) {
                Action action = model.getAction(i);
                for (Points point : action.getPoints()) {
                    if (isPointInEraserArea(point, points)) {
                        action.getPoints().remove(point);
                        pointsRemoved = true;
                    }
                }
                if (pointsRemoved) {
                    history.push(new Model(model)); // Сохраняем текущее состояние модели с помощью конструктора копирования
                }
                if (action.getPoints().isEmpty()) {
                    model.removeLastAction();
                }
            }
            update(model);
        }
    }

    private boolean isPointInEraserArea(Points point, Points eraserPoint) {
        double eraserRadius = sl.getValue() / 2;
        return Math.abs(point.getX() - eraserPoint.getX()) <= eraserRadius &&
                Math.abs(point.getY() - eraserPoint.getY()) <= eraserRadius;
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
        for (int i = 0; i < model.getActionCount(); i++) {
            Action action = model.getAction(i);
            if (!action.getPoints().isEmpty()) {
                Points point = action.getPoints().get(0); // Получаем первую точку из списка
                gc.clearRect(point.getX(), point.getY(), point.getwP(), point.gethP());
            }
        }
    }

    public void kar(ActionEvent actionEvent) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        model = new Model();
        gc.setFill(Color.BLACK);
        for (int i = 0; i < model.getActionCount(); i++) {
            Action action = model.getAction(i);
            if (!action.getPoints().isEmpty()) {
                Points point = action.getPoints().get(0); // Получаем первую точку из списка
                gc.clearRect(point.getX(), point.getY(), point.getwP(), point.gethP());
            }
        }
    }

    public void act(ActionEvent actionEvent) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(cp.getValue());
    }

    public void click2(MouseEvent mouseEvent) {
        // Дополнительная логика для второго клика, если необходимо
    }

    public void toggleLanguage(ActionEvent actionEvent) {
        String currentText = toggleButton.getText();
        RuEng ruEng = new RuEng(toggleButton, NewLine, Eraser, Save, Download, Undo, cp); // Передаем ColorPicker
        if ("ENG".equals(currentText)) {
            ruEng.Eng();
        } else if ("RU".equals(currentText)) {
            ruEng.Ru();
        }
    }

    public void undo(ActionEvent actionEvent) {
        undo();
    }

    public void undo() {
        if (!history.isEmpty()) {
            model = history.pop(); // Восстанавливаем предыдущее состояние модели
            update(model); // Обновляем холст с восстановленным состоянием модели
        } else {
            // Если история пуста, очищаем холст
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        }
    }
}