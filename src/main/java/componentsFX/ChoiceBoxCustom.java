package componentsFX;

import javafx.scene.control.ChoiceBox;

public class ChoiceBoxCustom extends ChoiceBox {
    public ChoiceBoxCustom(String[] labels) {
        for (String label : labels) {
            this.getItems().add(label);
        }


    }


}