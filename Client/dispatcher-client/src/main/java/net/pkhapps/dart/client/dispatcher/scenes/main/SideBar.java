package net.pkhapps.dart.client.dispatcher.scenes.main;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

/**
 * Created by peholmst on 09-12-2015.
 */
class SideBar extends VBox {

    SideBar() {
        init();
    }

    private void init() {
        getStyleClass().add("side-bar");

        Button newAssignmentButton = GlyphsDude.createIconButton(FontAwesomeIcon.PLUS);
        getChildren().add(newAssignmentButton);


    }
}
