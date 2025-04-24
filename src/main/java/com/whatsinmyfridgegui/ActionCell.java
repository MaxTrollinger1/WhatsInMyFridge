package com.example.whatsinmyfridgegui;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;

public class ActionCell<T> extends TableCell<T, T> {
    private final Button plus  = new Button("+");
    private final Button minus = new Button("−");
    private final HBox   box   = new HBox(5, plus, minus);

    public ActionCell() {
        box.setStyle("-fx-alignment:CENTER;");
        plus.getStyleClass().add("action-button");
        minus.getStyleClass().add("action-button");

        plus.setOnAction(e -> {
            InventoryItem it = (InventoryItem)getItem();
            it.setAmount(String.valueOf(
                    Integer.parseInt(it.getAmount().replaceAll("\\D","")) + 1
            ));
        });
        minus.setOnAction(e -> {
            InventoryItem it = (InventoryItem)getItem();
            it.setAmount(String.valueOf(
                    Math.max(0, Integer.parseInt(it.getAmount().replaceAll("\\D","")) - 1)
            ));
        });
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        setGraphic(empty || item==null ? null : box);
    }
}
