

import componentsFX.SliderAndLabel;
import componentsFX.TextfieldAndLabel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class HelloFX extends Application {
    double percentValue = 50.0;
    String descValue = "";
    @Override
    public void start(Stage stage) {

        SliderAndLabel percent = new SliderAndLabel(0.0, 100.0, percentValue, "Pourcentage");
        TextfieldAndLabel desc = new TextfieldAndLabel("Votre description :");
        //GridPane desc = ComponentsFX.textfieldAndLabel("Votre description :");
        Button submit = new Button("valider");
        submit.setOnAction(event -> {
            percentValue = percent.getValue();
            descValue = desc.getText();
            System.out.println(percentValue);
            System.out.println(descValue);
        });

        FlowPane flowpane = new FlowPane();
        flowpane.getChildren().add(percent);
        flowpane.getChildren().add(desc);
        flowpane.getChildren().add(submit);

        Scene scene = new Scene(flowpane, 640, 480);
        stage.setScene(scene);
        stage.show();
    }




    public static void main(String[] args) {
        launch();
    }

}