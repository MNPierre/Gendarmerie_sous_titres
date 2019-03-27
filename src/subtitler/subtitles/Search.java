package subtitler.subtitles;

public class Search {
	public static SubtitlesList recherche(String mot, SubtitlesList subtitles){
		SubtitlesList res = new SubtitlesList();
		if(!mot.equals("")) {
			for(Subtitle sub:subtitles.getSubtitles()) {
				for(Speech sp: sub.getContenu()) {
					if(sp.getText().toUpperCase().contains(mot.toUpperCase())) {
						res.addSubtitles(sub);
					}
				}

			}
		}
		return res;
	}
}
