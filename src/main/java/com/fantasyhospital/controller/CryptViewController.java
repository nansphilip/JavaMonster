package com.fantasyhospital.controller;

import com.fantasyhospital.enums.BudgetType;
import com.fantasyhospital.model.Hospital;
import com.fantasyhospital.model.creatures.abstractclass.Creature;
import com.fantasyhospital.model.rooms.medicalservice.Crypt;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.fantasyhospital.util.CropImageUtils.cropImage;
import static com.fantasyhospital.util.RemovePngBackgroundUtils.removePngBackground;

@Component
public class CryptViewController {

    @FXML
    private GridPane cryptGridPane;

    private Crypt crypt;
    private final Random random = new Random();
    private final List<String> bedImagePaths = new ArrayList<>();

    /**
     * Initialise la vue de la crypte
     */
    @FXML
    public void initialize() {
        // Initialisation de base de la grille
        if (cryptGridPane != null) {
            cryptGridPane.getChildren().clear();
            cryptGridPane.setStyle("-fx-background-color: #202020; -fx-border-color: #444444; -fx-border-width: 1px; -fx-background-image: url('/images/tiles/FloorCrypt.png')");
        }
    }

    /**
     * Configure l'hôpital et trouve la crypte
     * @param hospital l'hôpital contenant la crypte
     */
    public void setHospital(Hospital hospital) {
        // Rechercher la crypte dans les services de l'hôpital
        if (hospital != null && hospital.getServices() != null) {
            hospital.getServices().stream()
                .filter(room -> room instanceof Crypt)
                .map(room -> (Crypt) room)
                .findFirst()
                .ifPresent(this::setCrypt);
        }
    }

    /**
     * Configure la crypte et met à jour l'affichage
     * @param crypt la crypte à afficher
     */
    public void setCrypt(Crypt crypt) {
        this.crypt = crypt;

        // Génération des images de lits si nécessaire
        if (this.bedImagePaths.isEmpty() && crypt != null) {
            generateBedImagePaths(crypt.getMAX_CREATURE());
        }

        updateCryptView();
    }

    /**
     * Génère les chemins des images de lits pour la crypte
     * @param maxCreature le nombre maximum de créatures/lits
     */
    private void generateBedImagePaths(int maxCreature) {
        bedImagePaths.clear();
        for (int i = 0; i < maxCreature; i++) {
            // Pour la crypte, on utilise des lits spéciaux qui correspondent à l'ambiance froide
            String bedImage = getRandomCryptBedImage();
            bedImagePaths.add(bedImage);
        }
    }

    /**
     * Sélectionne aléatoirement une image de lit pour la crypte
     * @return le chemin de l'image sélectionnée
     */
    private String getRandomCryptBedImage() {
        // Images de lits spécifiques à la crypte avec une ambiance plus sombre/froide
        String[] options = {
            "/images/room/BedBones.png",  // Lit avec des os pour l'ambiance crypte
            "/images/room/Bed.png"        // Lit standard en alternative
        };
        return options[random.nextInt(options.length)];
    }

    /**
     * Met à jour l'affichage de la crypte
     */
    public void updateCryptView() {
        Platform.runLater(() -> {
            if (cryptGridPane == null || crypt == null) return;

            cryptGridPane.getChildren().clear();

            // Titre de la crypte
            Label titleLabel = new Label("CRYPTE");
            titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #cccccc;");
            cryptGridPane.add(titleLabel, 0, 0, 2, 1);

            try {
                // État du système de ventilation
                boolean airflowStatus = crypt.isAirflow();
                Label airflowLabel = new Label("Ventilation: " + (airflowStatus ? "Fonctionnelle ✓" : "En panne ✗"));
                airflowLabel.setStyle("-fx-text-fill: " + (airflowStatus ? "#00ff00" : "#ff0000") + ";");
                cryptGridPane.add(airflowLabel, 0, 1, 2, 1);

                // Température actuelle
                int temperature = crypt.getTemperature();
                Label tempLabel = new Label("Température: " + temperature + "°C");

                // Couleur en fonction de la température
                String tempColor;
                if (temperature <= 20) tempColor = "#00ffff"; // Bleu pour froid
                else if (temperature <= 30) tempColor = "#00ff00"; // Vert pour normal
                else if (temperature <= 40) tempColor = "#ffff00"; // Jaune pour chaud
                else tempColor = "#ff0000"; // Rouge pour très chaud

                tempLabel.setStyle("-fx-text-fill: " + tempColor + ";");
                cryptGridPane.add(tempLabel, 0, 2, 2, 1);

                // Barre de progression pour la température
                ProgressBar tempProgressBar = new ProgressBar((double)temperature / 50);
                tempProgressBar.setPrefWidth(200);
                tempProgressBar.setStyle("-fx-accent: " + tempColor + ";");
                cryptGridPane.add(tempProgressBar, 0, 3, 2, 1);

                // Liste des créatures dans la crypte
                Label creaturesLabel = new Label("Créatures en repos: " + crypt.getCreatures().size());
                creaturesLabel.setStyle("-fx-text-fill: #cccccc;");
                cryptGridPane.add(creaturesLabel, 0, 4, 2, 1);

                // Affichage visuel des lits et des créatures
                FlowPane bedsFlowPane = createBedsView();
                cryptGridPane.add(bedsFlowPane, 0, 5, 2, 1);

                // Affichage des créatures et de leur progression de guérison
                if (crypt.getCreatureWaitNbTour() != null && !crypt.getCreatureWaitNbTour().isEmpty()) {
                    VBox creatureProgressBox = new VBox(5);
                    creatureProgressBox.setStyle("-fx-padding: 10 0 0 0;");

                    for (Map.Entry<Creature, Integer> entry : crypt.getCreatureWaitNbTour().entrySet()) {
                        Creature creature = entry.getKey();
                        Integer tours = entry.getValue();

                        HBox creatureBox = new HBox(10);
                        creatureBox.setAlignment(Pos.CENTER_LEFT);

                        // Nom de la créature
                        Label creatureName = new Label(creature.getFullName());
                        creatureName.setStyle("-fx-text-fill: #cccccc;");
                        creatureName.setPrefWidth(120);

                        // Progression vers la guérison
                        ProgressBar healingProgress = new ProgressBar(tours / 3.0);
                        healingProgress.setPrefWidth(80);
                        healingProgress.setStyle("-fx-accent: #00ff77;");

                        // Label pour les tours à mettre dans une popup
//                        Label tourLabel = new Label(tours + "/3");
//                        tourLabel.setStyle("-fx-text-fill: #cccccc;");

//                        creatureBox.getChildren().addAll(creatureName, healingProgress, tourLabel);
//                        creatureProgressBox.getChildren().add(creatureBox);
                    }

                    cryptGridPane.add(creatureProgressBox, 0, 6, 2, 1);
                }

            } catch (Exception e) {
                // Affichage d'un message d'erreur dans l'interface en cas de problème
                Label errorLabel = new Label("Erreur lors du chargement des données de la crypte");
                errorLabel.setStyle("-fx-text-fill: #ff0000;");
                cryptGridPane.add(errorLabel, 0, 1, 2, 1);
                e.printStackTrace();
            }
        });
    }

    /**
     * Crée une vue graphique avec les lits et les créatures dessus
     * @return un FlowPane contenant les images de lits avec les créatures
     */
    private FlowPane createBedsView() {
        FlowPane flowPane = new FlowPane();
        flowPane.setHgap(5);
        flowPane.setVgap(5);
        flowPane.setPrefWrapLength(200);
        flowPane.setStyle("-fx-background-color: #333333; -fx-padding: 5; -fx-border-color: #555555; -fx-border-width: 1;");

        // Valider qu'il y a des chemins d'images pour les lits
        if (bedImagePaths.isEmpty() && crypt != null) {
            generateBedImagePaths(crypt.getMAX_CREATURE());
        }

        // S'assurer qu'il y a suffisamment d'images de lit
        while (bedImagePaths.size() < crypt.getMAX_CREATURE()) {
            bedImagePaths.add(getRandomCryptBedImage());
        }

        // Image de créature pour les lits occupés
        String creatureImagePath = "/images/room/BedCreature.png";

        List<Creature> creatures = new ArrayList<>(crypt.getCreatures());

        // Créer la vue avec les lits et éventuellement les créatures dessus
        for (int i = 0; i < bedImagePaths.size(); i++) {
            String bedImagePath = bedImagePaths.get(i);

            try {
                // Créer la vue du lit
                ImageView bedView = new ImageView(new Image(getClass().getResourceAsStream(bedImagePath)));
                bedView.setFitWidth(30);
                bedView.setFitHeight(54);

                StackPane bedStack = new StackPane();
                bedStack.getChildren().add(bedView);

                // Si cette place est occupée par une créature
                if (i < creatures.size()) {
                    try {
                        // Charger l'image de la créature
                        Image creatureImage = new Image(getClass().getResourceAsStream(creatureImagePath));

                        // Appliquer les transformations pour rendre l'image de la créature jolie
                        Image transparentImage = removePngBackground(creatureImage);
                        Image croppedImage = cropImage(transparentImage);

                        // Ajouter l'image de la créature sur le lit
                        ImageView creatureView = new ImageView(croppedImage);
                        creatureView.setFitWidth(30);
                        creatureView.setFitHeight(30);
                        StackPane.setAlignment(creatureView, Pos.CENTER);
                        bedStack.getChildren().add(creatureView);

                        // Ajouter un effet de gel si la température est basse (optionnel)
                        if (crypt.getTemperature() <= 20) {
                            bedStack.setStyle("-fx-effect: dropshadow(three-pass-box, cyan, 3, 0.5, 0, 0);");
                        }
                    } catch (Exception e) {
                        System.err.println("Erreur lors du chargement de l'image de créature: " + e.getMessage());
                    }
                }

                // Ajouter le lit (avec ou sans créature) au FlowPane
                flowPane.getChildren().add(bedStack);

            } catch (Exception e) {
                System.err.println("Erreur lors du chargement de l'image de lit: " + e.getMessage());
                // Ajouter une boîte vide en cas d'erreur pour préserver la mise en page
                Pane errorPane = new Pane();
                errorPane.setPrefSize(30, 54);
                flowPane.getChildren().add(errorPane);
            }
        }

        return flowPane;
    }
}
