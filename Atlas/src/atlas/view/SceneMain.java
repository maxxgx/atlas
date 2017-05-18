package atlas.view;

import java.util.List;
import java.util.Set;

import atlas.model.Body;
import atlas.model.BodyType;
import atlas.utils.Pair;
import atlas.utils.ResourceLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 * The main scene of the application, it contains: a CruiseControl, MenuOption,
 * MenuInfo and RenderScreen.
 * 
 * @author MaXX
 *
 */
public class SceneMain extends Scene {

	private BorderPane root = new BorderPane();
	CruiseControl cruise;
	MenuInfo infoMenu;
    RenderScreen renderPanel;
	MenuOption optionMenu;

	private View view;

	/**
	 * Construct the main scene and its children nodes.
	 */
	public SceneMain() {
		super(new Pane());
		this.view = ViewImpl.getView();
		this.renderPanel = new RenderScreen();
		this.cruise = new CruiseControl();
		this.optionMenu = new MenuOption();
		this.infoMenu = new MenuInfo();

		this.root.setMinSize(0, 0);
		this.root.setCenter(renderPanel);
		this.root.setBottom(cruise);
		this.root.setLeft(optionMenu);
		this.root.setRight(infoMenu);

		this.setRoot(root);

		this.setCommands();
		
		/* load the CSS style sheet*/
		this.getStylesheets().clear();
		this.getStylesheets().add(ResourceLoader.loadAsURL("UIStyle.css").toExternalForm());
	}

	/**
	 * Convenient method to call all action setup methods.
	 */
	private void setCommands() {
		this.setKeyboardCommands();
		this.setScrollCommands();
		this.setMouseCommands();
	}

	/**
	 * Sets the action on mouse scroll.
	 */
	private void setScrollCommands() {
		this.renderPanel.setOnScroll(e -> {
			if (e.getDeltaY() > 0) {
				this.view.setMousePos(new Pair<Double, Double>(e.getSceneX(), e.getSceneY()));
				this.view.notifyObserver(SimEvent.MOUSE_WHEEL_UP);
			} else {
				this.view.setMousePos(new Pair<Double, Double>(e.getX(), e.getY()));
				this.view.notifyObserver(SimEvent.MOUSE_WHEEL_DOWN);
			}
		});
	}

	/**
	 * Sets the keyboard actions.
	 */
	private void setKeyboardCommands() {
		root.setOnKeyPressed(k -> {
			switch (k.getCode()) {
			case W:
				view.notifyObserver(SimEvent.W);
				break;
			case A:
				view.notifyObserver(SimEvent.A);
				break;
			case S:
				view.notifyObserver(SimEvent.S);
				break;
			case D:
				view.notifyObserver(SimEvent.D);
				break;

			case ESCAPE:
				view.notifyObserver(SimEvent.ESC);
				break;

			case SPACE:
				view.notifyObserver(SimEvent.SPACEBAR_PRESSED);
				break;
			default:
				break;
			}
		});
	}

	/**
	 * Sets the mouse actions.
	 */
	private void setMouseCommands() {
		this.renderPanel.setOnMouseClicked(e -> {
			if(e.getButton() == MouseButton.SECONDARY) {
				ViewImpl.getView().setSelectedBody(null);
				ViewImpl.getView().notifyObserver(SimEvent.ESC);
			}
			view.setMousePos(new Pair<Double, Double>(e.getX(), e.getY()));
			view.notifyObserver(SimEvent.STOP_EDIT);
			view.notifyObserver(SimEvent.MOUSE_CLICKED);
			
		});

	}

	/**
	 * Draw the bodies by delegating the task to the render screen.
	 * 
	 * @param bodies
	 *            the bodies to be drawn
	 * @param scale
	 *            scale of the simulation
	 * @param translate
	 *            offset from the center of the screen
	 * @param time
	 *            time of the simulation
	 * @param fps
	 *            frames per second
	 */
	protected void draw(List<Body> bodies, double scale, Pair<Double, Double> translate, String time, int fps) {
		RenderScale scaleType = this.cruise.viewMenu.getSelectedScale();
		Set<BodyType> disabledTrail = this.cruise.viewMenu.getDisableTrailTypes();

		this.cruise.setTime(time);
		if (this.infoMenu.isShown()) {
			this.infoMenu.update(ViewImpl.getView().getSelectedBody());
		}
		this.renderPanel.render(bodies, scaleType, scale, translate, fps, disabledTrail);
	}
}
