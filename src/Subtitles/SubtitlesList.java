package Subtitles;

import java.util.ArrayList;

public class SubtitlesList {
	
	ArrayList<Style>styles;
	ArrayList<Subtitle>subtitles;
	
	public SubtitlesList() {
		styles = new ArrayList<>();
		subtitles = new ArrayList<>();
	}
	
	public void addStyle(Style style) {
		styles.add(style);
	}
	public void addSubtitles(Subtitle subtitle) {
		subtitles.add(subtitle);
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
