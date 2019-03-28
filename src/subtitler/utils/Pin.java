package subtitler.utils;

import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import subtitler.controlers.MainControler;

public class Pin {

	Circle head;
	Rectangle body;

	Mode mode;

	Pin siblingPin;

	TextField timeText;

	boolean isMoving;

	boolean isActivate;

	double boundingStart;
	double boundingEnd;

	double mouseXCoordinateBuffer;
	
	Rectangle selectedZone;

	public static enum Mode{
		START, END;
	}

	public Pin(Mode mode) {
		this.mode = mode;
		isActivate=true;

		isMoving = false;

		head = new Circle();
		body = new Rectangle();

		head.setRadius(5);
		body.setHeight(15);
		body.setWidth(2);

		head.setFill(Paint.valueOf("#ff1f1f"));
		body.setFill(Paint.valueOf("#ccff1f"));

	}
	
	public void setPlayableZone(Rectangle selectedZone) {
		this.selectedZone=selectedZone;
	}

	public void setBounding(double start, double end) {
		boundingStart=start;
		boundingEnd=end;
	}

	public void setTimeField(TextField timeTextField) {
		timeText = timeTextField;
	}

	public void setSiblingPin(Pin pin) {
		siblingPin=pin;
	}

	public void setSize(double length) {
		body.setHeight(length);
	}

	public double getSize() {
		return body.getHeight();
	}

	public void setLayoutX(double x) {
		head.setLayoutX(x+head.getRadius()/2);
		body.setLayoutX(x+body.getWidth()/2);
	}

	public void setLayoutY(double y) {
		head.setLayoutY(y-head.getRadius()/2);
		body.setLayoutY(y);
	}

	public void setVisibility(boolean visibility) {
		head.setOpacity(visibility?1:0);
		body.setOpacity(visibility?1:0);
		head.setDisable(!visibility);
		head.setDisable(!visibility);
		isActivate=visibility;
		isMoving = false;
		selectedZone.setVisible(visibility);
	}

	public double getLayoutX() {
		return head.getLayoutX()-head.getRadius()/2;
	}

	public double getLayoutY() {
		return head.getLayoutY();
	}

	/*
	 * Warning, this function is not returning the correct XProperty
	 */
	public DoubleProperty layoutXProperty() {
		return body.layoutYProperty();
	}

	public DoubleProperty layoutYProperty() {
		return body.layoutYProperty();
	}

	public void addToPane(Pane pane) {
		pane.getChildren().add(body);
		pane.getChildren().add(head);
	}
	
	public void update() {
		if(isActivate ) {

			switch(mode) {
			case START:
				if( ConversionStringMilli.StringToMillisecond(timeText.getText())<ConversionStringMilli.StringToMillisecond(siblingPin.timeText.getText()) ) {
					setLayoutX(boundingStart+ConversionStringMilli.StringToMillisecond(timeText.getText())*(MainControler.barre_fond.getWidth()/MainControler.player.getTotalDuration().toMillis()));
					selectedZone.setWidth(siblingPin.getLayoutX()-getLayoutX());
					selectedZone.setLayoutX(getLayoutX()-boundingStart);
				}
				break;

			case END:
				if( ConversionStringMilli.StringToMillisecond(timeText.getText())>ConversionStringMilli.StringToMillisecond(siblingPin.timeText.getText()) ) {
					setLayoutX(boundingStart+ConversionStringMilli.StringToMillisecond(timeText.getText())*(MainControler.barre_fond.getWidth()/MainControler.player.getTotalDuration().toMillis()));
					selectedZone.setWidth(getLayoutX()-siblingPin.getLayoutX());
				}
				break;	
			}
			
		}
	}

	public void addListener() {
		if(siblingPin == null)
			throw new Error("The sibling pin wasn't set");

		else if(mode == null)
			throw new Error("The mode wasn't set");
		else if(timeText == null)
			throw new Error("The time TextField wasn't set");
		else if(selectedZone == null)
			throw new Error("The PlayableZone Rectangle wasn't set");
		else
			head.setOnMouseDragged(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {

					if(isActivate ) {
						double newMouseCoordinate = event.getX()-mouseXCoordinateBuffer+getLayoutX();

						double newCoordinate;
						
						switch(mode) {
						case START:
							if(newMouseCoordinate+10 < siblingPin.getLayoutX()) {

								if(newMouseCoordinate<boundingEnd) {

									if(newMouseCoordinate> boundingStart) {
										newCoordinate = newMouseCoordinate;
									}else {
										newCoordinate = boundingStart;
									}
								}else {
									newCoordinate = boundingEnd;
								}
							}else {
								newCoordinate = siblingPin.getLayoutX()-10;
							}

							setLayoutX(newCoordinate);
							selectedZone.setLayoutX(getLayoutX()-boundingStart);
							selectedZone.setWidth(siblingPin.getLayoutX()-getLayoutX());
							break;

						case END:
							if(newMouseCoordinate-10 > siblingPin.getLayoutX()) {

								if(newMouseCoordinate<boundingEnd) {

									if(newMouseCoordinate> boundingStart) {
										newCoordinate = newMouseCoordinate;
									}else {
										newCoordinate = boundingStart;
									}
								}else {
									newCoordinate = boundingEnd;
								}
							}else {
								newCoordinate = siblingPin.getLayoutX()+10;
							}

							setLayoutX(newCoordinate);
							selectedZone.setWidth(getLayoutX()-siblingPin.getLayoutX());
							break;	
						}
						
						timeText.setText( ConversionStringMilli.MillisecondsToString( (long) ((getLayoutX()-boundingStart)/MainControler.barre_fond.getWidth()*MainControler.player.getTotalDuration().toMillis()) ) );

					}
				}
			});

	}

}
