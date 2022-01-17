
import java.awt.Color;

public class Sim {
    private Obj[] obs;
    private Color[] colors = { StdDraw.BLUE, StdDraw.BOOK_RED, StdDraw.CYAN, StdDraw.DARK_GRAY, StdDraw.GREEN,
            StdDraw.MAGENTA, StdDraw.ORANGE, StdDraw.PINK, StdDraw.PRINCETON_ORANGE, StdDraw.RED, StdDraw.YELLOW };
    private int size = 100;
    private int[][] bounds = { { 0, 200 }, { 0, 200 } };
    private double spt = 0.003;
    private Stopwatch lUpdate;
    private Stopwatch sw;
    private int updates = 0;
    private double maxTime = 0.0;
    private boolean started = false;

    private double[] massRange;

    public Sim(Obj[] inObs) {
        obs = inObs;
        if (obs.length > colors.length)
            System.out.println("Warning: more objects than there are colors");
    }

    public void start() {
        started = true;
        initDraw();
        info(obs);
        sw = new Stopwatch();
        lUpdate = new Stopwatch();
        while (!StdDraw.isKeyPressed(27) && (maxTime == 0.0 || sw.elapsedTime() < maxTime)) {
            if (lUpdate.elapsedTime() >= spt) {
                update(obs, colors);
                if (updates % 500 == 0) {
                    System.out.println("Avg fps: " + (updates / sw.elapsedTime()));
                }
            }
        }
        info(obs);
        endPrint();
    }

    public void info(Obj[] obs) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < obs.length; i++)
            sb.append(i + " " + obs[i] + "\n");
        System.out.println(sb);
    }

    public void setMaxTime(double t) {
        maxTime = t;
    }

    public void setTps(double d) {
        spt = 1.0 / d;
    }

    public void setSpt(double d) {
        spt = d;
    }

    public void setSize(int inSize) {
        if (started)
            System.out.println("cannot change size after starting");
        else
            size = inSize;
    }

    public void setMassRange(double[] inMassRange) {
        massRange = inMassRange;
    }

    public void setBounds(int[][] nB) {
        bounds[0][0] = nB[0][0];
        bounds[0][1] = nB[0][1];
        bounds[1][0] = nB[1][0];
        bounds[1][1] = nB[1][1];
    }

    private void update(Obj[] obs, Color[] colors) {
        // fix stuck in wall obs
        wallStuck(obs);
        // collisons
        updateC(obs);

        double t = lUpdate.elapsedTime();
        lUpdate = new Stopwatch();

        // add velocity to pos
        updatePos(obs, t);

        // update velocity
        updateVel(obs, t);
        // wallStuck(obs);
        // updateC(obs);

        // update display
        updateDraw(obs, colors);
        // reset update time
        updates++;
    }

    private void updateC(Obj[] obs) {
        // wall collisons
        for (Obj ob : obs)
            if (ob.collideWallX(bounds[0]))
                ob.getVel().setX(ob.getVel().getX() * -1);
            else if (ob.collideWallY(bounds[1]))
                ob.getVel().setY(ob.getVel().getY() * -1);

        for (int i = 0; i < obs.length; i++)
            for (int j = i + 1; j < obs.length; j++)
                if (Obj.collide(obs[i], obs[j]))
                    Obj.elasticCollision(obs[i], obs[j]);
    }

    private void updatePos(Obj[] obs, double time) {
        // add velocity to pos
        for (Obj ob : obs)
            ob.updatePos(time);
        // ob.getPos().add(Vector.times(ob.getVel(), t));
    }

    private void updateVel(Obj[] obs, double time) {
        for (Obj ob : obs)
            ob.updateVel(time);
    }

    private void wallStuck(Obj[] obs) {
        for (Obj ob : obs)
            ob.boundsFix(bounds);
        // y top
        // if (ob.getPos().getY() > size - ob.getR())
        // ob.getPos().setY(size - ob.getR());
        // // y bot
        // if (ob.getPos().getY() < 1 * ob.getR())
        // ob.getPos().setY(ob.getR());
        // // x top
        // if (ob.getPos().getX() > size - ob.getR())
        // ob.getPos().setX(size - ob.getR());
        // // x bot
        // if (ob.getPos().getX() < 1 * ob.getR())
        // ob.getPos().setX(ob.getR());

    }

    private void updateDraw(Obj[] obs, Color[] colors) {
        StdDraw.setPenColor(StdDraw.WHITE);
        // StdDraw.filledSquare(bounds[0][1] / 2, size / 2, size / 2);
        StdDraw.filledRectangle((bounds[0][0] + bounds[0][1]) / 2, (bounds[1][0] + bounds[1][1]) / 2,
                (bounds[0][1] - bounds[0][0]) / 2, (bounds[1][1] - bounds[1][0]) / 2);
        // drawGridLines();
        drawObj(obs, colors);
        StdDraw.show();
    }

    private void drawGridLines() {
        int s = 10;
        double d = size / s;
        StdDraw.setPenColor(StdDraw.BLACK);
        for (int i = 0; i < s; i++) {
            StdDraw.line(i * (bounds[0][1] - bounds[0][0]) / s, bounds[1][0], i * (bounds[0][1] - bounds[0][0]) / s,
                    bounds[1][1]);
            StdDraw.line(bounds[0][0], i * (bounds[1][1] - bounds[1][0]) / s, bounds[0][1],
                    i * (bounds[1][1] - bounds[1][0]) / s);
        }

        // for (int i = 0; i < s; i++)
        // StdDraw.line(i * d, bounds[1][0], i * d, bounds[1][1]);

        // for (int i = 0; i < s; i++)
        // StdDraw.line(bounds[0][0], i * d, bounds[0][1], i * d);

    }

    private void drawObj(Obj[] obs, Color[] colors) {
        for (int i = 0; i < obs.length; i++)
            drawObj(obs[i], colors[i % colors.length]);
    }

    private void drawObj(Obj ob, Color c) {
        double x = ob.getPos().getX();
        double y = ob.getPos().getY();
        StdDraw.setPenColor(c);
        StdDraw.filledCircle(x, y, ob.getR());
        StdDraw.setPenColor(StdDraw.BLACK);
        // StdDraw.filledCircle(x, y, 0.2);
        StdDraw.circle(x, y, ob.getR());
        if (massRange != null) {
            double r = ob.getR() * 0.75;
            StdDraw.filledCircle(x, y, ob.getMass() / massRange[1] * r);
        }
    }

    private void initDraw() {
        StdDraw.enableDoubleBuffering();
        StdDraw.pause(100);
        StdDraw.setScale(0, 100);
        StdDraw.setCanvasSize(400, 800);
        StdDraw.setXscale(Math.min(bounds[0][0], 0), Math.max(bounds[0][1], 100));
        StdDraw.setYscale(Math.min(bounds[1][0], 0), Math.max(bounds[1][1], 100));
        StdDraw.show();
    }

    private void endPrint() {
        double t = sw.elapsedTime();
        StringBuilder sb = new StringBuilder();
        sb.append("Ended \n");
        sb.append("Time: " + t + " Updates: " + updates + " \n");
        sb.append("Avg fps: " + (updates / t));
        System.out.println(sb);
        StdDraw.pause(50);
        System.exit(0);
    }

}
