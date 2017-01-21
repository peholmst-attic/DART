package net.pkhapps.dart.dispatcher.ui;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;
import net.pkhapps.dart.common.ui.LoginDialog;
import net.pkhapps.dart.dispatcher.ui.tickets.TicketsModule;

public class DispatcherApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Get locale from the command line parameter


        Injector injector = Guice.createInjector(new TicketsModule());

        /*Group group = new Group();
        group.getChildren().add(injector.getInstance(TicketForm.class).getView());

        Scene scene = new Scene(group);
        stage.setScene(scene);
        stage.show();*/

        System.out.println("Showing login dialog");
        LoginDialog loginDialog = new LoginDialog();
        loginDialog.setApplicationName("DART Dispatcher");
        if (new LoginDialog().showAndWait().isPresent()) {
            System.out.println("User attempted login");
        }
    }

    public static void main(String[] args) {
        System.out.println("Launching");
        launch(args);
    }
}
