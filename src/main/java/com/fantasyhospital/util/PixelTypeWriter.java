package com.fantasyhospital.util;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Utility class for creating a pixelated typing effect on a Text node.
 * This class allows you to display a message character by character, simulating a typewriter effect.
 */
public class PixelTypeWriter {

    /**
     * Plays a typing effect on the given Text node.
     *
     * @param textNode   The Text node to display the message on.
     * @param message    The message to display with the typing effect.
     * @param fontPath   The path to the font file to use for the text.
     * @param fontSize   The size of the font to use for the text.
     * @param speedMillis The speed of the typing effect in milliseconds per character.
     */
    public static void playTypingEffect(Text textNode, String message, String fontPath, int fontSize, int speedMillis) {

        Font pixelFont = Font.loadFont(PixelTypeWriter.class.getResourceAsStream(fontPath), fontSize);
        textNode.setFont(pixelFont);

        Timeline timeline = new Timeline();
        StringBuilder displayedText = new StringBuilder();

        for (int i = 0; i < message.length(); i++) {
            final int index = i;
            KeyFrame keyFrame = new KeyFrame(Duration.millis(speedMillis * i), e -> {
                displayedText.append(message.charAt(index));
                String[] lines = displayedText.toString().split("\n", -1);
                int start = Math.max(0, lines.length - 2);
                StringBuilder visible = new StringBuilder();
                for (int j = start; j < lines.length; j++) {
                    visible.append(lines[j]);
                    if (j < lines.length - 1) visible.append("\n");
                }
                textNode.setText(visible.toString());
            });
            timeline.getKeyFrames().add(keyFrame);
        }

        timeline.play();
    }
}
