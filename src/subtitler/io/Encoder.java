package subtitler.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import subtitler.controlers.MainControler;
import subtitler.subtitles.SubtitlesList;
import subtitler.utils.MarqueurCommentaire;

public class Encoder {

	private static SubtitlesList subtitles;

	public static void encodeXML(SubtitlesList subtitlesInput, File xmlFile) throws IOException {
		subtitles=subtitlesInput;

		if(!xmlFile.exists())
			xmlFile.createNewFile();
		BufferedWriter write = new BufferedWriter(new FileWriter(xmlFile));
		write.write(encodageXML());
		write.close();
	}



	private static String encodageXML() {
		String res = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<USFSubtitles version=\"1.0\">\n";

		res += subtitles.getXml();

		res += "\n\t<Commentaires>";
		for(MarqueurCommentaire comm:MainControler.commentaires) {
			res += "\t\t<Commentaire time=\""+comm.getTime()+"\">"+comm.getTextArea().getText()+"\n";
			res += "\t\t</Commentaire>\n";
		}
		res += "\t</Commentaires>";

		res += "</USFSubtitles>";

		return res;
	}
}
