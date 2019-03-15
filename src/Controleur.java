import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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
	
    @FXML
    private TextField wordToSearchBox;
    
    @FXML
    private TextField videoPlayStart;

    @FXML
    private TextField videoPlayEnd;

    Image img_play;
	Image img_pause;
	
	MediaPlayer player;
	ImageView image_bouton;
	
	ComboBox<String> personneInput;

	File subtitleFile;
	SubtitlesList subtitles;

	ObservableList<String> personnes;
	
	Rectangle fond_bouton;
	Rectangle barre_fond;
    Rectangle barre_lecture;
	
	@FXML
    void showEditSpeakers(ActionEvent event) {

    }

    @FXML
    void showFileImport(ActionEvent event) {

    }
    
    @FXML
    void EditerPersonnes(ActionEvent event) {
    	System.out.println("Ouverture fenêtre\n");
    }

	@FXML
	void validSubititlesOnClick(ActionEvent event) throws IOException {
		//addSubtitles(subtitlesInput.getText());
	}

	@FXML
	void saveSubtitlesOnClick(ActionEvent event) throws IOException {
		//saveSubtitles(subtitleFile);
	}

	
	private void playPauseVideo() {
		//pause
		if(player.getStatus() == Status.PLAYING){
			image_bouton.setImage(img_play);
			player.pause();
			
		//play
		}else{
			image_bouton.setImage(img_pause);
			player.play();
		}
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


	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {


		//D�clarations
		//video.setMediaPlayer(value);
		File path = new File("Sans titre.mp4");
		System.out.println("test : "+path.getAbsoluteFile());
		Media fichierVideo = new Media(path.toURI().toString());
		player = new MediaPlayer(fichierVideo);
		MediaView video = new MediaView(player);
		debutInput = new TextField();
		panePrincipal.getChildren().add(video);
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
		videoTimeMax.setLayoutY(ajouterButton.getLayoutY()+570);
		
		personneInput.setLayoutX(ajouterButton.getLayoutX()+298);
		personneInput.setLayoutY(ajouterButton.getLayoutY()+127);

		debutInput.setLayoutX(ajouterButton.getLayoutX()+298);
		debutInput.setLayoutY(ajouterButton.getLayoutY()+62);

		finInput.setLayoutX(ajouterButton.getLayoutX()+298);
		finInput.setLayoutY(ajouterButton.getLayoutY()+95);
		
		subtitlesInput.setLayoutX(ajouterButton.getLayoutX());
		subtitlesInput.setLayoutY(ajouterButton.getLayoutY()+55);
		subtitlesInput.setPrefWidth(275);
		subtitlesInput.setPrefHeight(96);

		Group fonctions = new Group();
		Rectangle fond = new Rectangle(video.getFitWidth(),30);
		fond.setOpacity(0.5);
		fond.setLayoutX(video.getLayoutX());
		fond.setLayoutY(538);
		fonctions.getChildren().add(fond);
		//fonctions.setTranslateY(video.getFitHeight()-30);
		panePrincipal.getChildren().add(fonctions);
		panePrincipal.getChildren().add(videoTime);
		panePrincipal.getChildren().add(videoTimeMax);
		
		


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
		fond_bouton = new Rectangle(30,30);
		fond_bouton.setArcHeight(5);
		fond_bouton.setArcWidth(5);
		
		try {
			img_play = new Image(new File("PLAY.png").toURI().toURL().toString());
			img_pause = new Image(new File("PAUSE.png").toURI().toURL().toString());

			image_bouton = new ImageView(img_pause);
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
					playPauseVideo();
				}
			});
			
			//Event clique sur la video
	        video.setOnMouseClicked(new EventHandler<MouseEvent>(){
				public void handle(MouseEvent me){
					playPauseVideo();
				}
			});
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		fonctions.getChildren().add(play_pause);
		
		Group barres = new Group();
        barres.setTranslateX(video.getLayoutX() + 35);
        barres.setTranslateY(541);
        barres.setCursor(Cursor.HAND);
        barre_fond = new Rectangle(video.getFitWidth()-60, 22);
        barre_lecture = new Rectangle(0, 22);
        
        barre_lecture.setFill(Color.BLUE);
        barre_fond.setFill(Color.BROWN);
       // barre_lecture_supprimer1.setFill(Color.color(0.251, 0.251, 0.251, 0.5));
       // barre_lecture_supprimer2.setFill(Color.color(0.251, 0.251, 0.251, 0.5));
        
        fonctions.getChildren().add(barres);
        barres.getChildren().add(barre_fond);
        barres.getChildren().add(barre_lecture);
        
       

        //On ajuste la longueure de la barre de lecture au taux de lecture de la vidéo
        player.currentTimeProperty().addListener(new ChangeListener(){
            @Override public void changed(ObservableValue o, Object oldVal, Object newVal){
            	
            	if(player.getCurrentTime().toMillis()<Decoder.StringToMillisecond(videoPlayStart.textProperty().get()))
            		player.seek( Duration.millis(Decoder.StringToMillisecond(videoPlayStart.textProperty().get())) );
            	
            	if(player.getCurrentTime().toMillis()>Decoder.StringToMillisecond(videoPlayEnd.textProperty().get())) {
            		player.seek( Duration.millis(Decoder.StringToMillisecond(videoPlayEnd.textProperty().get())) );
            		player.pause();
            	}
            	
            	
                barre_lecture.setWidth( (player.getCurrentTime().toMillis()-Decoder.StringToMillisecond(videoPlayStart.textProperty().get())) /(Decoder.StringToMillisecond(videoPlayEnd.textProperty().get())-Decoder.StringToMillisecond(videoPlayStart.textProperty().get()))*barre_fond.getWidth());
                videoTime.setText(Subtitle.MillisecondsToString((long)player.getCurrentTime().toMillis()));
                
            }
        });
        
        
        barres.setOnMouseClicked(new EventHandler<MouseEvent>(){
            public void handle(MouseEvent me){

                player.seek( Duration.millis(me.getX()/barre_fond.getWidth()*(Decoder.StringToMillisecond(videoPlayEnd.textProperty().get()) - Decoder.StringToMillisecond(videoPlayStart.textProperty().get()) ) ).add(Duration.millis( Decoder.StringToMillisecond(videoPlayStart.textProperty().get()) ) )  );

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

}
