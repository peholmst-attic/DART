package net.pkhapps.dart.clients.dispatch;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.pkhapps.dart.clients.dispatch.ui.MainPane;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class DispatchClient extends Application {

    private static String[] args;

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() throws Exception {
        applicationContext = SpringApplication.run(DispatchClientConfig.class, args);
    }

    @Override
    public void stop() throws Exception {
        applicationContext.close();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("DART Dispatch Client");
        MainPane mainPane = applicationContext.getBean(MainPane.class);
        Scene scene = new Scene(mainPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        DispatchClient.args = args; // Save for passing to Spring Boot
        launch(args);
    }
}
