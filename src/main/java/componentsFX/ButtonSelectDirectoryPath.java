package componentsFX;

import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ButtonSelectDirectoryPath extends Button {

    private String path;
    private final DirectoryChooser directoryChooser;

    public ButtonSelectDirectoryPath(String label, String path, Stage stage) {
        this.path = path;
        this.directoryChooser = new DirectoryChooser();
        this.directoryChooser.setInitialDirectory(new File(path));
        this.setText(label);
        this.setOnAction(e -> {
            File selectedDirectory = directoryChooser.showDialog(stage);
            this.path = selectedDirectory.getAbsolutePath();
        });

    }
    public ButtonSelectDirectoryPath(String label, Stage stage) {
        this(label,"src",stage);

    }


    public String getPath() {
        return path;
    }
}
