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
    private Label label;
    private ImageView imageView;

    public void setLabel(String label) {
        this.label.setText(label);

    }

    public void setImageView(String imagePath) {
        FileInputStream input = null;
        try {
            input = new FileInputStream(imagePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Image image = new Image(input);

        this.imageView.setImage(image);

    }

    public ImageViewer(String imagePath, String labelString) {

        FileInputStream input = null;
        try {
            input = new FileInputStream(imagePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Image image = new Image(input);
        this.imageView = new ImageView(image);
        this.imageView.setPreserveRatio(true);
        this.imageView.setFitHeight(380);
        this.imageView.setFitWidth(700);

        this.label = new Label(labelString);

        this.label.setStyle("-fx-font-size: 20px; -fx-font-weight: bold");

        this.getChildren().add(imageView);
        this.getChildren().add(label);
        setAlignment(Pos.BASELINE_CENTER);
    }

}
