package net.pkhapps.dart.map.ui;

import javafx.beans.property.SimpleStringProperty;

public abstract class MapObjectModel {

    private final SimpleStringProperty name;

    public MapObjectModel(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }
}
