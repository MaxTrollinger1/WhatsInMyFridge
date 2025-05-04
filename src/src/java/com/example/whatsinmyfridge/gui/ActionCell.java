package com.example.whatsinmyfridge.gui;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;

// Cell that adds "+" and "−" buttons to adjust an InventoryItem’s amount (hooks into JSON backend later)
public class ActionCell<T> extends TableCell<T, T> {
    // UI controls
    private final Button plus  = new Button("+");
    private final Button minus = new Button("−");
    private final HBox   box   = new HBox(5, plus, minus);

    // Set up styles and button actions
    public ActionCell() {
        box.setStyle("-fx-alignment:CENTER;");
        plus.getStyleClass().add("action-button");
        minus.getStyleClass().add("action-button");

        plus.setOnAction(e -> {
            InventoryItem it = (InventoryItem)getItem();
            it.setAmount(String.valueOf(
                    Integer.parseInt(it.getAmount().replaceAll("\\D","")) + 1
            ));
            // TODO: push update to JSON backend
        });

        minus.setOnAction(e -> {
            InventoryItem it = (InventoryItem)getItem();
            it.setAmount(String.valueOf(
                    Math.max(0, Integer.parseInt(it.getAmount().replaceAll("\\D","")) - 1)
            ));
            // TODO: push update to JSON backend
        });
    }

    // Show or hide the button box depending on cell content
    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        setGraphic(empty || item == null ? null : box);
    }
}
