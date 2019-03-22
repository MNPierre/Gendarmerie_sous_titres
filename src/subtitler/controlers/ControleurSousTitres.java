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
	}
		
	@FXML
    void modifierSousTitresOnClick(ActionEvent event) {
		actualSpeech.setText(commentaireModifInput.getText());
		MainControler.controleur.updatebarreSubtitle();
		MainControler.modifySubtitlesStage.close();
		//System.out.println(MainControler.subtitles.getSubtitles());
    }

}


