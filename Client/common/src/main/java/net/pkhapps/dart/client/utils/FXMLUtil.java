package net.pkhapps.dart.client.utils;

import javafx.fxml.FXMLLoader;

import java.io.IOException;

/**
 * Created by peholmst on 09-12-2015.
 */
public final class FXMLUtil {

    private FXMLUtil() {
    }

    /**
     * @param o
     */
    public static void loadFXML(Object o) {
        FXMLLoader fxmlLoader = new FXMLLoader(o.getClass().getResource(o.getClass().getSimpleName() + ".fxml"));
        fxmlLoader.setRoot(o);
        fxmlLoader.setController(o);
        try {
            fxmlLoader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
