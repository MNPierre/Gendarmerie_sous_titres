package Subtitles;

public class Speech {

	private static int globalId=0;
	private int id;
	private String text;
	private String author;
	
	public Speech(String textInput, String authorInput) {
		text = textInput;
		author = authorInput;
		id=globalId++;
	}
	
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
	
	public int getId() {
		return id;
	}
	
	public String getXml() {
		return "\t\t\t<text speaker=\""+getAuthor()+"\">"+getText()+"</text>\n";
	}
	
	public String toString() {
		return "author=\""+author+"\", text=\""+text+"\" ";
	}
}
