package subtitler.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import subtitler.subtitles.Speech;
import subtitler.subtitles.Subtitle;
import subtitler.subtitles.SubtitlesList;
import subtitler.utils.ConversionStringMilli;

public class Saver {

	public static void Save(SubtitlesList subtitles, File file, String extention) throws IOException {
		File newFile;
		String path ="";
		int extentionsetter = file.getAbsolutePath().split("[.]").length>1?-1:0;
		for(int i=0;i<file.getAbsolutePath().split("[.]").length+extentionsetter;i++) {
			path+=file.getAbsolutePath().split("[.]")[i]+".";
		}
		switch(extention) {
		case "PDF":
			newFile = new File(path+"pdf");
			MakePDF(subtitles, newFile);
			break;
		case "SRT":
			newFile = new File(path+"srt");
			MakeSRT(subtitles, newFile);
			break;
		case "XML":
			newFile = new File(path+"xml");
			Encoder.encodeXML(subtitles, newFile);
			
		}
	}
	
	public static void MakeSRT(SubtitlesList subtitles, File out) throws IOException {
		int subtitleNumber=1;
		String srtContent="";
		for(Subtitle sub:subtitles.getSubtitlesAsSortedCollection()) {
			srtContent+=subtitleNumber+"\n";
			srtContent+=ConversionStringMilli.MillisecondsToString(sub.getTimeStart()).replace(".", ",")
					+" --> "+ConversionStringMilli.MillisecondsToString(sub.getTimeStop()).replace(".", ",")+"\n";

			for(Speech speech:sub.getContenu()) {
				srtContent+=speech.getText()+"\n\n";
			}
			subtitleNumber++;
		}

			FileWriter fw = new FileWriter(out);
			fw.write(srtContent);
			fw.close();


	}
	
	public static void MakePDF(SubtitlesList subtitles, File out) throws FileNotFoundException {
		PdfWriter writer = new PdfWriter(out);
		PdfDocument pdf = new PdfDocument(writer);
		Document document = new Document(pdf);
		Paragraph par = new Paragraph();
		par.setBold();
		par.add("Sous-titres :");
		document.add(par);


		for(Subtitle sub:subtitles.getSubtitlesAsSortedCollection()) {
			par = new Paragraph();	
			par.setMarginLeft(5);
			par.setFontSize(15);
			par.add("Commence de "+ConversionStringMilli.MillisecondsToString(sub.getTimeStart())
			+" â "+ConversionStringMilli.MillisecondsToString(sub.getTimeStart()));
			document.add(par);
			
			for(Speech speech:sub.getContenu()) {

				par = new Paragraph();
				par.setMarginLeft(10);

				par.add("- "+speech.getAuthor()+" :");
				par.setFontSize(10);
				document.add(par);
				par = new Paragraph();
				par.setItalic();
				par.setMarginLeft(16);
				par.add("\""+speech.getText()+"\"");
				document.add(par);
			}
		}

		document.close();
}
	
}
