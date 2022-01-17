public class Obj {
    private Vector pos = new Vector();
    private Vector vel = new Vector();
    private Vector acc = new Vector();
    private final double r;
    private double mass = 1.0;

    public Obj(Vector inPos, Vector inVel, double inR) {
        pos = inPos;
        vel = inVel;
        r = inR;
    }

    public Obj(Vector inPos, Vector inVel, double inR, double inMass) {
        pos = inPos;
        vel = inVel;
        r = inR;
        mass = inMass;
    }

    public Obj(Vector inPos, Vector inVel, double inR, double inMass, Vector inAcc) {
        pos = inPos;
        vel = inVel;
        r = inR;
        mass = inMass;
        acc = inAcc;
    }

    public Obj(Vector inPos, Vector inVel, Vector inAcc, double inR, double inMass) {
        pos = inPos;
        vel = inVel;
        r = inR;
        mass = inMass;
        acc = inAcc;
    }

    /**
     * Updates the position vector according to the velocity vector and time
     * 
     * @param time time passed in seconds
     */
    public void updatePos(double time) {
        pos.add(Vector.times(vel, time));
    }

    /**
     * Updates the velocity vector according to the acceleration vector and time
     * 
     * @param time time passed in seconds
     */
    public void updateVel(double time) {
        vel.add(Vector.times(acc, time));
    }

    /**
     * @return Vector
     */
    public Vector getPos() {
        return pos;
    }

    /**
     * @return Vector
     */
    public Vector getVel() {
        return vel;
    }

    /**
     * @return Vector
     */
    public Vector getAcc() {
        return acc;
    }

    /**
     * @return double
     */
    public double getR() {
        return r;
    }

    /**
     * @return double
     */
    public double getMass() {
        return mass;
    }

    /**
     * @return double
     */
    public double getMom() {
        return mass * vel.mag();
    }

    /**
     * @param nPos
     */
    public void setPos(Vector nPos) {
        pos = nPos;
    }

    /**
     * @param nVel
     */
    public void setVel(Vector nVel) {
        vel = nVel;
    }

    /**
     * @param nAcc
     */
    public void setAcc(Vector nAcc) {
        acc = nAcc;
    }

    /**
     * @return String Pos: <pos> Vel: <vel> Acc: <acc> r: <r> mass: <mass>
     */
    @Override
    public String toString() {
        return "Pos: " + pos + " Vel: " + vel + " Acc: " + acc + " r: " + r + " mass: " + mass;
    }

    /**
     * Checks if the given Obj have collided. If the distance between them is less
     * than or equal to the sum of their radii
     * 
     * @param a
     * @param b
     * @return boolean true if collided
     */
    public static boolean collide(Obj a, Obj b) {
        double l = a.r + b.r;
        double dist = Vector.dist(a.pos, b.pos);
        return l >= dist;
    }

    /**
     * @param a
     * @param size assumes the area has x E (0, size) & y E (0,size)
     * @return boolean
     */
    public static boolean collideWall(Obj a, int size) {
        return (a.pos.getX() - a.r) <= 0 || (a.pos.getY() - a.r) <= 0 || (a.pos.getX() + a.r) >= size
                || (a.pos.getY() + a.r) >= size;
    }

    /**
     * @param size
     * @return boolean
     */
    public boolean collideWall(int size) {
        return collideWall(this, size);
    }

    /**
     * @param size
     * @return boolean
     */
    public boolean collideWallX(int size) {
        return (pos.getX() - r) <= 0 || (pos.getX() + r) >= size;
    }

    public boolean collideWallX(int[] xbounds) {
        return (pos.getX() - r) <= xbounds[0] || (pos.getX() + r) >= xbounds[1];
    }

    /**
     * @param size
     * @return boolean
     */
    public boolean collideWallY(int size) {
        return (pos.getY() - r) <= 0 || (pos.getY() + r) >= size;
    }

    public boolean collideWallY(int[] ybounds) {
        return (pos.getY() - r) <= ybounds[0] || (pos.getY() + r) >= ybounds[1];
    }

    /**
     * @param bounds
     */
    public void boundsFix(int[][] bounds) {
        // y top
        if (pos.getY() > bounds[1][1] - r)
            pos.setY(bounds[1][1] - r);
        // y bot
        else if (pos.getY() < r + bounds[1][0])
            pos.setY(r + bounds[1][0]);
        // x top
        if (pos.getX() > bounds[0][1] - r)
            pos.setX(bounds[0][1] - r);
        // x bot
        else if (pos.getX() < r + bounds[0][0])
            pos.setX(r + bounds[0][0]);
    }

    /**
     * @param a
     * @param b
     */
    public static void elasticCollision(Obj a, Obj b) {
        Vector un = Vector.unitNormal(a.pos, b.pos);
        Vector ut = Vector.unitTangent(a.pos, b.pos);

        double v1n = Vector.dotProduct(un, a.vel);
        double v1t = Vector.dotProduct(ut, a.vel);
        double v2n = Vector.dotProduct(un, b.vel);
        double v2t = Vector.dotProduct(ut, b.vel);

        double v1tp = v1t;
        double v2tp = v2t;

        double top = v1n * (a.mass - b.mass) + 2 * b.mass * v2n;
        double bot = a.mass + b.mass;
        double v1np = top / bot;

        top = v2n * (b.mass - a.mass) + 2 * a.mass * v1n;
        bot = a.mass + b.mass;
        double v2np = top / bot;

        // convert to vectors
        Vector v1npV = Vector.times(un, v1np);
        Vector v1tpV = Vector.times(ut, v1tp);
        Vector v2npV = Vector.times(un, v2np);
        Vector v2tpV = Vector.times(ut, v2tp);

        Vector v1p = Vector.add(v1npV, v1tpV);
        Vector v2p = Vector.add(v2npV, v2tpV);

        a.vel = v1p;
        b.vel = v2p;
    }

}
