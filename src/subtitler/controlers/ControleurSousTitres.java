package subtitler.controlers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import subtitler.subtitles.Speech;
import subtitler.subtitles.Subtitle;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class ControleurSousTitres implements Initializable{
	ArrayList<Speech> speechToShow;
	Speech actualSpeech;
	
	@FXML
	private TextArea commentaireModifInput;

	@FXML
	private ComboBox<String> personneChoix;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		speechToShow = new ArrayList<Speech>();
		intiSpeechToShow();
		if(MainControler.subtitles.getNarrators().isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setContentText("Il n'y a aucun commentaire à modifier !");
			MainControler.modifySubtitlesStage.close();
		}
		
		personneChoix.setItems(MainControler.subtitles.getNarrators());
		personneChoix.setValue(MainControler.subtitles.getNarrators().get(0));
		actualSpeech = getSpeech(personneChoix.getValue());
		
		commentaireModifInput.setText(actualSpeech.getText());
		
		personneChoix.valueProperty().addListener(new InvalidationListener() {
			
			@Override
			public void invalidated(Observable observable) {
				commentaireModifInput.setText(getSpeech(personneChoix.getValue()).getText());			
			}
		});
		
	}
	
	public void intiSpeechToShow() {
		for(Subtitle subtitle : MainControler.subtitlesToShow) {
			for(Speech speech : subtitle.getContenu()) {
				speechToShow.add(speech);
			}
		}
	
	}
	

	public Speech getSpeech(String author) {
		for(Speech s : speechToShow) {
			if(s.getAuthor().equals(author)) {
				return s;
			}
		}
		return null;
	}
	
	@FXML
    void modifierSousTitresOnClick(ActionEvent event) {
		actualSpeech.setText(commentaireModifInput.getText());
		System.out.println(MainControler.subtitles.getSubtitles());
    }

}


