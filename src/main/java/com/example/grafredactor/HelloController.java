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
    private Slider sl;
    @FXML
    private Canvas canvas;
    private Model model;
    private Points points;



    private Image bgImage;
    private double bgX, bgY, bgW = 300.0, bgH = 300.0;
    private String flag;

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
        model = new Model();
        model.addPoint(new Points((int) mouseEvent.getX(), (int) mouseEvent.getY()));
        for (int i = 0; i < model.getPointCount(); i++) {
            gc.fillOval(model.getPoint(i).getX(), model.getPoint(i).getY(), model.getPoint(i).getwP(), model.getPoint(i).gethP());
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
        for (int i = 0; i < model.getPointCount(); i++) {
            gc.fillOval(model.getPoint(i).getX(), model.getPoint(i).getY(), model.getPoint(i).getwP(), model.getPoint(i).gethP());
        }
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

    public void toggleLanguage(ActionEvent actionEvent) {
        String currentText = toggleButton.getText();

        if ("ENG".equals(currentText)) {
            toggleButton.setText("RU");
            NewLine.setText("Карандаш");
            Eraser.setText("Ластик");
            Save.setText("Сохранить");
            Download.setText("Загрузить");
            System.out.println("Language set to Russian");
            // Ваш код для переключения на русский язык
        } else if ("RU".equals(currentText)) {
            toggleButton.setText("ENG");
            NewLine.setText("NewLine");
            Eraser.setText("Eraser");
            Save.setText("Save");
            Download.setText("Download");
            System.out.println("Language set to English");
            // Ваш код для переключения на английский язык
        }
    }

}