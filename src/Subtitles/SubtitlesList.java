package Subtitles;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class SubtitlesList {
	
	private ObservableList<Style>styles;
	private ArrayList<Subtitle>subtitles;
	
	public SubtitlesList() {
		styles = FXCollections.observableArrayList();
		subtitles = new ArrayList<>();
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
		String res = "<styles>\n";
		for(Style s : styles) {
			res += s.getXml();
		}
		
		res += "</styles>\n";
		res += "<subtitles>\n";
		
		for(Subtitle sub : subtitles) {
			res += sub.getXml();
		}
		res += "</subtitles>\n";
		return res;
	}
	
	public ObservableList<Style> getStyles(){
		return styles;
	}

	public ArrayList<Subtitle> getSubtitles() {
		return subtitles;
	}
	
	public void deleteByName(String name) {
		Style result = null;
		for(Style s : styles) {
			if(s.getNarrator().equals(name)) {
				result = s;
			}
		}
		if(result != null)
			styles.remove(result);
	}
	
	/*
	 * Return a new Subtitle object with parmaters in it 
	 * */
	public Subtitle createSubtitle(Long debutInput, Long finInput) {
		
		Subtitle subNew = new Subtitle(debutInput, finInput);
		addSubtitles(subNew);
		return subNew;
	}
}
