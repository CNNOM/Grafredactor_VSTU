package com.example.grafredactor;

import java.util.ArrayList;

public class Model {
    public  int pointCount;
    public ArrayList<Points> p;

    public Model() {

        this.pointCount = pointCount;
       this.p = new ArrayList<Points>();

    }

    public int getPointCount() {
        return p.size();//возвращает размер листа с точками
    }

    public void addPoint(Points point)
    {
        p.add(point);//добавляет точку в лист
    }
    public void  removePoint(Points point)
    {
        this.p.remove(point);
    }

    public Points getPoint(int i){
        return this.p.get(i);//вернет элемент по индексу
    }
    public  void  deleteArray()
    {
        p.clear();///полностью очишает массив с точками
    }

    public int serchPoint(int x, int y){
        int index = -1;//ищем точку по координатам
        for (int i = 0; i < this.p.size(); i++){
            if (this.p.get(i).x == x && this.p.get(i).y == y) index = i;
        }
        return index;
    }

}
