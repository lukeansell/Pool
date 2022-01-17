import java.awt.Color;

public class App {
    public static void main(String[] args) throws Exception {
        // Obj white = new Obj(new Vector(), new Vector(), 26.25);
        Ball2 white = new Ball2(new Vector(480, 915), 0, Color.WHITE);
        // Ball2 w = new Ball2(new Vector(480, 457.5), 0, Color.WHITE);
        double center = 480;
        double r = 26.25;
        // row 1
        double row1 = 1372.5 + r;
        Ball2 b01 = new Ball2(new Vector(center, 1372.5 + r), 1, Color.YELLOW);
        // row 2
        double row2 = row1 + ((r + 2) / Math.tan(Math.toRadians(30)));
        Ball2 b02 = new Ball2(new Vector(center - r - 2.5, row2), 2,
                Color.CYAN);
        Ball2 b03 = new Ball2(new Vector(center + r + 2.5, row2), 3,
                Color.RED);
        // row 3
        double row3 = row2 + ((r + 2) / Math.tan(Math.toRadians(30)));
        Ball2 b04 = new Ball2(new Vector(center - r * 2 - 4, row3), 4, Color.BLUE);
        Ball2 b05 = new Ball2(new Vector(center, row3), 5, Color.ORANGE);
        Ball2 b06 = new Ball2(new Vector(center + r * 2 + 4, row3), 6, Color.GREEN);
        // row 4
        double row4 = row3 + ((r + 2) / Math.tan(Math.toRadians(30)));
        Ball2 b07 = new Ball2(new Vector(center - r * 3 - 8.0, row4), 7, Color.RED);
        Ball2 b08 = new Ball2(new Vector(center - r * 1 - 2.5, row4), 8, Color.BLACK);
        Ball2 b09 = new Ball2(new Vector(center + r * 1 + 2.5, row4), 9, Color.YELLOW, true);
        Ball2 b10 = new Ball2(new Vector(center + r * 3 + 8.0, row4), 10, Color.CYAN, true);
        // row 5
        double row5 = row4 + ((r + 2) / Math.tan(Math.toRadians(30)));
        Ball2 b11 = new Ball2(new Vector(center - r * 4 - 8.0, row5), 11, Color.RED, true);
        Ball2 b12 = new Ball2(new Vector(center - r * 2 - 4.0, row5), 12, Color.BLUE, true);
        Ball2 b13 = new Ball2(new Vector(center, row5), 13, Color.ORANGE, true);
        Ball2 b14 = new Ball2(new Vector(center + r * 2 + 4.0, row5), 14, Color.ORANGE, true);
        Ball2 b15 = new Ball2(new Vector(center + r * 4 + 8.0, row5), 15, Color.GREEN, true);

        Ball2[] balls = { white, b01, b02, b03, b04, b05, b06, b07, b08, b09, b10, b11, b12, b13, b14, b15 };
        Table table = new Table(balls);
        table.start();

        // 910 x 1830 (mm)
    }
}
