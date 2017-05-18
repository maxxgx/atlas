package atlas.view;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import atlas.controller.Controller;
import atlas.model.Body;
import atlas.utils.Pair;
import atlas.utils.Units;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * Implementation of the view interface, it's the application's GUI.
 *
 */
public class ViewImpl implements View {

	private static View view;

	// relation between the real size of the solar system and its size on the
	// screen
	private double scale;
	// offset from the center of the screen (in pixel)
	private Pair<Double, Double> translate = new Pair<>(0.0, 0.0);

	private SceneMain mainScene;
	private SceneLoading loadingScene;

	private Optional<Body> selectedBody = Optional.empty();
	private boolean lockedCamera = false;
	private Optional<Pair<Double, Double>> mousePos = Optional.empty();

	private Controller ctrl;
	private Stage stage;

	public static View getView() {
		if (view == null) {
			throw new IllegalStateException("View not initialized! ERROR");
		}
		return view;
	}

	public ViewImpl(Controller c, Stage primaryStage) {
		view = this;
		this.ctrl = c;
		this.stage = primaryStage;
		this.mainScene = new SceneMain();
		this.loadingScene = new SceneLoading();
		this.stage.setScene(loadingScene);
		this.stage.getIcons().add(SceneLoading.LOGO.getImage());
		primaryStage.setOnCloseRequest(e -> {
			onClose();
			e.consume();
		});
		primaryStage.show();
	}

	@Override
	public void render(List<Body> b, String time, int fps) {
		if (mainScene != null) {
			Platform.runLater(() -> {

				synchronized (b) {
					mainScene.draw(b, scale, translate, time, fps);
				}
				if (!isMainScene()) {
					this.switchToMainScene();
				}
			});
		}
	}

	@Override
	public void notifyObserver(SimEvent event) {
		this.ctrl.update(event);
	}

	@Override
	public Optional<Body> getSelectedBody() {
		return this.selectedBody;
	}

	@Override
	public void setSelectedBody(Body body) { // ToChangeName
		this.selectedBody = Optional.ofNullable(body);

	}

	@Override
	public boolean isCameraLocked() {
		return this.lockedCamera;
	}

	@Override
	public void setCameraLocked(boolean b) {
		this.lockedCamera = b;
	}

	@Override
	public Optional<Pair<Integer, Units>> getSpeedInfo() {
		Pair<Integer, Units> p = null;
		Pair<String, Units> tmp = this.mainScene.cruise.getSpeed();
		try {
			if (!Arrays.asList(Units.values()).contains(tmp.getY())) {
				throw new IllegalArgumentException();
			}
			p = new Pair<>(Integer.parseInt(tmp.getX()), tmp.getY());
		} catch (IllegalArgumentException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR, "Input error, please check.", ButtonType.OK);
			alert.setTitle("Invalid input");
			alert.setHeaderText(null);
			alert.showAndWait();
		}
		return Optional.ofNullable(p);
	}

	@Override
	public Optional<Body> getUpdatedBody() {
		return this.mainScene.infoMenu.extractInfo();
	}

	@Override
	public Optional<String> getSaveName() {
		return InputDialog.getSaveName(this.stage,
				"Save" + new SimpleDateFormat("_dd-MM-yyyy_HH-mm-ss").format(new Date()));
	}

	@Override
	public Optional<File> getLoadFile(String title, String action, Map<File, List<File>> files) {
		return InputDialog.loadFile(this.stage, title, action, files);
	}

	@Override
	public void setMousePos(Pair<Double, Double> coordinates) {
		this.mousePos = Optional.of(coordinates);
	}

	@Override
	public Pair<Double, Double> getLastMousePos() {
		return this.mousePos.get();
	}

	@Override
	public synchronized void updateReferences(Pair<Double, Double> translate, double scale) {
		this.scale = scale;
		this.translate = translate;
	}

	@Override
	public Pair<Double, Double> getTranslate() {
		return this.translate;
	}

	@Override
	public boolean isMainScene() {
		return this.stage.getScene().equals(mainScene);
	}

	@Override
	public void switchToMainScene() {
		this.stage.setScene(mainScene);
	}

	@Override
	public void switchToLoadingScene() {
		this.stage.setScene(loadingScene);
	}

	@Override
	public Pair<Double, Double> getRenderScreenOrig() {
		return new Pair<>(this.mainScene.renderPanel.getWidth() / 2, this.mainScene.renderPanel.getHeight() / 2);
	}

	@Override
	public void setFullScreen(boolean full) {
		this.stage.setFullScreen(full);
	}
	
	@Override
	public void showCredits() {
		InputDialog.credits(stage);		
	}

	@Override
	public void onClose() {
		Alert alert = new Alert(Alert.AlertType.WARNING, "Do you really want to exit?", ButtonType.YES, ButtonType.NO);
		alert.initOwner(this.stage);
		alert.setTitle("Exit ATLAS");
		alert.setHeaderText(null);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.YES) {
			Platform.exit();
			System.exit(0);
		}
	}
}
