package atlas.view;

import atlas.utils.Pair;
import atlas.utils.Units;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * This class contains part of the UI which is used to control the simulation.
 * It provides the following functionalities: - Play/Pause - Show date or time
 * of the simulation - A way to change the simulation speed - EDIT and ADD
 * buttons - Center and deselect.
 * 
 * @author MaXX
 *
 */
public class CruiseControl extends BorderPane {
	private Button buttonPlay = new Button("PLAY");// setGraphic
	private Button buttonStop = new Button("STOP");
	private Button buttonSpeed = new Button("SPEED");
	private Button buttonEdit = new Button("EDIT");
	private Button buttonAdd = new Button("ADD");
	private Button buttonCenter = new Button("CENTER");
	private Button buttonESC = new Button("DESELECT");
	private boolean play;

	private Label labelTime = new Label("Sample: 01/01/2000");

	private TextField textSpeed = new TextField();
	private ChoiceBox<Units> choiceSpeedUnit = new ChoiceBox<>();

	protected ViewMenuOption viewMenu = new ViewMenuOption();
	private HBox left = new HBox();
	private HBox center = new HBox();
	private HBox right = new HBox();
	
	private View view = ViewImpl.getView();

	/**
	 * Constructor of the class.
	 */
	public CruiseControl() {
		this.setId("cruise");

		left.getChildren().add(0, buttonStop);
		left.getChildren().add(1, labelTime);
		left.setAlignment(Pos.CENTER);
		left.setSpacing(10);
		left.getChildren().add(2, textSpeed);
		left.getChildren().add(3, choiceSpeedUnit);
		left.getChildren().add(4, buttonSpeed);
		center.getChildren().add(buttonEdit);
		center.getChildren().add(buttonAdd);
		center.setAlignment(Pos.CENTER);

		right.setAlignment(Pos.CENTER);
		right.getChildren().add(viewMenu);
		right.getChildren().add(buttonCenter);
		right.getChildren().add(buttonESC);

		this.setLeft(left);
		this.setCenter(center);
		this.setRight(right);
		this.play = false;
		this.choiceSpeedUnit.getItems().addAll(Units.values());

		/* Setting actions */
		this.setActions();
	}

	/**
	 * Sets/updates the time label.
	 * 
	 * @param time
	 *            the time of the simulation
	 */
	public void setTime(String time) {
		this.labelTime.setText(time);
	}

	/**
	 * Returns the text in the text field and the selected unit in the choice
	 * box.
	 * 
	 * @return speed text and unit.
	 */
	public Pair<String, Units> getSpeed() {
		return new Pair<>(this.textSpeed.getText(), this.choiceSpeedUnit.getValue());
	}

	/**
	 * Determines which button to show and hide.
	 */
	private void switchPlayStop() {
		left.getChildren().remove(this.play ? this.buttonPlay : this.buttonStop);
		left.getChildren().add(0, this.play ? this.buttonStop : this.buttonPlay);
		this.play = !this.play;
	}

	/**
	 * Sets the components actions.
	 */
	private void setActions() {
		EventHandler<ActionEvent> stopPlayHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				switchPlayStop();
				if (e.getSource().equals(buttonPlay) || e.getSource().equals(buttonStop)) {
					view.notifyObserver(SimEvent.SPACEBAR_PRESSED);
				} else {
					new IllegalAccessException("Button unknow(not play nor stop)");
				}
			}
		};

		this.buttonPlay.setOnAction(stopPlayHandler);
		this.buttonStop.setOnAction(stopPlayHandler);

		this.buttonAdd.setOnAction(e -> view.notifyObserver(SimEvent.ADD));
		
		this.buttonEdit.setOnAction(e -> view.notifyObserver(SimEvent.EDIT));

		this.buttonCenter.setOnAction(e -> view.notifyObserver(SimEvent.CENTER));

		this.buttonSpeed.setOnAction(e -> {
			view.notifyObserver(SimEvent.SPEED_CHANGED);
			this.textSpeed.setText("");
		});

		this.buttonESC.setOnAction(e -> {
			ViewImpl.getView().setSelectedBody(null);
			view.notifyObserver(SimEvent.ESC);
		});
	}
}
