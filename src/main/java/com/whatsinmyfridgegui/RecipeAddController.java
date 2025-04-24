package com.example.whatsinmyfridgegui;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Optional;

public class RecipeAddController {
    @FXML private TextField nameField;
    @FXML private TextArea  descriptionField;
    @FXML private Button    addIngredientButton;
    @FXML private TableView<InventoryItem> ingredientsTable;
    @FXML private TableColumn<InventoryItem,String> ingNameCol;
    @FXML private TableColumn<InventoryItem,String> ingAmountCol;
    @FXML private TableColumn<InventoryItem,InventoryItem> ingActionCol;
    @FXML private TextArea  instructionsField;

    private ObservableList<RecipeItem> recipes;   // injected from list controller

    /** Called from RecipeListController to pass in the shared list */
    public void setRecipeList(ObservableList<RecipeItem> recipes) {
        this.recipes = recipes;
    }

    @FXML
    private void initialize() {
        // Setup ingredients table columns
        ingNameCol.setCellValueFactory(c -> c.getValue().nameProperty());
        ingAmountCol.setCellValueFactory(c -> c.getValue().amountProperty());
        ingActionCol.setCellValueFactory(c ->
                new ReadOnlyObjectWrapper<>(c.getValue())
        );
        ingActionCol.setCellFactory(col -> new TableCell<>() {
            private final Button del = new Button("🗑");
            { del.setOnAction(e ->
                    getTableView().getItems().remove(getItem()));
            }
            @Override protected void updateItem(InventoryItem item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : del);
            }
        });

        ingredientsTable.setFixedCellSize(24);
        ingredientsTable.setMinHeight(Region.USE_PREF_SIZE);

        // Wire the Add Ingredient button
        addIngredientButton.setOnAction(this::onAddIngredient);
    }

    @FXML
    private void onAddIngredient(ActionEvent evt) {
        Dialog<InventoryItem> dlg = new Dialog<>();
        dlg.setTitle("Add Ingredient");
        dlg.getDialogPane().getButtonTypes()
                .addAll(ButtonType.OK, ButtonType.CANCEL);

        // Build the small form
        VBox box = new VBox(10);
        box.setPadding(new Insets(20));
        TextField nm = new TextField(), amt = new TextField();
        nm.setPromptText("Ingredient"); amt.setPromptText("Amount");
        box.getChildren().setAll(
                new Label("Name:"), nm,
                new Label("Amount:"), amt
        );
        dlg.getDialogPane().setContent(box);

        // disable OK until form valid
        Node ok = dlg.getDialogPane().lookupButton(ButtonType.OK);
        ok.setDisable(true);
        nm.textProperty().addListener((o,a,b) ->
                ok.setDisable(b.trim().isEmpty()||amt.getText().trim().isEmpty())
        );
        amt.textProperty().addListener((o,a,b) ->
                ok.setDisable(b.trim().isEmpty()||nm.getText().trim().isEmpty())
        );

        // convert result
        dlg.setResultConverter(bt -> bt==ButtonType.OK
                ? new InventoryItem(nm.getText().trim(), amt.getText().trim())
                : null
        );
        Optional<InventoryItem> res = dlg.showAndWait();
        res.ifPresent(item -> ingredientsTable.getItems().add(item));
    }

    /** Cancel → go back to recipes list */
    @FXML
    private void onCancel(ActionEvent evt) throws IOException {
        Parent listRoot = FXMLLoader.load(
                getClass().getResource("RecipeList.fxml")
        );
        Stage st = (Stage)((Node)evt.getSource()).getScene().getWindow();
        st.getScene().setRoot(listRoot);
    }

    /** Save → add to shared list, then back */
    @FXML
    private void onSave(ActionEvent evt) throws IOException {
        String name = nameField.getText().trim();
        if (!name.isEmpty()) {
            RecipeItem r = new RecipeItem(name);
            recipes.add(r);
        }
        onCancel(evt);  // reuse cancel navigation
    }
}
