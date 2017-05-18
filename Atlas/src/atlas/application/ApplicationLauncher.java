package atlas.application;

import atlas.controller.Controller;
import atlas.controller.ControllerImpl;
import atlas.view.View;
import atlas.view.ViewImpl;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * 
 * This class contains the main method that is used to launch the application.
 *
 */
public class ApplicationLauncher extends Application{
	
	private static int HEIGHT = 800;
	private static int WIDTH = 1280;

    public static void main(String[] args) {
        //this method must create the controller and the view
        javafx.application.Application.launch();
    }

    @Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("ATLAS");
		primaryStage.setWidth(WIDTH);
		primaryStage.setHeight(HEIGHT);
		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
		primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) /2 );
		primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) /2  );

		Controller c = ControllerImpl.getIstanceOf();
        
        View v = new ViewImpl(c, primaryStage);
        c.setView(v);
        c.startSim();
	}
}
