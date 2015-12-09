package net.pkhapps.dart.client.dispatcher.views.assignment;

import javafx.scene.layout.VBox;

/**
 * Created by peholmst on 09-12-2015.
 */
public class AssignmentView extends VBox {

    public AssignmentView() {
        init();
    }

    private void init() {
        getChildren().add(new AssignmentDetailsForm());
    }
}
