package subtitler.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.stream.IntStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import subtitler.subtitles.Speech;
import subtitler.subtitles.Style;
import subtitler.subtitles.Subtitle;
import subtitler.subtitles.SubtitlesList;
import subtitler.utils.ConversionStringMilli;

public class Decoder {
	
	public static SubtitlesList Decode(String fileName) throws FileNotFoundException {
		SubtitlesList subtitles = new SubtitlesList();
		File xmlFile = new File(fileName);
		if(!xmlFile.exists()) {
			throw new FileNotFoundException("Unable to find : \""+fileName+"\"");
		}
		try {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();	
		Document xml = (Document) builder.parse(xmlFile);
		
		Node root = xml.getFirstChild();
		int n = root.getChildNodes().item(1).getChildNodes().getLength();
		
		for(int i=1;i<n;i+=2) {
			Node node = root.getChildNodes().item(1).getChildNodes().item(i);
			String narratorInput=null;
			String colorInput=null;
			
			for (int a=0;a<node.getAttributes().getLength();a++) {
				Node attribut = node.getAttributes().item(a);
				
				switch(attribut.getNodeName()) {
				case "name":
					narratorInput = attribut.getTextContent();
					break;
				case "color":
					colorInput = attribut.getTextContent();
					break;
				}
			}
			
			Style style = new Style(narratorInput, colorInput);
			subtitles.addStyle(style);
		}
		
		n = root.getChildNodes().item(3).getChildNodes().getLength();
		
		for(int i=1;i<n;i+=2) {
			Node node = root.getChildNodes().item(3).getChildNodes().item(i);
			
			long timeStartIntput=-1;
			long timeStopInput=-1;
			
			
			for (int a=0;a<node.getAttributes().getLength();a++) {
				Node attribut = node.getAttributes().item(a);
				
				switch(attribut.getNodeName()) {
				case "start":
					timeStartIntput = ConversionStringMilli.StringToMillisecond(attribut.getTextContent());
					break;
				case "stop":
					timeStopInput = ConversionStringMilli.StringToMillisecond(attribut.getTextContent());
					break;
				}
			}
			
			Subtitle subtitle = new Subtitle(timeStartIntput, timeStopInput);
			
			for(int t=1;t<node.getChildNodes().getLength();t+=2) {
				String speaker=node.getChildNodes().item(t).getAttributes().item(0).getNodeValue();
				String textInput = node.getChildNodes().item(t).getTextContent();
				
				int numCharacterToCutText = 55;
				
				if(textInput.length()>numCharacterToCutText+10) {
					String newText="";
					int stringCharAt=0;

					for(int charAt=stringCharAt;charAt<textInput.length();charAt++) {

						if(charAt+10>=textInput.length()) {
							newText=newText+textInput.substring(stringCharAt, textInput.length());
							stringCharAt=textInput.length();
							
						}else if(charAt-stringCharAt>numCharacterToCutText && textInput.charAt(charAt) == ' ') {
							newText=newText+textInput.substring(stringCharAt, charAt)+"\n";
							stringCharAt=charAt;
							
						}

					}
					textInput=newText;
				}
				
				
				subtitle.addSpeech(new Speech(textInput, speaker));
			}
			
			
			
			
			subtitles.addSubtitles(subtitle);
		}
		
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		
		return subtitles;
	}
	
}
