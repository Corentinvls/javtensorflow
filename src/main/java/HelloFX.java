import componentsFX.ButtonSelectDirectoryPath;
import componentsFX.SliderAndLabel;
import componentsFX.TextfieldAndLabel;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import componentsFX.ImageViewer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bytedeco.javacv.FrameGrabber;


public class HelloFX extends Application {
    double percentValue = 50.0;
    String descValue = "";

    @Override
    public void start(Stage stage) throws FrameGrabber.Exception {
        ArrayList<Object> classifyResult = ClassifyImage.displayClassify("src/inception5h/", "src/inception5h/tensorPics/jack.jpg");
        ImageViewer viewer = new ImageViewer("src/inception5h/tensorPics/jack.jpg", String.format("BEST MATCH: %s (%.2f%% )%n",
                classifyResult.get(0),
                classifyResult.get(1)));

        GridPane.setFillWidth(viewer, true);
        viewer.setMaxWidth(Double.MAX_VALUE);

        SliderAndLabel percent = new SliderAndLabel(0.0, 100.0, percentValue, "Pourcentage");

        TextfieldAndLabel desc = new TextfieldAndLabel("Votre description :");

        /* Buttons */

        ButtonSelectDirectoryPath directoryToTest = new ButtonSelectDirectoryPath("Which ?", stage);
        ButtonSelectDirectoryPath directoryToSave = new ButtonSelectDirectoryPath("Where ?", stage);
        Button run = new Button("run");
        run.setOnAction(event ->
        {
            percentValue = percent.getValue();
            descValue = desc.getText();
            List<ArrayList<Object>> results = ClassifyImage.ArrayClassify("src/inception5h/", directoryToTest.getPath());

            for (ArrayList result : results) {
                String[] labelFound = result.get(0).toString().split(" ");
                if (((Float) result.get(1)) >= percentValue) {
                    for (String labelWord : labelFound) {
                        if (descValue.contains(labelWord)) {
                            try {
                                Utils.copyFile(result.get(2).toString(), labelWord, directoryToSave.getPath());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                }
            }
        });



        HBox buttonBox = new HBox(directoryToTest, directoryToSave, run);
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.BOTTOM_CENTER);

        GridPane gridPane = new GridPane();
        gridPane.add(viewer, 0, 0, 3, 1);
        gridPane.add(percent, 0, 1, 1, 1);
        gridPane.add(desc, 1, 1, 1, 1);
        gridPane.add(buttonBox, 2, 1, 1, 1);
        gridPane.add(new ClassifyWebcam(), 1, 3, 3, 1);
        gridPane.setHgap(10);


        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(100);
        gridPane.getColumnConstraints().addAll(col3, col3, col3);

        viewer.setAlignment(Pos.CENTER);
        percent.setAlignment(Pos.CENTER);
        desc.setAlignment(Pos.CENTER);
        run.setAlignment(Pos.CENTER);

        Scene scene = new Scene(gridPane, 640, 480);
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }

}