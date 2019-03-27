package subtitler.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import subtitler.subtitles.Speech;
import subtitler.subtitles.Subtitle;
import subtitler.subtitles.SubtitlesList;
import subtitler.utils.ConversionStringMilli;

public class SubtitlesToSRT {
	
	public static void MakeSRT(SubtitlesList subtitles, File out) throws IOException {
		int subtitleNumber=1;
		String srtContent="";
		for(Subtitle sub:subtitles.getSubtitles()) {
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
}
