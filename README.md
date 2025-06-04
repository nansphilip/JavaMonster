# Fantasy Hospital

## Présentation du projet

**Fantasy Hospital** est un projet d'apprentissage du langage Java, centré sur la modélisation et la gestion d'un hôpital fantastique pour créatures (elfes, orques, vampires, etc.). Il met en pratique l'héritage, les interfaces, les collections, la programmation orientée objet et l'utilisation de JavaFX pour l'interface graphique. Ce projet s'appuie sur un sujet pédagogique détaillé (voir [consigne](docs/instruction)).

## Prérequis et installation

- **Java JDK 17 ou supérieur**
- **Maven** (gestionnaire de dépendances)
- **SceneBuilder** (édition graphique des fichiers FXML)

### Installater Java JDK et Maven

- **Sur Mac (avec Homebrew) :**
  ```sh
  brew install openjdk maven
  ```
- **Sur Ubuntu (avec apt) :**
  ```sh
  sudo apt update
  sudo apt install openjdk-17-jdk maven
  ```

### Installer Java extension pour VSCode/Cursor

- Installer [Java](https://marketplace.visualstudio.com/items?itemName=Oracle.oracle-java)
- Pour Mac, ajouter le chemin de Java à `settings.json`

```json
{
  "jdk.jdkhome": "/opt/homebrew/opt/openjdk/libexec/openjdk.jdk/Contents/Home"
}
```

### Installer SceneBuilder
- Télécharger SceneBuilder : [https://gluonhq.com/products/scene-builder/](https://gluonhq.com/products/scene-builder/)
- Installer la version adaptée à votre système (Windows, Mac, Linux)

### Exécution du projet

Dans un terminal, placez-vous à la racine du projet et lancez :

```sh
mvn clean javafx:run
```

L'application s'ouvre dans une fenêtre native.

### Compilation et distribution

Créer le JAR exécutable :

```sh
mvn clean package
```

Exécuter le JAR :

```sh
java -jar target/FantasyHospital.jar
```

## Architecture du projet

- `src/main/java/com/fantasyhospital/` : code source principal
- `src/main/java/com/fantasyhospital/model/` : classes métier (voir README dédié)
- `src/main/resources/` : fichiers FXML et ressources graphiques

### Documentation et ressources
- [README du modèle (classes, héritages, interfaces...)](docs/models.md)
- [Sujet et consignes détaillées](docs/instruction)
- [Exigences et barème](docs/requirements)
- [Règles du jeu](docs/rules)

## JavaFX, FXML et Scene Builder

L'interface graphique est définie dans des fichiers FXML (ex : [MainView.fxml](src/main/resources/com/fantasyhospital/MainView.fxml)) puis importée dans un contrôleur Java (ex : [MainController.java](src/main/java/com/fantasyhospital/MainController.java)).

Pour éditer les fichiers FXML de manière interactive, il suffit d'ouvrir le fichier FXML avec SceneBuilder.

Pour les modifier facilement :
1. Ouvrez SceneBuilder.
2. Faites "Fichier > Ouvrir" et sélectionnez le fichier FXML à modifier.
3. Modifiez l'interface en glissant-déposant les composants JavaFX.
4. Enregistrez le fichier : les modifications seront prises en compte au prochain lancement de l'application.
