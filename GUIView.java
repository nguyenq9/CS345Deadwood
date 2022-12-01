import java.io.FileInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.Action;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.media.*;

public class GUIView extends Application {
    
    private static BoardController board;
    private static double scale;
    private static Text playerInfo;
    private static Text dayNum;
    private static int playerCount;
    private static Stage stage;
    private static AnchorPane root;
    private static AnchorPane boardGroup;
    private static HashMap<String, ImageView> playerNodes = new HashMap<String, ImageView>();
    private static HashMap<String, Button> locationNodes = new HashMap<String, Button>();
    private static HashMap<String, Button> roleNodes = new HashMap<String, Button>();
    private static HashMap<String, Button> buttonNodes = new HashMap<String, Button>();
    private static HashMap<String, Button> upgradeNodes = new HashMap<String, Button>();
    private static HashMap<String, ArrayList<Button>> onCardRoleNodes = new HashMap<String, ArrayList<Button>>();
    private static Font[] deadwoodFonts = new Font[5];
    private static Image[][] diceImages = new Image[9][6];
    private static Image shotImage;
    private static Background cardBackground;
    private static Background transparentBackground;
    public static Media sound; 
    public static MediaPlayer mediaPlayer;

    public static void launchApp(double screenScale, BoardController boardController) throws Exception {
        board = boardController;
        scale = screenScale;
        // Load in card background image and create blank transparent background to use later
        cardBackground = backgroundFromImagePath("resources/CardBack-small.jpg");
        transparentBackground = new Background(new BackgroundFill(null, null, null));
        shotImage = new Image(new FileInputStream("resources/shot.png"));
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

    public void setPlayerCount(int numPlayers) throws Exception {
        playerCount = numPlayers;
        Deadwood.initializePlayers(playerCount);
    }

    @Override
    public void start(Stage mainStage) throws Exception {
        //set music
        sound = new Media(new File("resources/gamemusic.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setVolume(0.05);
        mediaPlayer.setCycleCount(mediaPlayer.INDEFINITE);
        mediaPlayer.play();

        stage = mainStage;
        // Set the title of the window
        mainStage.setTitle("Deadwood Test");
        stage.setResizable(false);
        mainStage.getIcons().add(new Image(new FileInputStream("resources/shot.png")));

        // Create a root node to display
        AnchorPane root = new AnchorPane();
        // Back the default background black instead of white (for the sake of my eyes)
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

        for (int i = 2; i <= 8; i++) {
            Button button = new Button();
            int playerNum = i;
            button.setText(i + " Players");
            button.setFont(deadwoodFonts[1]);
            // Set the button to call Deadwood.RoleClicked when clicked, passing in the associated Role
            button.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    try {
                        setPlayerCount(playerNum);
                        displayBoard();
                        Deadwood.initialDisplay();
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
        root = new AnchorPane();

        // Back the default background black instead of white (for the sake of my eyes)
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

        // Load in image of the board, set its size to the appropriate values
        Image boardImage = new Image(new FileInputStream("resources/Board.jpg"));
        ImageView imageView = new ImageView(boardImage);
        imageView.setFitWidth(1200 * scale);
        imageView.setFitHeight(900 * scale);

        // Create a panel with the board image as the base node
        boardGroup = new AnchorPane(imageView);

        // Get locations, sets, and roles into lists
        ArrayList<Location> locations = board.getBoardLocations();
        ArrayList<Set> sets = board.getBoardSets();
        ArrayList<Role> roles = new ArrayList<Role>();
        for (int i = 0; i < sets.size(); i++) {
            Set set = sets.get(i);
            roles.addAll(set.getRoles());
        }
        
        // Create UI elements
        createLocations(locations);
        createShotCounters(sets);
        createOffCardRoles(roles);
        createPlayers();
        createUpgrades(Currency.CREDITS, board.getBoardOffice().getUpgradeAreaCredit());
        createUpgrades(Currency.DOLLARS, board.getBoardOffice().getUpgradeAreaDollar());
        
        // Add the board panel to the root node
        root.setBottomAnchor(boardGroup, 0d);
        root.setRightAnchor(boardGroup, 0d);
        root.getChildren().add(boardGroup);

        // Create text elements
        playerInfo = createText("", Color.WHITE, deadwoodFonts[2], 1200, 20);
        dayNum = createText("", Color.WHITE, deadwoodFonts[4], 100, 50);

        createButton("Move", 50, 325, new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Deadwood.moveButtonClicked();
            }
        });

        createButton("Take", 50, 475, new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Deadwood.takeButtonClicked();
            }
        });

        createButton("Act", 50, 625, new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Deadwood.actButtonClicked();
            }
        });

        createButton("Rehearse", 50, 775, new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Deadwood.rehearseButtonClicked();
            }
        });

        createButton("Upgrade", 50, 925, new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Deadwood.upgradeButtonClicked();
            }
        });

        createButton("End Turn", 50, 1075, new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Deadwood.endTurnButtonClicked();
            }
        });
        
        // Set the application to display the root node
        stage.setScene(new javafx.scene.Scene(root, imageView.getFitWidth() + 400 * scale, imageView.getFitHeight() + 300 * scale));
    }

    private static Text createText(String text, Color color, Font font, double posX, double posY) {
        Text textNode = new Text(text);
        textNode.setFill(color);
        textNode.setFont(font);
        root.setLeftAnchor(textNode, posX * scale);
        root.setTopAnchor(textNode, posY * scale);
        root.getChildren().add(textNode);
        return textNode;
    }

    private static void createLocations(ArrayList<Location> locations) {
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
    }

    private static void createOffCardRoles(ArrayList<Role> roles) throws Exception {
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
            roleButton.setMinHeight(role.getArea()[2] * scale);
            roleButton.setMinWidth(role.getArea()[3] * scale);
            roleButton.setPrefHeight(role.getArea()[2] * scale);
            roleButton.setPrefWidth(role.getArea()[3] * scale);
            boardGroup.setLeftAnchor(roleButton, role.getArea()[0] * scale);
            boardGroup.setTopAnchor(roleButton, role.getArea()[1] * scale);

        
            roleNodes.put(role.getRoleName(), roleButton);            // Add the button to the board panel
            boardGroup.getChildren().add(roleButton);
        }
    }

    private static void createShotCounters(ArrayList<Set> sets) throws Exception {
        for (int i = 0; i < sets.size(); i++) {
            Set set = sets.get(i);
            for (int j = 0; j < set.getMaxShotCounters(); j++) {
                int[] shotCounterArea = set.getShotCounterAreas()[j];
                ImageView shotCounter = new ImageView(shotImage);
                shotCounter.setX(shotCounterArea[0] * scale);
                shotCounter.setY(shotCounterArea[1] * scale);
                shotCounter.setFitHeight(shotCounterArea[2] * scale);
                shotCounter.setFitWidth(shotCounterArea[3] * scale);
                boardGroup.getChildren().add(shotCounter);
            }
        }
    }

    private static void createPlayers() {
        String[] playerNames = {"Blue", "Cyan", "Green", "Orange", "Pink", "Red", "Violet", "Yellow", "White"};
        int startingRank = playerCount > 6 ? 2 : 1;
        for (int j = 0; j < playerCount; j++) {
            ImageView playerImage = new ImageView(diceImages[j][startingRank - 1]);
            playerImage.setFitWidth(50 * scale);
            playerImage.setFitHeight(50 * scale);
            playerImage.setX((board.getBoardTrailer().getLocationArea()[0] + 15 + 60 * ((j % 3))) * scale);
            playerImage.setY((board.getBoardTrailer().getLocationArea()[1] + 15 + 60 * ((j / 3))) * scale);
            playerNodes.put(playerNames[j], playerImage);
            boardGroup.getChildren().add(playerImage);
        }
    }

    private static void createButton(String buttonName, double xPos, double yPos, EventHandler<ActionEvent> event) {
        Button button = new Button();
        buttonNodes.put(buttonName, button);
        // Set the text, size, and coordinates of the button to their appropriate values
        button.setText(buttonName);
        button.setFont(deadwoodFonts[2]);
        button.setPrefHeight(100 * scale);
        button.setPrefWidth(300 * scale);
        boardGroup.setLeftAnchor(button, xPos * scale);
        boardGroup.setTopAnchor(button, yPos * scale);
        // Set the button to call Deadwood.MoveButtonClicked when Clicked
        button.setOnAction(event);
        root.getChildren().add(button);
    }

    private static void createUpgrades(Currency currency, int[][] areas) {
        for (int i = 0; i < 5; i++) {
            for (int j = 1; j < 4; j++) {
                System.out.println(areas[i][j]);
            }
        }
        for (int i = 0; i < 5; i++) {
            Button upgradeButton = new Button();
            int rank = i + 2;
            // Set the text, size, and coordinates of the button to their appropriate values
            upgradeButton.setBackground(transparentBackground);
            upgradeButton.setMinHeight(areas[i][2] * scale);
            upgradeButton.setMinWidth(areas[i][3] * scale);
            upgradeButton.setPrefHeight(areas[i][2] * scale);
            upgradeButton.setPrefWidth(areas[i][3] * scale);
            boardGroup.setLeftAnchor(upgradeButton, areas[i][0] * scale);
            boardGroup.setTopAnchor(upgradeButton, areas[i][1] * scale);
            // Set the button to call Deadwood.MoveButtonClicked when Clicked
            upgradeButton.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    Deadwood.upgradeChoiceClicked(rank, currency);
                }
            });
            if (currency == Currency.DOLLARS) {
                upgradeNodes.put("d" + rank, upgradeButton);
            } else if (currency == Currency.CREDITS) {
                upgradeNodes.put("c" + rank, upgradeButton);
            }
            boardGroup.getChildren().add(upgradeButton);
        }
    }

    public static void displayPlayerInfo(String name, int rank, int dollars, int credits, int rehearsals) {
        String info = "Player: " + name + "\nRank: " + rank + "\nDollars: "
                        + dollars + "\nCredits: " + credits + "\nRehearsals: " + rehearsals;
        playerInfo.setText(info);
    }

    public static void displayCurrentDay(int currDay) {
        String info = "Day " + currDay;
        dayNum.setText(info);
    }

    public static void revealSet(Set set) {
        Button locationButton = locationNodes.get(set.getLocationName());
        try {
            locationButton.setBackground(backgroundFromImagePath("resources/cards/" + set.getScene().getSceneImg()));
            ArrayList<Role> roles = set.getScene().getRoles();
            ArrayList<Button> cardRoleNodes = new ArrayList<Button>();
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
                roleButton.setMinHeight(role.getArea()[2] * scale);
                roleButton.setMinWidth(role.getArea()[3] * scale);
                roleButton.setPrefHeight(role.getArea()[2] * scale);
                roleButton.setPrefWidth(role.getArea()[3] * scale);
                boardGroup.setLeftAnchor(roleButton, locationButton.getLayoutX() + (role.getArea()[0]) * scale);
                boardGroup.setTopAnchor(roleButton, locationButton.getLayoutY() + (role.getArea()[1]) * scale);
    
                // Add the button to the board panel
                roleNodes.put(role.getRoleName(), roleButton);
                cardRoleNodes.add(roleButton);
                boardGroup.getChildren().add(roleButton);
            }
            onCardRoleNodes.put(set.getLocationName(), cardRoleNodes);
        } catch (Exception e) {
            System.out.println("Error loading card image");
            e.printStackTrace();
        }
    }

    public static void removeSet(Set set) {
        Button locationButton = locationNodes.get(set.getLocationName());
        locationButton.setBackground(transparentBackground);
        ArrayList<Button> cardRoleNodes = onCardRoleNodes.get(set.getLocationName());
        for (int i = 0; i < cardRoleNodes.size(); i++) {
            boardGroup.getChildren().remove(cardRoleNodes.get(i));
        }
    }

    public static void highlightLocations(ArrayList<Location> locations) {
        for (int i = 0; i < locations.size(); i++) {
            Button locationButton = locationNodes.get(locations.get(i).getLocationName());
            locationButton.setBorder(new Border(new BorderStroke(Color.HOTPINK, new BorderStrokeStyle(StrokeType.OUTSIDE, null, null, 10, 0, null), null, new BorderWidths(5))));
        }
    }

    public static void clearHightlightLocations(ArrayList<Location> locations) {
        for (int i = 0; i < locations.size(); i++) {
            Button locationButton = locationNodes.get(locations.get(i).getLocationName());
            locationButton.setBorder(Border.EMPTY);
        }
    }

    public static void highlightRoles(ArrayList<Role> roles) {
        for (int i = 0; i < roles.size(); i++) {
            Button roleButton = roleNodes.get(roles.get(i).getRoleName());
            if (roleButton == null) {
                continue;
            }
            roleButton.setBorder(new Border(new BorderStroke(Color.HOTPINK, new BorderStrokeStyle(StrokeType.OUTSIDE, null, null, 10, 0, null), new CornerRadii(5), new BorderWidths(5))));
        }
    }

    public static void clearHighlightRoles(ArrayList<Role> roles) {
        for (int i = 0; i < roles.size(); i++) {
            Button roleButton = roleNodes.get(roles.get(i).getRoleName());
            if (roleButton == null) {
                continue;
            }
            roleButton.setBorder(Border.EMPTY);
        }
    }

    public static void highlightUpgrades(ArrayList<String> upgrades) {
        for (int i = 0; i < upgrades.size(); i++) {
            Button upgradeButton = upgradeNodes.get(upgrades.get(i));
            upgradeButton.setBorder(new Border(new BorderStroke(Color.HOTPINK, new BorderStrokeStyle(StrokeType.OUTSIDE, null, null, 10, 0, null), new CornerRadii(1), new BorderWidths(2))));
        }
    }

    public static void clearHighlightUpgrades() {
        for (int i = 2; i <= 6; i++) {
            Button upgradeButton = upgradeNodes.get("d" + i);
            upgradeButton.setBorder(Border.EMPTY);
        }
        for (int i = 2; i <= 6; i++) {
            Button upgradeButton = upgradeNodes.get("c" + i);
            upgradeButton.setBorder(Border.EMPTY);
        }
    }
    
    public static void updatePlayerLocation(Location location) {
        int standingPlayerCount = 0;
        for (int i = 0; i < location.getPlayers().size(); i++) {
            PlayerController player = location.getPlayers().get(i);
            if (!player.getPlayerIsWorking()) {
                ImageView playerNode = playerNodes.get(player.getPlayerName());
                if (location.getLocationName().equals("trailer")) {
                    playerNode.setFitWidth(50 * scale);
                    playerNode.setFitHeight(50 * scale);
                    boardGroup.setLeftAnchor(playerNode, (location.getLocationArea()[0] + 15 + 60 * ((standingPlayerCount % 3))) * scale);
                    boardGroup.setTopAnchor(playerNode, (location.getLocationArea()[1] + 15 + 60 * ((standingPlayerCount / 3))) * scale);
                } else if (location.getLocationName().equals("office")){
                    playerNode.setFitWidth(20 * scale);
                    playerNode.setFitHeight(20 * scale);
                    boardGroup.setLeftAnchor(playerNode, (location.getLocationArea()[0] + 5) * scale);
                    boardGroup.setTopAnchor(playerNode, (location.getLocationArea()[1] + location.getLocationArea()[2] - 25d - standingPlayerCount * 25) * scale);
                } else {
                    playerNode.setFitWidth(20 * scale);
                    playerNode.setFitHeight(20 * scale);
                    boardGroup.setLeftAnchor(playerNode, (location.getLocationArea()[0] + 5 + standingPlayerCount * 25) * scale);
                    boardGroup.setTopAnchor(playerNode, (location.getLocationArea()[1] + location.getLocationArea()[2] - 25d) * scale);
                }
                standingPlayerCount++;
            }
        }
    }

    public static void movePlayerToRole(String playerName, String roleName, boolean onCard) {
        ImageView playerNode = playerNodes.get(playerName);
        playerNode.toFront();
        Button roleNode = roleNodes.get(roleName);
        if (onCard) {
            playerNode.setFitWidth(40 * scale);
            playerNode.setFitHeight(40 * scale);
        } else {
            playerNode.setFitWidth(46 * scale);
            playerNode.setFitHeight(46 * scale);
        }
        boardGroup.setLeftAnchor(playerNode, (roleNode.getLayoutX()));
        boardGroup.setTopAnchor(playerNode, (roleNode.getLayoutY()));
    }

    public static void displayActInformation(int roll, boolean success) {
        
    }

    public static void wrapScene() {
        
    }
}
