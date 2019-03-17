package XML;

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

import Subtitles.Speech;
import Subtitles.Style;
import Subtitles.Subtitle;
import Subtitles.SubtitlesList;

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
			System.out.println(style);
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
					timeStartIntput = StringToMillisecond(attribut.getTextContent());
					break;
				case "stop":
					timeStopInput = StringToMillisecond(attribut.getTextContent());
					break;
				}
			}
			
			Subtitle subtitle = new Subtitle(timeStartIntput, timeStopInput);
			
			for(int t=1;t<node.getChildNodes().getLength();t+=2) {
				String speaker=node.getChildNodes().item(t).getAttributes().item(0).getNodeValue();
				String textInput = node.getChildNodes().item(t).getTextContent();
				
				int numCharacterToCutText = 65;
				
				if(textInput.length()>numCharacterToCutText+10) {
					String newText="";
					int stringCharAt=0;

					for(int charAt=stringCharAt;charAt<textInput.length();charAt++) {

						if(charAt+10>=textInput.length()) {
							newText=newText+textInput.substring(stringCharAt, textInput.length());
							stringCharAt=textInput.length();
							
						}else if(charAt-stringCharAt>numCharacterToCutText && textInput.charAt(charAt) == ' ') {
							System.out.println("New \n");
							newText=newText+textInput.substring(stringCharAt, charAt)+"\n";
							stringCharAt=charAt;
							
						}

					}
					textInput=newText;
				}
				
				
				subtitle.addSpeech(new Speech(textInput, speaker));
			}
			
			
			
			
			System.out.println(subtitle);
			subtitles.addSubtitles(subtitle);
		}
		
		
		
		
		
		
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		
		return subtitles;
	}

	public static long StringToMillisecond(String time) {
		try {
			String stringMilli = time.replaceAll("[^0123456789:.]", "");
			String[] splitedTime = stringMilli.split(":");
			int hours = splitedTime.length<1?0:Integer.parseInt(splitedTime[0]);
			int minutes = splitedTime.length<2?0:Integer.parseInt(splitedTime[1]);
			String[] secAndMilliSec = splitedTime.length<3?"00.000".split("[.]"):splitedTime[2].split("[.]");
			int seconds = Integer.parseInt(secAndMilliSec[0]);
			int milliseconds = Integer.parseInt(secAndMilliSec[1]);
			return hours*1000*60*60 + minutes*1000*60 + seconds*1000 + milliseconds;
		}catch(NumberFormatException e) {
			e.printStackTrace();
			return 0;
		}
		
	}
	
}
