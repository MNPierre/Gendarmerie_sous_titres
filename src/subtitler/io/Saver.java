package subtitler.io;

import java.io.File;
import java.io.IOException;

import subtitler.subtitles.SubtitlesList;

public class Saver {

	public static void Save(SubtitlesList subtitles, File file) throws IOException {
		String extentionName = file.getName().split(".")[1];
		switch(extentionName) {
		case "pdf":
			SubtitlesToPDF.MakePDF(subtitles, file);
			break;
		case "srt":
			SubtitlesToSRT.MakeSRT(subtitles, file);
			break;
		case "xml":
			Encoder.encodeXML(subtitles, file);
			
		}
	}
}
