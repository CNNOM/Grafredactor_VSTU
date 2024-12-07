package com.example.grafredactor;

import java.awt.*;

public class Points extends Point {
    private Color color;
    private double wp;
    private double hp;
    public void setColor(Color color) {
        this.color = color;
    }



    public Points(){
        this.color=Color.BLACK;
        this.wp=5;
        this.hp=5;
    }

    public Color getColor()
    {
        return  color;
    }

    public Points(int x, int y){
        super(x,y);

        this.color=Color.BLACK;
        this.wp=wp;
        this.hp=hp;
    }



    public double getwP() {
        return wp;
    }

    public void setwP(double wP) {
        this.wp = wp;
    }

    public double gethP() {
        return hp;
    }

    public void sethP(double hP) {
        this.hp = hp;
    }
    public Points(int x, int y, Color color, double wp, double hp) {
        super(x,y);
        this.color=Color.BLACK;
        this.wp=wp;
        this.hp=hp;

    }
    public  void  setSizePoint(double wp,double hp)
    {
        this.wp=wp;
        this.hp=hp;
    }


}