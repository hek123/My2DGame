package Utility;

public class Vector2D {
    public int x, y;

    public Vector2D() {
        x = y = 0;
    }

    public Vector2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(Vector2D vector) {
        x = vector.x;
        y = vector.y;
    }

    public void translate(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Vector2D vector) {
            return (x == vector.x) && (y == vector.y);
        }
        return super.equals(obj);
    }

    public String toString() {
        return getClass().getName() + "[x=" + x + ",y=" + y + "]";
    }

    static public Vector2D add(Vector2D v1, Vector2D v2) {
        return new Vector2D(v1.x + v2.x, v1.y + v2.y);
    }

    static public Vector2D sub(Vector2D v1, Vector2D v2) {
        return new Vector2D(v1.x - v2.x, v1.y - v2.y);
    }

    static public int ManhattanDistance(Vector2D v1, Vector2D v2) {
        return Math.abs(v1.x - v2.x) + Math.abs(v1.y - v2.y);
    }

    static public double EuclideanDistance(Vector2D v1, Vector2D v2) {
        return Math.sqrt(Math.pow(v1.x - v2.x, 2) + Math.pow(v1.y - v2.y, 2));
    }

    static public final Vector2D[] directions = getDirections(1);

    static public Vector2D[] getDirections(int step) {
        return new Vector2D[]{
                new Vector2D(-step, 0), new Vector2D(step, 0), new Vector2D(0, -step), new Vector2D(0, step)
        };
    }
}
