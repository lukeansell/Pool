import java.awt.Color;

public class Table {
    private Obj[] obs;
    private Color[] colors = { StdDraw.BLUE, StdDraw.BOOK_RED, StdDraw.CYAN, StdDraw.DARK_GRAY, StdDraw.GREEN,
            StdDraw.MAGENTA, StdDraw.ORANGE, StdDraw.PINK, StdDraw.PRINCETON_ORANGE, StdDraw.RED, StdDraw.YELLOW };
    private int size = 100;
    private int[][] bounds = { { 0, 910 }, { 0, 1830 } };
    private double spt = 0.003;
    private Stopwatch lUpdate;
    private Stopwatch sw;
    private int updates = 0;
    private double maxTime = 0.0;
    private boolean started = false;

    private double[] massRange;

    private Ball2[] balls;

    private double sockRadius = 40;

    private boolean won = false;

    public Table(Obj[] inObs) {
        obs = inObs;
        if (obs.length > colors.length)
            System.out.println("Warning: more objects than there are colors");
    }

    public Table(Ball2[] inBalls) {
        balls = inBalls.clone();
    }

    public void start() {
        started = true;
        initDraw();
        // info(obs);
        updateDraw();
        sw = new Stopwatch();
        lUpdate = new Stopwatch();
        while (!won) {
            turnInput();
            doP();
            whiteBallFix();
            printBalls();
        }
        printBalls();
        // turnInput();
        // doP();
        endPrint();
    }

    private void whiteBallFix() {
        if (balls[0].getSunk()) {
            balls[0].setPos(new Vector(480, 457.5));
            balls[0].setSunk(false);
            updateDraw();
        }
    }

    private void doP() {
        // sw = new Stopwatch();
        lUpdate = new Stopwatch();
        // balls[0].getObj().setVel(new Vector(100, 0));
        while (!(StdDraw.isKeyPressed(27) || ballsStill()))
            // if (lUpdate.elapsedTime() >= spt)
            update();

        if (StdDraw.isKeyPressed(27))
            endPrint();

    }

    private boolean ballsStill() {
        for (Ball2 ball : balls)
            if (ball.getVel().mag() > 0.0)
                return false;

        return true;
    }

    private void turnInput() {
        System.out.println("Selecting direction");
        double angle = 0;
        System.out.println("angle: " + angle);
        while (!StdDraw.isKeyPressed(10)) {
            if (StdDraw.isKeyPressed(38)) {
                angle += 10;
                System.out.println("angle: " + angle);
                while (StdDraw.isKeyPressed(38)) {
                }
            }
            if (StdDraw.isKeyPressed(40)) {
                angle -= 10;
                System.out.println("angle: " + angle);
                while (StdDraw.isKeyPressed(40)) {
                }
            }
            if (StdDraw.isKeyPressed(27)) {
                endPrint();
            }
        }
        System.out.println("Selected angle: " + angle);
        while (StdDraw.isKeyPressed(10)) {
        }
        ;
        System.out.println("Selecting vel");
        double vel = 250;
        System.out.println("vel: " + vel);
        while (!StdDraw.isKeyPressed(10)) {
            if (StdDraw.isKeyPressed(38)) {
                vel += 10;
                System.out.println("vel: " + vel);
                while (StdDraw.isKeyPressed(38)) {
                }
            }
            if (StdDraw.isKeyPressed(40)) {
                vel -= 10;
                System.out.println("vel: " + vel);
                while (StdDraw.isKeyPressed(40)) {
                }
            }
            if (StdDraw.isKeyPressed(27)) {
                endPrint();
            }
        }
        System.out.println("Selected vel: " + vel);
        Vector v = new Vector(vel * Math.cos(Math.toRadians(angle)), vel * Math.sin(Math.toRadians(angle)));
        // System.out.println("Vector: " + v);
        balls[0].setVel(v);
    }

    public void info(Obj[] obs) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < obs.length; i++)
            sb.append(i + " " + obs[i] + "\n");
        System.out.println(sb);
    }

    public void printBalls() {
        StringBuilder sb = new StringBuilder();
        for (Ball2 ball : balls) {
            sb.append(ball.getNum() + ": sunk " + ball.getSunk() + "\n");
        }
        System.out.print(sb);
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

    private void drawTable() {
        StdDraw.setPenColor(StdDraw.BLACK);
        // bottom two
        StdDraw.filledCircle(bounds[0][0], bounds[1][0], sockRadius);
        StdDraw.filledCircle(bounds[0][1], 0, sockRadius);
        // middle two
        StdDraw.filledCircle(bounds[0][0], bounds[1][1] / 2.0, sockRadius);
        StdDraw.filledCircle(bounds[0][1], bounds[1][1] / 2.0, sockRadius);
        // top two
        StdDraw.filledCircle(bounds[0][0], bounds[1][1], sockRadius);
        StdDraw.filledCircle(bounds[0][1], bounds[1][1], sockRadius);
    }

    private void update() {
        sunkBalls();
        // fix stuck in wall obs
        wallStuck();
        // collisons
        updateC();

        double t = lUpdate.elapsedTime();
        lUpdate = new Stopwatch();

        // add velocity to pos
        updatePos(t);

        // update velocity
        updateVel(t);

        // update display
        updateDraw();
        // reset update time
        updates++;
    }

    private void sunkBalls() {
        Vector[] socks = { new Vector(bounds[0][0], bounds[1][0]), new Vector(bounds[0][1], 0),
                new Vector(bounds[0][0], bounds[1][1] / 2.0), new Vector(bounds[0][1], bounds[1][1] / 2.0),
                new Vector(bounds[0][0], bounds[1][1]), new Vector(bounds[0][1], bounds[1][1]) };
        for (Ball2 ball : balls)
            if (!ball.getSunk())
                for (Vector sock : socks)
                    if (Vector.dist(ball.getPos(), sock) < sockRadius) {
                        ball.setSunk(true);
                        ball.setVel(new Vector());
                    }

    }

    private void updateC() {
        // wall collisons
        for (Ball2 ball : balls)
            if (ball.collideWallX(bounds[0]))
                ball.getVel().setX(ball.getVel().getX() * -1);
            else if (ball.collideWallY(bounds[1]))
                ball.getVel().setY(ball.getVel().getY() * -1);

        for (int i = 0; i < balls.length; i++)
            for (int j = i + 1; j < balls.length; j++)
                if (Obj.collide(balls[i], balls[j]))
                    Obj.elasticCollision(balls[i], balls[j]);
    }

    private void updatePos(double time) {
        // add velocity to pos
        for (Ball2 ball : balls)
            ball.updatePos(time);
    }

    private void updateVel(double time) {

        for (Ball2 ball : balls) {
            if (ball.getVel().mag() > 0) {
                // Obj obj = ball;
                Vector d = Vector.unitVector(ball.getVel());
                // accerlation due to friction
                Vector da = Vector.times(d, -5 * time);
                Vector vel = Vector.add(ball.getVel(), da);
                if (ball.getVel().mag() < da.mag())
                    vel = new Vector();

                ball.setVel(vel);
            }
        }
    }

    private void wallStuck() {
        for (Ball2 ball : balls)
            ball.boundsFix(bounds);
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

    private void updateDraw() {
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.filledRectangle((bounds[0][0] + bounds[0][1]) / 2, (bounds[1][0] + bounds[1][1]) / 2,
                (bounds[0][1] - bounds[0][0]) / 2, (bounds[1][1] - bounds[1][0]) / 2);
        drawTable();
        for (Ball2 ball : balls) {
            if (!ball.getSunk())
                drawBall(ball);
        }
        StdDraw.show();
    }

    private void drawBall(Ball2 ball) {
        // Obj ob = ball.getObj();
        double x = ball.getPos().getX();
        double y = ball.getPos().getY();
        StdDraw.setPenColor(ball.getColor());
        if (ball.getStriped())
            StdDraw.filledCircle(x, y, ball.getR() / 2.0);
        else
            StdDraw.filledCircle(x, y, ball.getR());
        StdDraw.setPenColor(StdDraw.BLACK);
        // StdDraw.filledCircle(x, y, 0.2);
        StdDraw.circle(x, y, ball.getR());
        // StdDraw.setPenColor(StdDraw);
        StdDraw.text(x, y, ball.getNum() + "");

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
