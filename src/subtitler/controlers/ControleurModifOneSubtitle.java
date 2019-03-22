package subtitler.controlers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import subtitler.utils.ConversionStringMilli;

public class ControleurModifOneSubtitle implements Initializable{

	@FXML
	private TextArea textModifierOneSubtitle;

	@FXML
	private TextField startModifierOneSubtitle;

	@FXML
	private TextField stopModifierOneSubtitle;

	@FXML
	void supprimerModifierOneSubtitle(ActionEvent event) {

	}

	@FXML
	void validerModifierOneSubtitle(ActionEvent event) {
		MainControler.controleur.selectedSubtitle.getContenu().get(0).setText(textModifierOneSubtitle.getText());
		MainControler.controleur.selectedSubtitle.setTimeStart(ConversionStringMilli.StringToMillisecond(startModifierOneSubtitle.getText()));
		MainControler.controleur.selectedSubtitle.setTimeStop(ConversionStringMilli.StringToMillisecond(stopModifierOneSubtitle.getText()));
		MainControler.controleur.updatebarreSubtitle();
		MainControler.controleur.modifOneSubtitleStage.close();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		textModifierOneSubtitle.setWrapText(true);
		textModifierOneSubtitle.setText(MainControler.controleur.selectedSubtitle.getContenu().get(0).getText());
		startModifierOneSubtitle.setText(ConversionStringMilli.MillisecondsToString(MainControler.controleur.selectedSubtitle.getTimeStart()));
		stopModifierOneSubtitle.setText(ConversionStringMilli.MillisecondsToString(MainControler.controleur.selectedSubtitle.getTimeStop()));
	
	}
}
