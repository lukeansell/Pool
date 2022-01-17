import java.awt.Color;

public class Ball {

    private Obj obj;
    private int num;
    private Color color = Color.WHITE;
    private boolean sunk = false;

    public Ball(Vector place, int inNum) {
        obj = new Obj(place, new Vector(), 26.25);
        num = inNum;
    }

    public Ball(Vector place, int inNum, Color inColor) {
        obj = new Obj(place, new Vector(), 26.25);
        num = inNum;
        color = inColor;
    }

    public Obj getObj() {
        return obj;
    }

    public int getNum() {
        return num;
    }

    public Color getColor() {
        return color;
    }

    public void setSunk(boolean in) {
        sunk = in;
    }

    public boolean getSunk() {
        return sunk;
    }

}
