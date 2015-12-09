package net.pkhapps.dart.client.dispatcher.scenes.main;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

/**
 * Created by peholmst on 09-12-2015.
 */
class TitleBar extends HBox {

    TitleBar() {
        init();
    }

    private void init() {
        getStyleClass().add("title-bar");

        Label title = new Label("DART Dispatcher Client");
        title.getStyleClass().add("title");
        getChildren().add(title);

        Region spacer = new Region();
        getChildren().add(spacer);
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Circle userImage = new Circle(20);
        userImage.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("sample-user-photo.jpg"))));
        userImage.getStyleClass().add("user-image");
        getChildren().add(userImage);

        Label userName = new Label("Joe Cool");
        userName.getStyleClass().add("user-name");
        getChildren().add(userName);

        Button closeButton = GlyphsDude.createIconButton(FontAwesomeIcon.POWER_OFF);
        closeButton.getStyleClass().add("close-button");
        closeButton.setTooltip(new Tooltip("Click here to quit this application"));
        getChildren().add(closeButton);
    }
}
