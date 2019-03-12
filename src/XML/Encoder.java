package XML;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import Subtitles.SubtitlesList;

public class Encoder {
	
	private SubtitlesList subtitles;
	
	public Encoder(SubtitlesList subtitlesInput, String fileNameInput) throws IOException {
		subtitles=subtitlesInput;
		File xmlFile = new File(fileNameInput+".xml");
		xmlFile.createNewFile();
		BufferedWriter write = new BufferedWriter(new FileWriter(xmlFile));
		write.write(encodageXML());
		write.close();
	}
	
	
	
	public String encodageXML() {
		String res = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<USFSubtitles version=\"1.0\">\n";
		
		res += subtitles.getXml();
		
		res += "</USFSubtitles>";
		return res;
	}
}
