package componentsFX;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ImageViewer extends GridPane {
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
        imageView = new ImageView(image);
        imageView.maxWidth(400);

        this.label = new Label(labelString);

        this.add(imageView, 0, 0, 1, 1);
        this.add(label, 0, 1, 1, 1);
    }

}
