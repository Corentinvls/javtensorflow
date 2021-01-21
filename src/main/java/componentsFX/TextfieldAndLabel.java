package componentsFX;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class TextfieldAndLabel extends GridPane {
    private final Label label;
    private final TextField text;

    public TextfieldAndLabel(String labelString, String valueString) {
        this.label = new Label(labelString);
        this.text = new TextField(valueString);
        this.add(label, 0, 0, 1, 1);
        this.add(text, 0, 1, 1, 1);
    }

    public TextfieldAndLabel(String labelString) {
        this(labelString, "");
    }

    public String getText() {
        return text.getText();
    }

}
