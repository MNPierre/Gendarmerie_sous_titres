package XML;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import Subtitles.SubtitlesList;

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
		
		res += "</USFSubtitles>";
		return res;
	}
}
