package fr.pagelib.termapp;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 *
 */
public class JobSettingsController extends PageController {

    @FXML Label documentNameLabel;
    @FXML TextField pagesField;
    @FXML ToggleButton colorToggle;
    @FXML Label greyLevelsHintLabel;
    @FXML TextField copiesField;
    @FXML Label priceLabel;
    @FXML Button okButton;

    SimpleStringProperty pages;
    SimpleBooleanProperty color;
    SimpleStringProperty copies;

    public JobSettingsController() {
        pages = new SimpleStringProperty();
        color = new SimpleBooleanProperty();
        copies = new SimpleStringProperty();
    }

    public void initialize() {
        // Set up data bindings
        Bindings.bindBidirectional(pages, pagesField.textProperty());
        Bindings.bindBidirectional(color, colorToggle.selectedProperty());
        Bindings.bindBidirectional(copies, copiesField.textProperty());

        // Afficher "oui" ou "non" sur le bouton "couleur" en fonction du choix
        color.addListener(new ChangeListener<Boolean>() {
            @Override public void changed(ObservableValue<? extends Boolean> obs, Boolean oldValue, Boolean newValue) {
                colorToggle.setText(newValue ? "Oui" : "Non");
                greyLevelsHintLabel.setVisible(!newValue);
            }
        });
    }

    public void reset() {
        pages.set("");
        color.set(false);
        copies.set("1");
    }

}
