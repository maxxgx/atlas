package atlas.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * The initial scene when the program is launched but the renderer is not ready
 * yet.
 * 
 * @author MaXX
 *
 */
public class SceneLoading extends Scene {

	/**
	 * ATLAS logo.
	 */
	protected final static ImageView LOGO = new ImageView(new Image("/images/" + "logo.png"));
	final static int FONT_SIZE = 16;

	private BorderPane root = new BorderPane();
	// private ProgressBar progress = new ProgressBar(0.6);
	private VBox bottom = new VBox();

	/**
	 * Construct a loading scene.
	 */
	public SceneLoading() {
		super(new Pane());
		root.setStyle("-fx-background-color: black;");
		root.setCenter(LOGO);
		Label loadingText = new Label("Loading...");
		loadingText.setTextFill(Color.WHITESMOKE);
		loadingText.setFont(Font.font(FONT_SIZE));
		root.setBottom(bottom);
		BorderPane.setAlignment(bottom, Pos.CENTER);

		bottom.setAlignment(Pos.CENTER);
		bottom.getChildren().addAll(loadingText);

		this.setRoot(root);
	}

}
