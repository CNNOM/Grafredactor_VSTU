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
import javafx.scene.control.Label;
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
    private Slider sl2;
    @FXML
    private Canvas canvas;
    @FXML
    private ComboBox<String> shapeType;
    @FXML
    private ComboBox<String> eraserShapeType;
    @FXML
    private Label mouseCoordinatesLabel;
    @FXML
    private Label colorInCoordinatesLabel;
    @FXML
    private Label drawingStatusLabel;
    @FXML
    private Label shapeTypeLabel; // Метка для выбора формы карандаша
    @FXML
    private Label eraserShapeTypeLabel; // Метка для выбора формы ластика
    @FXML
    private Label eraserSizeLabel; // Метка для выбора размера ластика

    private String eraserShape = "Круг"; // Форма ластика по умолчанию
    private Model model;
    private Image bgImage;
    private double bgX, bgY, bgW = 300.0, bgH = 300.0;
    private String flag;
    private boolean isErasing = false;
    private boolean isDrawing = false;
    private Stack<Model> history = new Stack<>();

    public void initialize() {

        GraphicsContext gc = canvas.getGraphicsContext2D();
        model = new Model();
        SliderTol();

        // Добавляем варианты фигур и устанавливаем значения по умолчанию
        shapeType.getItems().addAll("Круг", "Квадрат", "Треугольник", "Точка");
        shapeType.setValue("Круг");

        // Добавляем варианты форм ластика
        eraserShapeType.getItems().addAll("Круг", "Квадрат", "Треугольник", "Точка");
        eraserShapeType.setValue("Круг");

        // Устанавливаем обработчики событий
        canvas.setOnMouseClicked(this::handleMouseClick);
        canvas.setOnMouseDragged(this::handleMouseDrag);

        canvas.setOnMouseMoved(this::handleMouseMove); // Обработчик движения мыши
        canvas.setOnMousePressed(this::handleMousePress); // Обработчик нажатия мыши
        canvas.setOnMouseReleased(this::handleMouseRelease); // Обработчик отпускания мыши

        // Обработчик изменения выбора формы ластика
        eraserShapeType.setOnAction(event -> eraserShape = eraserShapeType.getValue());
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
            mouseCoordinatesLabel.setText(String.format("Координаты: (%.2f, %.2f)", mouseEvent.getX(), mouseEvent.getY()));
        } else {
            drawShape(mouseEvent);
            mouseCoordinatesLabel.setText(String.format("Координаты: (%.2f, %.2f)", mouseEvent.getX(), mouseEvent.getY()));
        }
        Color color = getColorAt(mouseEvent.getX(), mouseEvent.getY());
        colorInCoordinatesLabel.setText("Цвет: " + (color != null ? color.toString() : "Нет цвета"));
    }

    private void handleMouseDrag(MouseEvent mouseEvent) {
        if (isErasing) {
            erase(mouseEvent);
            mouseCoordinatesLabel.setText(String.format("Координаты: (%.2f, %.2f)", mouseEvent.getX(), mouseEvent.getY()));
        } else {
            drawShape(mouseEvent);
            mouseCoordinatesLabel.setText(String.format("Координаты: (%.2f, %.2f)", mouseEvent.getX(), mouseEvent.getY()));
        }
        Color color = getColorAt(mouseEvent.getX(), mouseEvent.getY());
        colorInCoordinatesLabel.setText("Цвет: " + (color != null ? color.toString() : "Нет цвета"));
    }


    private void handleMouseMove(MouseEvent mouseEvent) {
        // Обновляем координаты мыши
        mouseCoordinatesLabel.setText(String.format("Координаты: (%.2f, %.2f)", mouseEvent.getX(), mouseEvent.getY()));

        // Получаем цвет под курсором
        Color color = getColorAt(mouseEvent.getX(), mouseEvent.getY());
        colorInCoordinatesLabel.setText("Цвет: " + (color != null ? color.toString() : "Нет цвета"));

        // Обновляем статус рисования
        updateDrawingStatus();
    }

    private void handleMousePress(MouseEvent mouseEvent) {
        // Устанавливаем флаг рисования
        isDrawing = true;

        // Устанавливаем статус рисования
        drawingStatusLabel.setText(isErasing ? "Статус: Стирает" : "Статус: Рисует");
    }

    private void handleMouseRelease(MouseEvent mouseEvent) {
        // Сбрасываем флаг рисования
        isDrawing = false;

        // Устанавливаем статус рисования
        if (isErasing) {
            drawingStatusLabel.setText("Статус: Не стирает");
        } else {
            drawingStatusLabel.setText("Статус: Не рисует");
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
        double eraserSize = sl2.getValue();
        gc.setFill(Color.WHITESMOKE);

        // Выбираем форму ластика
        switch (eraserShape) {
            case "Круг":
            case "Circle":
                gc.fillOval(
                        mouseEvent.getX() - eraserSize / 2,
                        mouseEvent.getY() - eraserSize / 2,
                        eraserSize,
                        eraserSize
                );
                break;
            case "Квадрат":
            case "Square":
                gc.fillRect(
                        mouseEvent.getX() - eraserSize / 2,
                        mouseEvent.getY() - eraserSize / 2,
                        eraserSize,
                        eraserSize
                );
                break;
            case "Треугольник":
            case "Triangle":
                gc.fillPolygon(
                        new double[]{mouseEvent.getX(), mouseEvent.getX() - eraserSize / 2, mouseEvent.getX() + eraserSize / 2},
                        new double[]{mouseEvent.getY() - eraserSize / 2, mouseEvent.getY() + eraserSize / 2, mouseEvent.getY() + eraserSize / 2},
                        3
                );
                break;
            case "Point":
            case "Точка":
                gc.fillRect(
                        mouseEvent.getX(),
                        mouseEvent.getY(),
                        1,
                        1
                );
                break;
        }
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

    private Color getColorAt(double x, double y) {
        WritableImage snapshot = canvas.snapshot(null, null);
        return snapshot.getPixelReader().getColor((int) x, (int) y);
    }

    private void updateDrawingStatus() {
        drawingStatusLabel.setText(isErasing ? "Статус: Не cтирает" : "Статус: Не рисует");
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

    public void toggleLanguage(ActionEvent actionEvent) {
        String currentText = toggleButton.getText();
        RuEng ruEng = new RuEng(toggleButton, NewLine, Eraser, Save, Download, Undo, shapeType, eraserShapeType, mouseCoordinatesLabel, colorInCoordinatesLabel, drawingStatusLabel, shapeTypeLabel, eraserShapeTypeLabel, eraserSizeLabel); // Передаем метки
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