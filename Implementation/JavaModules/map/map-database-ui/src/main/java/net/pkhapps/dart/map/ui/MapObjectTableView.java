package net.pkhapps.dart.map.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import net.pkhapps.dart.common.i18n.Locales;
import net.pkhapps.dart.common.i18n.UTF8Control;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * TODO Come up with a better name
 */
public class MapObjectTableView extends TableView<MapObjectModel> {

    private final TableColumn<MapObjectModel, String> name = new TableColumn<>();
    private final TableColumn<MapObjectModel, MapObjectType> type = new TableColumn<>();
    private final TableColumn<MapObjectModel, Locale> language = new TableColumn<>();

    private ResourceBundle messages;

    /**
     *
     */
    public MapObjectTableView() {
        setLocale(Locale.getDefault());
        getColumns().addAll(name, language, type);
    }

    /**
     * @param locale
     */
    public void setLocale(@NotNull Locale locale) {
        messages = ResourceBundle.getBundle(getClass().getPackage().getName() + ".messages", locale, new UTF8Control());
        name.setText(messages.getString("MapObjectTableView.columns.name"));
        type.setText(messages.getString("MapObjectTableView.columns.type"));
        language.setText(messages.getString("MapObjectTableView.columns.language"));
    }

    /**
     * @return
     */
    @NotNull
    public Locale getLocale() {
        return messages.getLocale();
    }

    public static class SampleApp extends Application {

        public static void main(String[] args) {
            launch(args);
        }

        @Override
        public void start(Stage stage) throws Exception {
            Scene scene = new Scene(new Group());
            stage.setTitle("Sample");
            stage.setWidth(300);
            stage.setHeight(500);

            final Label label = new Label("Sample");
            label.setFont(new Font("Arial", 20));

            MapObjectTableView table = new MapObjectTableView();
            table.setLocale(Locales.SWEDISH);

            final VBox vbox = new VBox();
            vbox.setSpacing(5);
            vbox.setPadding(new Insets(10, 0, 0, 10));
            vbox.getChildren().addAll(label, table);

            ((Group) scene.getRoot()).getChildren().addAll(vbox);

            stage.setScene(scene);
            stage.show();
        }
    }
}
