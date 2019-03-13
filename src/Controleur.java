import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Subtitles.Speech;
import Subtitles.Style;
import Subtitles.Subtitle;
import Subtitles.SubtitlesList;
import XML.Decoder;
import XML.Encoder;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
public class Controleur implements Initializable {

	@FXML
	private Pane paneVideo;

	@FXML
	private Pane panePrincipal;

	private Pane PaneVideoControl;


	private Text timeOutput;


	private Slider videoSlider;


	private TextArea subtitlesInput;


	private TextField debutInput;


	private TextField finInput;
	
	private Label videoTime;
	
	private Label videoTimeMax;

	@FXML
	private Button ajouterButton;

	@FXML
	private Button sauvegarderButton;



	ComboBox<String> personneInput;

	File subtitleFile;
	SubtitlesList subtitles;

	ObservableList<String> personnes;

	@FXML
	void validSubititlesOnClick(ActionEvent event) throws IOException {
		//addSubtitles(subtitlesInput.getText());
	}

	@FXML
	void saveSubtitlesOnClick(ActionEvent event) throws IOException {
		//saveSubtitles(subtitleFile);
	}



	@Override
	public void initialize(URL location, ResourceBundle resources) {


		//D�clarations
		//video.setMediaPlayer(value);
		File path = new File("Sans titre.mp4");
		System.out.println("test : "+path.getAbsoluteFile());
		Media fichierVideo = new Media(path.toURI().toString());
		MediaPlayer player = new MediaPlayer(fichierVideo);
		MediaView video = new MediaView(player);
		debutInput = new TextField();
		paneVideo.getChildren().add(video);
		videoSlider = new Slider();
		subtitles = new SubtitlesList();
		PaneVideoControl = new Pane();
		timeOutput = new Text();
		videoTime = new Label();
		videoTimeMax = new Label();
		//long videoTimeValue;
		

		debutInput = new TextField();
		finInput = new TextField();
		subtitlesInput = new TextArea();

		personneInput = new ComboBox<>();

		personnes = FXCollections.observableArrayList("personne1","personne2");

		personneInput.setItems(personnes);

		//ajout des textfiels plus ajout au pan

		panePrincipal.getChildren().add(personneInput);
		panePrincipal.getChildren().add(debutInput);
		panePrincipal.getChildren().add(finInput);
		panePrincipal.getChildren().add(subtitlesInput);

		paneVideo.setMaxWidth(480);
		paneVideo.setMaxHeight(200);
		video.setFitWidth(950);
		video.setFitHeight(650);
		video.setLayoutY(4);
		video.setLayoutX(118);
		video.getMediaPlayer().play();

		videoTime.setText("00:00:00");
		videoTime.setLayoutX(video.getLayoutX());
		videoTime.setLayoutY(570);
		
		//System.out.println(fichierVideo.getDuration());
		videoTimeMax.setText("/ "+fichierVideo.getDuration().toMillis());
		videoTimeMax.setLayoutX(video.getLayoutX()+75);
		videoTimeMax.setLayoutY(570);

		personneInput.setLayoutX(698);
		personneInput.setLayoutY(777);

		debutInput.setLayoutX(698);
		debutInput.setLayoutY(712);

		finInput.setLayoutX(698);
		finInput.setLayoutY(745);

		Group fonctions = new Group();
		Rectangle fond = new Rectangle(video.getFitWidth(),30);
		fond.setOpacity(0.5);
		fond.setLayoutX(video.getLayoutX());
		fond.setLayoutY(538);
		fonctions.getChildren().add(fond);
		//fonctions.setTranslateY(video.getFitHeight()-30);
		paneVideo.getChildren().add(fonctions);
		paneVideo.getChildren().add(videoTime);
		paneVideo.getChildren().add(videoTimeMax);
		
		subtitlesInput.setLayoutX(288);
		subtitlesInput.setLayoutY(705);
		subtitlesInput.setPrefWidth(275);
		subtitlesInput.setPrefHeight(96);


		videoSlider.valueProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable ov) {
				if (videoSlider.isValueChanging()) {
					// multiply duration by percentage calculated by slider position
					player.seek(fichierVideo.getDuration().multiply(videoSlider.getValue() / 100.0));
				}
			}
		});


		//création du bouton play/pause
		Group play_pause = new Group();
		Rectangle fond_bouton = new Rectangle(30,30);
		fond_bouton.setArcHeight(5);
		fond_bouton.setArcWidth(5);
		Image img_play = new Image("file:///home/etudiants/info/rauriac/git/Gendarmerie_sous_titres/PAUSE.png");
		Image img_pause = new Image("file:///home/etudiants/info/rauriac/git/Gendarmerie_sous_titres/PLAY.png");
		ImageView image_bouton = new ImageView(img_play);
		image_bouton.setFitWidth(22);;
		image_bouton.setLayoutX(4);
		image_bouton.setLayoutY(4);
		image_bouton.setFitHeight(22);
		play_pause.getChildren().add(fond_bouton);
		play_pause.getChildren().add(image_bouton);
		play_pause.setTranslateX(video.getLayoutX());
		play_pause.setTranslateY(538);
		play_pause.setCursor(Cursor.HAND);
		//Quand on clique sur le bouton play/pause, on démarre ou on arrête la vidéo
		play_pause.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent me){
				if(player.getStatus() == Status.PLAYING){//pause
					image_bouton.setImage(img_pause);
					player.pause();
				}
				else{//play
					image_bouton.setImage(img_play);
					player.play();
				}
			}
		});
		fonctions.getChildren().add(play_pause);
		
		Group barres = new Group();
        barres.setTranslateX(video.getLayoutX() + 35);
        barres.setTranslateY(541);
        barres.setCursor(Cursor.HAND);
        Rectangle barre_fond = new Rectangle(video.getFitWidth()-60, 22);
        Rectangle barre_lecture = new Rectangle(0, 22);
        barres.getChildren().add(barre_fond);
        barres.getChildren().add(barre_lecture);
        barre_lecture.setFill(Color.BLUE);
        barre_fond.setFill(Color.BROWN);
        fonctions.getChildren().add(barres);

        //On ajuste la longueure de la barre de lecture au taux de lecture de la vidéo
        player.currentTimeProperty().addListener(new ChangeListener(){
            @Override public void changed(ObservableValue o, Object oldVal, Object newVal){
                barre_lecture.setWidth(player.getCurrentTime().toMillis()/(player.getTotalDuration().toMillis())*barre_fond.getWidth());
                videoTime.setText(Subtitle.MillisecondsToString((long)player.getCurrentTime().toMillis()));
            }
        });
        
        barres.setOnMouseClicked(new EventHandler<MouseEvent>(){
            public void handle(MouseEvent me){

                player.seek(Duration.millis(me.getX()/barre_fond.getWidth()*fichierVideo.getDuration().toMillis()));

                barre_lecture.setWidth((me.getX()/barre_fond.getWidth()*fichierVideo.getDuration().toMillis()) / (player.getTotalDuration().toMillis())*barre_fond.getWidth());
                
            }

        });
        //debutInput.textProperty().bind((player.currentTimeProperty()).asString());
        
        debutInput.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				debutInput.setText(videoTime.getText());
			}
        	
		});
        		
	}





	@FXML
	void ajouterCommentaire(ActionEvent event) {
		if(debutInput.getText().equals("") || finInput.getText().equals("") || personneInput.getValue() == null || subtitlesInput.getText().equals("")) {
			throw new Error("Remplir tous les champs");
		}

		Subtitle sub = subtitles.createSubtitle(Decoder.StringToMillisecond(debutInput.getText()), Decoder.StringToMillisecond(finInput.getText()));
		sub.addSpeech(new Speech(subtitlesInput.getText(), personneInput.getValue()));

		debutInput.setText("");
		finInput.setText("");
		subtitlesInput.setText("");
		personneInput.setValue(null);
		System.out.println(subtitles.getXml());
	}

	@FXML
	//TODO CATCH ERROR
	void sauvegarderOnClick(ActionEvent event) throws IOException{
		for(String p : personnes) {
			subtitles.addStyle(new Style(p, "#FFFFFF"));
		}

		Encoder encodeur = new Encoder(subtitles, "Final");

	}
}
