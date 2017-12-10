package net.pkhapps.dart.clients.dispatch.ui;

import javafx.scene.layout.BorderPane;
import net.pkhapps.dart.clients.dispatch.ui.components.MunicipalityField;
import net.pkhapps.nlsmap.api.query.MunicipalityQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MainPane extends BorderPane {

    @Autowired
    MunicipalityQuery municipalityQuery;

    @PostConstruct
    void init() {
        MunicipalityField municipalityField = new MunicipalityField(municipalityQuery);
        municipalityField.refreshItems();
        setTop(municipalityField);
    }
}
