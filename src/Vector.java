public class Vector {
    private double x;
    private double y;

    public Vector(double inX, double inY) {
        x = inX;
        y = inY;
    }

    public Vector() {
        x = 0.0;
        y = 0.0;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double nx) {
        x = nx;
    }

    public void setY(double ny) {
        y = ny;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public void add(Vector v) {
        x += v.x;
        y += v.y;
    }

    public static Vector times(Vector v, double d) {
        return new Vector(v.x * d, v.y * d);
    }

    public static Vector add(Vector a, Vector b) {
        return new Vector(a.x + b.x, a.y + b.y);
    }

    public static Vector subtract(Vector a, Vector b) {
        return new Vector(a.x - b.x, a.y - b.y);
    }

    public static double mag(Vector a) {
        return Math.sqrt(a.x * a.x + a.y * a.y);
    }

    public double mag() {
        return mag(this);
    }

    public static double dotProduct(Vector a, Vector b) {
        return a.x * b.x + a.y * b.y;
    }

    public static double dist(Vector a, Vector b) {
        return mag(subtract(a, b));
    } 

    public static Vector unitTangent(Vector a, Vector b) {
        Vector u = unitNormal(a, b);
        return new Vector(-1 * u.y, u.x);
    }

    public static Vector unitNormal(Vector a, Vector b) {
        return unitVector(new Vector(b.x - a.x, b.y - a.y));
    }

    public static Vector unitVector(Vector a) {
        double mag = mag(a);
        return new Vector(a.x / mag, a.y / mag);
    }

    public void makeUnitVector() {
        double mag = mag();
        x = x / mag;
        y = y / mag;
    }

    @Override
    public Vector clone() {
        return new Vector(x, y);
    }

}
