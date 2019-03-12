package Subtitles;

import java.util.ArrayList;

public class Subtitle {

		private ArrayList<Speech> contenu;
		private long timeStart;
		private long timeStop;
		
		public Subtitle(long timeStartIntput, long timeStopInput) {
			contenu = new ArrayList<>();
			timeStart = timeStartIntput;
			timeStop = timeStopInput;
		}
		
		public void addSpeech(Speech speech) {
			contenu.add(speech);
		}
		
		public ArrayList<Speech> getContenu() {
			return contenu;
		}

		public void setContenu(ArrayList<Speech> contenu) {
			this.contenu = contenu;
		}

		public long getTimeStart() {
			return timeStart;
		}

		public void setTimeStart(long timeStart) {
			this.timeStart = timeStart;
		}

		public long getTimeStop() {
			return timeStop;
		}

		public void setTimeStop(long timeStop) {
			this.timeStop = timeStop;
		}
		
		public static String MillisecondsToString(long time) {
	        
	        long milliseconds=time%1000; 
	        time=time/1000; 
	        long seconds=time%60; 
	         time=time/60; 
	        long minutes=time%60; 
	         time=time/60; 
	        long hours=time;
	        return (hours+":"+minutes+":"+seconds+"."+milliseconds);
	    }


		public String getXml() {
			String res = "\t<subtitle start=\""+MillisecondsToString(getTimeStart())+"\" stop=\""+MillisecondsToString(getTimeStop())+"\">\n";
					for(Speech s : contenu) {
						res += s.getXml();
					}
				res += "\t</subtitle>\n";
				return res;
		}
		
}
