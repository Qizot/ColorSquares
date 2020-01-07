package pl.qizot.ColorSquares;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


/**
 * Obstacle is an object that takes number of tiles to generate on map
 * All tiles are connected with each other creating one bigger object
 */
public class Obstacle {
    private static Color color = Color.BLACK;
    private static Random rand = new Random();
    private int n;
    private int width;
    private int height;

    private List<Tile> tiles = new ArrayList<>();
    private SquaresMap map;

    public Obstacle(int n, int width, int height, Vector2d startPosition, SquaresMap map) {
        this.n = n;
        this.map = map;
        this.width = width;
        this.height = height;
        putTile(startPosition);

        int i = 0;

        while(i < n && placeRandomTile()) i++;
    }

    public List<Tile> tiles() {
        return tiles;
    }

    private boolean placeRandomTile() {
        int height = map.getHeight();
        int width = map.getWidth();

        List<Vector2d> positions = tiles
            .stream()
            .map(this::availableNeighboursPositions)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        if (positions.isEmpty()) return false;

        Vector2d pos = positions.get(rand.nextInt(positions.size()));
        putTile(pos);
        return true;
    }

    private List<Vector2d> availableNeighboursPositions(Tile tile) {
        List<Vector2d> positions = new ArrayList<>();
        Vector2d pos = tile.getPosition();

        if (map.canBePlaced(pos.x + 1, pos.y)) {
            positions.add(new Vector2d(pos.x + 1, pos.y));
        }
        if (map.canBePlaced(pos.x - 1, pos.y)) {
            positions.add(new Vector2d(pos.x - 1, pos.y));
        }
        if (map.canBePlaced(pos.x, pos.y + 1)) {
            positions.add(new Vector2d(pos.x, pos.y + 1));
        }
        if (map.canBePlaced(pos.x , pos.y - 1)) {
            positions.add(new Vector2d(pos.x, pos.y - 1));
        }
        return positions;
    }

    private void putTile(Vector2d pos) {
        Tile tile = new Tile(pos.x, pos.y, width, height, color);
        map.placeSquare(tile);
        tiles.add(tile);
    }






}
