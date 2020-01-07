package pl.qizot.ColorSquares;

import javafx.scene.shape.Rectangle;

import java.util.*;
import java.util.stream.Collectors;

public class SquaresMap {
    private static Random rand = new Random();
    private int width;
    private int height;

    private Map<Vector2d, Tile> squares = new HashMap<>();
    private Set<Vector2d> emptyPositions = new HashSet<>();

    public SquaresMap(int width, int height) {
        this.width = width;
        this.height = height;

        for (int i = 0;  i < width; i++) {
            for (int j = 0; j < height; j++) {
                emptyPositions.add(new Vector2d(i, j));
            }
        }
    }

    public void placeSquare(Tile tile) {
        if (canBePlaced(tile.getPosition())) {
            squares.put(tile.getPosition(), tile);
            emptyPositions.remove(tile.getPosition());
            return;
        }
        if (!isInBounds(tile.getPosition())) {
            throw new IllegalArgumentException("Square position must be withing valid bounds of the map");
        }
        throw new IllegalArgumentException("Square cannot be placed on occupied position");
    }

    public void reset() {
        squares.clear();
        emptyPositions.clear();

        for (int i = 0;  i < width; i++) {
            for (int j = 0; j < height; j++) {
                emptyPositions.add(new Vector2d(i, j));
            }
        }
    }

    public Vector2d getRandomEmptyPosition() {
        if (emptyPositions.isEmpty()) return  null;
        return new ArrayList<>(emptyPositions).get(rand.nextInt(emptyPositions.size()));
    }

    public void removeSquare(Vector2d pos) {
        emptyPositions.add(pos);
        squares.remove(pos);
    }

    public List<Rectangle> getRectangles() {
        return squares.values().stream().map(Tile::getRect).collect(Collectors.toList());
    }

    public boolean canBePlaced(Vector2d pos) {
        return emptyPositions.contains(pos);
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
