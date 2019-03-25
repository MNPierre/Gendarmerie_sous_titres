package subtitler.subtitles;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import subtitler.utils.ConversionStringMilli;

public class Speech {

	private static int globalId=0;
	private int id;
	private StringProperty text;
	private StringProperty author;
	
	public Speech(String textInput, String authorInput) {
		text = new SimpleStringProperty(textInput);
		author = new SimpleStringProperty(authorInput);
		id=globalId++;
	}
	
	public String getText() {
		return text.getValue();
	}

	public void setText(String text) {
		this.text.setValue(text);
	}

	public String getAuthor() {
		return author.getValue();
	}
	
	public StringProperty getTextProperty() {
		return text;
	}
	
	public StringProperty getAuthorProperty() {
		return author;
	}

	public void setAuthor(String author) {
		this.author.setValue(author);
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
