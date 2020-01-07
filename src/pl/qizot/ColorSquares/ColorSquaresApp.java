package pl.qizot.ColorSquares;


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.*;
import java.util.stream.Collectors;

public class ColorSquaresApp extends Application {

    private long animationTimeStep = 50 * 100_000_000;
    private final int  DEFAULT_TILE = 40;
    private int TILE_SIZE =  DEFAULT_TILE;

    private int W = 800;
    private int H = 600;

    private int X_TILES = W / TILE_SIZE;
    private int Y_TILES = H / TILE_SIZE;

    SquaresMap map;
    Random rand = new Random();
    Set<Player> players = new HashSet<>();
    List<Obstacle> obstacles = new ArrayList<>();
    Player humanPlayer;
    private boolean isRunning = false;
    private boolean isFinished = false;

    Pane squares;

    private void placePlayer(Vector2d pos) {
        if (map.canBePlaced(pos)) {
            humanPlayer.clear();
            humanPlayer.startAt(pos);
            players.add(humanPlayer);
            update(squares);
        } else {
            System.out.println(" cant place player");
        }
    }

    private Pane createContent() {
        Pane root = new Pane();
        root.setPrefSize(800, 600);

        root.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!isRunning && !isFinished) {
                    int x = (int)event.getX() / TILE_SIZE;
                    int y = (int)event.getY() / TILE_SIZE;
                    placePlayer(new Vector2d(x, y));
                }
            }
        });
        return root;
    }

    private void startGame() {
        isRunning = true;
    }

    private void resetGame() {
        map.reset();
        players.clear();
        obstacles.clear();
        humanPlayer.clear();
        placeEnemyPlayers(4);
        placeObstacles(5);
        isRunning = false;
        update(squares);
    }

    void showScores() {
//        Stage dialog = new Stage();
//
//
//
//        dialog.initOwner(parentStage);
//        dialog.initModality(Modality.APPLICATION_MODAL);
//        dialog.showAndWait();
    }

    private HBox actionButtons() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #336699;");

        Button start = new Button("Start game");
        start.setPrefSize(100, 20);
        start.setOnMouseClicked(e -> startGame());

        Button reset = new Button("Reset");
        reset.setPrefSize(100, 20);
        reset.setOnMouseClicked(e -> resetGame());


        hbox.getChildren().addAll(start, reset);

        return hbox;
    }

    private void placeEnemyPlayers(int n) {
        for (int i = 0; i < n; i++) {
            double r = rand.nextDouble();
            double g = rand.nextDouble();
            double b = rand.nextDouble();

            Color randomColor = new Color(r, g, b, 1.0);

            Player player = new Player(randomColor, TILE_SIZE, TILE_SIZE, map);
            player.placeTileAtRandom();
            players.add(player);
        }
    }

    private void placeObstacles(int n) {
        for (int i = 0; i < n; i++) {
            Vector2d pos = map.getRandomEmptyPosition();
            Obstacle o1 = new Obstacle(rand.nextInt(6), TILE_SIZE, TILE_SIZE, pos, map);
            obstacles.add(o1);
        }
    }

    private void setup() {
        map = new SquaresMap(X_TILES, Y_TILES);

        double r = rand.nextDouble();
        double g = rand.nextDouble();
        double b = rand.nextDouble();

        Color randomColor = new Color(r, g, b, 1.0);
        humanPlayer = new Player(randomColor, TILE_SIZE, TILE_SIZE, map);
        players.add(humanPlayer);

        placeEnemyPlayers(4);
        placeObstacles(5);

    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        setup();

        BorderPane pane = new BorderPane();

        squares = createContent();
        update(squares);

        pane.setTop(actionButtons());
        pane.setCenter(squares);
        Scene scene = new Scene(pane);

        primaryStage.setTitle("ColorSquares");

        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;
            boolean hasBeenChanged = false;
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= animationTimeStep && isRunning) {
                    hasBeenChanged = false;
                    List<Player> toWave = players.stream().collect(Collectors.toList());
                    Collections.shuffle(toWave, rand);
                    toWave.forEach(player -> {
                       hasBeenChanged = player.generateTileWave() || hasBeenChanged;
                    });
                    if (!hasBeenChanged) {
                        isRunning = false;
                        isFinished = true;
                    }
                    update(squares);
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
        map.getRectangles().forEach(rect -> mapChart.getChildren().add(rect));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
