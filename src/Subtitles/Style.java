package Subtitles;

public class Style {
	
	private String narrator;
	private String color;
	
	public Style(String narratorInput, String colorInput) {
		narrator = narratorInput;
		color = colorInput;
	}
	
	public String getXml() {
		return "\t<style name=\""+getNarrator()+"\" color=\""+getColor()+"\"></style>\n";
	}

	public String getNarrator() {
		return narrator;
	}

	public void setNarrator(String narrator) {
		this.narrator = narrator;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}
