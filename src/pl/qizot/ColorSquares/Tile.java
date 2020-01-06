package pl.qizot.ColorSquares;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile {
    private Vector2d position;
    private Color color;

    private Rectangle rect;
    private boolean isValid;

    public Tile(int x, int y, int width, int height, Color color) {
        this.position = new Vector2d(x, y);
        this.color = color;
        this.isValid = true;

        rect = new Rectangle(width - 2, height - 2);
        rect.setX(x * width);
        rect.setY(y * height);
        rect.setFill(color);
    }

    public void setColor(Color color) {
        this.color = color;
        rect.setFill(color);
    }

    public Vector2d getPosition() {
        return position;
    }

    public Rectangle getRect() {
        return this.rect;
    }

    public void markInvalid() {
        this.isValid = false;
    }

    public void markValid() {
        this.isValid = true;
    }

    public boolean isValid() {
        return isValid;
    }

}
