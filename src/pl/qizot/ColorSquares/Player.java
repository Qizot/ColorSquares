package pl.qizot.ColorSquares;

import javafx.scene.paint.Color;

import java.util.*;
import java.util.stream.Collectors;

public class Player {
    private static final Random rand = new Random();
    private Color color;
    private int tileWidth;
    private int tileHeight;
    Map<Vector2d, Tile> tiles = new LinkedHashMap<>();
    private SquaresMap map;


    public Player(Color color, int tileWidth, int tileHeight, SquaresMap map) {
        this.color = color;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.map = map;
    }

    public void startAt(Vector2d pos) {
        putTile(createTile(pos));
    }

    public void placeTileAtRandom() {
        int width = map.getWidth();
        int height = map.getHeight();


        Vector2d pos = map.getRandomEmptyPosition();
        putTile(createTile(pos));
    }

    public void clear() {
        tiles.keySet().stream().forEach(pos -> map.removeSquare(pos));
        tiles.clear();
    }


    /**
     * @return indicate whether any new tile has been placed while generating wave
     */
    public boolean generateTileWave() {
        List<Tile> validTiles = tiles.values().stream().filter(Tile::isValid).collect(Collectors.toList());
        if (validTiles.isEmpty()) return false;

        for (Tile tile: validTiles) {
            Vector2d pos = tile.getPosition();
            int x = pos.x;
            int y = pos.y;

            if (map.canBePlaced(x + 1, y)) {
                putTile(createTile(new Vector2d(x + 1, y)));
            }
            if (map.canBePlaced(x - 1, y)) {
                putTile(createTile(new Vector2d(x - 1, y)));
            }
            if (map.canBePlaced(x, y + 1)) {
                putTile(createTile(new Vector2d(x, y + 1)));
            }
            if (map.canBePlaced(x, y - 1)) {
                putTile(createTile(new Vector2d(x, y - 1)));
            }
            tile.markInvalid();
        }
        return true;
    }

    private void putTile(Tile tile) {
        map.placeSquare(tile);
        tiles.put(tile.getPosition(), tile);
    }

    private Tile createTile(Vector2d position) {
        return new Tile(position.x, position.y, tileWidth, tileHeight, color);
    }

    public void clearTiles() {
        tiles.clear();
    }

    public List<Tile> getTiles() {
        return new ArrayList<>(tiles.values());
    }

    @Override
    public String toString() {
        return color.toString();
    }
}
