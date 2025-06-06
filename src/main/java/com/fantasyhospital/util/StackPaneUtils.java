package com.fantasyhospital.util;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class StackPaneUtils {

    /**
     * Crée un StackPane avec les nœuds passés et leurs alignements.
     *
     * @param size taille préférée du StackPane (largeur = hauteur)
     * @param baseNode nœud principal en arrière-plan (ex : image principale)
     * @param alignedNodes tableau de nœuds avec leur position
     * @return StackPane configuré
     */
    public static StackPane createStackPane(double size, Node baseNode, NodeAlignment... alignedNodes) {
        StackPane stack = new StackPane();
        stack.getChildren().add(baseNode);

        for (NodeAlignment na : alignedNodes) {
            stack.getChildren().add(na.node());
            StackPane.setAlignment(na.node(), na.position());
        }

        stack.setPrefSize(size, size);
        return stack;
    }

    public record NodeAlignment(Node node, Pos position) {}
}