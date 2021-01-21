package componentsFX;

import javafx.application.Application;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;

public class ChoiceBoxFilter extends ChoiceBox {
    public ChoiceBoxFilter() {

        this.getItems().add("None");
        this.getItems().add("Red");
        this.getItems().add("Green");
        this.getItems().add("Blue");
        this.getItems().add("Black and White");
        this.getItems().add("Sepia");

    }


}