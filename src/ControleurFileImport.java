import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

import XML.Decoder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;

public class ControleurFileImport implements Initializable{

	@FXML
    private TextField textFieldFile;

    @FXML
    private TextField textFieldSubtitlesFile;
    
    Alert alert;
    
    FileChooser fileChooserMedia;
    FileChooser fileChooserSubtitles;

    @FXML
    void buttonImportFile(ActionEvent event) {
		textFieldFile.setText(fileChooserMedia.showOpenDialog(Controleur.fileImportStage).getAbsolutePath());
    }

    @FXML
    void buttonImportSubtitlesFile(ActionEvent event) {
    	textFieldSubtitlesFile.setText(fileChooserSubtitles.showOpenDialog(Controleur.fileImportStage).getAbsoluteFile().toString());
    }

    @FXML
    void buttonValidate(ActionEvent event) {
    	String xmlFile = textFieldSubtitlesFile.getText();
    	String file = textFieldFile.getText();
    	
    	if(!(new File(xmlFile).exists())) {
    		xmlFile = null;
		}
    	
    	if(!(new File(file).exists())) {
			alert.setContentText("Le fichier \""+file+"\" est introuvable");
			alert.show();
		}else {
			Controleur.setNewVideoXml(file, xmlFile);
			Controleur.fileImportStage.close();
		}
    	
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		alert = new Alert(AlertType.ERROR);
		fileChooserMedia = new FileChooser();
		fileChooserSubtitles = new FileChooser();
		
		fileChooserMedia.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("Tout types de fichier supporter",
						"*.wav", "*.mp3", "*.mp4", "*.m4a", "*.m4v", "*.mp4", "*.m4a", "*.m4v", "*.aif", "*.aiff", "*.fxm", "*.m3u8", "*.flv"), 
			       new FileChooser.ExtensionFilter("WAV", "*.wav"), 
			       new FileChooser.ExtensionFilter("MP3", "*.mp3"),
			       new FileChooser.ExtensionFilter("MP4", "*.mp4", "*.m4a", "*.m4v"),
			       new FileChooser.ExtensionFilter("AIF", "*.aif", "*.aiff"),
			       new FileChooser.ExtensionFilter("FXM", "*.fxm"),
			       new FileChooser.ExtensionFilter("HLS", "*.m3u8"),
			       new FileChooser.ExtensionFilter("FLV", "*.flv"),
			       new FileChooser.ExtensionFilter("Tout types de fichier", "*.*"));
		
		fileChooserSubtitles.getExtensionFilters().add(
				new FileChooser.ExtensionFilter("XML", "*.xml"));
	}

}
