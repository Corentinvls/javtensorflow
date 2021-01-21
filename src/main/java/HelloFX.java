

import componentsFX.ImageViewer;
import componentsFX.SliderAndLabel;
import componentsFX.TextfieldAndLabel;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.bytedeco.javacv.FrameGrabber;


public class HelloFX extends Application {
    double percentValue = 50.0;
    String descValue = "";
    @Override
    public void start(Stage stage) throws FrameGrabber.Exception {

        ImageViewer viewer = new ImageViewer("src/inception5h/tensorPics/jack.jpg","salut");
        GridPane.setFillWidth(viewer, true);
        viewer.setMaxWidth(Double.MAX_VALUE);

        SliderAndLabel percent = new SliderAndLabel(0.0, 100.0, percentValue, "Pourcentage");

        TextfieldAndLabel desc = new TextfieldAndLabel("Votre description :");

        Button submit = new Button("Valider");
        submit.setOnAction(event -> {
            percentValue = percent.getValue();
            descValue = desc.getText();
            System.out.println(percentValue);
            System.out.println(descValue);
        });
        GridPane gridPane = new GridPane();
        gridPane.add(viewer, 0,0,3,1);
        gridPane.add(percent,0,1,1,1);
        gridPane.add(desc,1,1,1,1);
        gridPane.add(submit,2,1,1,1);
        gridPane.add(new ClassifyWebcam(),1,3,3,1);
        gridPane.setHgap(10);


        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(100);
        gridPane.getColumnConstraints().addAll(col3,col3,col3);

        viewer.setAlignment(Pos.CENTER);
        percent.setAlignment(Pos.CENTER);
        desc.setAlignment(Pos.CENTER);
        submit.setAlignment(Pos.CENTER);

        Scene scene = new Scene(gridPane, 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}