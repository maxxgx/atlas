package atlas.view;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import atlas.model.Body;
import atlas.model.BodyType;
import atlas.utils.Pair;
import atlas.utils.ResourceLoader;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * This pane serves as the render screen, which displays all the bodies for each
 * frame. It consists of multiple layers (from lower to higher): - background
 * image - effects, such as trails - bodies images - bodies labels
 * 
 * @author MaXX
 *
 */
public class RenderScreen extends StackPane {

	private final static String DEFAULT_BACKGROUND = "/images/" + "background.jpg";
	private final static float TRAIL_OPACITY = 0.4f;
	private final static int TRAIL_WIDTH = 5;
	private final static int MIN_IMAGE_SIZE = 1;
	private final static double MIN_SIZE = (double)MIN_IMAGE_SIZE/2;

	private Canvas lBottom = new Canvas(); // the bottom layer
	private Pane lMid = new Pane();
	private Pane lTop = new Pane(); // the top layer -> labels
	private Label fpsCounter = new Label();
	private Label bodyCounter = new Label();

	private Map<Long, Pair<Pair<ImageView, Label>, Color>> bMap = new HashMap<>();

	/*
	 * second chance = if a entry is not used it will be removed from the bMap
	 */
	private Map<Long, Boolean> secondChanceMap = new HashMap<>();

	private double currentScale;
	private Pair<Double, Double> currentTranlate;

	private View view = ViewImpl.getView();

	/**
	 * Constructor
	 */
	public RenderScreen() {
		this.setId("game-screen");
		this.setBackgroundImage(DEFAULT_BACKGROUND);

		/* Resizable canvas */
		DoubleBinding x = this.widthProperty().subtract(0);
		DoubleBinding y = this.heightProperty().subtract(0);
		this.lBottom.widthProperty().bind(x);
		this.lBottom.heightProperty().bind(y);
		this.lBottom.widthProperty().bind(x);
		this.lBottom.heightProperty().bind(y);

		/* FPS counter */
		this.lTop.getChildren().add(fpsCounter);
		this.lTop.getChildren().add(bodyCounter);
		fpsCounter.setTextFill(Color.MAGENTA);
		fpsCounter.setFont(Font.font("Roboto", FontWeight.BOLD, 20));
		bodyCounter.setTextFill(Color.MAGENTA);
		bodyCounter.setFont(Font.font("Roboto", FontWeight.BOLD, 20));
		bodyCounter.setTranslateY(25);

		/* Resizable pane */
		this.maxHeight(Double.MAX_VALUE);
		this.maxWidth(Double.MAX_VALUE);
		this.setMinSize(0, 0);

		/* Adding children/layers */
		this.getChildren().addAll(this.lBottom);
		this.getChildren().addAll(this.lMid, this.lTop);

		/* Default translate */
		Double defTran = new Double(0);
		this.currentTranlate = new Pair<>(defTran, defTran);
	}

	/**
	 * Sets the background image.
	 * 
	 * @param imageUrl
	 *            the Url of the image
	 */
	public void setBackgroundImage(String imageUrl) {
		this.setStyle("-fx-background-image: url('" + imageUrl + "'); " + "-fx-background-position: center center; "
				+ "-fx-background-repeat: stretch;" + "-fx-background-size: inherit;");
	}

	/**
	 * Draws the bodies to the screen with the relative image, name and trail.
	 * 
	 * @param bodies
	 *            the bodies to be rendered
	 * @param scaleType
	 *            the rendering mode (size of the bodie's image)
	 * @param scale
	 *            the relation between the real size of the solar system and its
	 *            size on the screen.
	 * @param translate
	 *            offset from the center of the screen (in pixel)
	 * @param fps
	 *            frames per second
	 * @param disabledTrail
	 *            list of body types to hide the trail
	 */
	public void render(List<Body> bodies, RenderScale scaleType, double scale, Pair<Double, Double> translate, int fps,
			Set<BodyType> disabledTrail) {
		/* Preliminary actions */
		this.adjustScreen(scale, translate);
		this.clearScreen();
		this.fpsCounter.setText("FPS: " + fps);
		this.bodyCounter.setText("# bodies: " + bodies.size());

		this.secondChanceMap.replaceAll((k, v) -> Boolean.FALSE);

		/* Drawing the new frame */
		bodies.forEach(b -> {
			ImageView img = this.getScaledBodyImage(b, scaleType, scale);

			// If not present, create new entry
			if (!this.bMap.keySet().contains(b.getId())) {
				Label lab = new Label(b.getName());
				lab.setTextFill(Color.WHITESMOKE);
				bMap.put(b.getId(), new Pair<>(new Pair<>(img, lab), this.pickColor()));
				secondChanceMap.put(b.getId(), true);

				lMid.getChildren().add(img);
				lTop.getChildren().add(lab);

				// Sets the actions
				this.setLableOnClick(lab, b);

				// this.setLabelOnRelease(entry.getX().getY(), b);
			}

			Pair<Pair<ImageView, Label>, Color> entry = bMap.get(b.getId());
			this.secondChanceMap.put(b.getId(), true);

			/* Draw the body's trail only if it's enabled */
			if (!disabledTrail.contains(b.getType())) {
				this.drawTrail(b, scale, entry.getY());
			}

			/* updates the label name if it has been changed */
			entry.getX().getY().setText(b.getName());

			if (b.getType() == BodyType.SATELLITE) {
				entry.getX().getY().setVisible(b.getProperties().getRadius() * 2 * scale > MIN_SIZE);
			}

			/*
			 * Place the image centered to the body point. Labels are placed
			 * next to the image
			 */
			entry.getX().getX().relocate(this.calcPosX(b.getPosX()) - entry.getX().getX().getFitWidth() / 2,
					this.calcPosY(b.getPosY()) - entry.getX().getX().getFitHeight() / 2);
			entry.getX().getY().relocate(this.calcPosX(b.getPosX() + b.getProperties().getRadius()),
					this.calcPosY(b.getPosY()));
		});

		// remove all non used bodies
		this.secondChanceMap.entrySet().stream().filter(i -> !i.getValue()).forEach(i -> {
			// remove image
			this.lMid.getChildren().remove(bMap.get(i.getKey()).getX().getX());
			// remove label
			this.lTop.getChildren().remove(bMap.get(i.getKey()).getX().getY());
			bMap.remove(i.getKey());
		});
		this.secondChanceMap = this.secondChanceMap.entrySet().stream().filter(i -> i.getValue())
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
	}

	/**
	 * Calculates the X position on screen given the real X coordinate.
	 * 
	 * @param realX
	 *            the x coordinate in real units.
	 * @return the x coordinate on the screen.
	 */
	private double calcPosX(double realX) {
		return this.getWidth() / 2 + this.currentTranlate.getX() + realX * this.currentScale;
	}

	/**
	 * Calculates the Y position on screen given the real Y coordinate.
	 * 
	 * @param realY
	 *            the y coordinate in real units.
	 * @return the y coordinate on the screen.
	 */
	private double calcPosY(double realY) {
		return this.getHeight() / 2 + this.currentTranlate.getY() - realY * this.currentScale;
	}

	/**
	 * Picks a random rgb color.
	 * 
	 * @return a random rgb color with TRAIL_OPACITY
	 */
	private Color pickColor() {
		Random r = new Random();
		return new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), TRAIL_OPACITY);
	}

	/**
	 * Draws the trail of the given body.
	 * 
	 * @param b
	 *            the body
	 * @param scale
	 *            scale of the simulation
	 * @param color
	 *            color of the trail
	 */
	private void drawTrail(Body b, double scale, Color color) {
		int minPointsTodraw = 2;
		if (b.getTrail().size() < minPointsTodraw) {
			return;
		}
		int arraySize = b.getTrail().size();
		double[] pointsX = new double[arraySize];
		double[] pointsY = new double[arraySize];
		double[] points = new double[arraySize * 2];
		Iterator<Pair<Double, Double>> it = b.getTrail().iterator();
		int x = 0;
		int y = 0;
		int i = 0;
		while (it.hasNext() && x < arraySize) {
			Pair<Double, Double> p = it.next();
			pointsX[x++] = this.calcPosX(p.getX());
			pointsY[y++] = this.calcPosY(p.getY());
			points[i++] = this.calcPosX(p.getX());
			points[i++] = this.calcPosY(p.getY());
		}
		/* Stokes the trail on the canvas layer */
		GraphicsContext gc = this.lBottom.getGraphicsContext2D();
		gc.setStroke(color);
		gc.setLineWidth(TRAIL_WIDTH);
		gc.strokePolyline(pointsX, pointsY, arraySize);
	}

	/**
	 * It computes the image that should be drawn on the screen.
	 * 
	 * @param b
	 *            the body
	 * @param scaleType
	 *            rendering scale mode
	 * @param scale
	 *            scale of the simulation
	 * @return the image of the body, properly sized.
	 */
	private ImageView getScaledBodyImage(Body b, RenderScale scaleType, double scale) {
		/* Getting or loading the image */
		ImageView img = null;
		try {
			if (bMap.containsKey(b.getId())) {
				img = bMap.get(b.getId()).getX().getX();
			} else {
				img = new ImageView(new Image(ResourceLoader.loadAsStream(b.getImagePath())));
			}
		} catch (IllegalArgumentException ie) {
			throw new IllegalStateException("body image path can't be found : " + b.getImagePath());
		} 

		/* Scaling to appropriate scale */
		double diamScaled = MIN_IMAGE_SIZE;
		switch (scaleType) {
		case REAL:
			diamScaled = b.getProperties().getRadius() * 2 * scale;
			break;

		default:
			diamScaled = scaleType.getSize(b.getType());
			break;
		}

		img.setFitHeight(diamScaled >= MIN_IMAGE_SIZE ? diamScaled : MIN_IMAGE_SIZE);
		img.setFitWidth(diamScaled >= MIN_IMAGE_SIZE ? diamScaled : MIN_IMAGE_SIZE);
		img.setPreserveRatio(true);
		img.setRotate(b.getProperties().getRotationAngle());

		return img;
	}

	/**
	 * Updates scale and translate according to changes.
	 * 
	 * @param scale
	 *            scale of the simulation
	 * @param translate
	 *            offset from the center of the screen
	 */
	private void adjustScreen(double scale, Pair<Double, Double> translate) {
		if (view.isCameraLocked() && view.getSelectedBody().isPresent()) {
			this.currentTranlate = new Pair<>(view.getSelectedBody().get().getPosX() * -scale,
					view.getSelectedBody().get().getPosY() * scale);
			this.currentScale = scale;
		} else if (this.currentScale != scale || !translate.equals(currentTranlate)) {
			this.currentScale = scale;
			this.currentTranlate = translate;
		}
	}

	/**
	 * Clears the rendering screen, deleting the trails.
	 */
	private void clearScreen() {
		this.lBottom.getGraphicsContext2D().clearRect(0, 0, lBottom.getWidth(), lBottom.getHeight());
	}

	/**
	 * Sets the action for a body's label.
	 * 
	 * @param lab
	 *            the body's label
	 * @param body
	 */
	private void setLableOnClick(Label lab, Body body) {
		lab.setOnMouseClicked(e -> {
			view.setSelectedBody(body);
			if (e.getClickCount() > 1) {
				view.notifyObserver(SimEvent.LOCK);
			}
		});
	}
}
