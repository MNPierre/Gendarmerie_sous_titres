package subtitler.utils;

import java.io.File;
import java.io.IOException;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class MarqueurCommentaire {

	Rectangle body;
	TextArea commentaire;
	long temps;
	boolean commentaireMouseOver;
	boolean commentaireBoxMouseOver;
	
	public static Stage modifCommentStage;
	
	public static MarqueurCommentaire commentaireToEdit;
	
	MarqueurCommentaire moi;

	public MarqueurCommentaire(String comment, long temps, double x, double y) {
		moi = this;
		body = new Rectangle();
		this.commentaire = new TextArea(comment);
		this.commentaire.setDisable(true);
		this.commentaire.setVisible(false);
		commentaire.setLayoutX(x+4);
		commentaire.setLayoutY(y+1);
		commentaire.setPrefWidth(200);
		commentaire.setWrapText(true);
		commentaire.setEditable(false);
		
		commentaireMouseOver = false;
		commentaireBoxMouseOver = false;

		this.temps=temps;
		body.setWidth(4);
		body.setHeight(9);
		body.setLayoutX(x);
		body.setLayoutY(y+1);
		body.setFill(Color.AQUA);

		body.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				commentaireBoxMouseOver=true;
				if(commentaireBoxMouseOver || commentaireMouseOver) {
					commentaire.setDisable(false);
					commentaire.setVisible(true);
				}

			}
		});

		body.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				commentaireBoxMouseOver = false;
				if(!commentaireBoxMouseOver && !commentaireMouseOver) {
					commentaire.setDisable(true);
					commentaire.setVisible(false);
				}

			}
		});
		
		body.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent me){
				if(MarqueurCommentaire.modifCommentStage != null)
					MarqueurCommentaire.modifCommentStage.close();
				
				commentaireToEdit = moi;
				AnchorPane root;
				try {
					root = (AnchorPane) FXMLLoader.load(new File("assets/modifCommentaire.fxml").toURI().toURL());
					modifCommentStage = new Stage(); 
					Scene scene = new Scene(root, 430, 360); 
					modifCommentStage.setResizable(false);
					modifCommentStage.setTitle("Modification commentaire");
					modifCommentStage.setScene(scene); 
					modifCommentStage.show();

				} catch (IOException e) {
					e.printStackTrace();
				} 
			}
		});
		
		commentaire.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				commentaireMouseOver=true;
				if(commentaireBoxMouseOver || commentaireMouseOver) {
					commentaire.setDisable(false);
					commentaire.setVisible(true);
				}

			}
		});

		commentaire.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				commentaireMouseOver = false;
				if(!commentaireBoxMouseOver && !commentaireMouseOver) {
					commentaire.setDisable(true);
					commentaire.setVisible(false);
				}

			}
		});
	}

	public TextArea getTextArea() {
		return commentaire;
	}
	
	public void setVisible(boolean value) {
		commentaire.setDisable(!value);
		body.setDisable(!value);
		body.setVisible(value);
	}

	public void setLayoutX(double x) {
		body.setLayoutX(x);
		commentaire.setLayoutX(x+4);
	}

	public void setLayoutY(double y) {
		body.setLayoutY(y);
		commentaire.setLayoutY(y);
	}

	public void setLayout(double x, double y) {
		setLayoutX(x);
		setLayoutY(y);
	}

	public long getTime() {
		return temps;
	}
	
	public void setTime(long time) {
		temps=time;
	}

	public void addToPane(Pane pane) {
		pane.getChildren().add(body);
		pane.getChildren().add(commentaire);
	}
	
	public String toString() {
		return "Time: "+getTime()+", Text: \""+commentaire.getText()+"\"";
	}

	public void removeFromPane(Pane pane) {
		pane.getChildren().remove(body);
		pane.getChildren().remove(commentaire);
	}
}
