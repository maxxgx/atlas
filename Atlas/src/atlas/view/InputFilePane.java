package atlas.view;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 * Generic file tab pane, files are organized in different sections, separated
 * by tabs.
 * 
 * @author MaXX
 *
 */
public class InputFilePane extends TabPane {

	private Optional<String> selectedPath = Optional.empty();

	/**
	 * Creates a tab pane using a map, each tab represents a folder and its the
	 * content the files
	 * 
	 * @param path
	 *            the root path from which to search the files
	 */
	public InputFilePane(Map<File, List<File>> files) {

		// creates the tabs and list view
		files.entrySet().forEach(i -> {
			Tab tab = new Tab(i.getKey().getName());

			ListView<String> list = new ListView<>();

			// Add each file to the list view
			i.getValue().forEach(j -> {

				list.getItems().add(j.getName());
				list.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
				
				//upates the selected item
				list.getSelectionModel().selectedItemProperty().addListener(l -> {
					if(list.getSelectionModel().getSelectedItem().equals(j.getName())) {
						selectedPath = Optional.ofNullable(j.getAbsolutePath());
					}
				});
			});

			tab.setContent(new ScrollPane(list));
			this.getTabs().add(tab);
		});
		this.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
	}

	/**
	 * Gets an optional of the selected item.
	 * 
	 * @return an optional the selected item
	 */
	public Optional<String> getSelectedPath() {		
		return selectedPath;
	}
}