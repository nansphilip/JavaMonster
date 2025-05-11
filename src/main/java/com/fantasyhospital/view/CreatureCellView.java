package com.fantasyhospital.view;

import com.fantasyhospital.model.creatures.abstractclass.Creature;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import static com.fantasyhospital.util.CropImageUtils.cropImage;
import static com.fantasyhospital.util.RemovePngBackgroundUtils.removePngBackground;

public class CreatureCellView extends ListCell<Creature> {
    private HBox content;
    private ImageView imageView;
    private Text name;
    private Label detailsLabel;

    public CreatureCellView() {
        super();
        imageView = new ImageView();
        imageView.setFitHeight(30);
        imageView.setFitWidth(30);
        name = new Text();
        detailsLabel = new Label();
        detailsLabel.setWrapText(true);
        detailsLabel.setStyle("-fx-text-fill: lightgray;");
        content = new HBox(10, imageView, detailsLabel);
    }

    @Override
    protected void updateItem(Creature creature, boolean empty) {
        super.updateItem(creature, empty);
        if (creature == null || empty) {
            setGraphic(null);
        } else {
            String imagePath = "/images/" + creature.getRace().toLowerCase() + ".png";
            Image image = new Image(getClass().getResourceAsStream(imagePath));

            Image transparentImage = removePngBackground(image);
            Image croppedImage = cropImage(transparentImage);

            imageView.setImage(croppedImage);
            name.setText(creature.getNomComplet());
            detailsLabel.setText(creature.toString());
            setGraphic(content);
        }
    }
}
