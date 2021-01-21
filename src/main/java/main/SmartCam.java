package main;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import utils.Utils;
import componentsFX.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacv.FrameGrabber;


public class SmartCam extends Application {
    double percentValue = 50.0;
    String descValue = "";

    @Override
    public void start(Stage stage) throws FrameGrabber.Exception {

        // Tabs declaration
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        Tab tabImage = new Tab("Image");
        Tab tabCamera = new Tab("Camera");
        Tab tabFilters = new Tab("Filters");
        tabPane.getTabs().add(tabImage);
        tabPane.getTabs().add(tabCamera);
        tabPane.getTabs().add(tabFilters);

        // Results of image classification
        ArrayList<Object> classifyResult = ClassifyImage.displayClassify("src/inception5h/", "src/inception5h/tensorPics/jack.jpg");

        // Display of image with classification
        ImageViewer viewer = new ImageViewer("src/inception5h/tensorPics/jack.jpg", String.format("BEST MATCH: %s (%.2f%%)%n",
                classifyResult.get(0),
                classifyResult.get(1)));
        viewer.setMaxWidth(Double.MAX_VALUE);

        // slider for percent
        SliderAndLabel percent = new SliderAndLabel(0.0, 100.0, percentValue, "Pourcentage");

        // text input for description
        TextfieldAndLabel desc = new TextfieldAndLabel("Votre description :");

        /* Buttons */

        ButtonSelectDirectoryPath directoryToTest = new ButtonSelectDirectoryPath("Image dir.", stage);
        ButtonSelectDirectoryPath directoryToSave = new ButtonSelectDirectoryPath("Save dir.", stage);
        Button run = new Button("Run");
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

        // box for buttons
        HBox buttonBox = new HBox(directoryToTest, directoryToSave, run);
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.BOTTOM_CENTER);

        /* Image Pane */
        GridPane gridImage = new GridPane();
        gridImage.add(viewer, 0, 0, 3, 1);
        gridImage.add(percent, 0, 1, 1, 1);
        gridImage.add(desc, 1, 1, 1, 1);
        gridImage.add(buttonBox, 2, 1, 1, 1);
        gridImage.setHgap(10);
        tabImage.setContent(gridImage);

        viewer.setAlignment(Pos.CENTER);
        percent.setAlignment(Pos.CENTER);
        desc.setAlignment(Pos.CENTER);
        run.setAlignment(Pos.CENTER);

        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(100);
        gridImage.getColumnConstraints().addAll(col3, col3, col3);

        /* Camera Pane */
        GridPane gridCamera = new GridPane();
        gridCamera.add(new ClassifyWebcam(), 1, 3, 3, 1);
        tabCamera.setContent(gridCamera);

        /* Scene declaration for window */
        Scene scene = new Scene(tabPane, 700, 520);
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }

}