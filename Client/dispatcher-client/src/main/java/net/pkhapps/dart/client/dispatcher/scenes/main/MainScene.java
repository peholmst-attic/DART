package net.pkhapps.dart.client.dispatcher.scenes.main;

import javafx.scene.Scene;

/**
 * Created by peholmst on 09-12-2015.
 */
public class MainScene extends Scene {

    public MainScene() {
        super(new RootLayout());
        getStylesheets().add(getClass().getResource("main.css").toExternalForm());
    }
}
