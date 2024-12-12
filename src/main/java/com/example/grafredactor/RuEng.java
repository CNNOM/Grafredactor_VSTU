package com.example.grafredactor;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.util.HashMap;
import java.util.Map;

public class RuEng {
    private Button toggleButton;
    private Button NewLine;
    private Button Eraser;
    private Button Save;
    private Button Download;
    private Button Undo;

    private ComboBox<String> shapeType;
    private ComboBox<String> eraserShapeType;
    private Label mouseCoordinatesLabel;
    private Label colorInCoordinatesLabel;
    private Label drawingStatusLabel;
    private Label shapeTypeLabel; // Метка для выбора формы карандаша
    private Label eraserShapeTypeLabel; // Метка для выбора формы ластика
    private Label eraserSizeLabel; // Метка для выбора размера ластика

    // Карта для хранения переводов кнопок
    private Map<String, String> buttonTranslations = new HashMap<>();

    // Карта для хранения переводов элементов ComboBox
    private Map<String, String[]> comboBoxTranslations = new HashMap<>();

    // Карта для хранения переводов текстовых меток
    private Map<String, String> labelTranslations = new HashMap<>();

    RuEng(Button toggleButton, Button NewLine, Button Eraser, Button Save, Button Download, Button Undo, ComboBox<String> shapeType, ComboBox<String> eraserShapeType, Label mouseCoordinatesLabel, Label colorInCoordinatesLabel, Label drawingStatusLabel, Label shapeTypeLabel, Label eraserShapeTypeLabel, Label eraserSizeLabel) {
        this.toggleButton = toggleButton;
        this.NewLine = NewLine;
        this.Eraser = Eraser;
        this.Save = Save;
        this.Download = Download;
        this.Undo = Undo;
        this.shapeType = shapeType;
        this.eraserShapeType = eraserShapeType;
        this.mouseCoordinatesLabel = mouseCoordinatesLabel;
        this.colorInCoordinatesLabel = colorInCoordinatesLabel;
        this.drawingStatusLabel = drawingStatusLabel;
        this.shapeTypeLabel = shapeTypeLabel;
        this.eraserShapeTypeLabel = eraserShapeTypeLabel;
        this.eraserSizeLabel = eraserSizeLabel;

        // Инициализация переводов
        initTranslations();
    }

    // Метод для инициализации переводов
    private void initTranslations() {
        // Переводы кнопок
        buttonTranslations.put("NewLine_RU", "Карандаш");
        buttonTranslations.put("NewLine_ENG", "NewLine");
        buttonTranslations.put("Eraser_RU", "Ластик");
        buttonTranslations.put("Eraser_ENG", "Eraser");
        buttonTranslations.put("Save_RU", "Сохранить");
        buttonTranslations.put("Save_ENG", "Save");
        buttonTranslations.put("Download_RU", "Загрузить");
        buttonTranslations.put("Download_ENG", "Download");
        buttonTranslations.put("Undo_RU", "Отмена");
        buttonTranslations.put("Undo_ENG", "Undo");

        // Переводы элементов ComboBox для фигур
        comboBoxTranslations.put("ShapeType_RU", new String[]{"Круг", "Квадрат", "Треугольник", "Точка"});
        comboBoxTranslations.put("ShapeType_ENG", new String[]{"Circle", "Square", "Triangle", "Point"});

        // Переводы элементов ComboBox для форм ластика
        comboBoxTranslations.put("EraserShapeType_RU", new String[]{"Круг", "Квадрат", "Треугольник", "Точка"});
        comboBoxTranslations.put("EraserShapeType_ENG", new String[]{"Circle", "Square", "Triangle", "Point"});

        // Переводы текстовых меток
        labelTranslations.put("MouseCoordinates_RU", "Координаты мыши:");
        labelTranslations.put("MouseCoordinates_ENG", "Mouse Coordinates:");
        labelTranslations.put("ColorInCoordinates_RU", "Цвет в координатах:");
        labelTranslations.put("ColorInCoordinates_ENG", "Color in Coordinates:");
        labelTranslations.put("DrawingStatus_RU", "Тип рисует / не рисует:");
        labelTranslations.put("DrawingStatus_ENG", "Drawing / Not Drawing:");
        labelTranslations.put("ShapeType_RU", "Выбор формы карандаша:");
        labelTranslations.put("ShapeType_ENG", "Choose Pencil Shape:");
        labelTranslations.put("EraserShapeType_RU", "Выбор формы ластика:");
        labelTranslations.put("EraserShapeType_ENG", "Choose Eraser Shape:");
        labelTranslations.put("EraserSize_RU", "Выбор размера ластика:");
        labelTranslations.put("EraserSize_ENG", "Choose Eraser Size:");
    }

    // Метод для переключения на русский язык
    public void Eng() {
        toggleButton.setText("RU");
        NewLine.setText("✎");
        Eraser.setText("⌫");
        Save.setText("\uD83D\uDCBE");
        Download.setText("\uD83D\uDCE5");
//        Undo.setText("Отмена"); // Обновляем текст кнопки "Undo"
        updateButtons("RU");
        updateComboBox(shapeType, "ShapeType_RU");
        updateComboBox(eraserShapeType, "EraserShapeType_RU");
        updateLabels("RU");
        System.out.println("Language set to RUSSIA");
    }

    // Метод для переключения на английский язык
    public void Ru() {
        toggleButton.setText("ENG");
        NewLine.setText("✎");
        Eraser.setText("⌫");
        Save.setText("\uD83D\uDCBE");
        Download.setText("\uD83D\uDCE5");
//        Undo.setText("Undo"); // Обновляем текст кнопки "Undo"
        updateButtons("ENG");
        updateComboBox(shapeType, "ShapeType_ENG");
        updateComboBox(eraserShapeType, "EraserShapeType_ENG");
        updateLabels("ENG");
        System.out.println("Language set to English");
    }

    // Метод для обновления текста кнопок
    private void updateButtons(String language) {
        NewLine.setText(buttonTranslations.get("NewLine_" + language));
        Eraser.setText(buttonTranslations.get("Eraser_" + language));
        Save.setText(buttonTranslations.get("Save_" + language));
        Download.setText(buttonTranslations.get("Download_" + language));
        Undo.setText(buttonTranslations.get("Undo_" + language));
    }

    // Метод для обновления элементов ComboBox
    private void updateComboBox(ComboBox<String> comboBox, String key) {
        comboBox.getItems().setAll(comboBoxTranslations.get(key));
        comboBox.setValue(comboBoxTranslations.get(key)[0]); // Устанавливаем первый элемент по умолчанию
    }

    // Метод для обновления текста меток
    private void updateLabels(String language) {
        mouseCoordinatesLabel.setText(labelTranslations.get("MouseCoordinates_" + language));
        colorInCoordinatesLabel.setText(labelTranslations.get("ColorInCoordinates_" + language));
        drawingStatusLabel.setText(labelTranslations.get("DrawingStatus_" + language));
        shapeTypeLabel.setText(labelTranslations.get("ShapeType_" + language));
        eraserShapeTypeLabel.setText(labelTranslations.get("EraserShapeType_" + language));
        eraserSizeLabel.setText(labelTranslations.get("EraserSize_" + language));
    }
}