import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class GUIView extends Application {
    
    private static BoardController board;
    private static double scale;
    private static Text testText;
    private static int playerCount;
    private static Stage stage;
    private static AnchorPane boardGroup;
    private static HashMap<String, ImageView> playerNodes = new HashMap<String, ImageView>();
    private static HashMap<String, Button> locationNodes = new HashMap<String, Button>();
    private static HashMap<String, Button> roleNodes = new HashMap<String, Button>();
    private static HashMap<String, Button> buttonNodes = new HashMap<String, Button>();
    private static Font[] deadwoodFonts = new Font[5];
    private static Image[][] diceImages = new Image[9][6];

    public static void launchApp(double screenScale, BoardController boardController) throws Exception {
        board = boardController;
        scale = screenScale;
        for (int i = 0; i < 5; i++) {
            FileInputStream fontFile = new FileInputStream("resources/WEST.TTF");
            deadwoodFonts[i] = Font.loadFont(fontFile, (i + 1) * 20 * scale);
            fontFile.close();
        }
        for (int i = 0; i < 9; i++) {
            char[] colors = {'b', 'c', 'g', 'o', 'p', 'r', 'v', 'y', 'w'};
            for (int j = 0; j < 6; j++) {
                diceImages[i][j] = new Image(new FileInputStream(("resources/dice/" + colors[i] + (j + 1) + ".png")));
            }
        }
        launch();
    }

    public static Background backgroundFromImage(Image image) throws Exception {
        return new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, null, new BackgroundSize(100 * scale, 100 * scale, true, true, true, false)));
    }

    public static Background backgroundFromImagePath(String imagePath) throws Exception {
        return new Background(new BackgroundImage(new Image(new FileInputStream(imagePath)), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, null, new BackgroundSize(100 * scale, 100 * scale, true, true, true, false)));
    }

    public void setPlayerCount(String numPlayers) throws Exception {
        playerCount = Integer.parseInt(numPlayers);
        Deadwood.initializePlayers(playerCount);
        displayBoard();
    }

    @Override
    public void start(Stage mainStage) throws Exception {
        stage = mainStage;
        // Set the title of the window
        mainStage.setTitle("Deadwood Test");
        mainStage.setResizable(false);
        mainStage.getIcons().add(new Image(new FileInputStream("resources/shot.png")));

        // Create a root node to display
        AnchorPane root = new AnchorPane();
        // Back the default background black instead of white (for the sake of my eyes)
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

        for (int i = 2; i <= 8; i++) {
            Button button = new Button();
            button.setId("" + i);
            button.setText(i + " Players");
            button.setFont(deadwoodFonts[1]);
            // Set the button to call Deadwood.RoleClicked when clicked, passing in the associated Role
            button.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    try {
                        setPlayerCount(button.getId());
                    } catch (Exception e) {
                        System.out.println("Error running application");
                        e.printStackTrace();
                    }
                }
            });

            // Set the size and coordinates of the button to their appropriate values
            button.setPrefHeight(100 * scale);
            button.setPrefWidth(200 * scale);
            root.setLeftAnchor(button, 400 * scale);
            root.setRightAnchor(button, 400 * scale);
            root.setTopAnchor(button, 150 * (i - 1) * scale);

            // Add the button to the board panel
            root.getChildren().add(button);
        }

        Text playerText = new Text("How many Players?");
        playerText.setTextAlignment(TextAlignment.RIGHT);
        playerText.setFill(Color.WHITE);
        playerText.setFont(deadwoodFonts[3]);
        root.setTopAnchor(playerText, 50 * scale);
        root.setLeftAnchor(playerText, 400 * scale);
        root.setRightAnchor(playerText, 400 * scale);
        root.getChildren().add(playerText);

        // Set the application to display the root node
        stage.setScene(new javafx.scene.Scene(root, 1600 * scale, 1200 * scale));

        // Display the application
        mainStage.show();
    }

    public void displayBoard() throws Exception {
        // Create a root node to display
        AnchorPane root = new AnchorPane();
        // Back the default background black instead of white (for the sake of my eyes)
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

        // Load in image of the board, set its size to the appropriate values
        Image boardImage = new Image(new FileInputStream("resources/Board.jpg"));
        ImageView imageView = new ImageView(boardImage);
        imageView.setFitWidth(1200 * scale);
        imageView.setFitHeight(900 * scale);
        // Create a panel with the board image as the base node
        boardGroup = new AnchorPane(imageView);

        // Load in card background image and create blank transparent background to use later
        Background cardBackground = backgroundFromImagePath("resources/CardBack-small.jpg");
        Background transparentBackground = new Background(new BackgroundFill(null, null, null));

        // For every location on the board, create a button for it on the panel
        ArrayList<Location> locations = board.getBoardLocations();
        for (int i = 0; i < locations.size(); i++) {
            Location location = locations.get(i);

            // Create a new button
            Button locationButton = new Button();

            // Set the button to call Deadwood.LocationClicked when clicked, passing in the associated Location
            locationButton.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    Deadwood.locationClicked(location);
                }
            });

            // Set the background of the button to the card back or to transparency for the office and trailer
            if (!location.getLocationName().equals("office") && !location.getLocationName().equals("trailer")) {
                locationButton.setBackground(cardBackground);
            } else if (location.getLocationName().equals("trailer")) {
                locationButton.setBackground(transparentBackground);
                String[] playerNames = {"Blue", "Cyan", "Green", "Orange", "Pink", "Red", "Violet", "Yellow", "White"};
                for (int j = 0; j < playerCount; j++) {
                    ImageView playerImage = new ImageView(diceImages[j][0]);
                    playerImage.setFitWidth(50 * scale);
                    playerImage.setFitHeight(50 * scale);
                    playerImage.setX((location.getLocationArea()[0] + 15 + 60 * ((j % 3))) * scale);
                    playerImage.setY((location.getLocationArea()[1] + 15 + 60 * ((j / 3))) * scale);
                    playerNodes.put(playerNames[j], playerImage);
                    boardGroup.getChildren().add(playerImage);
                }
            } else {
                locationButton.setBackground(transparentBackground);
            }

            // Set the size and coordinates of the button to their appropriate values
            locationButton.setPrefHeight(location.getLocationArea()[2] * scale);
            locationButton.setPrefWidth(location.getLocationArea()[3] * scale);
            boardGroup.setLeftAnchor(locationButton, location.getLocationArea()[0] * scale);
            boardGroup.setTopAnchor(locationButton, location.getLocationArea()[1] * scale);

            locationNodes.put(location.getLocationName(), locationButton);

            // Add the button to the board panel
            boardGroup.getChildren().add(locationButton);
        }

        // Create list of off-card roles
        ArrayList<Set> sets = board.getBoardSets();
        ArrayList<Role> roles = new ArrayList<Role>();
        for (int i = 0; i < sets.size(); i++) {
            Set set = sets.get(i);
            roles.addAll(set.getRoles());
        }
        
        // For every role on the board, create a button for it on the panel
        for (int i = 0; i < roles.size(); i++) {
            Role role = roles.get(i);

            // Create a new button
            Button roleButton = new Button();
            // Set the button to call Deadwood.RoleClicked when clicked, passing in the associated Role
            roleButton.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    Deadwood.roleClicked(role);
                }
            });

            // Set the background of the button to the appropriate dice image
            roleButton.setBackground(backgroundFromImage(diceImages[8][role.getRank() - 1]));

            // Set the size and coordinates of the button to their appropriate values
            roleButton.setPrefHeight(role.getArea()[2] * scale);
            roleButton.setPrefWidth(role.getArea()[3] * scale);
            boardGroup.setLeftAnchor(roleButton, role.getArea()[0] * scale);
            boardGroup.setTopAnchor(roleButton, role.getArea()[1] * scale);

        
            roleNodes.put(role.getRoleName(), roleButton);            // Add the button to the board panel
            boardGroup.getChildren().add(roleButton);
        }

        for (int i = 0; i < sets.size(); i++) {
            Set set = sets.get(i);
            for (int j = 0; j < set.getMaxShotCounters(); j++) {
                int[] shotCounterArea = set.getShotCounterAreas()[j];
                ImageView shotCounter = new ImageView(new Image(new FileInputStream("resources/shot.png")));
                shotCounter.setX(shotCounterArea[0] * scale);
                shotCounter.setY(shotCounterArea[1] * scale);
                shotCounter.setFitHeight(shotCounterArea[2] * scale);
                shotCounter.setFitWidth(shotCounterArea[3] * scale);
                boardGroup.getChildren().add(shotCounter);
            }
        }
        
        // Add the board panel to the root node
        root.setBottomAnchor(boardGroup, 0d);
        root.setRightAnchor(boardGroup, 0d);
        root.getChildren().add(boardGroup);

        // Create a text element and add it to the root node [Temporary Testing]
        testText = new Text("Number of Players: " + playerCount);
        testText.setFill(Color.WHITE);
        testText.setFont(deadwoodFonts[4]);
        root.setTopAnchor(testText, 50 * scale);
        root.setLeftAnchor(testText, 50 * scale);
        root.getChildren().add(testText);

        // C// Create action buttons
        Button moveButton = new Button();
        buttonNodes.put("moveButton", moveButton);
        Button actButton = new Button();
        buttonNodes.put("actButton", actButton);
        Button rehearseButton = new Button();
        buttonNodes.put("rehearseButton", rehearseButton);
        Button upgradeButton = new Button();
        buttonNodes.put("upgradeButton", upgradeButton);
        Button takeButton = new Button();
        buttonNodes.put("takeButton", takeButton);
        Button endTurnButton = new Button();
        buttonNodes.put("endTurnButton", endTurnButton);        // Set the button to call Deadwood.MoveButtonClicked when Clicked
        moveButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Deadwood.moveButtonClicked();
            }
        });

        // Set the text, size, and coordinates of the button to their appropriate values
        moveButton.setText("Move");
        moveButton.setFont(deadwoodFonts[2]);
        moveButton.setPrefHeight(100 * scale);
        moveButton.setPrefWidth(300 * scale);
        boardGroup.setLeftAnchor(moveButton, 50 * scale);
        boardGroup.setTopAnchor(moveButton, 325 * scale);

        // Set the button to call Deadwood.MoveButtonClicked when Clicked
        takeButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Deadwood.takeButtonClicked();
            }
        });

        // Set the text, size, and coordinates of the button to their appropriate values
        takeButton.setText("Take");
        takeButton.setFont(deadwoodFonts[2]);
        takeButton.setPrefHeight(100 * scale);
        takeButton.setPrefWidth(300 * scale);
        boardGroup.setLeftAnchor(takeButton, 50 * scale);
        boardGroup.setTopAnchor(takeButton, 475 * scale);

        // Set the button to call Deadwood.MoveButtonClicked when Clicked
        actButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Deadwood.actButtonClicked();
            }
        });

        // Set the text, size, and coordinates of the button to their appropriate values
        actButton.setText("Act");
        actButton.setFont(deadwoodFonts[2]);
        actButton.setPrefHeight(100 * scale);
        actButton.setPrefWidth(300 * scale);
        boardGroup.setLeftAnchor(actButton, 50 * scale);
        boardGroup.setTopAnchor(actButton, 625 * scale);
        
        // Set the button to call Deadwood.MoveButtonClicked when Clicked
        rehearseButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Deadwood.rehearseButtonClicked();
            }
        });

        // Set the text, size, and coordinates of the button to their appropriate values
        rehearseButton.setText("Rehearse");
        rehearseButton.setFont(deadwoodFonts[2]);
        rehearseButton.setPrefHeight(100 * scale);
        rehearseButton.setPrefWidth(300 * scale);
        boardGroup.setLeftAnchor(rehearseButton, 50 * scale);
        boardGroup.setTopAnchor(rehearseButton, 775 * scale);

        // Set the button to call Deadwood.MoveButtonClicked when Clicked
        upgradeButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Deadwood.upgradeButtonClicked();
            }
        });

        // Set the text, size, and coordinates of the button to their appropriate values
        upgradeButton.setText("Upgrade");
        upgradeButton.setFont(deadwoodFonts[2]);
        upgradeButton.setPrefHeight(100 * scale);
        upgradeButton.setPrefWidth(300 * scale);
        boardGroup.setLeftAnchor(upgradeButton, 50 * scale);
        boardGroup.setTopAnchor(upgradeButton, 925 * scale);

        // Set the button to call Deadwood.MoveButtonClicked when Clicked
        endTurnButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Deadwood.endTurnButtonClicked();
            }
        });

        // Set the text, size, and coordinates of the button to their appropriate values
        endTurnButton.setText("End Turn");
        endTurnButton.setFont(deadwoodFonts[2]);
        endTurnButton.setPrefHeight(100 * scale);
        endTurnButton.setPrefWidth(300 * scale);
        boardGroup.setLeftAnchor(endTurnButton, 50 * scale);
        boardGroup.setTopAnchor(endTurnButton, 1075 * scale);

        // Add buttons to the root node
        root.getChildren().add(moveButton);
        root.getChildren().add(takeButton);
        root.getChildren().add(actButton);
        root.getChildren().add(rehearseButton);
        root.getChildren().add(upgradeButton);
        root.getChildren().add(endTurnButton);
        
        // Set the application to display the root node
        stage.setScene(new javafx.scene.Scene(root, imageView.getFitWidth() + 400 * scale, imageView.getFitHeight() + 300 * scale));
    }

    public static void displayPlayerInfo(String name, int rank, int dollars, int credits, int rehearsals) {
        testText.setText(name);
    }

    public static void highlightLocations(ArrayList<Location> locations) {
        
    }
    
    public static void updatePlayerLocation(Location location) {
        int standingPlayerCount = 0;
        for (int i = 0; i < location.getPlayers().size(); i++) {
            PlayerController player = location.getPlayers().get(i);
            if (!player.getPlayerIsWorking()) {
                ImageView playerNode = playerNodes.get(player.getPlayerName());
                if (!location.getLocationName().equals("trailer")) {
                    playerNode.setFitWidth(20 * scale);
                    playerNode.setFitHeight(20 * scale);
                    boardGroup.setLeftAnchor(playerNode, (location.getLocationArea()[0] + 5 + standingPlayerCount * 25) * scale);
                    boardGroup.setTopAnchor(playerNode, (location.getLocationArea()[2] - 25d + location.getLocationArea()[1]) * scale);
                } else {
                    playerNode.setFitWidth(50 * scale);
                    playerNode.setFitHeight(50 * scale);
                    playerNode.setX((location.getLocationArea()[0] + 15 + 60 * ((standingPlayerCount % 3))) * scale);
                    playerNode.setY((location.getLocationArea()[1] + 15 + 60 * ((standingPlayerCount / 3))) * scale);
                }
                standingPlayerCount++;
            }
        }
    }
}
