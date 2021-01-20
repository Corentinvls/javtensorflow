

import componentsFX.ImageViewer;
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

        ImageViewer viewer = new ImageViewer("src/inception5h/tensorPics/jack.jpg","salut");
        SliderAndLabel percent = new SliderAndLabel(0.0, 100.0, percentValue, "Pourcentage");
        TextfieldAndLabel desc = new TextfieldAndLabel("Votre description :");
        Button submit = new Button("valider");
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

        Scene scene = new Scene(gridPane, 640, 480);
        stage.setScene(scene);
        stage.show();
    }




    public static void main(String[] args) {
        launch();
    }

}