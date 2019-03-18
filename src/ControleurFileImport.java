import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

import XML.Decoder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.scene.control.Alert.AlertType;

public class ControleurFileImport implements Initializable{

	@FXML
    private TextField textFieldFile;

    @FXML
    private TextField textFieldSubtitlesFile;
    
    Alert alert;
    
    DirectoryChooser directoryChooserMedia;
    DirectoryChooser directoryChooserSubtitles;

    @FXML
    void buttonImportFile(ActionEvent event) {
    	textFieldFile.setText(directoryChooserMedia.showDialog(Controleur.fileImportStage).getAbsoluteFile().toString());
    }

    @FXML
    void buttonImportSubtitlesFile(ActionEvent event) {
    	textFieldSubtitlesFile.setText(directoryChooserSubtitles.showDialog(Controleur.fileImportStage).getAbsoluteFile().toString());
    }

    @FXML
    void buttonValidate(ActionEvent event) {
    	try {
			Controleur.subtitles = Decoder.Decode(textFieldSubtitlesFile.getText());
		} catch (FileNotFoundException e) {
			alert.setContentText("Le fichier \""+textFieldSubtitlesFile.getText()+"\" est introuvable");
			e.printStackTrace();
		}
    	
    	Controleur.video.setMediaPlayer(new MediaPlayer(new Media(textFieldFile.getText())));
    	
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		alert = new Alert(AlertType.ERROR);
		directoryChooserMedia = new DirectoryChooser();
		directoryChooserSubtitles = new DirectoryChooser();
		
		//TODO make .xml et .wav to be imported
	}

}
