import XML.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Subtitles.Speech;
import Subtitles.Style;
import Subtitles.Subtitle;
import Subtitles.SubtitlesList;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
public class Controleur implements Initializable {

	@FXML
    private Pane paneVideo;
	
	 @FXML
	 private Pane panePrincipal;

    @FXML
    private Pane PaneVideoControl;

    @FXML
    private Text timeOutput;

    @FXML
    private Slider videoSlider;

    
    private TextArea subtitlesInput;

    
    private TextField debutInput;

    
    private TextField finInput;

    @FXML
    private Button ajouterButton;

    @FXML
    private Button sauvegarderButton;



    ComboBox<String> personneInput;
    
    File subtitleFile;
    SubtitlesList subtitles;
    
    ObservableList<String> personnes;
    
    @FXML
    void validSubititlesOnClick(ActionEvent event) throws IOException {
    	//addSubtitles(subtitlesInput.getText());
    }
    
    @FXML
    void saveSubtitlesOnClick(ActionEvent event) throws IOException {
    	//saveSubtitles(subtitleFile);
    }



	@Override
    public void initialize(URL location, ResourceBundle resources) {
    	
		
		//Déclarations
    	//video.setMediaPlayer(value);
    	File path = new File("Sans titre.mp4");
    	System.out.println("test : "+path.getAbsoluteFile());
    	Media fichierVideo = new Media(path.toURI().toString());
    	MediaPlayer player = new MediaPlayer(fichierVideo);
    	MediaView video = new MediaView(player);
    	debutInput = new TextField();
    	paneVideo.getChildren().add(video);
    	videoSlider = new Slider();
    	subtitles = new SubtitlesList();
    	
    	debutInput = new TextField();
    	finInput = new TextField();
    	subtitlesInput = new TextArea();
    	
    	personneInput = new ComboBox<>();
    	
    	personnes = FXCollections.observableArrayList("personne1","personne2");
    	
    	personneInput.setItems(personnes);
    	
    	//ajout des textfiels plus ajout au pan
    	
    	panePrincipal.getChildren().add(personneInput);
    	panePrincipal.getChildren().add(debutInput);
    	panePrincipal.getChildren().add(finInput);
    	panePrincipal.getChildren().add(subtitlesInput);
    	
    	paneVideo.setMaxWidth(480);
    	paneVideo.setMaxHeight(200);
    	video.setFitWidth(600);
    	video.setFitHeight(190);
    	video.setLayoutY(4);
    	video.setLayoutX(-50);
    	video.getMediaPlayer().play();
    	
    	personneInput.setLayoutX(457);
    	personneInput.setLayoutY(356);
    	
    	debutInput.setLayoutX(457);
    	debutInput.setLayoutY(291);
    	
    	finInput.setLayoutX(457);
    	finInput.setLayoutY(324);
    	
    	subtitlesInput.setLayoutX(47);
    	subtitlesInput.setLayoutY(284);
    	subtitlesInput.setPrefWidth(275);
    	subtitlesInput.setPrefHeight(96);
   	
    	
    	videoSlider.valueProperty().addListener(new InvalidationListener() {
    		public void invalidated(Observable ov) {
    	        if (videoSlider.isValueChanging()) {
    	        // multiply duration by percentage calculated by slider position
    	        	player.seek(fichierVideo.getDuration().multiply(videoSlider.getValue() / 100.0));
    	        }
    	     }
    	 });
    	
  
 	}


	@FXML
    void ajouterCommentaire(ActionEvent event) {
		if(debutInput.getText().equals("") || finInput.getText().equals("") || personneInput.getValue() == null || subtitlesInput.getText().equals("")) {
			throw new Error("Remplir tous les champs");
		}
		
		Subtitle sub = subtitles.createSubtitle(Long.valueOf(debutInput.getText()), Long.valueOf(finInput.getText()));
		sub.addSpeech(new Speech(subtitlesInput.getText(), personneInput.getValue()));
				
		debutInput.setText("");
		finInput.setText("");
		subtitlesInput.setText("");
		personneInput.setValue(null);
		System.out.println(subtitles.getXml());
	}
	
	@FXML
	//TODO CATCH ERROR
    void sauvegarderOnClick(ActionEvent event) throws IOException{
		for(String p : personnes) {
			subtitles.addStyle(new Style(p, "#FFFFFF"));
		}
		
		Encoder encodeur = new Encoder(subtitles, "Final");

    }
}
