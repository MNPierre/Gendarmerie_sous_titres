package Subtitles;

import java.util.ArrayList;

public class SubtitlesList {
	
	private ArrayList<Style>styles;
	private ArrayList<Subtitle>subtitles;
	
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

	public ArrayList<Subtitle> getSubtitles() {
		return subtitles;
	}
	
	/*
	 * Return a new Subtitle object with parmaters in it 
	 * Or if there is already a subtitle at less than 1 second of the paramaters : returns this subtitle
	 * */
	public Subtitle createSubtitle(Long debutInput, Long finInput) {
		for(Subtitle sub : subtitles) {
			System.out.println("debutInput" + debutInput);
			System.out.println("finInput" + finInput);
			System.out.println("sub.getTimeStart" + sub.getTimeStart());
			System.out.println("sub.getTimeStop" + sub.getTimeStop());
			if(Math.abs(sub.getTimeStart()-debutInput) <= 1000 || Math.abs(sub.getTimeStop()-finInput) <= 1000) {
				if(sub.getTimeStart() >= debutInput) {
					sub.setTimeStart(debutInput);
				}
				
				if(sub.getTimeStop() <= finInput) {
					sub.setTimeStop(finInput);
				}
				return sub;
			}
		}
		Subtitle subNew = new Subtitle(debutInput, finInput);
		addSubtitles(subNew);
		return subNew;
	}
}
