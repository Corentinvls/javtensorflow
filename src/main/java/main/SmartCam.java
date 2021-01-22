package main;


import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import utils.Converters;
import utils.Filter;
import utils.Utils;
import componentsFX.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacv.FrameGrabber;

import javax.imageio.ImageIO;


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
        ImageViewer viewer = new ImageViewer("src/inception5h/tensorPics/jack.jpg", String.format("Best match: %s (%.2f%%)%n",
                classifyResult.get(0),
                classifyResult.get(1)));
        viewer.setMaxWidth(Double.MAX_VALUE);

         String labels[] = {"aucun", "vert", "rouge", "bleu", "noir et blanc", "sepia"};
         String labelsFrame[] = {"Doré", "Trait"};
        CheckBox checkBoxFilter = new CheckBox();
        ChoiceBoxCustom choiceBoxFilter = new ChoiceBoxCustom(labels);
        FlowPane flowPaneFilter = new FlowPane();
        flowPaneFilter.getChildren().add(checkBoxFilter);
        flowPaneFilter.getChildren().add(choiceBoxFilter);
//CADRES
        CheckBox checkBoxFrame = new CheckBox();
        ChoiceBoxCustom choiceBoxFrame = new ChoiceBoxCustom(labelsFrame);
        FlowPane flowPaneFrame = new FlowPane();
        flowPaneFrame.getChildren().add(checkBoxFrame);
        flowPaneFrame.getChildren().add(choiceBoxFrame);
//IMAGE
        CheckBox checkBoxImageToPaste = new CheckBox();
        ButtonSelectFilePath buttonSelectImage = new ButtonSelectFilePath("Choose image", stage);
        Spinner<Integer> spinnerX = new Spinner<Integer>(0, 10000, 0);
        Spinner<Integer> spinnerY = new Spinner<Integer>(0, 10000, 0);
        Spinner<Integer> spinnerH = new Spinner<Integer>(0, 10000, 50);
        Spinner<Integer> spinnerW = new Spinner<Integer>(0, 10000, 50);
        spinnerX.setEditable(true);
        spinnerY.setEditable(true);
        FlowPane flowPaneImage = new FlowPane();
        flowPaneImage.getChildren().addAll(checkBoxImageToPaste, buttonSelectImage, spinnerX, spinnerY, spinnerH, spinnerW);


        // slider for percent
        SliderAndLabel percent = new SliderAndLabel(0.0, 100.0, percentValue, "Percentage");

        // text input for description
        TextfieldAndLabel desc = new TextfieldAndLabel("Your description:");

        /* Buttons */

        ButtonSelectFilePath fileToOpen = new ButtonSelectFilePath("Open img.", stage);
        ButtonSelectDirectoryPath directoryToTest = new ButtonSelectDirectoryPath("Image dir.", stage);
        ButtonSelectDirectoryPath directoryToSave = new ButtonSelectDirectoryPath("Save dir.", stage);


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
            if (fileToOpen.getPath() != null && !fileToOpen.getPath().equals("src")) {
                BufferedImage imgBuff = null;
                // Results of image classification
                ArrayList<Object> tempResult = ClassifyImage.displayClassify("src/inception5h/", fileToOpen.getPath());
                try {
                    imgBuff = ImageIO.read(new File(fileToOpen.getPath()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (choiceBoxFilter.getValue() != null && checkBoxFilter.isSelected()) {
                    imgBuff = Filter.applyColor(imgBuff, (String) choiceBoxFilter.getValue());

                }
                if (choiceBoxFrame.getValue() != null && checkBoxFrame.isSelected()) {
                    imgBuff = ModifyImage.applyFrame(imgBuff, "src/frame/" + choiceBoxFrame.getValue() + ".png", null);

                }
                if (buttonSelectImage.getPath() != null && !buttonSelectImage.getPath().equals("src") && checkBoxImageToPaste.isSelected()) {
                    try {
                        imgBuff = ModifyImage.applyImage(imgBuff,
                                buttonSelectImage.getPath(),
                                spinnerX.getValue(), spinnerY.getValue(),
                                spinnerH.getValue(), spinnerW.getValue());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                viewer.setImageView(imgBuff);
                viewer.setLabel(String.format("Best match: %s (%.2f%%)%n",
                        tempResult.get(0),
                        tempResult.get(1)));
            }
        });

        // box for filter buttons
        ChoiceBoxCustom choiceBoxFilter2 = new ChoiceBoxCustom(labels);
        HBox buttonFilterBox = new HBox(directoryToTest, directoryToSave, choiceBoxFilter2, runFilter);
        buttonFilterBox.setSpacing(0);
        buttonFilterBox.setAlignment(Pos.BOTTOM_CENTER);

        // box for open buttons
        HBox buttonFileBox = new HBox(fileToOpen, updateImage);
        buttonFileBox.setSpacing(0);
        buttonFileBox.setAlignment(Pos.BOTTOM_CENTER);

        /* Image Pane */
        GridPane gridImage = new GridPane();
        GridPane gridControl=new GridPane();

        gridControl.add(flowPaneFilter, 0, 0, 1, 1);
        gridControl.add(flowPaneFrame, 0, 1, 1, 1);
        gridControl.add(flowPaneImage, 0, 2, 1, 1);

        gridImage.add(buttonFileBox, 0, 0, 3, 1);
        gridImage.add(viewer, 0, 1, 2, 1);
        gridImage.add(gridControl,2,1,3,1);
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
        ClassifyWebcam webcamFeed = new ClassifyWebcam(stage);
        gridCamera.add(webcamFeed, 0, 0, 1, 1);

        webcamFeed.setAlignment(Pos.CENTER);
        gridCamera.getColumnConstraints().addAll(col3);

        tabCamera.setContent(gridCamera);

        /* Scene declaration for window */
        Scene scene = new Scene(tabPane, 760, 550);
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }

}