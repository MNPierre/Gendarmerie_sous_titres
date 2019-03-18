
import java.net.URL;
import java.util.ResourceBundle;

import Subtitles.Style;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class ControleurPersonne implements Initializable{

	@FXML
	private ComboBox<String> modifPersonneInput;

	@FXML
	private ColorPicker colorPersonneInput;

	@FXML
	private TextField ajoutPersonneInput;

	@FXML
	private Button ajoutPersonneButton;

	@FXML
	private Button deletePersonneButton;

	@FXML
	private Button modifValiderPersonneButton;


	@FXML
	void addPersonneOnClick(ActionEvent event) {
		Controleur.subtitles.addStyle(new Style(ajoutPersonneInput.getText(), "#FFFFFF"));
		modifPersonneInput.setItems(Controleur.subtitles.getNarrators());
	}
	
	@FXML
    void validerModifPersonne(ActionEvent event) {
		Controleur.subtitles.changeColor(modifPersonneInput.getValue(), String.format("#%02X%02X%02X", 
				((int)colorPersonneInput.getValue().getRed())*255,
				((int)colorPersonneInput.getValue().getGreen())*255,
				((int)colorPersonneInput.getValue().getBlue())*255
				));
		System.out.println(Controleur.subtitles.getStyles());
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		modifPersonneInput.setItems(Controleur.subtitles.getNarrators());
		
		modifPersonneInput.valueProperty().addListener(new InvalidationListener() {
			
			@Override
			public void invalidated(Observable observable) {
				colorPersonneInput.setValue(Color.web((Controleur.subtitles.searchColor(modifPersonneInput.getValue()))));
			}
		});
	}
}
