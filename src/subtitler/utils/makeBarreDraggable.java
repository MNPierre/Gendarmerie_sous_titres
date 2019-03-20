package subtitler.utils;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import subtitler.controlers.MainControler;

public class makeBarreDraggable {

	static double m_nX = 0;
	static double m_nMouseX = 0;

	public static EventHandler<MouseEvent> getEventOnMousePressed(Group barre) {
		EventHandler<MouseEvent> mousePressHandler = new EventHandler<MouseEvent>() {

			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.PRIMARY) {
					// get the current mouse coordinates according to the scene.
					m_nMouseX = event.getSceneX();

					// get the current coordinates of the draggable node.
					m_nX = barre.getLayoutX();
				}
			}
		};return mousePressHandler;
	}

	public static EventHandler<MouseEvent> getEventDragMouse(Group barre) {
		/*otherBar = MainControler.controleur.getSelectionBarre(0);
		if(otherBar.getLayoutX() - barre.getLayoutX() == 0) {
			otherBar = MainControler.controleur.getSelectionBarre(1);
		}*/
		
		EventHandler<MouseEvent> dragHandler = new EventHandler<MouseEvent>() {
			
			
			public void handle(MouseEvent event) {
				
				if (event.getButton() == MouseButton.PRIMARY) {
					// find the delta coordinates by subtracting the new mouse
					// coordinates with the old.
					double deltaX = event.getSceneX() - m_nMouseX;

					// add the delta coordinates to the node coordinates.
					m_nX += deltaX;
					if(m_nX >=0 && m_nX <= 888)
							barre.setLayoutX(m_nX);						




					// get the latest mouse coordinate.
					m_nMouseX = event.getSceneX();

				}
			}
		};
		return dragHandler;
	}
}
