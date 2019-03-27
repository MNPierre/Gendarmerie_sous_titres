package subtitler.subtitles;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class SubtitlesList {
	
	private ObservableList<Style>styles;
	private SortedSet<Subtitle>subtitles;
	
	public SubtitlesList() {
		styles = FXCollections.observableArrayList();
		subtitles = new TreeSet<Subtitle>();
	}
	
	public ObservableList<String> getNarrators(){
		ObservableList<String> result = FXCollections.observableArrayList();
		for(Style s : styles) {
			result.add(s.getNarrator());
		}return result;
	}
	
	public Style searchColor(String name) {
		for(Style s : styles) {
			if(s.getNarrator().equals(name)) {
				return s;
			}
		}return null;
	}
	
	public void addStyle(Style style) {
		styles.add(style);
	}
	
	public void addSubtitles(Subtitle subtitle) {
		subtitles.add(subtitle);
	}
	
	public void changeColor(String name, String color) {
		for(Style s : styles) {
			if(s.getNarrator().equals(name)) {
				s.setColor(color);
			}
		}
	}
	
	public String getXml() {
		String res = "\t<styles>\n";
		for(Style s : styles) {
			res += s.getXml();
		}
		
		res += "\t</styles>\n";
		res += "\t<subtitles>\n";
		
		for(Subtitle sub : subtitles) {
			res += sub.getXml();
		}
		res += "\t</subtitles>\n";
		return res;
	}
	
	public ObservableList<Style> getStyles(){
		return styles;
	}
	
	public ObservableList<Subtitle> getObservableSubtitles(){
		return FXCollections.observableArrayList(subtitles);
	}

	public Collection<Subtitle> getSubtitles() {
		return subtitles;
	}
	
	public void deleteByName(String name) {
		for(int i = 0; i < styles.size(); i++) {
			if(styles.get(i).getNarrator().equals(name)) {
				styles.remove(i);
			}
		}
		for(int i =0; i < subtitles.size();i++) {
			if(subtitles.get(i).getContenu().get(0).getAuthor().equals(name)) {
				subtitles.remove(i);
			}
		}
	}
	
}
