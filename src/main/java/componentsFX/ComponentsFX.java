package componentsFX;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class ComponentsFX {

    public static GridPane textfieldAndLabel(String labelString,String valueString) {
        GridPane gridPane = new GridPane();

        Label label = new Label(labelString);
        TextField text = new TextField(valueString);

        gridPane.add(label, 0, 0, 1, 1);
        gridPane.add(text, 0, 1, 1, 1);
        return gridPane;
    }
    public static GridPane textfieldAndLabel(String labelString) {
       return textfieldAndLabel(labelString,"");
    }

    public static GridPane sliderAndLabel(Double min, Double max,Double value,String labelString) {
        GridPane gridPane = new GridPane();
        Label label = new Label(labelString);
        Slider slider = new Slider(min, max, value);
        Tooltip tooltip1 = new Tooltip(value.toString()+"%");
        tooltip1.setShowDelay(Duration.ZERO);
        slider.setTooltip(tooltip1);
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<?extends Number> observable, Number oldValue, Number newValue){
                String result = String.format("%.2f", newValue);
               tooltip1.setText(result+"%");
            }
        });


        gridPane.add(label, 0, 0, 1, 1);
        gridPane.add(slider, 0, 1, 1, 1);
        return gridPane;
     }
}
