package net.pkhapps.dart.clients.dispatch.ui.components;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import net.pkhapps.nlsmap.api.codes.Municipality;
import net.pkhapps.nlsmap.api.query.MunicipalityQuery;
import net.pkhapps.nlsmap.api.types.Language;
import net.pkhapps.nlsmap.api.types.LocalizedString;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class MunicipalityField extends ComboBox<Municipality> {

    private final MunicipalityQuery municipalityQuery;

    /**
     * Creates a new {@code MunicipalityField}. Remember to call {@link #refreshItems()} before using the field.
     *
     * @param municipalityQuery the query to read municipalities from.
     */
    public MunicipalityField(@NotNull MunicipalityQuery municipalityQuery) {
        this.municipalityQuery = Objects.requireNonNull(municipalityQuery, "municipalityQuery must not be null");
        setCellFactory(this::getListCell);
    }

    private ListCell<Municipality> getListCell(ListView<Municipality> listView) {
        return new ListCell<Municipality>() {
            @Override
            protected void updateItem(Municipality item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    setText(getFormattedMunicipalityName(item));
                }
            }
        };
    }

    private static String getFormattedMunicipalityName(Municipality municipality) {
        LocalizedString description = municipality.getDescription();
        List<String> names = new LinkedList<>();
        description.get(Language.FINNISH).ifPresent(names::add);
        description.get(Language.SWEDISH).ifPresent(names::add);
        return String.join(" - ", names);
    }

    /**
     * Refreshes the field with the latest municipalities from the {@link MunicipalityQuery}. Since the number of
     * municipalities is fairly small (about 300), they are all fetched in a single query and stored in memory.
     *
     * @see MunicipalityQuery#findAll()
     */
    public void refreshItems() {
        setItems(FXCollections.observableList(municipalityQuery.findAll()));
    }
}
