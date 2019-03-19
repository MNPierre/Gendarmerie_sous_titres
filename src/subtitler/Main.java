package subtitler;
import java.io.File;
import java.net.URL;

import com.sun.prism.paint.Color;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Main extends Application {

	public static Stage primaryStage;

	@Override
	public void start(Stage primaryStage) {
		try {
			Main.primaryStage = primaryStage;
			FXMLLoader loader = new FXMLLoader();
			URL url = new File("Test.fxml").
					toURI().toURL();
			loader.setLocation(url);
			System.out.println(loader.getLocation());
			Parent root = FXMLLoader.load(url);
			Scene scene = new Scene(root,1200,850);
			primaryStage.setScene(scene);
			primaryStage.show();


			//création du fond de la barre de fonction


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