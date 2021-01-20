

import componentsFX.ButtonSelectDirectoryPath;
import componentsFX.SliderAndLabel;
import componentsFX.TextfieldAndLabel;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


import componentsFX.ImageViewer;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class HelloFX extends Application {
    double percentValue = 50.0;
    String descValue = "";

    @Override
    public void start(Stage stage) {
        FlowPane flowpane = createPaneStory3(stage);

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
        gridPane.setHgap(10);

        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(100);
        gridPane.getColumnConstraints().addAll(col3,col3,col3);

        viewer.setAlignment(Pos.CENTER);
        percent.setAlignment(Pos.CENTER);
        desc.setAlignment(Pos.CENTER);
        submit.setAlignment(Pos.CENTER);
gridPane.add(flowpane,0,3);
        Scene scene = new Scene(gridPane, 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    private FlowPane createPaneStory3(Stage stage) {
        SliderAndLabel percent = new SliderAndLabel(0.0, 100.0, percentValue, "Pourcentage");
        TextfieldAndLabel desc = new TextfieldAndLabel("Votre description :");

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


        FlowPane flowpane = new FlowPane();
        flowpane.getChildren().add(percent);
        flowpane.getChildren().add(desc);

        flowpane.getChildren().add(directoryToTest);
        flowpane.getChildren().add(directoryToSave);
        flowpane.getChildren().add(run);
        return flowpane;
    }


    public static void main(String[] args) {
        launch();
    }

}