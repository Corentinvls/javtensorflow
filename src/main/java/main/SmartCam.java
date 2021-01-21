package main;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import utils.Filter;
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
        tabPane.getTabs().add(tabImage);
        tabPane.getTabs().add(tabCamera);

        // Results of image classification
        ArrayList<Object> classifyResult = ClassifyImage.displayClassify("src/inception5h/", "src/inception5h/tensorPics/jack.jpg");

        // Display of image with classification
        ImageViewer viewer = new ImageViewer("src/inception5h/tensorPics/jack.jpg", String.format("BEST MATCH: %s (%.2f%%)%n",
                classifyResult.get(0),
                classifyResult.get(1)));
        viewer.setMaxWidth(Double.MAX_VALUE);

        // slider for percent
        SliderAndLabel percent = new SliderAndLabel(0.0, 100.0, percentValue, "Percentage");

        // text input for description
        TextfieldAndLabel desc = new TextfieldAndLabel("Your description:");

        /* Buttons */

        ButtonSelectFilePath fileToOpen = new ButtonSelectFilePath("Open img.", stage);
        ButtonSelectDirectoryPath directoryToTest = new ButtonSelectDirectoryPath("Image dir.", stage);
        ButtonSelectDirectoryPath directoryToSave = new ButtonSelectDirectoryPath("Save dir.", stage);
        ChoiceBoxFilter choiceBoxFilter = new ChoiceBoxFilter();
        Button runFilter = new Button("Run");
        runFilter.setOnAction(event ->
        {
            percentValue = percent.getValue();
            descValue = desc.getText();
            List<ArrayList<Object>> results = ClassifyImage.ArrayClassify("src/inception5h/", directoryToTest.getPath());

            for (ArrayList result : results) {
                String[] labelFound = result.get(0).toString().split(" ");
                if (((Float) result.get(1)) >= percentValue) {
                    for (String labelWord : labelFound) {
                        if (descValue.contains(labelWord) || descValue.equals("")) {
                            try {
                                Utils.copyFile(result.get(2).toString(), labelWord, directoryToSave.getPath());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            String filter = (String) choiceBoxFilter.getValue();
                            if (filter != null && !filter.equals("aucun")) {
                                try {
                                    Filter.filter(directoryToSave.getPath() + "/" + labelWord + "." + Utils.getExtension(result.get(2).toString()), labelWord, directoryToSave.getPath(), filter);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                            break;
                        }
                    }
                }
            }
        });

        Button updateImage = new Button("Update Image");
        updateImage.setOnAction(event ->
        {
            // Results of image classification
            ArrayList<Object> tempResult = ClassifyImage.displayClassify("src/inception5h/", fileToOpen.getPath());

            viewer.setImageView(fileToOpen.getPath());
            viewer.setLabel(String.format("BEST MATCH: %s (%.2f%%)%n",
                    tempResult.get(0),
                    tempResult.get(1)));
        });

        // box for filter buttons
        HBox buttonFilterBox = new HBox(directoryToTest, directoryToSave, choiceBoxFilter, runFilter);
        buttonFilterBox.setSpacing(0);
        buttonFilterBox.setAlignment(Pos.BOTTOM_CENTER);

        // box for open buttons
        HBox buttonFileBox = new HBox(fileToOpen, updateImage);
        buttonFileBox.setSpacing(0);
        buttonFileBox.setAlignment(Pos.BOTTOM_CENTER);

        /* Image Pane */
        GridPane gridImage = new GridPane();
        gridImage.add(buttonFileBox, 0, 0, 3, 1);
        gridImage.add(viewer, 0, 1, 3, 1);
        gridImage.add(percent, 0, 2, 1, 1);
        gridImage.add(desc, 1, 2, 1, 1);
        gridImage.add(buttonFilterBox, 2, 2, 1, 1);
        tabImage.setContent(gridImage);

        viewer.setAlignment(Pos.CENTER);
        percent.setAlignment(Pos.CENTER);
        desc.setAlignment(Pos.CENTER);
        updateImage.setAlignment(Pos.CENTER);


        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(100);
        gridImage.getColumnConstraints().addAll(col3, col3, col3);


        /* Camera Pane */
        GridPane gridCamera = new GridPane();
        ClassifyWebcam webcamFeed = new ClassifyWebcam();
        gridCamera.add(webcamFeed, 0, 0, 1, 1);

        webcamFeed.setAlignment(Pos.CENTER);
        gridCamera.getColumnConstraints().addAll(col3);

        tabCamera.setContent(gridCamera);

        /* Scene declaration for window */
        Scene scene = new Scene(tabPane, 720, 550);
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }

}