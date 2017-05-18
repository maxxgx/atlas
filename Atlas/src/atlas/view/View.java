package atlas.view;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import atlas.model.Body;
import atlas.utils.Pair;
import atlas.utils.Units;

/**
 * This interface defines the main methods that a view must have in order to
 * communicate with other classes.
 * 
 * @author MaXX
 *
 */
public interface View {

	/**
	 * Renders the bodies to the screen.
	 * 
	 * @param b
	 *            the bodies
	 * @param time
	 *            time of the simulation
	 * @param fps
	 *            frames per second
	 */
	public void render(List<Body> b, String time, int fps);

	/**
	 * Notifies the observer of the new event.
	 * 
	 * @param event
	 *            the new event
	 */
	public void notifyObserver(SimEvent event);

	/**
	 * Gets the currently selected body.
	 * 
	 * @return an optional of the currently selected body
	 */
	public Optional<Body> getSelectedBody();

	/**
	 * Sets a new Selected body.
	 * 
	 * @param body
	 *            the new body to be selected as an optional, if null sets an
	 *            optional empty
	 */
	public void setSelectedBody(Body body);

	/**
	 * @return whether the screen is locked on the selected body's position
	 */
	public boolean isCameraLocked();

	/**
	 * Set whether the screen is locked on the selected body's position
	 * 
	 * @param b
	 *            locked value
	 */
	public void setCameraLocked(boolean b);

	/**
	 * Gets the center of the rendering screen (the one the bodies are drawn on).
	 * 
	 * @return center of the rendering screen
	 */
	public Pair<Double, Double> getRenderScreenOrig();

	/**
	 * Saves the current mouse position.
	 * 
	 * @param coordinates
	 *            the mouse current position
	 */
	public void setMousePos(Pair<Double, Double> coordinates);

	/**
	 * Gets the last mouse known position.
	 * 
	 * @return the last mouse known position
	 */
	public Pair<Double, Double> getLastMousePos();

	/**
	 * Composed by a number and its unit of measurement. If the input is not
	 * correct it opens an error dialog.
	 * 
	 * @return the speed specified by the user
	 */
	public Optional<Pair<Integer, Units>> getSpeedInfo();

	/**
	 * Gets the modified body by the user.
	 * 
	 * @return modified and updated body
	 */
	public Optional<Body> getUpdatedBody();

	/**
	 * Opens a pop-up window and asks to enter a name.
	 * 
	 * @return the designated save name
	 */
	public Optional<String> getSaveName();

	/**
	 * Opens a pop-up window and asks to choose a file.
	 * 
	 * @param title
	 *            the title of the dialog window
	 * @param action
	 *            name of the action button
	 * @param files
	 *            the files organized as multiple folders, each with its files
	 * @return the chosen file
	 */
	public Optional<File> getLoadFile(String title, String action, Map<File, List<File>> files);

	/**
	 * Updates the offset from the center of the screen (translate) and the
	 * scale of the simulation.
	 * 
	 * @param translate
	 *            the new offset from the center of the screen
	 * @param scale
	 *            the new scale (the relation between the real size of the solar
	 *            system and its size on the screen)
	 */
	public void updateReferences(Pair<Double, Double> translate, double scale);

	/**
	 * @return the current translate (offset from the center of the screen)
	 */
	public Pair<Double, Double> getTranslate();

	/**
	 * @return whether the main scene of the application is set on the stage.
	 */
	public boolean isMainScene();

	/**
	 * Switches the current scene to the main scene.
	 */
	public void switchToMainScene();

	/**
	 * Switches the current scene to the loading scene.
	 */
	public void switchToLoadingScene();

	/**
	 * Enables or disables the full screen mode.
	 * 
	 * @param full
	 *            enable/disable full screen
	 */
	public void setFullScreen(boolean full);
	
	/**
	 * Opens a pop-up window showing the credits.
	 */
	public void showCredits();

	/**
	 * Opens an alert dialog to confirm exit.
	 */
	public void onClose();

}