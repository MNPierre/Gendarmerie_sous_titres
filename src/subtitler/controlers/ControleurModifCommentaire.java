package subtitler.controlers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import subtitler.utils.ConversionStringMilli;
import subtitler.utils.MarqueurCommentaire;

public class ControleurModifCommentaire implements Initializable{

    @FXML
    private TextArea textCommentaire;

    @FXML
    private TextField timeStartText;

    @FXML
    void supprimerModifier(ActionEvent event) {
    	MainControler.commentaires.remove(MarqueurCommentaire.commentaireToEdit);
    	MarqueurCommentaire.modifCommentStage.close();
    	MainControler.updateVideo();
    }

    @FXML
    void validerModifier(ActionEvent event) {
    	MarqueurCommentaire.commentaireToEdit.setTime(ConversionStringMilli.StringToMillisecond(timeStartText.getText()));
    	MarqueurCommentaire.modifCommentStage.close();
    	MainControler.updateVideo();
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		textCommentaire.setText(MarqueurCommentaire.commentaireToEdit.getTextArea().getText());
		MarqueurCommentaire.commentaireToEdit.getTextArea().textProperty().bind(textCommentaire.textProperty());
		timeStartText.setText( ConversionStringMilli.MillisecondsToString(MarqueurCommentaire.commentaireToEdit.getTime()));
	}

}
