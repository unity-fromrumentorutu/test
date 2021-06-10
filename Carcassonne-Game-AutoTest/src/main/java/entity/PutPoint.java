package entity;

public class PutPoint {

    private Integer x;
    private Integer y;
    private Integer rotation;


    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getRotation() {
        return rotation;
    }

    public void setRotation(Integer rotation) {
        this.rotation = rotation;
    }

    public PutPoint(Integer x, Integer y, Integer rotation) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
    }

    @Override
    public String toString() {
        return "PutPoint{" +
                "x=" + x +
                ", y=" + y +
                ", rotation=" + rotation +
                '}';
    }
}
