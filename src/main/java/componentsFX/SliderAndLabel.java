package componentsFX;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class SliderAndLabel extends GridPane {
    private final Label label;
    private final Slider slider;
    private final Tooltip tooltip;

    public SliderAndLabel(Double min, Double max, Double value, String labelString) {

        this.label = new Label(labelString);
        this.slider = new Slider(min, max, value);
        this.tooltip = new Tooltip(value.toString() + "%");
        this.tooltip.setShowDelay(Duration.ZERO);
        this.slider.setTooltip(tooltip);
        this.slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            String result = String.format("%.2f", newValue);
            tooltip.setText(result + "%");
        });


        this.add(label, 0, 0, 1, 1);
        this.add(slider, 0, 1, 1, 1);

    }

    public Double getValue() {
        return slider.getValue();
    }
}
