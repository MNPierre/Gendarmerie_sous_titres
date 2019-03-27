package subtitler.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import subtitler.io.Decoder;
import subtitler.subtitles.Speech;
import subtitler.subtitles.Subtitle;
import subtitler.subtitles.SubtitlesList;

public class SubtitlesToPDF {

	public static void MakePDF(SubtitlesList subtitles, File out) {
		try {
			PdfWriter writer = new PdfWriter(out);
			PdfDocument pdf = new PdfDocument(writer);
			Document document = new Document(pdf);
			Paragraph par = new Paragraph();
			par.setBold();
			par.add("Sous-titres :");
			document.add(par);


			for(Subtitle sub:subtitles.getSubtitles()) {
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

		} catch ( IOException e) {
			e.printStackTrace();
		}
	}

}
