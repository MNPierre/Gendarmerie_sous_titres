package subtitler.controlers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import subtitler.utils.ConversionStringMilli;
import subtitler.utils.modifSubtitleUtils;

public class ControleurModifOneSubtitle implements Initializable{

	@FXML
	private TextArea textModifierOneSubtitle;

	@FXML
	private TextField startModifierOneSubtitle;

	@FXML
	private TextField stopModifierOneSubtitle;

	@FXML
	void supprimerModifierOneSubtitle(ActionEvent event) {
		MainControler.controleur.subtitles.getSubtitles().remove(modifSubtitleUtils.selectedSubtitle);
		MainControler.controleur.updatebarreSubtitle();
	}

	@FXML
	void validerModifierOneSubtitle(ActionEvent event) {
		modifSubtitleUtils.selectedSubtitle.getContenu().get(0).setText(textModifierOneSubtitle.getText());
		modifSubtitleUtils.selectedSubtitle.setTimeStart(ConversionStringMilli.StringToMillisecond(startModifierOneSubtitle.getText()));
		modifSubtitleUtils.selectedSubtitle.setTimeStop(ConversionStringMilli.StringToMillisecond(stopModifierOneSubtitle.getText()));
		MainControler.controleur.updatebarreSubtitle();
		modifSubtitleUtils.modifOneSubtitleStage.close();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		textModifierOneSubtitle.setWrapText(true);
		textModifierOneSubtitle.setText(modifSubtitleUtils.selectedSubtitle.getContenu().get(0).getText());
		startModifierOneSubtitle.setText(ConversionStringMilli.MillisecondsToString(modifSubtitleUtils.selectedSubtitle.getTimeStart()));
		stopModifierOneSubtitle.setText(ConversionStringMilli.MillisecondsToString(modifSubtitleUtils.selectedSubtitle.getTimeStop()));
	
	}
}
