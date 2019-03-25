package subtitler.utils;

import java.io.File;
import java.io.IOException;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import subtitler.subtitles.SubtitlesList;

public class SubtitlesToPDF {

	public static void main(String[] args) {
		MakePDF(null, new File("TestPdf.pdf"));
	}
	
	public static void MakePDF(SubtitlesList subtitles, File out) {
		try {
			PdfWriter writer = new PdfWriter(out);
			PdfDocument pdf = new PdfDocument(writer);
			Document document = new Document(pdf);
			document.add(new Paragraph("Hello World!"));
			document.close();
			
		} catch ( IOException e) {
			e.printStackTrace();
		}
	}

}
