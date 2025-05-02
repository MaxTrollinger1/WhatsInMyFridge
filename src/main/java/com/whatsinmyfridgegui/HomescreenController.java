package com.whatsinmyfridgegui;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class HomescreenController {

    @FXML private TableView<InventoryItem> inventoryTable;
    @FXML private TableColumn<InventoryItem,String> invItemCol;
    @FXML private TableColumn<InventoryItem,String> invAmountCol;
    @FXML private TableColumn<InventoryItem,String> invMeasurementCol;
    @FXML private TableColumn<InventoryItem,InventoryItem> invActionCol;
    @FXML private Button addInventoryButton;

    @FXML private ListView<String> recipesList;
    @FXML private Button viewAllButton;

    @FXML private TableView<InventoryItem> groceryTable;
    @FXML private TableColumn<InventoryItem,String> groItemCol;
    @FXML private TableColumn<InventoryItem,String> groAmountCol;
    @FXML private TableColumn<InventoryItem,String> groMeasurementCol;
    @FXML private TableColumn<InventoryItem,InventoryItem> groActionCol;
    @FXML private Button addGroceryButton;

    @FXML
    public void initialize() {
        inventoryTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        groceryTable .setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        invItemCol        .setCellValueFactory(c -> c.getValue().nameProperty());
        invAmountCol      .setCellValueFactory(c -> c.getValue().amountProperty());
        invMeasurementCol .setCellValueFactory(c -> c.getValue().measurementProperty());
        invActionCol      .setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue()));
        invActionCol      .setCellFactory(col -> new QuantityCell());

        groItemCol        .setCellValueFactory(c -> c.getValue().nameProperty());
        groAmountCol      .setCellValueFactory(c -> c.getValue().amountProperty());
        groMeasurementCol .setCellValueFactory(c -> c.getValue().measurementProperty());
        groActionCol      .setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue()));
        groActionCol      .setCellFactory(col -> new QuantityCell());

        inventoryTable.setItems(FXCollections.observableArrayList(
                new InventoryItem("Apples","3",""),
                new InventoryItem("Ground Beef","2","lbs"),
                new InventoryItem("Milk","1","Gallon")
        ));
        groceryTable.setItems(FXCollections.observableArrayList(
                new InventoryItem("Bananas","3",""),
                new InventoryItem("Eggs","12","Dozen"),
                new InventoryItem("Butter","","")
        ));
        recipesList.setItems(FXCollections.observableArrayList(
                "Butter Chicken","Pico De Gallo","Salisbury Steak"
        ));
    }

    @FXML public void onAddInventory(ActionEvent evt) {
        showAddDialog("Add Inventory Item", inventoryTable);
    }

    @FXML public void onAddGrocery(ActionEvent evt) {
        showAddDialog("Add Grocery Item", groceryTable);
    }

    private void showAddDialog(String title, TableView<InventoryItem> table) {
        Dialog<InventoryItem> dlg = new Dialog<>();
        dlg.setTitle(title);
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nameF = new TextField(); nameF.setPromptText("Item name");
        TextField amtF  = new TextField();  amtF.setPromptText("Amount");
        TextField measF = new TextField(); measF.setPromptText("Measurement (optional)");
        amtF.setTextFormatter(new TextFormatter<>(c ->
                c.getControlNewText().matches("\\d*") ? c : null
        ));

        grid.add(new Label("Name:"),        0, 0);
        grid.add(nameF,                     1, 0);
        grid.add(new Label("Amount:"),      0, 1);
        grid.add(amtF,                      1, 1);
        grid.add(new Label("Measurement:"), 0, 2);
        grid.add(measF,                     1, 2);
        dlg.getDialogPane().setContent(grid);

        Node ok = dlg.getDialogPane().lookupButton(ButtonType.OK);
        ok.setDisable(true);
        nameF.textProperty().addListener((o,ov,nv) ->
                ok.setDisable(nv.trim().isEmpty() || amtF.getText().trim().isEmpty())
        );
        amtF.textProperty().addListener((o,ov,nv) ->
                ok.setDisable(nv.trim().isEmpty() || nameF.getText().trim().isEmpty())
        );

        dlg.setResultConverter(bt -> bt == ButtonType.OK
                ? new InventoryItem(nameF.getText().trim(), amtF.getText().trim(), measF.getText().trim())
                : null
        );
        Optional<InventoryItem> res = dlg.showAndWait();
        res.ifPresent(table.getItems()::add);
    }

    @FXML public void onViewAllRecipes(ActionEvent evt) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("RecipeList.fxml"));
        Stage st = (Stage)((Node)evt.getSource()).getScene().getWindow();
        st.getScene().setRoot(root);
    }

    /** Icons‐only +/− and 🗑 for delete; larger size and transparent chrome */
    private static class QuantityCell extends TableCell<InventoryItem,InventoryItem> {
        private final Button plus  = new Button("+");
        private final Button minus = new Button("−");
        private final Button del   = new Button("🗑");

        QuantityCell() {
            String style = "-fx-background-color: transparent; -fx-border-color: transparent; -fx-padding: 0; -fx-font-size: 16px;";
            plus .setStyle(style);
            minus.setStyle(style);
            del  .setStyle(style);

            plus .setFocusTraversable(false);
            minus.setFocusTraversable(false);
            del  .setFocusTraversable(false);

            plus .setOnAction(e -> adjust(1));
            minus.setOnAction(e -> adjust(-1));
            del  .setOnAction(e -> getTableView().getItems().remove(getItem()));
        }

        private void adjust(int delta) {
            InventoryItem item = getItem();
            if (item == null) return;
            String t = item.getAmount();
            int v = t.matches("\\d+") ? Integer.parseInt(t) : 0;
            item.setAmount(String.valueOf(Math.max(0, v + delta)));
        }

        @Override
        protected void updateItem(InventoryItem itm, boolean empty) {
            super.updateItem(itm, empty);
            if (empty || itm == null) {
                setGraphic(null);
            } else {
                HBox box = new HBox(6, plus, minus, del);
                box.setAlignment(Pos.CENTER);
                setGraphic(box);
            }
        }
    }
}
