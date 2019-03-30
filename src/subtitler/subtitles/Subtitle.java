package subtitler.subtitles;

import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import subtitler.utils.ConversionStringMilli;

public class Subtitle implements Comparable<Subtitle>{

	private ArrayList<Speech> contenu;
	private long timeStart;
	private long timeStop;

	private StringProperty timeStartProperty;
	private StringProperty timeStopProperty;

	public Subtitle(long timeStartIntput, long timeStopInput) {
		contenu = new ArrayList<Speech>();
		timeStart = timeStartIntput;
		timeStop = timeStopInput;
		timeStartProperty = new SimpleStringProperty(ConversionStringMilli.MillisecondsToString(timeStart));
		timeStopProperty = new SimpleStringProperty(ConversionStringMilli.MillisecondsToString(timeStop));
	}

	public void addSpeech(Speech speech) {
		contenu.add(speech);
	}

	public ArrayList<Speech> getContenu() {
		return contenu;
	}

	public void setContenu(ArrayList<Speech> contenu) {
		this.contenu = contenu;
	}

	public long getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(long timeStart) {
		this.timeStart = timeStart;
		this.timeStartProperty.set(ConversionStringMilli.MillisecondsToString(timeStart));
	}

	public long getTimeStop() {
		return timeStop;
	}

	public StringProperty getTimeStartProperty() {
		return timeStartProperty;
	}

	public StringProperty getTimeStopProperty() {
		return timeStopProperty;
	}

	public void setTimeStop(long timeStop) {
		this.timeStop = timeStop;
		this.timeStopProperty.set(ConversionStringMilli.MillisecondsToString(timeStop));
	}

	public Speech getSpeech(String author) {
		Speech result = null;
		for(Speech s : contenu) {
			if(s.getAuthor().equals(author)) {
				result = s;
			}
		}
		return result;
	}

	public ObservableList<Speech> getAuthors(){
		return FXCollections.observableArrayList(contenu);
	}

	public static String MillisecondsToString(long time) {

		long milliseconds=time%1000; 
		time=time/1000; 
		long seconds=time%60; 
		time=time/60; 
		long minutes=time%60; 
		time=time/60; 
		long hours=time;
		return (hours+":"+minutes+":"+seconds+"."+milliseconds);
	}

	public int compareTo(Subtitle o) {
		if(timeStart<o.timeStart) {
			return -1;
		}else if(timeStart==o.timeStart){
			
			if(timeStop<o.timeStop) {
				return -1;
			}else{
				return 1;
			}
			
		}else {
			return 1;
		}
	}

	public String getXml() {
		String res = "\t\t<subtitle start=\""+MillisecondsToString(getTimeStart())+"\" stop=\""+MillisecondsToString(getTimeStop())+"\">\n";
		for(Speech s : contenu) {
			res += s.getXml();
		}
		res += "\t\t</subtitle>\n";
		return res;
	}

	public String toString() {
		String string = "timeStart="+timeStart+", timeStop="+timeStop+" {\n";
		for(Speech speech:contenu) {
			string+=" "+speech.toString()+"\n";
		}
		return string+"}";
	}
}
