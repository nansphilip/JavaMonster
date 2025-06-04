package com.fantasyhospital.view;

import com.fantasyhospital.Singleton;
import com.fantasyhospital.enums.GenderType;
import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.Doctor;

import com.fantasyhospital.model.rooms.medicalservice.MedicalService;
import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;

import static com.fantasyhospital.util.CropImageUtils.cropImage;
import static com.fantasyhospital.util.RemovePngBackgroundUtils.removePngBackground;

public class DoctorsCellView extends ListCell<Doctor> {

    private HBox content;
    private ImageView doctorImageView;
    private ImageView moraleImageView;
    private ImageView genderImageView;
    private ImageView moraleTrendImageView;
    private HBox moraleBox;
    private Text name;
    private Label moraleLabel;
    private HBox topRow;
    private HBox nameGenderAgeBox;
    private Label roomLabel;
    private Label ageLabel;
    private TranslateTransition moraleAnimation;
    private VBox lifeBox;
    private VBox nameBox;

    private Hospital hospital;

    public DoctorsCellView(Hospital hospital) {
        super();
        this.hospital = hospital;

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

        ageLabel = new Label();
        ageLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: black; -fx-font-size: 10px;");

        roomLabel = new Label();
        roomLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: gray; -fx-font-size: 10px;");

        moraleLabel = new Label();
        moraleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: black; -fx-font-size: 10px;");

        moraleBox = new HBox(5,moraleTrendImageView, moraleImageView, moraleLabel);
        moraleBox.setAlignment(Pos.CENTER_LEFT);

        lifeBox = new VBox(2, moraleBox);
        lifeBox.setAlignment(Pos.CENTER_LEFT);

        nameGenderAgeBox = new HBox(5, name, genderImageView, ageLabel, roomLabel);
        nameGenderAgeBox.setAlignment(Pos.CENTER_LEFT);

        nameBox = new VBox(2, nameGenderAgeBox, lifeBox);
        nameBox.setAlignment(Pos.CENTER_LEFT);

        StackPane creatureImageStack = new StackPane(doctorImageView, moraleTrendImageView);
        StackPane.setAlignment(moraleTrendImageView, Pos.TOP_RIGHT);
        creatureImageStack.setPrefSize(30, 30);

        moraleTrendImageView.setPickOnBounds(false);

        topRow = new HBox(10, creatureImageStack, nameBox);
        topRow.setAlignment(Pos.CENTER_LEFT);

        content = new HBox(5, topRow);
//        content = new VBox(5, topRow, detailsLabel);
        content.setAlignment(Pos.CENTER_LEFT);

        this.setOnMouseClicked(event -> {
            Doctor selected = getItem();
            if (selected != null) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    showDoctorPopup(selected);
                } else if (event.getButton() == MouseButton.SECONDARY) {
                    showDoctorMenu(event, selected);
                }
            }
        });
    }

    @Override
    protected void updateItem(Doctor doctor, boolean empty) {
        String roomName = "";

        super.updateItem(doctor, empty);
        if (doctor == null || empty) {
            setGraphic(null);
        } else {
            String imagePath = "/images/races/doctor.png";
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            Image transparentImage = removePngBackground(image);
            Image croppedImage = cropImage(transparentImage);

            doctorImageView.setImage(croppedImage);

            moraleImageView.setImage(getMoraleImageView(doctor.getMorale()));

            // Définir l'image de tendance du moral seulement si le moral a changé
            if (moraleAnimation != null) {
                moraleAnimation.stop();
                moraleTrendImageView.setTranslateY(0);
            }
            // Définir l'image de tendance du moral seulement si le moral a changé
            if (doctor.isMoraleIncreasing()) {
                moraleTrendImageView.setImage(new Image(getClass().getResourceAsStream("/images/morale/MoraleUp.png")));
                moraleTrendImageView.setVisible(true);

                moraleAnimation = new TranslateTransition(Duration.millis(500), moraleTrendImageView);
                moraleAnimation.setFromY(0);
                moraleAnimation.setToY(-3); // vers le haut
                moraleAnimation.setCycleCount(Animation.INDEFINITE);
                moraleAnimation.setAutoReverse(true);
                moraleAnimation.play();

            } else if (doctor.isMoraleDecreasing()) {
                moraleTrendImageView.setImage(new Image(getClass().getResourceAsStream("/images/morale/MoraleDown.png")));
                moraleTrendImageView.setVisible(true);

                moraleAnimation = new TranslateTransition(Duration.millis(500), moraleTrendImageView);
                moraleAnimation.setFromY(0);
                moraleAnimation.setToY(3); // vers le bas
                moraleAnimation.setCycleCount(Animation.INDEFINITE);
                moraleAnimation.setAutoReverse(true);
                moraleAnimation.play();

            } else {
                moraleTrendImageView.setVisible(false);
            }

            Doctor deadDoctor = Singleton.getInstance().peekDoctorStack();

            if (deadDoctor != null && deadDoctor.isHarakiriTriggered()) {
                Platform.runLater(() -> HarakiriCellView.show(deadDoctor));
                deadDoctor.setHarakiriTriggered(false);
            }

            if (doctor.getMedicalService() != null) {
                roomName = doctor.getMedicalService().getName();
            }

            genderImageView.setImage(getGenderImageView(doctor.getSex()));

            name.setText(doctor.getFullName());
            ageLabel.setText("(" + doctor.getAge() + ")");
            roomLabel.setText(roomName);

            moraleLabel.setText("Moral (" + doctor.getMorale() + "/100)");
//          serviceLabel.setText("Service: " + doctor.getMedicalService());

            setGraphic(content);
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

        Label nameLabel = new Label("Nom : " + doctor.getFullName());
        Label ageLabel = new Label("Âge : " + doctor.getAge());
        Label moraleLabel = new Label("Moral : " + doctor.getMorale() + "/100");
        Label serviceLabel = new Label("Service : " + doctor.getMedicalService());

        VBox detailContent = new VBox(10, nameLabel, ageLabel, moraleLabel, serviceLabel);
        detailContent.setPadding(new Insets(10));

        Scene scene = new Scene(detailContent, 300, 200);
        popup.setScene(scene);
        popup.showAndWait();
    }

    private void showDoctorMenu(MouseEvent event, Doctor doctor) {
        ContextMenu contextMenu = new ContextMenu();

        List<MedicalService> services = hospital.getMedicalServices();

        for (MedicalService service : services) {
            if (!service.equals(doctor.getMedicalService())) {
                MenuItem item = new MenuItem(service.getName());
                item.setOnAction(e -> {
                    doctor.goTo(doctor.getMedicalService(), service);
                    updateItem(doctor, false);
                });
                contextMenu.getItems().add(item);
            }
        }

        contextMenu.show((javafx.scene.Node) event.getSource(), event.getScreenX(), event.getScreenY());
    }
}

