package componentsFX;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ImageViewer extends VBox {
    private final Label label;
    private final ImageView imageView;

    public ImageViewer(String imagePath,String labelString) {

        FileInputStream input = null;
        try {
            input = new FileInputStream(imagePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Image image = new Image(input);
        this.imageView = new ImageView(image);
        this.imageView.setPreserveRatio(true);
        this.imageView.setFitHeight(400);
        this.imageView.setFitWidth(680);

        this.label = new Label(labelString);

        this.label.setStyle("-fx-font-weight: bold");

        this.getChildren().add(imageView);
        this.getChildren().add(label);
        setAlignment(Pos.BASELINE_CENTER);
    }

}
