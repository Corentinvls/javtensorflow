

import componentsFX.ComponentsFX;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class HelloFX extends Application {
    double percentValue = 50.0;
    String descValue = "";
    @Override
    public void start(Stage stage) {

        GridPane percent = ComponentsFX.sliderAndLabel(0.0, 100.0, percentValue, "Pourcentage");
        GridPane desc = ComponentsFX.textfieldAndLabel("Votre description :","beagle");
        Button submit = new Button("valider");
        submit.setOnAction(event -> {
            percentValue = ((Slider) percent.getChildren().get(1)).getValue();
            descValue = ((TextField) desc.getChildren().get(1)).getText();
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