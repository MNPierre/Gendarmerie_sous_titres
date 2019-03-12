import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Subtitles.Subtitles;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
public class Controleur implements Initializable {

    @FXML
    private TextField mp3path;

    @FXML
    private TextField soustitrepath;

    @FXML
    private Pane paneVideo;
    
    @FXML
    private Pane PaneVideoControl;
    
    @FXML
    private Slider videoSlider;

    @FXML
    private Button buttonMP3;

    @FXML
    private Button buttonsoustitre;
    
    @FXML
    private TextArea subtitlesInput;
    
    @FXML
    private Text timeOutput;
    
    @FXML
    private Button validSubititles;
    
    @FXML
    private Button saveSubtitlesButton;
    
    File subtitleFile;
    ArrayList<Subtitles> subtitles;
    
    @FXML
    void validSubititlesOnClick(ActionEvent event) throws IOException {
    	addSubtitles(subtitlesInput.getText());
    }
    
    @FXML
    void saveSubtitlesOnClick(ActionEvent event) throws IOException {
    	saveSubtitles(subtitleFile);
    }



	@Override
    public void initialize(URL location, ResourceBundle resources) {
    	
    	//video.setMediaPlayer(value);
    	File path = new File("Sans titre.mp4");
    	System.out.println("test : "+path.getAbsoluteFile());
    	Media fichierVideo = new Media(path.toURI().toString());
    	MediaPlayer player = new MediaPlayer(fichierVideo);
    	MediaView video = new MediaView(player);
    	paneVideo.getChildren().add(video);
    	videoSlider = new Slider();
    	
    	paneVideo.setMaxWidth(480);
    	paneVideo.setMaxHeight(200);
    	video.setFitWidth(600);
    	video.setFitHeight(190);
    	video.setLayoutY(4);
    	video.setLayoutX(-50);
    	video.getMediaPlayer().play();
    	
    	//création de la liste de sous-titres
    	subtitles = new ArrayList<>();
    	
    	try {
    		subtitleFile = createSubtitlesFiles();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
    	
    	videoSlider.valueProperty().addListener(new InvalidationListener() {
    		public void invalidated(Observable ov) {
    	        if (videoSlider.isValueChanging()) {
    	        // multiply duration by percentage calculated by slider position
    	        	player.seek(fichierVideo.getDuration().multiply(videoSlider.getValue() / 100.0));
    	        }
    	     }
    	 });
    	
  
 	}


	private File createSubtitlesFiles() throws IOException {
		subtitleFile =  new File("fileName.txt");		
		if(!subtitleFile.exists()) {
			System.out.println(subtitleFile.getName());
			subtitleFile.createNewFile();
		}
		return subtitleFile;
	}
	
	private void saveSubtitles(File f) throws IOException {			
		PrintWriter pw;
		pw = new PrintWriter(subtitleFile);
		for(Subtitles s : subtitles) {
			pw.println(s.getText());
		}
		pw.close();
	}
	
	private void addSubtitles(String text) {
		subtitles.add(new Subtitles(text , 50));
	}
}
