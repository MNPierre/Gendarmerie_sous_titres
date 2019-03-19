package subtitler.utils;

import javafx.scene.layout.Pane;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class SoundSpectrum {

	MediaPlayer media;
	
	long timeStart;
	long timeEnd;
	
	int samplePeriod;
	
	Pane graphPane;
	
	int[] audioSpectrumValues;
	int spectrumSize;
	
	public SoundSpectrum(MediaPlayer media, long timeStart, long timeEnd, int samplePeriod, int spectrumSize) {
		this.media = media;
		this.timeStart = timeStart;
		this.timeEnd = timeEnd;
		this.samplePeriod = samplePeriod;
		graphPane=new Pane();
		graphPane.setStyle("-fx-background-color: gray;");
		this.spectrumSize = spectrumSize;
		
		media.setAudioSpectrumListener(new AudioSpectrumListener() {

			@Override
			public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
				// TODO Auto-generated method stub
				System.out.println("timestamp : "+timestamp+", duration : "+duration+",\n\tMagnitudes : ");
				for(float f:magnitudes) {
					System.out.println("\t\t"+f);
				}
				System.out.println("\tPhases :");
				for(float f:phases) {
					System.out.println("\t\t"+f);
				}
			}
			
		});
		
	}
	
	public void makeAudioSpectrumGraph() {
		audioSpectrumValues = new int[(int) ((timeEnd-timeStart))+1];
		Duration time = media.getCurrentTime();
		for(long t=timeStart;t<timeEnd;t++) {
			media.seek(new Duration(t));
			audioSpectrumValues[(int) (t-timeStart)] = media.getAudioSpectrumThreshold();
			System.out.println(t+"/"+timeEnd+" : "+audioSpectrumValues[(int) (t-timeStart)]);
		}
		media.seek(time);
		graphPane.getChildren().clear();
		
		for(int i:audioSpectrumValues) {
			Rectangle rec = new Rectangle();
			rec.setLayoutX(i*(graphPane.getLayoutX()/audioSpectrumValues.length));
			rec.setLayoutY(graphPane.getHeight()-rec.getHeight());
			rec.setWidth( (graphPane.getLayoutX()/audioSpectrumValues.length));
			rec.setHeight(i*spectrumSize);
			rec.setStyle("-fx-background-color: black;");
			graphPane.getChildren().addAll(rec);
		}
		
	}
	
	public Pane getSpectrumPane() {
		return graphPane;
	}
	
	
}
