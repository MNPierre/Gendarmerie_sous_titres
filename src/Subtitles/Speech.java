package Subtitles;

public class Speech {
	
	private String text;
	private String author;
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Speech(String textInput, String authorInput) {
		text = textInput;
		author = authorInput;
	}
	
	public String getXml() {
		return "\t\t<text speaker=\""+getAuthor()+"\">"+getText()+"</text>\n";
	}
}
