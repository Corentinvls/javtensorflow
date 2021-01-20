

import componentsFX.ButtonSelectDirectoryPath;
import componentsFX.ButtonSelectFilePath;
import componentsFX.SliderAndLabel;
import componentsFX.TextfieldAndLabel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;


public class HelloFX extends Application {
    double percentValue = 50.0;
    String descValue = "";

    @Override
    public void start(Stage stage) {
        SliderAndLabel percent = new SliderAndLabel(0.0, 100.0, percentValue, "Pourcentage");
        TextfieldAndLabel desc = new TextfieldAndLabel("Votre description :");

        ButtonSelectDirectoryPath directoryToTest = new ButtonSelectDirectoryPath("Which ?", stage);
        ButtonSelectDirectoryPath directoryToSave = new ButtonSelectDirectoryPath("Where ?", stage);

        Button run = new Button("run");
        run.setOnAction(event ->
        {
            percentValue = percent.getValue();
            descValue = desc.getText();
            ClassifyImage.ArrayClassify("/Users/vallois/Documents/COURS/javatensorflow/src/inception5h/", directoryToTest.getPath(), directoryToSave.getPath());
        });


        FlowPane flowpane = new FlowPane();
        flowpane.getChildren().add(percent);
        flowpane.getChildren().add(desc);

        flowpane.getChildren().add(directoryToTest);
        flowpane.getChildren().add(directoryToSave);
        flowpane.getChildren().add(run);


        Scene scene = new Scene(flowpane, 640, 480);
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }

}