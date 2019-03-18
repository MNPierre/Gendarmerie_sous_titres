import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import Subtitles.Search;
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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
public class Controleur implements Initializable {


	@FXML
	private Pane panePrincipal;

	private static Slider videoSlider;


	private static TextArea subtitlesInput;


	private static TextField debutInput;


	private static TextField finInput;

	private static Label videoTime;

	private static Label videoTimeMax;

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


	static Image img_play;
	static Image img_pause;

	static MediaPlayer player;
	static MediaView video;
	static Media fichierVideo;
	static ImageView image_bouton;

	static ComboBox<String> personneInput;

	static File subtitleFile;
	static SubtitlesList subtitles;


	static Rectangle fond_bouton;
	static Rectangle barre_fond;
	static Rectangle barre_lecture;

	static Group fonctions ;
	static Group barres ;
	static Group play_pause ;
	static Rectangle fond;

	static ArrayList<Subtitle> subtitlesToShow = new ArrayList<Subtitle>();
	static Pane paneTextToShow;

	static Stage fileImportStage;

	static boolean asSetTime = false;
	static boolean doVideoAlreadyBeanLoad = false;

	protected static Controleur controleur;

	@FXML
	void showEditSpeakers(ActionEvent event) {
		if(player.getStatus() == Status.PLAYING) {
			playPauseVideo();
		}
		try { 
			AnchorPane root = (AnchorPane) FXMLLoader.load(new File("modifPersonne.fxml").toURI().toURL()); 
			Stage Ajouter = new Stage(); 
			Ajouter.setTitle("Modifier Personnes");
			Scene scene = new Scene(root, 640, 480); 
			Ajouter.setScene(scene); 
			Ajouter.show(); 
		} 
		catch (IOException e) {  
			e.printStackTrace(); 

		}
	}

	@FXML
	void modifSousTitres(ActionEvent event) {
		if(player.getStatus() == Status.PLAYING) {
			playPauseVideo();
		}
		if(subtitlesToShow.size()==0) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Aucun sous-titres en ce moment");
			alert.show();
		}else {
			try { 
				AnchorPane root = (AnchorPane) FXMLLoader.load(new File("modifSubtitle.fxml").toURI().toURL()); 
				Stage Ajouter = new Stage(); 
				Scene scene = new Scene(root, 640, 480); 
				Ajouter.setTitle("Modifier Sous-Titres");
				Ajouter.setScene(scene); 
				Ajouter.show(); 
			} 
			catch (IOException e) {  
				e.printStackTrace(); 
			}
		}
	}

	@FXML
	void showFileImport(ActionEvent event) {
		try {
			fileImportStage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			URL url = new File("src/FileImport.fxml").toURI().toURL();
			loader.setLocation(url);
			Parent root = FXMLLoader.load(url);
			Scene scene = new Scene(root,349,241);
			fileImportStage.setScene(scene);
			fileImportStage.show();


		} catch (IOException e) {
			e.printStackTrace();
		}
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


	private static void playPauseVideo() {
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
	}

	@FXML
	//TODO CATCH ERROR
	void sauvegarderOnClick(ActionEvent event) throws IOException{
		for(String p : subtitles.getNarrators()) {
			subtitles.addStyle(new Style(p, "#FFFFFF"));
		}

		Encoder encodeur = new Encoder(subtitles, "Final");

	}

	private static void updateVideo() {

		double currentTime = player.getCurrentTime().toMillis();

		if(!asSetTime) {
			asSetTime=true;
			Controleur.controleur.videoPlayEnd.setText(Subtitle.MillisecondsToString((long)player.getTotalDuration().toMillis()));
		}

		if(currentTime<Decoder.StringToMillisecond(Controleur.controleur.videoPlayStart.textProperty().get()))
			player.seek( Duration.millis(Decoder.StringToMillisecond(Controleur.controleur.videoPlayStart.textProperty().get())) );

		if(currentTime>Decoder.StringToMillisecond(Controleur.controleur.videoPlayEnd.textProperty().get())) {
			player.seek( Duration.millis(Decoder.StringToMillisecond(Controleur.controleur.videoPlayEnd.textProperty().get())) );
			player.pause();
		}

		barre_lecture.setWidth( (currentTime-Decoder.StringToMillisecond(Controleur.controleur.videoPlayStart.textProperty().get())) /(Decoder.StringToMillisecond(Controleur.controleur.videoPlayEnd.textProperty().get())-Decoder.StringToMillisecond(Controleur.controleur.videoPlayStart.textProperty().get()))*barre_fond.getWidth());
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
							if(style.getNarrator() != null)
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

	static ChangeListener<Duration> listenerVideoTime;

	@SuppressWarnings("unchecked")
	public static void setNewVideoXml(String file, String xmlFile) {
		asSetTime = false;

		fichierVideo = new Media( new File(file).toURI().toString() );

		if(listenerVideoTime != null) {
			player.currentTimeProperty().removeListener(listenerVideoTime);
		}

		if(player != null)
			player.dispose();

		player = new MediaPlayer(fichierVideo);
		video = new MediaView(player);

		if(subtitlesToShow != null)
			subtitlesToShow.clear();

		if(paneTextToShow != null)
			paneTextToShow.getChildren().clear();

		if(xmlFile != null)
			try {
				subtitles = Decoder.Decode(xmlFile);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		else {
			subtitles = new SubtitlesList();
		}
		
		//long videoTimeValue;
		if(subtitles != null)
			for(Subtitle sub : Search.recherche("rafael", subtitles)){
				for (Speech sp : sub.getContenu()) {
					//System.out.println("début : " + Subtitle.MillisecondsToString(sub.getTimeStart()) + " fin : " + Subtitle.MillisecondsToString(sub.getTimeStop()) + "\n" + sp.getText());
				}

			}

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
		videoTimeMax.setLayoutY(Controleur.controleur.ajouterButton.getLayoutY()+570);

		personneInput.setLayoutX(Controleur.controleur.ajouterButton.getLayoutX()+298);
		personneInput.setLayoutY(Controleur.controleur.ajouterButton.getLayoutY()+127);
		personneInput.setItems(subtitles.getNarrators());

		debutInput.setLayoutX(Controleur.controleur.ajouterButton.getLayoutX()+298);
		debutInput.setLayoutY(Controleur.controleur.ajouterButton.getLayoutY()+62);

		finInput.setLayoutX(Controleur.controleur.ajouterButton.getLayoutX()+298);
		finInput.setLayoutY(Controleur.controleur.ajouterButton.getLayoutY()+95);

		subtitlesInput.setLayoutX(Controleur.controleur.ajouterButton.getLayoutX());
		subtitlesInput.setLayoutY(Controleur.controleur.ajouterButton.getLayoutY()+55);
		subtitlesInput.setPrefWidth(275);
		subtitlesInput.setPrefHeight(96);

		fond = new Rectangle(video.getFitWidth(),30);
		fond.setOpacity(0.5);
		fond.setLayoutX(video.getLayoutX());
		fond.setLayoutY(538);



		subtitles.getStyles().addListener(new InvalidationListener() {

			@Override
			public void invalidated(Observable observable) {
				personneInput.setItems(subtitles.getNarrators());
			}
		});

		//Listener du temps de la video
		listenerVideoTime = new ChangeListener<Duration>(){
			@Override
			public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
				Controleur.updateVideo();
			}
		};
		player.currentTimeProperty().addListener(listenerVideoTime);

		videoSlider.valueProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable ov) {
				if (videoSlider.isValueChanging()) {
					// multiply duration by percentage calculated by slider position
					player.seek(fichierVideo.getDuration().multiply(videoSlider.getValue() / 100.0));
				}
			}
		});


		//création du bouton play/pause
		fond_bouton = new Rectangle(30,30);
		fond_bouton.setArcHeight(5);
		fond_bouton.setArcWidth(5);

		image_bouton.setFitWidth(22);;
		image_bouton.setLayoutX(4);
		image_bouton.setLayoutY(4);
		image_bouton.setFitHeight(22);

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

		barres.setTranslateX(video.getLayoutX() + 35);
		barres.setTranslateY(541);
		barres.setCursor(Cursor.HAND);
		barre_fond = new Rectangle(video.getFitWidth()-60, 22);


		barre_lecture.setFill(Color.BLUE);
		barre_fond.setFill(Color.BROWN);

		//Event Changement du temps de debut de la video
		Controleur.controleur.videoPlayStart.onKeyPressedProperty().addListener(new ChangeListener(){
			@Override public void changed(ObservableValue o, Object oldVal, Object newVal){
				updateVideo();
			}
		});

		//Event Changement du temps de fin de la video
		Controleur.controleur.videoPlayEnd.onKeyPressedProperty().addListener(new ChangeListener(){
			@Override public void changed(ObservableValue o, Object oldVal, Object newVal){
				updateVideo();
			}
		});

		//Event changement du temps de lecture de la video
		barres.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent me){

				player.seek( Duration.millis(me.getX()/barre_fond.getWidth()*(Decoder.StringToMillisecond(Controleur.controleur.videoPlayEnd.textProperty().get()) - Decoder.StringToMillisecond(Controleur.controleur.videoPlayStart.textProperty().get()) ) ).add(Duration.millis( Decoder.StringToMillisecond(Controleur.controleur.videoPlayStart.textProperty().get()) ) )  );

				barre_lecture.setWidth((me.getX()/barre_fond.getWidth()*fichierVideo.getDuration().toMillis()) / (player.getTotalDuration().toMillis())*barre_fond.getWidth());
			}
		});


		if(!doVideoAlreadyBeanLoad) {
			doVideoAlreadyBeanLoad=true;

			//ajout des textfiels plus ajout au pan
			Controleur.controleur.panePrincipal.getChildren().add(personneInput);
			Controleur.controleur.panePrincipal.getChildren().add(debutInput);
			Controleur.controleur.panePrincipal.getChildren().add(finInput);
			Controleur.controleur.panePrincipal.getChildren().add(subtitlesInput);
			Controleur.controleur.panePrincipal.getChildren().add(paneTextToShow);

			fonctions.getChildren().add(fond);
			fonctions.getChildren().add(play_pause);
			fonctions.getChildren().add(barres);
			barres.getChildren().add(barre_fond);
			barres.getChildren().add(barre_lecture);

			play_pause.getChildren().add(fond_bouton);
			play_pause.getChildren().add(image_bouton);

			Controleur.controleur.panePrincipal.getChildren().add(fonctions);
			Controleur.controleur.panePrincipal.getChildren().add(videoTime);
			Controleur.controleur.panePrincipal.getChildren().add(videoTimeMax);

			Controleur.controleur.videoPlayStart.setText("00:00:00.000");
			Controleur.controleur.videoPlayEnd.setText("00:00:01.000");

		}

	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Controleur.controleur = this;
		//D�clarations
		//panePrincipal = new Pane();
		try {
			img_play = new Image(new File("PLAY.png").toURI().toURL().toString());
			img_pause = new Image(new File("PAUSE.png").toURI().toURL().toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		debutInput = new TextField();
		videoSlider = new Slider();
		subtitles = null;
		paneTextToShow = new Pane();
		paneTextToShow.setStyle("-fx-background-color: black;");
		videoTime = new Label();
		videoTimeMax = new Label();
		debutInput = new TextField();
		finInput = new TextField();
		subtitlesInput = new TextArea();
		personneInput = new ComboBox<>();
		fonctions = new Group();
		barres = new Group();
		play_pause = new Group();
		barre_lecture = new Rectangle(0, 22);
		image_bouton = new ImageView(img_pause);

		//setNewVideoAndXml("Sans titre.mp4", "Final.xml");

	}

}
