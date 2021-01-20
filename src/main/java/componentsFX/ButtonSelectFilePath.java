package componentsFX;

import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ButtonSelectFilePath extends Button {

    private String path;
    private final FileChooser fileChooser;

    public ButtonSelectFilePath(String label, String path, Stage stage) {
        this.path = path;
        this.fileChooser = new FileChooser();
        this.fileChooser.setInitialDirectory(new File(path));
        this.fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"));
        this.setText(label);
        this.setOnAction(e -> {
            File selectedDirectory = fileChooser.showOpenDialog(stage);
            this.path = selectedDirectory.getAbsolutePath();
            System.out.println(selectedDirectory.getAbsolutePath());
        });

    }

    public ButtonSelectFilePath(String label, Stage stage) {
        this(label, "src", stage);

    }


    public String getPath() {
        return path;
    }
}
