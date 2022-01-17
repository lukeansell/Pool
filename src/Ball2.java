import java.awt.Color;

public class Ball2 extends Obj {

    private int num;
    private Color color = Color.YELLOW;
    private boolean sunk = false;
    private boolean striped = false;

    public Ball2(Vector place, int inNum) {
        super(place, new Vector(), 26.25);
        num = inNum;
    }

    public Ball2(Vector place, int inNum, Color inC) {
        super(place, new Vector(), 26.25);
        num = inNum;
        color = inC;
    }

    public Ball2(Vector place, int inNum, Color inC, boolean inS) {
        super(place, new Vector(), 26.25);
        num = inNum;
        color = inC;
        striped = inS;
    }

    public int getNum() {
        return num;
    }

    public Color getColor() {
        return color;
    }

    public boolean getSunk() {
        return sunk;
    }

    public boolean getStriped(){
        return striped;
    }

    public void setSunk(boolean in) {
        sunk = in;
    }

}
