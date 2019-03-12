import java.io.File;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) {
        try {
            
        FXMLLoader loader = new FXMLLoader();
        URL url = new File("src/Test.fxml").
        toURI().toURL();
        loader.setLocation(url);
        System.out.println(loader.getLocation());
        Parent root = FXMLLoader.load(url);
        Scene scene = new Scene(root,450,250);
        primaryStage.setScene(scene);
        primaryStage.show();

          /*  Scene scene = new Scene(new Group(), 540, 209);
            primaryStage.setScene(scene);

            // Name and display the Stage.
            primaryStage.setTitle("Hello Media");
            primaryStage.show();

            // Create the media source.
            File path = new File("Sans titre.mp4");
            Media media = new Media(path.toURI().toString());

            // Create the player and set to play automatically.
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setAutoPlay(true);

            // Create the view and add it to the Scene.
            MediaView mediaView = new MediaView(mediaPlayer);
            ((Group) scene.getRoot()).getChildren().add(mediaView);
            
            //primaryStage.show();
            
            */



        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        launch(args);

    }
}