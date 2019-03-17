import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.event.ChangeEvent;

import com.sun.javafx.scene.EventHandlerProperties;

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
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
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
	MediaView video;
	ImageView image_bouton;
	
	ComboBox<String> personneInput;

	File subtitleFile;
	SubtitlesList subtitles;

	ObservableList<String> personnes;
	
	Rectangle fond_bouton;
	Rectangle barre_fond;
    Rectangle barre_lecture;
    
    ArrayList<Subtitle> subtitlesToShow = new ArrayList<Subtitle>();
    Pane paneTextToShow;
    
    boolean asSetTime = false;
	
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
	
	private void updateVideo() {
		double currentTime = player.getCurrentTime().toMillis();
    	
    	if(!asSetTime) {
    		asSetTime=true;
    		videoPlayEnd.setText(Subtitle.MillisecondsToString((long)player.getTotalDuration().toMillis()));
    	}
    	
    	if(currentTime<Decoder.StringToMillisecond(videoPlayStart.textProperty().get()))
    		player.seek( Duration.millis(Decoder.StringToMillisecond(videoPlayStart.textProperty().get())) );
    	
    	if(currentTime>Decoder.StringToMillisecond(videoPlayEnd.textProperty().get())) {
    		player.seek( Duration.millis(Decoder.StringToMillisecond(videoPlayEnd.textProperty().get())) );
    		player.pause();
    	}
    	
        barre_lecture.setWidth( (currentTime-Decoder.StringToMillisecond(videoPlayStart.textProperty().get())) /(Decoder.StringToMillisecond(videoPlayEnd.textProperty().get())-Decoder.StringToMillisecond(videoPlayStart.textProperty().get()))*barre_fond.getWidth());
        videoTime.setText(Subtitle.MillisecondsToString((long)currentTime));
        
        //Affichage des soutitres
        for(Subtitle sub:subtitles.getSubtitles()) {
        	
        	//Ajout des sous-titres a afficher
        	if(sub.getTimeStart()<= currentTime && sub.getTimeStop()>currentTime) {
        		if(!subtitlesToShow.contains(sub)) {
        			subtitlesToShow.add(sub);
        			for(Speech speech:sub.getContenu()) {
        				Label text = new Label(speech.getText());
        				text.setId(""+speech.getId());
        				text.setScaleX(2);
        				text.setScaleY(2);
        				for(Style style:subtitles.getStyles()) {
        					if(style.getNarrator().equals(speech.getAuthor())) {
        						text.setStyle("-fx-text-fill : "+style.getColor()+"; -fx-alignment: center; -fx-background-color: none;");
        						text.backgroundProperty().set(Background.EMPTY);
        						break;
        					}
        				}
        				
        				paneTextToShow.getChildren().add(text);
        			}
        		}
        	//Suppression des sous-titres a ne plus afficher
        	}else {
        		if(subtitlesToShow.contains(sub)) {
        			subtitlesToShow.remove(sub);
        			
        			List<Node> nodeToRemove = new ArrayList<Node>();
        			
        			for(Node node:paneTextToShow.getChildren()) {
        				boolean textExist=false;
        				
        				for(Speech speech:sub.getContenu()) {
        					if(node.getId().equals(" "+speech.getId())) {
        						textExist=true;
        						break;
        					}
        				}
        				
        				if(!textExist) {
        					nodeToRemove.add(node);
        				}
        			}
        			
        			for(Node node:nodeToRemove) {
        				paneTextToShow.getChildren().remove(node);
        			}
        			
        		}
        	}
        	
        }
        
        //Edition de la position des sous-titres
        int labelNum = 1;
        for(Node node:paneTextToShow.getChildren()) {
        	((Label)node).setLayoutX(0);
        	((Label)node).setMinWidth(video.getFitWidth());
        	((Label)node).setLayoutY(labelNum*25+paneTextToShow.getMinHeight()/2);
        	labelNum++;
        	
        }
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
		video = new MediaView(player);
		debutInput = new TextField();
		panePrincipal.getChildren().add(video);
		videoSlider = new Slider();
		subtitles = null;
		try {
			subtitles = Decoder.Decode("Final.xml");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		PaneVideoControl = new Pane();
		paneTextToShow = new Pane();
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
		panePrincipal.getChildren().add(paneTextToShow);

		video.setFitWidth(950);
		video.setFitHeight(650);
		video.setLayoutY(4);
		video.setLayoutX(118);
		video.getMediaPlayer().play();

		paneTextToShow.setLayoutX(video.getLayoutX());
		paneTextToShow.setLayoutY(video.getLayoutY());
		
		paneTextToShow.setMinWidth(video.getFitWidth());
		paneTextToShow.setMinHeight(video.getFitHeight()-120);
		
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
		
		videoPlayStart.setText("00:00:00.000");
		videoPlayEnd.setText("00:00:01.000");


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
			paneTextToShow.setOnMouseClicked(new EventHandler<MouseEvent>(){
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
        
        fonctions.getChildren().add(barres);
        barres.getChildren().add(barre_fond);
        barres.getChildren().add(barre_lecture);
        
        

        //Listener du temps de la video
        player.currentTimeProperty().addListener(new ChangeListener(){
            @Override public void changed(ObservableValue o, Object oldVal, Object newVal){
            	updateVideo();
            }
        });
        
        //Event Changement du temps de debut de la video
        videoPlayStart.onKeyPressedProperty().addListener(new ChangeListener(){
			@Override public void changed(ObservableValue o, Object oldVal, Object newVal){
				updateVideo();
			}
		});
        
        //Event Changement du temps de fin de la video
        videoPlayEnd.onKeyPressedProperty().addListener(new ChangeListener(){
			@Override public void changed(ObservableValue o, Object oldVal, Object newVal){
				updateVideo();
			}
		});
        
        //Event changement du temps de lecture de la video
        barres.setOnMouseClicked(new EventHandler<MouseEvent>(){
            public void handle(MouseEvent me){

                player.seek( Duration.millis(me.getX()/barre_fond.getWidth()*(Decoder.StringToMillisecond(videoPlayEnd.textProperty().get()) - Decoder.StringToMillisecond(videoPlayStart.textProperty().get()) ) ).add(Duration.millis( Decoder.StringToMillisecond(videoPlayStart.textProperty().get()) ) )  );

                barre_lecture.setWidth((me.getX()/barre_fond.getWidth()*fichierVideo.getDuration().toMillis()) / (player.getTotalDuration().toMillis())*barre_fond.getWidth());
            }
        });
        
        /*
        debutInput.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				debutInput.setText(videoTime.getText());
			}
		});
        */
	}

}
