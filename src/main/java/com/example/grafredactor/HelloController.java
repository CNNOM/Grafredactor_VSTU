package com.example.grafredactor;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
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
    @FXML
    private ComboBox<String> eraserShape; // Новый элемент для выбора формы ластика
    private Model model;
    private Image bgImage;
    private String flag;
    @FXML
    private Button NewLine;

    public void initialize() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        model = new Model();
        SliderTol();

        // Инициализация списка форм ластика
        eraserShape.getItems().addAll("Круг", "Квадрат", "Треугольник");
        eraserShape.setValue("Круг"); // Значение по умолчанию
    }

    public void SliderTol() { // Толщина линии
        sl.setMin(3);
        sl.setMax(10);
        sl.setValue(3);
        flag = NewLine.getId();
    }

    public void clik_canvas(MouseEvent mouseEvent) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        model.addPoint(new Points((int) mouseEvent.getX(), (int) mouseEvent.getY()));
        for (int i = 0; i < model.getPointCount(); i++) {
            gc.fillOval(model.getPoint(i).getX(), model.getPoint(i).getY(), model.getPoint(i).getwP(), model.getPoint(i).gethP());
        }
    }

    public void open(ActionEvent actionEvent) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        FileChooser fileChooser = new FileChooser(); // Диалог выбора файла
        fileChooser.setTitle("Выберите изображение...");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Изображение", "*.jpg", "*.png", "*.bmp")
        );

        File loadImageFile = fileChooser.showOpenDialog(canvas.getScene().getWindow());
        if (loadImageFile != null) {
            initDraw(gc, loadImageFile);
        }
    }

    private void initDraw(GraphicsContext gc, File file) {
        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();

        bgImage = new Image(file.toURI().toString());
        double bgX = canvasWidth / 4;
        double bgY = canvasHeight / 7;
        double bgW = 300.0;
        double bgH = 300.0;
        gc.drawImage(bgImage, bgX, bgY, bgW, bgH);
    }

    public void save(ActionEvent actionEvent) throws IOException {
        WritableImage wim = new WritableImage(700, 700);
        SnapshotParameters spa = new SnapshotParameters();
        spa.setTransform(Transform.scale(2, 2));
        canvas.snapshot(spa, wim);
        File file = new File("Результат.png");
        ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
    }

    public void lastik(ActionEvent actionEvent) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITESMOKE); // Цвет ластика
    }

    public void print(MouseEvent mouseEvent) { // Для рисования и использования ластика
        GraphicsContext gc = canvas.getGraphicsContext2D();

        double x = mouseEvent.getX();
        double y = mouseEvent.getY();
        double size = sl.getValue(); // Размер ластика
        String selectedShape = eraserShape.getValue(); // Получаем выбранную форму

        switch (selectedShape) {
            case "Круг": // Круглый ластик
                gc.fillOval(x - size / 2, y - size / 2, size, size);
                break;
            case "Квадрат": // Квадратный ластик
                gc.fillRect(x - size / 2, y - size / 2, size, size);
                break;
            case "Треугольник": // Треугольный ластик
                double[] xPoints = {x, x - size / 2, x + size / 2};
                double[] yPoints = {y - size / 2, y + size / 2, y + size / 2};
                gc.fillPolygon(xPoints, yPoints, 3);
                break;
            default: // По умолчанию — круг
                gc.fillOval(x - size / 2, y - size / 2, size, size);
                break;
        }
    }

    public void kar(ActionEvent actionEvent) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        model = new Model();
        gc.setFill(Color.BLACK);
        for (int i = 0; i < model.getPointCount(); i++) {
            gc.clearRect(model.getPoint(i).getX(), model.getPoint(i).getY(), model.getPoint(i).getwP(), model.getPoint(i).gethP());
        }
    }

    public void act(ActionEvent actionEvent) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(cp.getValue());
    }


    public void click2(MouseEvent mouseEvent) {
        // Дополнительная логика для второго клика, если необходимо
    }
}
