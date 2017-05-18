package atlas.view;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

/**
 * This abstract class offers a hide button that when pressed it hides/shows the
 * content.
 * 
 * @author MaXX
 *
 */
public abstract class MenuHidable extends BorderPane {

	protected Button btn;

	/**
	 * Constructor for a generic hidable menu.
	 */
	public MenuHidable() {
		/* Set all children's style in the css file */
		this.setId("menu-hidable");

		btn = new Button("|||");
		btn.setMaxHeight(Double.MAX_VALUE);
		btn.setOnAction(e -> {
			if (isShown()) {
				hideContent();
			} else {
				showContent();
			}
		});
	}

	/**
	 * Show the content of this container.
	 */
	public abstract void showContent();

	/**
	 * Hides the content of this container by removing all its children. 
	 */
	public void hideContent() {
		this.getChildren().removeIf(i -> !i.equals(btn));
	}

	/**
	 * @return whether the content is shown or not.
	 */
	public boolean isShown() {
		return this.getChildren().size() > 1 && this.getChildren().contains(btn);
	}
}