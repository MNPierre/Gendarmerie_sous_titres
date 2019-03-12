package Subtitles;

import java.util.ArrayList;

public class Subtitles {
	
	ArrayList<Style>styles;
	ArrayList<Subtitle>subtitles;
	
	public Subtitles(ArrayList<Style>stylesInput, ArrayList<Subtitle>subtitlesInput) {
		styles = stylesInput;
		subtitles = subtitlesInput;
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
	
	
}
