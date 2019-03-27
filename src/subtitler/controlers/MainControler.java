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
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener.Change;
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
import subtitler.utils.Pin;
import subtitler.utils.Pin.Mode;
import subtitler.utils.WaveForm;
import subtitler.utils.modifSubtitleUtils;
public class MainControler implements Initializable {


	@FXML
	private Pane paneListePersonne;

	@FXML
	private Slider sliderPaneListePersonne;

	@FXML
	private Pane panePrincipal;

	@FXML
	private Button ajouterButton;

	@FXML
	private Button sauvegarderButton;

	@FXML
	private Button buttonEditSpeakers;

	@FXML
	private TextField wordToSearchBox;

	@FXML
	private Button buttonSearchKeyWord;

	@FXML
	private TextField videoPlayStart;

	@FXML
	private TextField videoPlayEnd;

	@FXML
	private Button buttonEditSubtitles;

	@FXML
	private CheckBox zoomCheckBox;

	@FXML
	private Slider volumeBarre;

	@FXML
	private Label volumeText;

	@FXML
	private TextField debutInput;

	@FXML
	private TextField finInput;

	@FXML
	private ComboBox<String> personneInput;

	@FXML
	private TextArea subtitlesInput;


	static Slider videoSlider;

	static Label videoTime;

	static Label videoTimeMax;

	static Image img_play;
	static Image img_pause;

	public static MediaPlayer player;
	static MediaView video;
	static Media fichierVideo;
	static ImageView image_bouton;

	static File subtitleFile;
	static SubtitlesList subtitles;

	static Rectangle selectedZone;
	static Rectangle fond_bouton;
	public static Rectangle barre_fond;
	static Rectangle barre_lecture;

	static Pin pin1;
	static Pin pin2;

	static Group fonctions ;
	static Group barres ;
	static Group play_pause ;
	static Group barresSubtitles;
	static Group barresRecherche;
	static Rectangle fond;

	static ArrayList<Subtitle> subtitlesToShow = new ArrayList<Subtitle>();
	static Pane paneTextToShow;

	static Stage fileImportStage;
	static Stage modifySubtitlesStage;
	static Stage modifOneSubtitleStage;

	static boolean asSetTime = false;
	static boolean doVideoAlreadyBeanLoad = false;

	public static MainControler controleur;
	static WaveForm wf;

	public static Subtitle selectedSubtitle;



	@FXML
	void searchKeyWord(ActionEvent event) {
		barresRecherche.getChildren().clear();

		SubtitlesList res = Search.recherche(wordToSearchBox.getText(), MainControler.subtitles);
		if(res != null) {
			for(Subtitle sub: res.getSubtitles()) {
				for(Speech sp: sub.getContenu()) {
					for(int i = 0; i<subtitles.getNarrators().size(); i++) {
						System.out.println(sp.getText());
						if(sp.getAuthor().equals(subtitles.getNarrators().get(i)) && sp.getText().toUpperCase().contains(wordToSearchBox.getText().toUpperCase())) {
							Rectangle r = new Rectangle((sub.getTimeStop()-sub.getTimeStart())*(barre_fond.getWidth()/player.getTotalDuration().toMillis()),5);
							r.setCursor(Cursor.HAND);
							r.setOnMouseEntered(new EventHandler<MouseEvent>(){
								public void handle(MouseEvent me){
									r.setHeight(10);
								}
							});
							r.setOnMouseExited(new EventHandler<MouseEvent>(){
								public void handle(MouseEvent me){
									r.setHeight(5);
								}
							});
							for(Style st : subtitles.getStyles()) {
								if(st != null) {
									if(st.getNarrator().equals(sp.getAuthor())) {
										r.setFill(Paint.valueOf(st.getColor()));
									}
								}
							}
							r.setTranslateX(sub.getTimeStart()*(barre_fond.getWidth()/player.getTotalDuration().toMillis()));
							r.setTranslateY(i*-4);
							barresRecherche.getChildren().add(r);
						}

					}
				}

			}
		}
	}

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

	public static void updatebarreSubtitle() {
		barresSubtitles.getChildren().clear();
		for(int i = 0; i < subtitles.getNarrators().size(); i++) {
			for(Subtitle s : subtitles.getSubtitles()) {
				for(Speech sp : s.getContenu()) {
					if(sp.getAuthor().equals(subtitles.getNarrators().get(i))) {
						Rectangle r;
						if(MainControler.controleur.zoomCheckBox.isSelected()) {							
							long sampleStart = ConversionStringMilli.StringToMillisecond(MainControler.controleur.videoPlayStart.getText());
							long sampleStop = ConversionStringMilli.StringToMillisecond(MainControler.controleur.videoPlayEnd.getText());;
							if(!(s.getTimeStop() < sampleStart || s.getTimeStart() > sampleStop)) {					
								r = new Rectangle((s.getTimeStop()-s.getTimeStart())*(barre_fond.getWidth()/(sampleStop - sampleStart)),3);
								r.setTranslateX((s.getTimeStart()-sampleStart)*(barre_fond.getWidth()/(sampleStop-sampleStart)));									
								if(s.getTimeStart() < sampleStart) {
									r.setWidth(r.getWidth()-(sampleStart-s.getTimeStart())*(barre_fond.getWidth()/(sampleStop - sampleStart)));
									r.setTranslateX(0);
								}if(s.getTimeStop() > sampleStop) {
									r.setWidth(r.getWidth()-(s.getTimeStop()-sampleStop)*(barre_fond.getWidth()/(sampleStop - sampleStart)));
								}

							}else {
								r = new Rectangle(0,0);
							}
						}else {	

							r = new Rectangle((s.getTimeStop()-s.getTimeStart())*(barre_fond.getWidth()/player.getTotalDuration().toMillis()),3);
							r.setTranslateX(s.getTimeStart()*(barre_fond.getWidth()/player.getTotalDuration().toMillis()));

							r.setCursor(Cursor.HAND);
							r.setOnMouseEntered(new EventHandler<MouseEvent>(){
								public void handle(MouseEvent me){
									r.setHeight(5);
									r.setTranslateY(r.getTranslateY()-2);
								}
							});
							r.setOnMouseExited(new EventHandler<MouseEvent>(){
								public void handle(MouseEvent me){
									r.setHeight(3);
									r.setTranslateY(r.getTranslateY()+2);
								}
							});
							r.setOnMouseClicked(new EventHandler<MouseEvent>(){
								public void handle(MouseEvent me){
									AnchorPane root;
									try {
										modifSubtitleUtils.selectedSubtitle = s;
										root = (AnchorPane) FXMLLoader.load(new File("modifOneSubtitle.fxml").toURI().toURL());
										modifSubtitleUtils.modifOneSubtitleStage = new Stage(); 
										Scene scene = new Scene(root, 640, 380); 
										modifSubtitleUtils.modifOneSubtitleStage.setTitle("Modifier Un Sous-Titre");
										modifSubtitleUtils.modifOneSubtitleStage.setScene(scene); 
										modifSubtitleUtils.modifOneSubtitleStage.show();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} 
								}
							});
							for(Style st : subtitles.getStyles()) {
								if(st != null) {
									if(st.getNarrator().equals(sp.getAuthor())) {
										r.setFill(Paint.valueOf(st.getColor()));
									}
								}
							}
							r.setTranslateY(i*-4);
							barresSubtitles.getChildren().add(r);
						}
					}
				}
			}
		}
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
		System.out.println("debut : " + controleur.debutInput.getText());
		System.out.println("fin: " + controleur.finInput.getText());
		System.out.println("personne : " + controleur.personneInput.getValue());
		System.out.println("text : " + controleur.subtitlesInput.getText());

		if(controleur.debutInput.getText().equals("") || controleur.finInput.getText().equals("") || controleur.personneInput.getValue() == null || controleur.subtitlesInput.getText().equals("")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("Veuillez remplire tous les champs.");
			alert.show();
		}else {

			Subtitle sub = new Subtitle(ConversionStringMilli.StringToMillisecond(debutInput.getText()), ConversionStringMilli.StringToMillisecond(finInput.getText()));
			sub.addSpeech(new Speech(subtitlesInput.getText(), controleur.personneInput.getValue()));
			subtitles.addSubtitles(sub);
			controleur.debutInput.setText("");
			controleur.finInput.setText("");
			controleur.subtitlesInput.setText("");
			controleur.personneInput.setValue(null);
			updatebarreSubtitle();
			updateVideo();
		}
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

	public static void updateVideo() {

		double currentTime = player.getCurrentTime().toMillis();

		if(!asSetTime) {
			player.setVolume(1);
			asSetTime=true;
			MainControler.controleur.videoPlayEnd.setText(ConversionStringMilli.MillisecondsToString((long)player.getTotalDuration().toMillis()));
			playPauseVideo();
			player.seek(Duration.millis(0));
			updatebarreSubtitle();
			wf.setBounds(0, player.getTotalDuration().toMillis());
			wf.setTImeMax(player.getTotalDuration().toMillis());
			wf.makeWaveForm();
			controleur.setVideoToolsSetDisable(false);
		}

		if(currentTime<ConversionStringMilli.StringToMillisecond(MainControler.controleur.videoPlayStart.textProperty().get()))
			player.seek( Duration.millis(ConversionStringMilli.StringToMillisecond(MainControler.controleur.videoPlayStart.textProperty().get())) );

		else if(currentTime-120>ConversionStringMilli.StringToMillisecond(MainControler.controleur.videoPlayEnd.textProperty().get()) || currentTime>ConversionStringMilli.StringToMillisecond(MainControler.controleur.videoPlayEnd.textProperty().get())) {
			player.pause();
			player.seek( Duration.millis(ConversionStringMilli.StringToMillisecond(MainControler.controleur.videoPlayEnd.textProperty().get())) );
			image_bouton.setImage(img_play);

		}

		if(controleur.zoomCheckBox.isSelected()) {
			barre_lecture.setWidth( (currentTime-ConversionStringMilli.StringToMillisecond(MainControler.controleur.videoPlayStart.textProperty().get())) /(ConversionStringMilli.StringToMillisecond(MainControler.controleur.videoPlayEnd.textProperty().get())-ConversionStringMilli.StringToMillisecond(MainControler.controleur.videoPlayStart.textProperty().get()))*barre_fond.getWidth());
		}else {
			barre_lecture.setWidth( (currentTime /
					player.getTotalDuration().toMillis())
					*barre_fond.getWidth());

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
				Label text = new Label(speech.getAuthor()+" : "+speech.getText());
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

	static double barreSize=70;

	@SuppressWarnings({ "unchecked", "rawtypes" })
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

			if(wf != null) {
				wf.startService(file, WaveForm.WaveFormJob.AMPLITUDES_AND_WAVEFORM);

			}else
				wf = new WaveForm(file, WaveForm.WaveFormJob.AMPLITUDES_AND_WAVEFORM, 10);


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

			video.setFitWidth(950);
			video.setFitHeight(600);
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

			controleur.personneInput.setItems(subtitles.getNarrators());

			fond = new Rectangle(video.getFitWidth(),barreSize);
			fond.setOpacity(0.5);
			fond.setLayoutX(video.getLayoutX());
			fond.setLayoutY(488);

			subtitles.getStyles().addListener(new ListChangeListener(){
				@Override
				public void onChanged(Change c) {
					controleur.personneInput.setItems(subtitles.getNarrators());
					fillListePersonne();
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

			//création du bouton play/pause
			fond_bouton = new Rectangle(30,barreSize);
			fond_bouton.setArcHeight(5);
			fond_bouton.setArcWidth(5);

			image_bouton.setFitWidth(22);
			image_bouton.setLayoutX(4);
			image_bouton.setLayoutY(4);
			image_bouton.setFitHeight(22);

			play_pause.setTranslateX(video.getLayoutX());
			play_pause.setTranslateY(488);
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
			barres.setLayoutY(494);
			barres.setCursor(Cursor.HAND);
			barre_fond = new Rectangle(video.getFitWidth()-60, (2.2/3)*barreSize);

			barresSubtitles.setLayoutY(480);
			barresSubtitles.setTranslateX(video.getLayoutX() + 35);

			barresRecherche.setLayoutY(495+ barre_fond.getHeight());
			barresRecherche.setTranslateX(video.getLayoutX() + 35);

			barre_lecture.setFill(Color.BLUE);
			barre_fond.setFill(Color.GREY);	

			pin1.setBounding(video.getLayoutX()+35, video.getLayoutX()+35+barre_fond.getWidth());
			pin2.setBounding(video.getLayoutX()+35, video.getLayoutX()+35+barre_fond.getWidth());

			pin1.setSize(barre_fond.getHeight());
			pin2.setSize(barre_fond.getHeight());

			pin1.setLayoutY(barres.getLayoutY());
			pin2.setLayoutY(barres.getLayoutY());

			pin1.setTimeField(controleur.videoPlayStart);
			pin2.setTimeField(controleur.videoPlayEnd);

			pin1.setLayoutX(video.getLayoutX()+35);
			pin2.setLayoutX(video.getLayoutX()+35+barre_fond.getWidth());

			selectedZone.setLayoutX(pin1.getLayoutX()-(video.getLayoutX()+35));
			selectedZone.setWidth(pin2.getLayoutX()-pin1.getLayoutX());

			fillListePersonne();

			if(!doVideoAlreadyBeanLoad) {

				//ajout des textfiels plus ajout au pan
				controleur.panePrincipal.getChildren().add(paneTextToShow);


				selectedZone.setFill(Paint.valueOf("#000000"));
				selectedZone.setHeight(barre_fond.getHeight());
				selectedZone.setOpacity(0.3);

				fonctions.getChildren().add(fond);
				fonctions.getChildren().add(play_pause);
				fonctions.getChildren().add(barres);
				fonctions.getChildren().add(barresSubtitles);
				fonctions.getChildren().add(barresRecherche);

				barres.getChildren().add(barre_fond);
				barres.getChildren().add(barre_lecture);
				barres.getChildren().add(selectedZone);

				play_pause.getChildren().add(fond_bouton);
				play_pause.getChildren().add(image_bouton);

				controleur.panePrincipal.getChildren().add(fonctions);
				controleur.panePrincipal.getChildren().add(videoTime);
				controleur.panePrincipal.getChildren().add(videoTimeMax);
				controleur.videoPlayStart.setText("00:00:00.000");
				controleur.videoPlayEnd.setText("00:00:01.000");
				controleur.panePrincipal.getChildren().add(wf.getPane());

				pin1.addToPane(controleur.panePrincipal);
				pin2.addToPane(controleur.panePrincipal);

				pin1.setPlayableZone(selectedZone);
				pin2.setPlayableZone(selectedZone);

				pin1.addListener();
				pin2.addListener();

				controleur.zoomCheckBox.selectedProperty().addListener(new ChangeListener() {

					@Override
					public void changed(ObservableValue observable, Object oldValue, Object newValue) {

						if(controleur.zoomCheckBox.isSelected()) {
							wf.setBounds(ConversionStringMilli.StringToMillisecond(controleur.videoPlayStart.getText()), ConversionStringMilli.StringToMillisecond(controleur.videoPlayEnd.getText()));
							pin1.setVisibility(false);
							pin2.setVisibility(false);

						}else {
							wf.setBounds(0, player.getTotalDuration().toMillis());
							pin1.setVisibility(true);
							pin2.setVisibility(true);

						}

						wf.makeWaveForm();
						updateVideo();
						updatebarreSubtitle();
					}

				});

				//Event Changement du temps de debut de la video
				controleur.videoPlayStart.onKeyPressedProperty().addListener(new ChangeListener(){
					@Override public void changed(ObservableValue o, Object oldVal, Object newVal){
						updateVideo();
					}
				});

				//Event Changement du temps de fin de la video
				controleur.videoPlayEnd.onKeyPressedProperty().addListener(new ChangeListener(){
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

				controleur.volumeBarre.valueProperty().addListener(new ChangeListener<Number>() {
					@Override
					public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
						player.setVolume(newValue.doubleValue()/100);
						controleur.volumeText.setText((int)newValue.doubleValue()+"%");
					}

				});;

				doVideoAlreadyBeanLoad=true;
			}
			player.setVolume(0);
			wf.getPane().setOpacity(0.5);
			wf.getPane().setPrefWidth(barre_fond.getWidth());
			wf.getPane().setPrefHeight(barre_fond.getHeight());
			wf.getPane().setLayoutX(video.getLayoutX()+35);
			wf.getPane().setLayoutY(barres.getLayoutY());
			wf.getPane().setDisable(true);
		}


	}

	public static void fillListePersonne() {
		int nbCol = MainControler.subtitles.getStyles().size()/8;
		MainControler.controleur.paneListePersonne.setPrefWidth(144*nbCol);
		for(int i = 0; i < MainControler.subtitles.getStyles().size(); i++) {
			Group g = new Group();
			g.setLayoutX((i/8)*124);
			g.prefWidth(MainControler.controleur.paneListePersonne.getPrefWidth());
			g.prefHeight(20);
			g.setLayoutY((i%8)*25);
			Label author = new Label(MainControler.subtitles.getStyles().get(i).getNarrator());
			author.setTextFill(Paint.valueOf("#FFFFFF"));
			author.setLayoutX(20);
			Rectangle color = new Rectangle(10, 10, Paint.valueOf(MainControler.subtitles.getStyles().get(i).getColor()));
			color.setLayoutX(5);
			color.setLayoutY(5);
			g.getChildren().addAll(author, color);
			MainControler.controleur.paneListePersonne.getChildren().add(g);
		}
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MainControler.controleur = this;
		//D�clarations
		try {
			img_play = new Image(new File("PLAY.png").toURI().toURL().toString());
			img_pause = new Image(new File("PAUSE.png").toURI().toURL().toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		videoSlider = new Slider();

		subtitles = null;

		paneTextToShow = new Pane();
		paneTextToShow.setStyle("-fx-background-color: black;");

		videoTime = new Label();
		videoTimeMax = new Label();

		fonctions = new Group();

		barres = new Group();

		play_pause = new Group();

		barresRecherche = new Group();

		barresSubtitles = new Group();

		barre_lecture = new Rectangle(0, (2.2/3)*barreSize);

		image_bouton = new ImageView(img_pause);

		selectedZone = new Rectangle();

		pin1 = new Pin(Mode.START);
		pin2 = new Pin(Mode.END);

		pin1.setSiblingPin(pin2);
		pin2.setSiblingPin(pin1);

		setVideoToolsSetDisable(true);
	}

	public void setVideoToolsSetDisable(boolean value) {
		zoomCheckBox.setDisable(value);
		ajouterButton.setDisable(value);
		sauvegarderButton.setDisable(value);
		wordToSearchBox.setDisable(value);
		videoPlayStart.setDisable(value);
		videoPlayEnd.setDisable(value);
		volumeBarre.setDisable(value);
		buttonSearchKeyWord.setDisable(value);
		buttonEditSpeakers.setDisable(value);
		buttonEditSubtitles.setDisable(value);
	}

	public static Pane getPanPrincipale() {
		return controleur.panePrincipal;
	}
}
