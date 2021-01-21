package componentsFX;

import javafx.application.Application;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;

public class ChoiceBoxFilter extends ChoiceBox {
    public ChoiceBoxFilter() {

        this.getItems().add("aucun");
        this.getItems().add("vert");
        this.getItems().add("rouge");
        this.getItems().add("bleu");
        this.getItems().add("noir et blanc");
        this.getItems().add("sepia");

    }


}