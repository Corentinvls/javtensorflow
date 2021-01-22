package componentsFX;

import javafx.scene.control.ChoiceBox;


public class ChoiceBoxCustom extends ChoiceBox {
    /**
     * ChoiceBox with labels as a parameter
     *
     * @param labels
     */
    public ChoiceBoxCustom(String[] labels) {
        for (String label : labels) {
            this.getItems().add(label);
        }
    }
}