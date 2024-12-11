package com.example.grafredactor;

import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

public class RuEng {
    private Button toggleButton;
    private Button NewLine;
    private Button Eraser;
    private Button Save;
    private Button Download;
    private Button Undo;
    private ColorPicker colorPicker; // Добавляем ColorPicker

    RuEng(Button toggleButton, Button NewLine, Button Eraser, Button Save, Button Download, Button Undo, ColorPicker colorPicker) {
        this.toggleButton = toggleButton;
        this.NewLine = NewLine;
        this.Eraser = Eraser;
        this.Save = Save;
        this.Download = Download;
        this.Undo = Undo;
        this.colorPicker = colorPicker; // Инициализируем ColorPicker
    }

    public void Eng() {
        toggleButton.setText("RU");
        NewLine.setText("✎");
        Eraser.setText("⌫");
        Save.setText("\uD83D\uDCBE");
        Download.setText("\uD83D\uDCE5");
//        Undo.setText("Отмена"); // Обновляем текст кнопки "Undo"
        System.out.println("Language set to RUSSIA");
    }

    public void Ru() {
        toggleButton.setText("ENG");
        NewLine.setText("✎");
        Eraser.setText("⌫");
        Save.setText("\uD83D\uDCBE");
        Download.setText("\uD83D\uDCE5");
//        Undo.setText("Undo"); // Обновляем текст кнопки "Undo"
        System.out.println("Language set to English");
    }
}