package pl.qizot.ColorSquares;

import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SquaresMap {
    private int width;
    private int height;

    private Map<Vector2d, Tile> squares = new HashMap<>();

    public SquaresMap(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void placeSquare(Tile tile) {
        if (canBePlaced(tile.getPosition())) {
            squares.put(tile.getPosition(), tile);
            return;
        }
        if (!isInBounds(tile.getPosition())) {
            throw new IllegalArgumentException("Square position must be withing valid bounds of the map");
        }
        throw new IllegalArgumentException("Square cannot be placed on occupied position");
    }

    public void removeSquare(Vector2d pos) {
        squares.remove(pos);
    }

    public List<Rectangle> getRecatngles() {
        return squares.values().stream().map(Tile::getRect).collect(Collectors.toList());
    }

    public boolean canBePlaced(Vector2d pos) {
        return !squares.containsKey(pos) && isInBounds(pos);
    }

    public boolean canBePlaced(int x, int y) {
        Vector2d pos = new Vector2d(x, y);
        return canBePlaced(pos);
    }

    public boolean isOccupied(Vector2d pos) {
        return squares.containsKey(pos);
    }

    private boolean isInBounds(Vector2d pos) {
        return pos.x >= 0 && pos.x < width && pos.y >= 0 && pos.y < height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
