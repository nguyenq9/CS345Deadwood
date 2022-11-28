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

public class Main extends Application {

    private SetUp setup = SetUp.setup;
    private static double scale = 1;
    private static String playerCount = "2";
    
    public static void main(String[] args) {
        if (args.length > 0) {
            playerCount = args[0];
        }
        if (args.length > 1) {
            scale = Double.parseDouble(args[1]);
        }
        launch(args);
    }

    public Background backgroundFromImagePath(String imagePath) throws Exception {
        return new Background(new BackgroundImage(new Image(new FileInputStream(imagePath)), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, null, new BackgroundSize(100 * scale, 100 * scale, true, true, true, false)));
    }

    @Override
    public void start(Stage mainStage) throws Exception {
        BoardController board = setup.initializeBoard();

        mainStage.setTitle("Deadwood Test");

        AnchorPane root = new AnchorPane();
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

        Image boardImage = new Image(new FileInputStream("resources/Board.jpg"));
        ImageView imageView = new ImageView(boardImage);
        imageView.setX(0);
        imageView.setY(0);
        imageView.setFitWidth(1200 * scale);
        imageView.setFitHeight(900 * scale);
        AnchorPane boardGroup = new AnchorPane(imageView);

        Background cardBackground = backgroundFromImagePath("resources/CardBack-small.jpg");
        Background transparentBackground = new Background(new BackgroundFill(null, null, null));

        Background diceBackgrounds[] = new Background[6];
        for (int i = 0; i < 6; i++) {
            diceBackgrounds[i] = backgroundFromImagePath("resources/dice/w" + (i + 1) + ".png");
        }

        ArrayList<Location> locations = board.getBoardLocations();
        for (int i = 0; i < locations.size(); i++) {
            Location location = locations.get(i);
            Button button = new Button();
            if (!location.getLocationName().equals("office") && !location.getLocationName().equals("trailer")) {
                button.setBackground(cardBackground);
            } else {
                button.setBackground(transparentBackground);
            }
            button.setPrefHeight(location.getLocationArea()[2] * scale);
            button.setPrefWidth(location.getLocationArea()[3] * scale);
            boardGroup.setLeftAnchor(button, (double) location.getLocationArea()[0] * scale);
            boardGroup.setTopAnchor(button, (double) location.getLocationArea()[1] * scale);
            boardGroup.getChildren().add(button);
        }

        ArrayList<Set> sets = board.getBoardSets();
        ArrayList<Role> roles = new ArrayList<Role>();
        for (int i = 0; i < sets.size(); i++) {
            Set set = sets.get(i);
            roles.addAll(set.getRoles());
        }
        for (int i = 0; i < roles.size(); i++) {
            Role role = roles.get(i);
            Button button = new Button();
            button.setBackground(diceBackgrounds[role.getRank() - 1]);
            button.setPrefHeight(role.getArea()[2] * scale);
            button.setPrefWidth(role.getArea()[3] * scale);
            boardGroup.setLeftAnchor(button, role.getArea()[0] * scale);
            boardGroup.setTopAnchor(button, role.getArea()[1] * scale);
            boardGroup.getChildren().add(button);
        }
        
        root.setTopAnchor(boardGroup, 300 * scale);
        root.getChildren().add(boardGroup);

        FileInputStream fontFile = new FileInputStream("resources/WEST.TTF");
        Font deadwoodFont = Font.loadFont(fontFile, 100 * scale);

        Text currentPlayerName = new Text("Test Name");
        currentPlayerName.setFill(Color.WHITE);
        currentPlayerName.setFont(deadwoodFont);
        root.setTopAnchor(currentPlayerName, 10 * scale);
        root.setLeftAnchor(currentPlayerName, 10 * scale);
        root.getChildren().add(currentPlayerName);

        Button nextTurnButton = new Button();
        nextTurnButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                
            }
        });

        mainStage.setScene(new javafx.scene.Scene(root, imageView.getFitWidth() + 400 * scale, imageView.getFitHeight() + 300 * scale));

        mainStage.show();
        Deadwood.start(new String[] {playerCount});
    }

    public void updateText() {

    }
}
