package pl.qizot.ColorSquares;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class ColorSquaresApp extends Application {

    private long animationTimeStep = 100_000_000;
    private final int  DEFAULT_TILE = 40;
    private int TILE_SIZE_X = DEFAULT_TILE;
    private int TILE_SIZE_Y = DEFAULT_TILE;

    private int W = 800;
    private int H = 600;

    private int X_TILES = W / TILE_SIZE_X;
    private int Y_TILES = H / TILE_SIZE_Y;

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

    private Pair<String, String> getBoardSize() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Board size");

        // Set the button types.
        ButtonType accept = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(accept);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField xTiles = new TextField();
        xTiles.setPromptText("X tiles");
        TextField yTiles = new TextField();
        yTiles.setPromptText("Y tiles");

        gridPane.add(xTiles, 0, 0);
        gridPane.add(new Label("To:"), 1, 0);
        gridPane.add(yTiles, 2, 0);

        dialog.getDialogPane().setContent(gridPane);

        // Request focus on the username field by default.

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == accept) {
                return new Pair<>(xTiles.getText(), yTiles.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(pair -> {
            System.out.println("From=" + pair.getKey() + ", To=" + pair.getValue());
        });
        return result.orElse(null);
    }

    private GridPane showScores() {


        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));
        List<Player> ps = players.stream().collect(Collectors.toList());
        ps.sort((Player a, Player b) -> -Integer.compare(a.getTiles().size(), b.getTiles().size()));

        for (int i = 0; i < ps.size(); i++) {
            HBox box = new HBox();
            box.getChildren().add(new Label(String.format("%s) %s", i + 1, ps.get(i).getTiles().size())));
            if (ps.get(i).equals(humanPlayer)) {
                box.getChildren().add(new Label(" You "));
            }
            box.getChildren().add(new Rectangle(10, 10, ps.get(i).getColor()));

            gridPane.add(box, 0, i);

        }
        return gridPane;
    }

    private Pane createContent() {
        Pane root = new Pane();
        root.setPrefSize(800, 600);

        root.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!isRunning && !isFinished) {
                    int x = (int)event.getX() / TILE_SIZE_X;
                    int y = (int)event.getY() / TILE_SIZE_Y;
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
        isFinished = false;
        update(squares);
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

            Player player = new Player(randomColor, TILE_SIZE_X, TILE_SIZE_Y, map);
            player.placeTileAtRandom();
            players.add(player);
        }
    }

    private void placeObstacles(int n) {
        for (int i = 0; i < n; i++) {
            Vector2d pos = map.getRandomEmptyPosition();
            Obstacle o1 = new Obstacle(3 + rand.nextInt(X_TILES/10), TILE_SIZE_X, TILE_SIZE_Y, pos, map);
            obstacles.add(o1);
        }
    }

    private void setup() {
        map = new SquaresMap(X_TILES, Y_TILES);

        double r = rand.nextDouble();
        double g = rand.nextDouble();
        double b = rand.nextDouble();

        Color randomColor = new Color(r, g, b, 1.0);
        humanPlayer = new Player(randomColor, TILE_SIZE_X, TILE_SIZE_Y, map);
        players.add(humanPlayer);

        placeEnemyPlayers(4);
        placeObstacles((X_TILES / 10) * (Y_TILES / 10));

    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        var size = getBoardSize();
        X_TILES = Integer.parseInt(size.getKey());
        Y_TILES = Integer.parseInt(size.getValue());
        TILE_SIZE_X = 800 / X_TILES;
        TILE_SIZE_Y = 600 / Y_TILES;

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
                if (isFinished) {
                    isFinished = false;
                    squares.getChildren().removeIf(n -> {
                        return true;
                    });
                    squares.getChildren().add(showScores());

                }
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
