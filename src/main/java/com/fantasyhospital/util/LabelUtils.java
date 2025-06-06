package com.fantasyhospital.util;

import javafx.scene.control.Label;

public class LabelUtils     {
    public static Label createStyledLabel(String style) {
        Label label = new Label();
        label.setStyle(style);
        return label;
    }
}
