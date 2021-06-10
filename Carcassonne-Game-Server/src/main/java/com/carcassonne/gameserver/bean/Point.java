package com.carcassonne.gameserver.bean;

import java.util.Objects;

public class Point {
    private int x;
    private int y;

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public void setPoint(int nx,int ny){
        x=nx;
        y=ny;
    }
    public String judgePosition(Point comparePoint){
        int x_comparePoint = comparePoint.getX()-x;
        int y_comparePoint = comparePoint.getY()-y;
        switch (x_comparePoint){
            case -1:return "left";
            case 1:return "right";
            default:break;
        }
        switch (y_comparePoint){
            case -1:return "bottom";
            case 1:return "top";
            default:break;
        }
        return "" ;
    }

}
