import java.io.FileInputStream;
import java.util.ArrayList;

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
import javafx.stage.Stage;

public class GUIView extends Application {
    
    private static BoardController board;
    private static double scale;
    private static String playerCount = "2";
    private static Text testText;

    public static void launchApp(double screenScale, BoardController boardController) {
        board = boardController;
        scale = screenScale;
        launch();
    }

    public Background backgroundFromImagePath(String imagePath) throws Exception {
        return new Background(new BackgroundImage(new Image(new FileInputStream(imagePath)), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, null, new BackgroundSize(100 * scale, 100 * scale, true, true, true, false)));
    }

    @Override
    public void start(Stage mainStage) throws Exception {
        // Set the title of the window
        mainStage.setTitle("Deadwood Test");

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
        AnchorPane boardGroup = new AnchorPane(imageView);

        // Load in card background image and create blank transparent background to use later
        Background cardBackground = backgroundFromImagePath("resources/CardBack-small.jpg");
        Background transparentBackground = new Background(new BackgroundFill(null, null, null));

        // Load in dice backgrounds to use later
        Background diceBackgrounds[] = new Background[6];
        for (int i = 0; i < 6; i++) {
            diceBackgrounds[i] = backgroundFromImagePath("resources/dice/w" + (i + 1) + ".png");
        }
        
        // Load in font to use later
        Font[] deadwoodFonts = new Font[5];
        for (int i = 0; i < 5; i++) {
            FileInputStream fontFile = new FileInputStream("resources/WEST.TTF");
            deadwoodFonts[i] = Font.loadFont(fontFile, (i + 1) * 20 * scale);
            fontFile.close();
        }

        // For every location on the board, create a button for it on the panel
        ArrayList<Location> locations = board.getBoardLocations();
        for (int i = 0; i < locations.size(); i++) {
            Location location = locations.get(i);

            // Create a new button
            Button button = new Button();

            // Set the button to call Deadwood.LocationClicked when clicked, passing in the associated Location
            button.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    Deadwood.LocationClicked(location);
                }
            });

            // Set the background of the button to the card back or to transparency for the office and trailer
            if (!location.getLocationName().equals("office") && !location.getLocationName().equals("trailer")) {
                button.setBackground(cardBackground);
            } else {
                button.setBackground(transparentBackground);
            }

            // Set the size and coordinates of the button to their appropriate values
            button.setPrefHeight(location.getLocationArea()[2] * scale);
            button.setPrefWidth(location.getLocationArea()[3] * scale);
            boardGroup.setLeftAnchor(button, location.getLocationArea()[0] * scale);
            boardGroup.setTopAnchor(button, location.getLocationArea()[1] * scale);

            // Add the button to the board panel
            boardGroup.getChildren().add(button);
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
            Button button = new Button();

            // Set the button to call Deadwood.RoleClicked when clicked, passing in the associated Role
            button.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    Deadwood.RoleClicked(role);
                }
            });

            // Set the background of the button to the appropriate dice image
            button.setBackground(diceBackgrounds[role.getRank() - 1]);

            // Set the size and coordinates of the button to their appropriate values
            button.setPrefHeight(role.getArea()[2] * scale);
            button.setPrefWidth(role.getArea()[3] * scale);
            boardGroup.setLeftAnchor(button, role.getArea()[0] * scale);
            boardGroup.setTopAnchor(button, role.getArea()[1] * scale);

            // Add the button to the board panel
            boardGroup.getChildren().add(button);
        }
        
        // Add the board panel to the root node
        root.setBottomAnchor(boardGroup, 0d);
        root.setRightAnchor(boardGroup, 0d);
        root.getChildren().add(boardGroup);

        // Create a text element and add it to the root node [Temporary Testing]
        testText = new Text("Test Text");
        testText.setFill(Color.WHITE);
        testText.setFont(deadwoodFonts[4]);
        root.setTopAnchor(testText, 50 * scale);
        root.setLeftAnchor(testText, 50 * scale);
        root.getChildren().add(testText);

        // Create a move button
        Button moveButton = new Button();

        // Set the button to call Deadwood.MoveButtonClicked when Clicked
        moveButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Deadwood.MoveButtonClicked();
            }
        });

        // Set the text, size, and coordinates of the button to their appropriate values
        moveButton.setText("Move");
        moveButton.setFont(deadwoodFonts[2]);
        moveButton.setPrefHeight(100 * scale);
        moveButton.setPrefWidth(300 * scale);
        boardGroup.setLeftAnchor(moveButton, 50 * scale);
        boardGroup.setTopAnchor(moveButton, 350 * scale);

        // Add button to the root node
        root.getChildren().add(moveButton);

        // Set the application to display the root node
        mainStage.setScene(new javafx.scene.Scene(root, imageView.getFitWidth() + 400 * scale, imageView.getFitHeight() + 300 * scale));

        // Display the application
        mainStage.show();
    }

    public static void updateText() { // Test method for updating GUI elements
        String s = testText.getText();
        s += ".";
        testText.setText(s);
    }
}
