# Fantasy Hospital

## Présentation du projet

**Fantasy Hospital** est un projet d'apprentissage du langage Java, centré sur la modélisation et la gestion d'un hôpital fantastique pour créatures (elfes, orques, vampires, etc.). Il met en pratique l'héritage, les interfaces, les collections, la programmation orientée objet et l'utilisation de JavaFX pour l'interface graphique. Ce projet s'appuie sur un sujet pédagogique détaillé (voir [consigne](docs/instruction)).

## Prérequis et installation

- **Java JDK**
- **Maven** (gestionnaire de dépendances)
- **SceneBuilder** (édition graphique des fichiers FXML)

### Installater Java JDK et Maven

- **Sur Mac (avec Homebrew) :**
  ```sh
  brew install openjdk maven
  ```
- **Sur Linux (avec apt) :**
  ```sh
  sudo apt install openjdk-17-jdk maven
  ```

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

### Lancer les tests

```sh
mvn clean test
```

### Générer la documentation

```sh
mvn javadoc:javadoc
```

Deux fichiers HTML sont générés dans le dossier `target/reports/apidocs` :
- `index.html` : page d'accueil de la documentation
- `index-all.html` : liste des classes de la documentation

## Architecture du projet

- `src/main/java/com/fantasyhospital/` : backend
- `src/test/java/com/fantasyhospital/` : tests unitaires
- `src/main/resources/` : frontend

### Documentation et ressources
- [README du modèle (classes, héritages, interfaces...)](docs/tree.md)
- [Sujet et consignes détaillées](docs/instruction)
- [Exigences et barème](docs/requirements)
- [Règles du jeu](docs/rules)
- Repository GitHub : [Fantasy Hospital]https://github.com/nansphilip/JavaMonster

## UML

### Diagramme de classes
![UML Diagram](<./diagrammes%20UML/Diagramme_de_classe.png>)

### Diagramme était-transition
![UML Diagram](<./diagrammes%20UML/Etats-transitions.png>)

### Diagramme cas utilisation
![UML Diagram](<./diagrammes%20UML/Diagramme_cas_utilisation.jpg>)