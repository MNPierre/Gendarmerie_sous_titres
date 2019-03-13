package XML;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import Subtitles.Speech;
import Subtitles.Style;
import Subtitles.Subtitle;
import Subtitles.SubtitlesList;

public class MainTest {

	/*
	public static void main(String[] args) throws IOException {
		Speech speech1 = new Speech("TEST1", "AUTEUR1");
		Speech speech2 = new Speech("TEST2", "AUTEUR2");
		
		Subtitle sub1 = new Subtitle(0, 1000);
		sub1.addSpeech(speech1);
		
		Subtitle sub2 = new Subtitle(1001, 2001);
		sub2.addSpeech(speech2);
		
		SubtitlesList subs = new SubtitlesList();
		subs.addSubtitles(sub1);
		subs.addSubtitles(sub2);
		
		Style style1 = new Style("AUTEUR1", "#FFFFFF");
		Style style2 = new Style("AUTEUR2", "#000000");
		
		subs.addStyle(style1);
		subs.addStyle(style2);
		
		Encoder encod = new Encoder(subs, "TEST2");
	}*/

	public static void main(String[] args) {
		try {
			Decoder.Decode("TEST2.xml");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
