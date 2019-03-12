package Subtitles;


public class Subtitles {

		private String text;
		private int time;
		
		public Subtitles(String textIntput, int timeIntput) {
			text = textIntput;
			time = timeIntput;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public int getTime() {
			return time;
		}

		public void setTime(int time) {
			this.time = time;
		}
		
		
}
