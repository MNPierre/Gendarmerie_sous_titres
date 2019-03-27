package subtitler.controlers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import subtitler.subtitles.Style;
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
	private Button modifValiderPersonneButton;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		modifPersonneInput.setItems(MainControler.subtitles.getNarrators());

		modifPersonneInput.valueProperty().addListener(new InvalidationListener() {

			@Override
			public void invalidated(Observable observable) {
				if(((SimpleObjectProperty)observable).getValue()!=null)
					colorPersonneInput.setValue(javafx.scene.paint.Color.web(MainControler.subtitles.searchColor(modifPersonneInput.getValue()).getColor()));
			}
		});


	}

	
	
	@FXML
	void validerModifPersonne(ActionEvent event) {
		if(modifPersonneInput.getValue() == null) {
			if(MainControler.subtitles.getNarrators().contains(ajoutPersonneInput.getText())) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("La personne existe déjà");
				alert.show();
			}else if (ajoutPersonneInput.getText().replaceAll("[ ]", "").equals("")) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("La personne n'a pas de nom");
				alert.show();
			}
			else {			
				MainControler.subtitles.addStyle(new Style(ajoutPersonneInput.getText(), "#"+colorPersonneInput.getValue().toString().substring(2, 8).toUpperCase()));
				modifPersonneInput.setItems(MainControler.subtitles.getNarrators());
				ajoutPersonneInput.setText("");
			}
		}else {
			String color = "#"+colorPersonneInput.getValue().toString().substring(2, 8).toUpperCase();
			MainControler.subtitles.changeColor(modifPersonneInput.getValue(), color);
			MainControler.subtitles.searchColor(modifPersonneInput.getValue()).setColor(color);
		}
		MainControler.fillListePersonne();
		MainControler.updatebarreSubtitle();
	}

	@FXML
	void supprimerPersonne(ActionEvent event) {
		if(modifPersonneInput.getValue() != null) {
			MainControler.subtitles.deleteByName(modifPersonneInput.getValue());
			// TODO fonction Remove ne marche pas sur les Collections. On ne peut pas supprimer de sous-titres.
			modifPersonneInput.setItems(MainControler.subtitles.getNarrators());
			MainControler.controleur.updatebarreSubtitle();
			MainControler.controleur.updateVideo();
		}
	}

}
