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
    private ComboBox<String> shapeType;
    @FXML
    private Button NewLine;

    private Model model;
    private Image bgImage;
    private double bgX, bgY, bgW = 300.0, bgH = 300.0;
    private String flag;
    private boolean isErasing = false;

    public void initialize() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        model = new Model();
        SliderTol();

        // Добавляем варианты фигур и устанавливаем значения по умолчанию
        shapeType.getItems().addAll("Круг", "Квадрат", "Треугольник", "Точка");
        shapeType.setValue("Круг");

        // Устанавливаем обработчики событий
        canvas.setOnMouseClicked(this::handleMouseClick);
        canvas.setOnMouseDragged(this::handleMouseDrag);
    }

    public void SliderTol() { // толщина линии
        sl.setMin(3);
        sl.setMax(10);
        sl.setValue(3);
        flag = NewLine.getId();
    }

    private void handleMouseClick(MouseEvent mouseEvent) {
        if (isErasing) {
            erase(mouseEvent);
        } else {
            drawShape(mouseEvent);
        }
    }

    private void handleMouseDrag(MouseEvent mouseEvent) {
        if (isErasing) {
            erase(mouseEvent);
        } else {
            drawShape(mouseEvent);
        }
    }

    private void drawShape(MouseEvent mouseEvent) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Color selectedColor = cp.getValue();
        double size = sl.getValue();

        // Создаём фигуру через фабрику
        Shape shape = ShapeFactory.createShape(
                shapeType.getValue(),
                mouseEvent.getX(),
                mouseEvent.getY(),
                size,
                selectedColor
        );

        // Добавляем фигуру в модель и рисуем её
        model.addShape(shape);
        shape.draw(gc);
    }

    private void erase(MouseEvent mouseEvent) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double eraserSize = sl.getValue();
        gc.setFill(Color.WHITESMOKE);

        // Стираем область
        gc.fillOval(
                mouseEvent.getX() - eraserSize / 2,
                mouseEvent.getY() - eraserSize / 2,
                eraserSize,
                eraserSize
        );
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
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

            update(model);
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
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Рисуем фоновое изображение, если оно есть
        if (bgImage != null) {
            gc.drawImage(bgImage, bgX, bgY, bgW, bgH);
        }

        // Рисуем все фигуры
        for (Shape shape : model.getShapes()) {
            shape.draw(gc);
        }
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
        isErasing = true;
    }

    public void kar(ActionEvent actionEvent) {
        isErasing = false;
    }

    public void act(ActionEvent actionEvent) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(cp.getValue());
    }

    public void click2(MouseEvent mouseEvent) {
        // Дополнительная логика для второго клика, если необходимо
    }
}
