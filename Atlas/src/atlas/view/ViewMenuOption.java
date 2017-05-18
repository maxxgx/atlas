package atlas.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import atlas.model.BodyType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;

/**
 * This menu bar provides two menus with the following functions: choose the
 * render scale type and which body type's trail to hide.
 * 
 * @author MaXX
 *
 */
public class ViewMenuOption extends MenuBar {

	private Menu size = new Menu("Scale");
	private Menu trail = new Menu("Hide trails");

	private RenderScale selectedScale = RenderScale.REAL;
	private List<CheckMenuItem> trailItems = new ArrayList<>();
	private Set<BodyType> selectedTypes = new HashSet<>();

	/**
	 * Construct the menu bar.
	 */
	public ViewMenuOption() {

		/* Setting scale options */
		ToggleGroup group = new ToggleGroup();
		for (RenderScale s : RenderScale.values()) {
			RadioMenuItem item = new RadioMenuItem(s.toString());
			item.setToggleGroup(group);
			this.size.getItems().add(item);
			if (s == RenderScale.REAL) {
				item.setSelected(true);
			}
			item.setOnAction(a -> {
				this.selectedScale = s;
			});
		}

		/* Setting Item - ALL */
		CheckMenuItem trailAll = new CheckMenuItem("All");
		this.trail.getItems().add(trailAll);
		trailAll.setOnAction(a -> {
			if (!trailAll.isSelected()) {
				this.disableAllTrail(true);
			} else {
				this.disableAllTrail(false);
			}
			this.trailItems.forEach(i -> {
				i.setSelected(false);
			});
		});

		/* Setting specific body types items */
		for (BodyType bt : BodyType.values()) {
			CheckMenuItem item = new CheckMenuItem(bt.toString());
			trailItems.add(item);
			this.trail.getItems().add(item);

			item.setOnAction(i -> {
				if (!item.isSelected()) {
					selectedTypes.remove(bt);
				} else {
					if (trailAll.isSelected()) {
						this.disableAllTrail(true);
					}
					selectedTypes.add(bt);
				}
				trailAll.setSelected(false);
			});
		}

		this.getMenus().addAll(size, trail);

	}

	/**
	 * 
	 * @return the body types to turn off trails.
	 */
	public Set<BodyType> getDisableTrailTypes() {
		return this.selectedTypes;
	}

	/**
	 * Get the currently selected scale type.
	 * 
	 * @return selected scale type.
	 */
	public RenderScale getSelectedScale() {
		return selectedScale;
	}

	/**
	 * Disable or enable all simulation trails.
	 */
	protected void disableAllTrail(boolean disable) {
		if (disable) {
			selectedTypes.removeAll(Arrays.asList(BodyType.values()));
		} else {
			selectedTypes.addAll(Arrays.asList(BodyType.values()));
		}
	}

}
