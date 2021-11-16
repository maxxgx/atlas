package atlas.view;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.List;

/**
 * This hidable menu contains the main options available to the user such as
 * load/save and additional settings for the simulation.
 *
 * @author MaXX
 */
public class MenuOption extends MenuHidable {

    private static final int BORDER = 10;
    private static final int VGAP = 15;
    private static final int HGAP = 15;
    private static final double BUTTON_HEIGHT = 100;
    private static final double BUTTON_WIDTH = 100;
    private static final double LOGO_HEIGHT = 220;
    private static final double LOGO_WIDTH = 320;

    private ImageView logo = SceneLoading.LOGO;
    private ScrollPane container = new ScrollPane();
    private VBox root = new VBox();
    private GridPane grid = new GridPane();
    private Button newSim = new Button("New");
    private Button save = new Button("Save");
    private Button load = new Button("Load");
    private Button credits = new Button("Credits");
    private Button exit = new Button("Exit");

    private CheckBox collisionOne = new CheckBox("Fragments");
    private CheckBox collisionTwo = new CheckBox("Absorb");

    private CheckBox nBodyOne = new CheckBox("Brute force - N^2 (most accurate)");
    private CheckBox nBodyTwo = new CheckBox("Two body - N (no collision)");
    private CheckBox nBodyThree = new CheckBox("BarnesHut Tree - NlogN (brand new*)");

    private CheckBox fullScreen = new CheckBox("Full screen mode");

    private CheckBox trail = new CheckBox("Disable all trails");

    private View view = ViewImpl.getView();

    /**
     * Construct and set a new MenuOption. The root pane is a ScrollPane, which
     * contains a VBox which in turn contains a GridPane.
     */
    public MenuOption() {
        super();

        super.setRight(super.btn);
        this.container.setContent(root);
        root.getChildren().add(grid);
        Insets in = new Insets(BORDER, BORDER, BORDER, BORDER);
        root.setPadding(in);
        root.setSpacing(VGAP);

        root.getChildren().addAll(new Separator(), new Label("ADDITIONAL SETTINGS"), fullScreen, trail);
        root.getChildren().addAll(new Separator(), new Label("Collision system: "), collisionOne, collisionTwo);
        root.getChildren().addAll(new Separator(), new Label("N-Body algorithm: "), nBodyOne, nBodyTwo, nBodyThree);

        this.logo = new ImageView(SceneLoading.LOGO.getImage());
        logo.setPreserveRatio(true);
        logo.setFitHeight(LOGO_HEIGHT);
        logo.setFitWidth(LOGO_WIDTH);

        collisionOne.setSelected(true);
        nBodyThree.setSelected(true);

        this.setupGrid();
        this.setActions();
    }

    @Override
    public void showContent() {
        this.setCenter(container);
    }

    /**
     * Sets the grid pane
     */
    private void setupGrid() {
        grid.setVgap(VGAP);
        grid.setHgap(HGAP);

        /* Setting up each node */
        List<Node> nodes = Arrays.asList(this.logo, this.newSim, this.save, this.load, this.credits, this.exit);
        Arrays.asList(this.newSim, this.save, this.load, this.credits, this.exit).forEach(i -> {
            i.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
            GridPane.setHalignment(i, HPos.CENTER);
        });

        Arrays.asList(this.newSim, this.save, this.load, this.credits, this.exit).forEach(i -> {
            i.setId("provaBtn");
        });

        for (int r = 0, i = 0; i < nodes.size(); r++, i++) {
            GridPane.setConstraints(nodes.get(i), 0, r);
            if (r != 0 && ++i < nodes.size()) {
                GridPane.setConstraints(nodes.get(i), 1, r);
            }
        }

        GridPane.setColumnSpan(logo, 2);
        GridPane.setHalignment(logo, HPos.CENTER);

        // Creates 2 columns with equal width
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(50);
        grid.getColumnConstraints().addAll(col);

        grid.getChildren().addAll(this.logo, this.newSim, this.save, this.load, this.credits, this.exit);
    }

    /**
     * Sets the actions.
     */
    private void setActions() {
        this.newSim.setOnAction(e -> view.notifyObserver(SimEvent.NEW_SIM));
        this.save.setOnAction(e -> view.notifyObserver(SimEvent.SAVE_SIM));
        this.load.setOnAction(e -> view.notifyObserver(SimEvent.LOAD));
        this.exit.setOnAction(e -> view.notifyObserver(SimEvent.EXIT));
        this.credits.setOnAction(e -> view.showCredits());

        this.fullScreen.setOnAction(e -> {
            view.setFullScreen(fullScreen.isSelected());
        });
        this.trail.setOnAction(e -> {
            view.notifyObserver(SimEvent.TOGGLE_TRAILS);
        });

        this.collisionOne.setOnAction(e -> {
            view.notifyObserver(SimEvent.COLLISION_ONE);
            collisionOne.setSelected(true);
            collisionTwo.setSelected(false);
        });
        this.collisionTwo.setOnAction(e -> {
            view.notifyObserver(SimEvent.COLLISION_TWO);
            collisionOne.setSelected(false);
            collisionTwo.setSelected(true);
        });
        this.nBodyOne.setOnAction(e -> {
            view.notifyObserver(SimEvent.NBODY_ONE);
            collisionOne.setDisable(false);
            collisionTwo.setDisable(false);
            nBodyOne.setSelected(true);
            nBodyTwo.setSelected(false);
            nBodyThree.setSelected(false);
        });
        this.nBodyTwo.setOnAction(e -> {
            view.notifyObserver(SimEvent.NBODY_TWO);
            collisionOne.setDisable(true);
            collisionTwo.setDisable(true);
            nBodyOne.setSelected(false);
            nBodyTwo.setSelected(true);
            nBodyThree.setSelected(false);
        });
        this.nBodyThree.setOnAction(e -> {
            view.notifyObserver(SimEvent.NBODY_THREE);
            collisionOne.setDisable(true);
            collisionTwo.setDisable(true);
            nBodyOne.setSelected(false);
            nBodyTwo.setSelected(false);
            nBodyThree.setSelected(true);
        });
    }

}
