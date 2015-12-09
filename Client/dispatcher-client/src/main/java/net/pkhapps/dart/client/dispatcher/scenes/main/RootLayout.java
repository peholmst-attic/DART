package net.pkhapps.dart.client.dispatcher.scenes.main;

import javafx.scene.layout.BorderPane;
import net.pkhapps.dart.client.dispatcher.views.assignment.AssignmentView;

/**
 * Created by peholmst on 09-12-2015.
 */
class RootLayout extends BorderPane {

    private TitleBar titleBar;
    private SideBar sideBar;

    RootLayout() {
        init();
    }

    private void init() {
        titleBar = new TitleBar();
        setTop(titleBar);

        sideBar = new SideBar();
        setRight(sideBar);

        setCenter(new AssignmentView());
    }
}
