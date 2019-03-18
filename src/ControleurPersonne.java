
import java.awt.Color;
import java.net.URL;
import java.util.ResourceBundle;

import Subtitles.Style;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;


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
	private Button modifValiderPersonneButton;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		modifPersonneInput.setItems(Controleur.subtitles.getNarrators());
		
		modifPersonneInput.valueProperty().addListener(new InvalidationListener() {

			@Override
			public void invalidated(Observable observable) {
				colorPersonneInput.setValue(javafx.scene.paint.Color.web(Controleur.subtitles.searchColor(modifPersonneInput.getValue()).getColor()));
				System.out.println(javafx.scene.paint.Color.web(Controleur.subtitles.searchColor(modifPersonneInput.getValue()).getColor())+" :::: "+Controleur.subtitles.searchColor(modifPersonneInput.getValue().toString()));
			}
		});
		
		
	}
	
	@FXML
	void addPersonneOnClick(ActionEvent event) {
		if(Controleur.subtitles.getNarrators().contains(ajoutPersonneInput.getText())) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("La personne existe déjà");
			alert.show();
		}else {			
			Controleur.subtitles.addStyle(new Style(ajoutPersonneInput.getText(), "#FFFFFF"));
			modifPersonneInput.setItems(Controleur.subtitles.getNarrators());
		}
	}
	
	@FXML
	void validerModifPersonne(ActionEvent event) {
		//Controleur.subtitles.changeColor(modifPersonneInput.getValue(),toHexString(colorPersonneInput.getValue()));
		//Controleur.subtitles.searchColor(modifPersonneInput.getValue()).setColor();
		/*TODO*/
		
		
		for(Style style:Controleur.subtitles.getStyles()) {
			if(modifPersonneInput.getValue().equals(style.getNarrator())) {
				style.setColor("#"+colorPersonneInput.getValue().toString().substring(2, 8).toUpperCase());
				System.out.println("#"+colorPersonneInput.getValue().toString().substring(2, 8).toUpperCase());
			}
		}
	}
	
	@FXML
    void supprimerPersonne(ActionEvent event) {
		Controleur.subtitles.deleteByName(modifPersonneInput.getValue());
		modifPersonneInput.setItems(Controleur.subtitles.getNarrators());
    }
	
	public final static String toHexString(Color colour) throws NullPointerException {
		  String hexColour = Integer.toHexString(colour.getRGB() & 0xffffff);
		  if (hexColour.length() < 6) {
		    hexColour = "000000".substring(0, 6 - hexColour.length()) + hexColour;
		  }
		  return "#" + hexColour;
		}

}
