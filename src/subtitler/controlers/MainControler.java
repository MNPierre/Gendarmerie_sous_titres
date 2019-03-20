package subtitler.controlers;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import subtitler.Main;
import subtitler.io.Decoder;
import subtitler.io.Encoder;
import subtitler.subtitles.Search;
import subtitler.subtitles.Speech;
import subtitler.subtitles.Style;
import subtitler.subtitles.Subtitle;
import subtitler.subtitles.SubtitlesList;
import subtitler.utils.ConversionStringMilli;
import subtitler.utils.makeBarreDraggable;
public class MainControler implements Initializable {


	@FXML
	private Pane panePrincipal;

	private static Slider videoSlider;


	private static TextArea subtitlesInput;


	private static TextField debutInput;


	private static TextField finInput;

	private static Label videoTime;

	private static Label videoTimeMax;

	@FXML
	private Group barreDeSelection1;

	@FXML
	private CheckBox zoomCheckBox;

	@FXML
	private Group barreDeSelection2;

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

	static Rectangle selectedZone;
	static Rectangle fond_bouton;
	static Rectangle barre_fond;
	static Rectangle barre_lecture;
	static Rectangle corpsBarreSelection1;
	static Rectangle corpsBarreSelection2;

	static Circle teteBarreSelection1;
	static Circle teteBarreSelection2;

	static Group fonctions ;
	static Group barres ;
	static Group play_pause ;
	static Rectangle fond;

	static ArrayList<Subtitle> subtitlesToShow = new ArrayList<Subtitle>();
	static Pane paneTextToShow;

	static Stage fileImportStage;
	static Stage modifySubtitlesStage;

	static boolean asSetTime = false;
	static boolean doVideoAlreadyBeanLoad = false;

	public static MainControler controleur;

	@FXML
	void showEditSpeakers(ActionEvent event) {
		if(player.getStatus() == Status.PLAYING) {
			playPauseVideo();
		}
		try { 
			AnchorPane root = (AnchorPane) FXMLLoader.load(new File("modifPersonne.fxml").toURI().toURL()); 
			modifySubtitlesStage = new Stage(); 
			modifySubtitlesStage.setTitle("Modifier Personnes");
			Scene scene = new Scene(root, 640, 480); 
			modifySubtitlesStage.setScene(scene); 
			modifySubtitlesStage.show();
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
			URL url = new File("FileImport.fxml").toURI().toURL();
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
			if(player.getCurrentTime().toMillis()>=ConversionStringMilli.StringToMillisecond(MainControler.controleur.videoPlayEnd.textProperty().get())) {
				player.seek( new Duration(ConversionStringMilli.StringToMillisecond(MainControler.controleur.videoPlayStart.textProperty().get())) );
			}
			player.play();
		}
	}

	@FXML
	void ajouterCommentaire(ActionEvent event) {
		if(debutInput.getText().equals("") || finInput.getText().equals("") || personneInput.getValue() == null || subtitlesInput.getText().equals("")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("Veuillez remplire tous les champs.");
			alert.show();
		}

		Subtitle sub = new Subtitle(ConversionStringMilli.StringToMillisecond(debutInput.getText()), ConversionStringMilli.StringToMillisecond(finInput.getText()));
		sub.addSpeech(new Speech(subtitlesInput.getText(), personneInput.getValue()));

		debutInput.setText("");
		finInput.setText("");
		subtitlesInput.setText("");
		personneInput.setValue(null);
	}

	@FXML
	void sauvegarderOnClick(ActionEvent event){
		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().add(
					new FileChooser.ExtensionFilter("XML", "*.xml"));
			File file = fileChooser.showSaveDialog(Main.primaryStage);

			Encoder.encodeXML(subtitles, file);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void updateVideo() {

		double currentTime = player.getCurrentTime().toMillis();

		if(!asSetTime) {
			asSetTime=true;
			MainControler.controleur.videoPlayEnd.setText(ConversionStringMilli.MillisecondsToString((long)player.getTotalDuration().toMillis()));
			playPauseVideo();
			player.seek(Duration.millis(0));
		}

		if(currentTime<ConversionStringMilli.StringToMillisecond(MainControler.controleur.videoPlayStart.textProperty().get()))
			player.seek( Duration.millis(ConversionStringMilli.StringToMillisecond(MainControler.controleur.videoPlayStart.textProperty().get())) );

		if(currentTime>ConversionStringMilli.StringToMillisecond(MainControler.controleur.videoPlayEnd.textProperty().get())) {
			player.seek( Duration.millis(ConversionStringMilli.StringToMillisecond(MainControler.controleur.videoPlayEnd.textProperty().get())) );
			image_bouton.setImage(img_play);
			player.pause();

		}

		if(MainControler.controleur.zoomCheckBox.isSelected()) {
			barre_lecture.setWidth( (currentTime-ConversionStringMilli.StringToMillisecond(MainControler.controleur.videoPlayStart.textProperty().get())) /(ConversionStringMilli.StringToMillisecond(MainControler.controleur.videoPlayEnd.textProperty().get())-ConversionStringMilli.StringToMillisecond(MainControler.controleur.videoPlayStart.textProperty().get()))*barre_fond.getWidth());
		}else {
			barre_lecture.setWidth( (currentTime / player.getTotalDuration().toMillis())*barre_fond.getWidth());

		}
		videoTime.setText(ConversionStringMilli.MillisecondsToString((long)currentTime));

		//Affichage des soutitres
		for(Subtitle sub:subtitles.getSubtitles()) {

			//Ajout des sous-titres a afficher
			if(sub.getTimeStart() <= currentTime && sub.getTimeStop() > currentTime) {
				if(!subtitlesToShow.contains(sub)) {
					subtitlesToShow.add(sub);

				}
				//Suppression des sous-titres a ne plus afficher
			}else {
				subtitlesToShow.remove(sub);

			}

		}

		paneTextToShow.getChildren().clear();
		for(Subtitle sub:subtitlesToShow) {
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

		if(xmlFile != null)
			try {
				subtitles = Decoder.Decode(xmlFile);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		else {
			subtitles = new SubtitlesList();
		}

		if(file != null) {

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
			videoTimeMax.setLayoutY(MainControler.controleur.ajouterButton.getLayoutY()+570);

			personneInput.setLayoutX(MainControler.controleur.ajouterButton.getLayoutX()+298);
			personneInput.setLayoutY(MainControler.controleur.ajouterButton.getLayoutY()+127);
			personneInput.setItems(subtitles.getNarrators());

			debutInput.setLayoutX(MainControler.controleur.ajouterButton.getLayoutX()+298);
			debutInput.setLayoutY(MainControler.controleur.ajouterButton.getLayoutY()+62);

			finInput.setLayoutX(MainControler.controleur.ajouterButton.getLayoutX()+298);
			finInput.setLayoutY(MainControler.controleur.ajouterButton.getLayoutY()+95);

			subtitlesInput.setLayoutX(MainControler.controleur.ajouterButton.getLayoutX());
			subtitlesInput.setLayoutY(MainControler.controleur.ajouterButton.getLayoutY()+55);
			subtitlesInput.setPrefWidth(275);
			subtitlesInput.setPrefHeight(96);

			fond = new Rectangle(video.getFitWidth(),30);
			fond.setOpacity(0.5);
			fond.setLayoutX(video.getLayoutX());
			fond.setLayoutY(538); 

			MainControler.controleur.barreDeSelection1.setLayoutX(barre_lecture.getLayoutX());
			MainControler.controleur.barreDeSelection1.setLayoutY(barre_lecture.getLayoutY()-48);

			MainControler.controleur.barreDeSelection2.setLayoutX(barre_lecture.getLayoutX()+888);
			MainControler.controleur.barreDeSelection2.setLayoutY(barre_lecture.getLayoutY()-48);

			MainControler.controleur.barreDeSelection1.setOpacity(0.32);
			MainControler.controleur.barreDeSelection2.setOpacity(0.32);

			MainControler.controleur.barreDeSelection1.setOnMouseEntered(new EventHandler<Event>() {

				@Override
				public void handle(Event event) {
					MainControler.controleur.barreDeSelection1.setOpacity(1);
				}
			});

			MainControler.controleur.barreDeSelection2.setOnMouseEntered(new EventHandler<Event>() {

				@Override
				public void handle(Event event) {
					MainControler.controleur.barreDeSelection2.setOpacity(1);
				}
			});

			MainControler.controleur.barreDeSelection1.setOnMouseExited(new EventHandler<Event>() {

				@Override
				public void handle(Event event) {
					MainControler.controleur.barreDeSelection1.setOpacity(0.32);
				}
			});

			MainControler.controleur.barreDeSelection2.setOnMouseExited(new EventHandler<Event>() {

				@Override
				public void handle(Event event) {
					MainControler.controleur.barreDeSelection2.setOpacity(0.32);
				}
			});

			makeBarreDragable(MainControler.controleur.barreDeSelection1);
			makeBarreDragable(MainControler.controleur.barreDeSelection2);

			MainControler.controleur.zoomCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {

				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					MainControler.controleur.barreDeSelection1.setVisible(!newValue);
					MainControler.controleur.barreDeSelection2.setVisible(!newValue);
					selectedZone.setVisible(!newValue);
				}
			});

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
					MainControler.updateVideo();
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

			MainControler.controleur.barreDeSelection1.layoutXProperty().addListener(new ChangeListener<Number>() {

				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					MainControler.controleur.videoPlayStart.setText(ConversionStringMilli.MillisecondsToString((long)(ConversionStringMilli.StringToMillisecond(MainControler.controleur.videoPlayStart.getText())+(newValue.longValue()-oldValue.longValue())*(player.getTotalDuration().toMillis()/barre_fond.getWidth()))));
					selectedZone.setLayoutX(MainControler.controleur.barreDeSelection1.getLayoutX());
					selectedZone.setWidth(MainControler.controleur.barreDeSelection2.getLayoutX() - MainControler.controleur.barreDeSelection1.getLayoutX());
				}
			});

			MainControler.controleur.barreDeSelection2.layoutXProperty().addListener(new ChangeListener<Number>() {

				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					MainControler.controleur.videoPlayEnd.setText(ConversionStringMilli.MillisecondsToString((long)(ConversionStringMilli.StringToMillisecond(MainControler.controleur.videoPlayEnd.getText())+(newValue.longValue()-oldValue.longValue())*(player.getTotalDuration().toMillis()/barre_fond.getWidth()))));
					selectedZone.setLayoutX(MainControler.controleur.barreDeSelection1.getLayoutX());
					selectedZone.setWidth(MainControler.controleur.barreDeSelection2.getLayoutX() - MainControler.controleur.barreDeSelection1.getLayoutX());
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
			MainControler.controleur.videoPlayStart.onKeyPressedProperty().addListener(new ChangeListener(){
				@Override public void changed(ObservableValue o, Object oldVal, Object newVal){
					updateVideo();
				}
			});

			//Event Changement du temps de fin de la video
			MainControler.controleur.videoPlayEnd.onKeyPressedProperty().addListener(new ChangeListener(){
				@Override public void changed(ObservableValue o, Object oldVal, Object newVal){
					updateVideo();
				}
			});

			//Event changement du temps de lecture de la video
			barres.setOnMouseClicked(new EventHandler<MouseEvent>(){
				public void handle(MouseEvent me){

					if(MainControler.controleur.zoomCheckBox.isSelected()) {
						player.seek( Duration.millis(me.getX()/barre_fond.getWidth()*(ConversionStringMilli.StringToMillisecond(MainControler.controleur.videoPlayEnd.textProperty().get()) - ConversionStringMilli.StringToMillisecond(MainControler.controleur.videoPlayStart.textProperty().get()) ) ).add(Duration.millis( ConversionStringMilli.StringToMillisecond(MainControler.controleur.videoPlayStart.textProperty().get()) ) )  );

						barre_lecture.setWidth((me.getX()/barre_fond.getWidth()*fichierVideo.getDuration().toMillis()) / (player.getTotalDuration().toMillis())*barre_fond.getWidth());
					}else {
						player.seek( Duration.millis(me.getX()/barre_fond.getWidth()*player.getTotalDuration().toMillis()  )  );

						barre_lecture.setWidth( (player.getCurrentTime().toMillis() / player.getTotalDuration().toMillis())*barre_fond.getWidth());
					}
				}
			});


			if(!doVideoAlreadyBeanLoad) {
				doVideoAlreadyBeanLoad=true;

				//ajout des textfiels plus ajout au pan
				MainControler.controleur.panePrincipal.getChildren().add(personneInput);
				MainControler.controleur.panePrincipal.getChildren().add(debutInput);
				MainControler.controleur.panePrincipal.getChildren().add(finInput);
				MainControler.controleur.panePrincipal.getChildren().add(subtitlesInput);
				MainControler.controleur.panePrincipal.getChildren().add(paneTextToShow);

				selectedZone = new Rectangle();
				selectedZone.setFill(Paint.valueOf("#000000"));
				selectedZone.setHeight(barre_fond.getHeight());
				selectedZone.setWidth(MainControler.controleur.barreDeSelection2.getLayoutX()-MainControler.controleur.barreDeSelection1.getLayoutX());
				selectedZone.setLayoutX(MainControler.controleur.barreDeSelection1.getLayoutX());
				selectedZone.setLayoutY(MainControler.controleur.barreDeSelection1.getLayoutY()+48);
				selectedZone.setOpacity(0.3);

				fonctions.getChildren().add(fond);
				fonctions.getChildren().add(play_pause);
				fonctions.getChildren().add(barres);
				barres.getChildren().add(barre_fond);
				barres.getChildren().add(barre_lecture);
				barres.getChildren().add(MainControler.controleur.barreDeSelection1);
				barres.getChildren().add(MainControler.controleur.barreDeSelection2);
				barres.getChildren().add(selectedZone);

				play_pause.getChildren().add(fond_bouton);
				play_pause.getChildren().add(image_bouton);

				MainControler.controleur.panePrincipal.getChildren().add(fonctions);
				MainControler.controleur.panePrincipal.getChildren().add(videoTime);
				MainControler.controleur.panePrincipal.getChildren().add(videoTimeMax);

				MainControler.controleur.videoPlayStart.setText("00:00:00.000");
				MainControler.controleur.videoPlayEnd.setText("00:00:01.000");

			}
		}
		//TODO
		/*
		SoundSpectrum ss = new SoundSpectrum(player, 0, 100000, 10, 100);
		Pane sp = ss.getSpectrumPane();
		controleur.panePrincipal.getChildren().add(sp);

		sp.setLayoutX(20);
		sp.setLayoutY(300);
		sp.setMinWidth(500);
		sp.setMinHeight(280);
		sp.setMaxWidth(500);
		sp.setMaxHeight(280);
		ss.makeAudioSpectrumGraph();
		 */
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MainControler.controleur = this;
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

		barreDeSelection1 = new Group();
		barreDeSelection2 = new Group();
		corpsBarreSelection1 = new Rectangle(2, 70);
		corpsBarreSelection2 = new Rectangle(2, 70);
		teteBarreSelection1 = new Circle();	
		teteBarreSelection2 = new Circle();
		teteBarreSelection1.setRadius(5);
		teteBarreSelection2.setRadius(5);
		teteBarreSelection1.setLayoutX(1);
		teteBarreSelection2.setLayoutX(1);
		barreDeSelection1.getChildren().addAll(corpsBarreSelection1, teteBarreSelection1);
		barreDeSelection2.getChildren().addAll(corpsBarreSelection2, teteBarreSelection2);
		teteBarreSelection1.setFill(Paint.valueOf("#ff1f1f"));
		corpsBarreSelection1.setFill(Paint.valueOf("#ccff1f"));
		teteBarreSelection2.setFill(Paint.valueOf("#ff1f1f"));
		corpsBarreSelection2.setFill(Paint.valueOf("#ccff1f"));
		//setNewVideoAndXml("Sans titre.mp4", "Final.xml");

	}

	public static void makeBarreDragable(Group barre){
		barre.setOnMousePressed(makeBarreDraggable.getEventOnMousePressed(barre));
		barre.setOnMouseDragged(makeBarreDraggable.getEventDragMouse(barre));
	}

	public Rectangle getSelectedZone() {
		return selectedZone;
	}
}
