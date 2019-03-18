package Subtitles;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;

public class Search {
	public static ArrayList<Subtitle> recherche(String mot, SubtitlesList subtitles){
		ArrayList<Subtitle> res = new ArrayList<Subtitle>();
		for(Subtitle sub:subtitles.getSubtitles()) {
			for(Speech sp: sub.getContenu()) {
				if(sp.getText().toUpperCase().contains(mot.toUpperCase())) {
					res.add(sub);
				}
			}

		}

		return res;

	}
}
