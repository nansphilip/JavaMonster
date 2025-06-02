package com.fantasyhospital.view;

import com.fantasyhospital.enums.GenderType;
import com.fantasyhospital.model.creatures.Doctor;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static com.fantasyhospital.util.CropImageUtils.cropImage;
import static com.fantasyhospital.util.RemovePngBackgroundUtils.removePngBackground;

public class DoctorsCellView extends ListCell<Doctor> {

    private HBox content;
    private ImageView doctorImageView;
    private ImageView moraleImageView;
    private ImageView genderImageView;
    private ImageView moraleTrendImageView;
    private HBox moraleBox;
    private VBox infoBox;
    private HBox nameGenderBox;
    private Text name;
    private Label serviceLabel;
    private Label ageLabel;
    private Label moraleLabel;
    private Label detailsLabel;

    public DoctorsCellView() {
        super();

        doctorImageView = new ImageView();
        doctorImageView.setFitHeight(30);
        doctorImageView.setFitWidth(30);
        doctorImageView.setPreserveRatio(true);

        moraleImageView = new ImageView();
        moraleImageView.setFitHeight(10);
        moraleImageView.setFitWidth(65);

        genderImageView = new ImageView();
        genderImageView.setFitHeight(10);
        genderImageView.setFitWidth(10);

        moraleTrendImageView = new ImageView();
        moraleTrendImageView.setFitHeight(10);
        moraleTrendImageView.setFitWidth(10);
        moraleTrendImageView.setVisible(false);

        name = new Text();
        name.setStyle("-fx-font-weight: bold;");

        nameGenderBox = new HBox(5, name, genderImageView);
        nameGenderBox.setAlignment(Pos.CENTER_LEFT);

        serviceLabel = new Label();
        serviceLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: black; -fx-font-size: 10px;");

        ageLabel = new Label();
        ageLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: black; -fx-font-size: 10px;");

        moraleLabel = new Label();
        moraleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: black; -fx-font-size: 10px;");

        moraleBox = new HBox(5, moraleTrendImageView, moraleImageView, moraleLabel);
        moraleBox.setAlignment(Pos.CENTER_LEFT);

        infoBox = new VBox(2, nameGenderBox, ageLabel);
        infoBox.setAlignment(Pos.CENTER_LEFT);

        content = new HBox(10, doctorImageView, infoBox, moraleBox);
        content.setAlignment(Pos.CENTER_LEFT);
    }

    @Override
    protected void updateItem(Doctor doctor, boolean empty) {
        super.updateItem(doctor, empty);
        if (empty || doctor == null) {
            setText(null);
            setGraphic(null);
        } else {
            String imagePath = "/images/races/doctor.png";
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            Image transparentImage = removePngBackground(image);
            Image croppedImage = cropImage(transparentImage);

            doctorImageView.setImage(croppedImage);

            moraleImageView.setImage(getMoraleImageView(doctor.getMorale()));

            // Définir l'image de tendance du moral seulement si le moral a changé
            if (doctor.isMoraleIncreasing()) {
                moraleTrendImageView.setImage(new Image(getClass().getResourceAsStream("/images/morale/moral-up.png")));
                moraleTrendImageView.setVisible(true);
            } else if (doctor.isMoraleDecreasing()) {
                moraleTrendImageView.setImage(new Image(getClass().getResourceAsStream("/images/morale/moral-down.png")));
                moraleTrendImageView.setVisible(true);
            } else {
                moraleTrendImageView.setVisible(false);
            }

            genderImageView.setImage(getGenderImageView(doctor.getSex()));

            name.setText(doctor.getFullName());
            ageLabel.setText("(" + doctor.getAge() + ")");
            moraleLabel.setText("Moral (" + doctor.getMorale() + "/100)");
//            serviceLabel.setText("Service: " + doctor.getMedicalService());

            setGraphic(content);

            this.setOnMouseClicked(event -> {
                Doctor selected = getItem();
                if (selected != null) {
                    showDoctorPopup(selected);
                }
            });
        }
    }

    private Image getMoraleImageView(int morale) {
        String moraleImagePath;

        if (morale == 0) {
            moraleImagePath = "/images/morale/EmptyLifeBar.png";
        } else if (morale <= 10) {
            moraleImagePath = "/images/morale/LowLifeBar1.png";
        } else if (morale <= 20) {
            moraleImagePath = "/images/morale/LowLifeBar2.png";
        } else if (morale <= 30) {
            moraleImagePath = "/images/morale/LowLifeBar3.png";
        } else if (morale <= 40) {
            moraleImagePath = "/images/morale/MediumLifeBar1.png";
        } else if (morale <= 50) {
            moraleImagePath = "/images/morale/MediumLifeBar2.png";
        } else if (morale <= 70) {
            moraleImagePath = "/images/morale/HighLifeBar1.png";
        } else if (morale <= 80) {
            moraleImagePath = "/images/morale/HighLifeBar2.png";
        } else {
            moraleImagePath = "/images/morale/FullLifeBar.png";
        }

        return new Image(getClass().getResourceAsStream(moraleImagePath));
    }

    private Image getGenderImageView(GenderType genderType) {
        String genderImagePath;

        if (genderType == GenderType.MALE) {
            genderImagePath = "/images/gender/Male.png";
        } else {
            genderImagePath = "/images/gender/Female.png";
        }
        return new Image(getClass().getResourceAsStream(genderImagePath));
    }

    private void showDoctorPopup(Doctor doctor) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Détails du docteur");

        detailsLabel = new Label(doctor.toString());
        detailsLabel.setWrapText(true);

        VBox detailContent = new VBox(10, detailsLabel);
        detailContent.setPadding(new Insets(10));

        Scene scene = new Scene(detailContent, 300, 200); // Taille de la popup
        popup.setScene(scene);
        popup.showAndWait();
    }
}

