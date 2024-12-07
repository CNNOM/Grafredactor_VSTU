package com.example.grafredactor;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private List<Action> actions;

    public Model() {
        this.actions = new ArrayList<>();
    }

    // Конструктор копирования
    public Model(Model other) {
        this.actions = new ArrayList<>(other.actions);
    }

    public int getActionCount() {
        return actions.size();
    }

    public void addAction(Action action) {
        actions.add(action);
    }

    public void removeLastAction() {
        if (!actions.isEmpty()) {
            actions.remove(actions.size() - 1);
        }
    }

    public Action getAction(int i) {
        return this.actions.get(i);
    }

    public void deleteArray() {
        actions.clear();
    }
}