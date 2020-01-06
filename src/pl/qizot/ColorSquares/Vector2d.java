package pl.qizot.ColorSquares;

import java.util.Objects;

public class Vector2d {
    public final int x;
    public final int y;

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return String.format("(%s, %s)", x, y);
    }

    public boolean precedes(Vector2d other) {
        return x <= other.x && y <= other.y;
    }

    public boolean follows(Vector2d other) {
        return x >= other.x && y >= other.y;
    }

    public Vector2d upperRight(Vector2d other) {
        if (other == null) return this;
        return new Vector2d(Math.max(x, other.x), Math.max(y, other.y));
    }

    public Vector2d lowerLeft(Vector2d other) {
        if (other == null) return this;
        return new Vector2d(Math.min(x, other.x), Math.min(y, other.y));
    }

    public Vector2d add(Vector2d other) {
        if (other == null) return this;
        return new Vector2d(x + other.x, y + other.y);
    }

    public Vector2d subtract(Vector2d other) {
        if (other == null) return this;
        return new Vector2d(x - other.x, y - other.y);
    }

    public Vector2d opposite() {
        return new Vector2d(-x, -y);
    }

    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null) return false;
        if(!(other instanceof Vector2d)) return false;
        Vector2d vector = (Vector2d)other;
        return Objects.equals(x, vector.x) && Objects.equals(y, vector.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
