package com.example.whatsinmyfridgegui;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class HomescreenController {
    @FXML private TableView<InventoryItem> inventoryTable;
    @FXML private TableColumn<InventoryItem, String> invItemCol;
    @FXML private TableColumn<InventoryItem, String> invAmountCol;
    @FXML private TableColumn<InventoryItem, InventoryItem> invActionCol;

    @FXML private TableView<InventoryItem> groceryTable;
    @FXML private TableColumn<InventoryItem, String> groItemCol;
    @FXML private TableColumn<InventoryItem, String> groAmountCol;
    @FXML private TableColumn<InventoryItem, InventoryItem> groActionCol;

    @FXML private ListView<String> recipesList;

    @FXML
    public void initialize() {
        inventoryTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        groceryTable .setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        invItemCol   .setCellValueFactory(c -> c.getValue().nameProperty());
        invAmountCol .setCellValueFactory(c -> c.getValue().amountProperty());
        invActionCol .setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue()));
        invActionCol .setCellFactory(col -> new ActionCell<>());

        groItemCol   .setCellValueFactory(c -> c.getValue().nameProperty());
        groAmountCol .setCellValueFactory(c -> c.getValue().amountProperty());
        groActionCol .setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue()));
        groActionCol .setCellFactory(col -> new ActionCell<>());

        inventoryTable.setItems(FXCollections.observableArrayList(
                new InventoryItem("Apples","3"),
                new InventoryItem("Ground Beef","2 lbs"),
                new InventoryItem("Milk","1 Gallon")
        ));
        groceryTable.setItems(FXCollections.observableArrayList(
                new InventoryItem("Bananas","3"),
                new InventoryItem("Eggs","1 Dozen"),
                new InventoryItem("Butter","")
        ));
        recipesList.setItems(FXCollections.observableArrayList(
                "Butter Chicken","Pico De Gallo","Salisbury Steak"
        ));
    }

    @FXML
    public void onAddInventory(ActionEvent evt) {
        Dialog<InventoryItem> d = new Dialog<>();
        d.setTitle("Add Inventory Item");
        d.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(20,150,10,10));

        TextField n = new TextField(); n.setPromptText("Item name");
        TextField a = new TextField(); a.setPromptText("Amount");
        grid.add(new Label("Name:"),0,0); grid.add(n,1,0);
        grid.add(new Label("Amount:"),0,1); grid.add(a,1,1);
        d.getDialogPane().setContent(grid);
        Platform.runLater(n::requestFocus);

        Node ok = d.getDialogPane().lookupButton(ButtonType.OK);
        ok.setDisable(true);
        n.textProperty().addListener((o,ov,nv)->ok.setDisable(nv.trim().isEmpty()||a.getText().trim().isEmpty()));
        a.textProperty().addListener((o,ov,nv)->ok.setDisable(nv.trim().isEmpty()||n.getText().trim().isEmpty()));

        d.setResultConverter(b-> b==ButtonType.OK
                ? new InventoryItem(n.getText().trim(),a.getText().trim())
                : null
        );
        Optional<InventoryItem> res = d.showAndWait();
        res.ifPresent(item-> inventoryTable.getItems().add(item));
    }

    @FXML
    public void onAddGrocery(ActionEvent evt) {
        Dialog<InventoryItem> d = new Dialog<>();
        d.setTitle("Add Grocery Item");
        d.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(20,150,10,10));

        TextField n = new TextField(); n.setPromptText("Item name");
        TextField a = new TextField(); a.setPromptText("Amount");
        grid.add(new Label("Name:"),0,0); grid.add(n,1,0);
        grid.add(new Label("Amount:"),0,1); grid.add(a,1,1);
        d.getDialogPane().setContent(grid);
        Platform.runLater(n::requestFocus);

        Node ok = d.getDialogPane().lookupButton(ButtonType.OK);
        ok.setDisable(true);
        n.textProperty().addListener((o,ov,nv)->ok.setDisable(nv.trim().isEmpty()||a.getText().trim().isEmpty()));
        a.textProperty().addListener((o,ov,nv)->ok.setDisable(nv.trim().isEmpty()||n.getText().trim().isEmpty()));

        d.setResultConverter(b-> b==ButtonType.OK
                ? new InventoryItem(n.getText().trim(),a.getText().trim())
                : null
        );
        Optional<InventoryItem> res = d.showAndWait();
        res.ifPresent(item-> groceryTable.getItems().add(item));
    }

    @FXML
    public void onViewAllRecipes(ActionEvent evt) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("RecipeList.fxml"));
        Stage st = (Stage)((Node)evt.getSource()).getScene().getWindow();
        st.getScene().setRoot(root);
    }
}
