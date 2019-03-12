package XML;

import java.io.IOException;
import java.util.ArrayList;

import Subtitles.Speech;
import Subtitles.Style;
import Subtitles.Subtitle;
import Subtitles.Subtitles;

public class Main {

	public static void main(String[] args) throws IOException {
		ArrayList<Style>styles = new ArrayList<>();
		styles.add(new Style("Narrateur 1", "#FFFFFF"));
		styles.add(new Style("Narrateur 2", "#000000"));
		
		
		ArrayList<Speech>speech = new ArrayList<>();
		ArrayList<Speech>speech2 = new ArrayList<>();
		
		speech.add(new Speech("Salut", "Auteur1"));
		speech2.add(new Speech("Yo cava", "Auteur2"));
		
		Subtitle subtitle1 = new Subtitle(speech, 0, 2000);
		Subtitle subtitle2 = new Subtitle(speech2, 2001, 3000);		
		ArrayList<Subtitle>subslist = new ArrayList<>();
		subslist.add(subtitle1);
		subslist.add(subtitle2);
		
		Subtitles subs = new Subtitles(styles, subslist);
		
		Encoder encod = new Encoder(subs, "TEST1");
		System.out.println(encod.encodageXML());
	}

}
