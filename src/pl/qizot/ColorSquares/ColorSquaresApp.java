package pl.qizot.ColorSquares;


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ColorSquaresApp extends Application {

    private long animationTimeStep = 100 * 100_000_000;
    private final int  DEFAULT_TILE = 40;
    private int TILE_SIZE =  DEFAULT_TILE;

    private int W = 800;
    private int H = 600;

    private int X_TILES = W / TILE_SIZE;
    private int Y_TILES = H / TILE_SIZE;

    SquaresMap map;
    List<Player> players = new ArrayList<>();


    private Pane createContent() {
        Pane root = new Pane();
        root.setPrefSize(800, 600);

        return root;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        map = new SquaresMap(X_TILES, Y_TILES);
        Player player = new Player(Color.BLACK, TILE_SIZE, TILE_SIZE, map);
        player.placeTileAtRandom();
        player.generateTileWave();
        player.generateTileWave();
        player.generateTileWave();
        player.generateTileWave();
        player.generateTileWave();
        players.add(player);
        Pane root = createContent();
        Scene scene = new Scene(root);

        primaryStage.setTitle("ColorSquares");

        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0 ;
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= animationTimeStep) {
                    players.forEach(Player::generateTileWave);
                    update(root);
                    lastUpdate = now;
                }
            }
        };

        timer.start();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void update(Pane mapChart) {
        mapChart.getChildren().removeIf(n -> {
            return true;
        });
        draw(mapChart);
    }

    public void draw(Pane mapChart){
        map.getRecatngles().forEach(rect -> mapChart.getChildren().add(rect));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
