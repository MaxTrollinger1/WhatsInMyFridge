package com.example.whatsinmyfridgegui;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
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
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class RecipeDetailController {
    @FXML private Label   nameLabel;
    @FXML private TextField nameField;
    @FXML private Button  toggleEditButton;

    @FXML private Label    descriptionLabel;
    @FXML private TextArea descriptionArea;

    @FXML private Button        addIngredientButton;
    @FXML private TableView<InventoryItem> ingredientsTable;
    @FXML private TableColumn<InventoryItem,String> ingNameCol;
    @FXML private TableColumn<InventoryItem,String> ingAmountCol;
    @FXML private TableColumn<InventoryItem,InventoryItem> ingActionCol;

    @FXML private Label    instructionsLabel;
    @FXML private TextArea instructionsArea;

    private RecipeItem recipe;
    private boolean    editMode = false;

    @FXML
    private void initialize() {
        // start in view‐only mode
        setViewMode();

        // wire ingredient columns
        ingNameCol.setCellValueFactory(c -> c.getValue().nameProperty());
        ingAmountCol.setCellValueFactory(c -> c.getValue().amountProperty());

        // delete button in action column
        ingActionCol.setCellValueFactory(c ->
                new ReadOnlyObjectWrapper<>(c.getValue())
        );
        ingActionCol.setCellFactory(col -> new TableCell<>() {
            private final Button del = new Button("🗑");
            { del.setOnAction(e ->
                    getTableView().getItems().remove(getItem())
            ); }
            @Override protected void updateItem(InventoryItem item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty || !editMode ? null : del);
            }
        });

        // auto‐size table
        ingredientsTable.setFixedCellSize(24);
        ingredientsTable.setMinHeight(Region.USE_PREF_SIZE);
    }

    /** Called by the list controller on double‐click */
    public void setRecipe(RecipeItem recipe) {
        this.recipe = recipe;
        nameLabel.setText(recipe.getName());
        nameField.setText(recipe.getName());

        // sample ingredients (replace with your JSON)
        List<InventoryItem> sample = List.of(
                new InventoryItem("Chicken thighs","1 lb"),
                new InventoryItem("Yogurt",        "1 cup")
        );
        ObservableList<InventoryItem> items = FXCollections.observableArrayList(sample);
        ingredientsTable.setItems(items);
        ingredientsTable.prefHeightProperty().bind(
                Bindings.size(items)
                        .multiply(ingredientsTable.getFixedCellSize())
                        .add(28)
        );

        // sample description & instructions
        descriptionLabel.setText("A luscious tomato-cream marinade...");
        descriptionArea.setText(descriptionLabel.getText());

        instructionsLabel.setText("1. Marinate…\n2. Cook…\n3. Serve…");
        instructionsArea.setText(instructionsLabel.getText());
    }

    /** Toggle between view & edit mode */
    @FXML
    private void onToggleEdit(ActionEvent evt) {
        editMode = !editMode;
        if (editMode) {
            setEditMode();
        } else {
            // save and go back to view
            recipe.setName(nameField.getText().trim());
            nameLabel.setText(recipe.getName());

            descriptionLabel.setText(descriptionArea.getText());
            instructionsLabel.setText(instructionsArea.getText());

            setViewMode();
        }
        toggleEditButton.setText(editMode ? "Save" : "Edit");
    }

    private void setViewMode() {
        nameLabel.setVisible(true);      nameLabel.setManaged(true);
        nameField.setVisible(false);     nameField.setManaged(false);

        descriptionLabel.setVisible(true);
        descriptionArea.setVisible(false);
        descriptionArea.setManaged(false);

        instructionsLabel.setVisible(true);
        instructionsArea.setVisible(false);
        instructionsArea.setManaged(false);

        addIngredientButton.setVisible(false);
        addIngredientButton.setDisable(true);

        ingredientsTable.setEditable(false);
        ingredientsTable.refresh();
    }

    private void setEditMode() {
        nameLabel.setVisible(false);     nameLabel.setManaged(false);
        nameField.setVisible(true);      nameField.setManaged(true);

        descriptionLabel.setVisible(false);
        descriptionArea.setVisible(true);
        descriptionArea.setManaged(true);

        instructionsLabel.setVisible(false);
        instructionsArea.setVisible(true);
        instructionsArea.setManaged(true);
        instructionsArea.setEditable(true);

        addIngredientButton.setVisible(true);
        addIngredientButton.setDisable(false);

        ingredientsTable.setEditable(true);
        ingredientsTable.refresh();
    }

    /** Show dialog to add a new ingredient */
    @FXML
    private void onAddIngredient(ActionEvent evt) {
        Dialog<InventoryItem> dlg = new Dialog<>();
        dlg.setTitle("Add Ingredient");
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        VBox box = new VBox(10);
        box.setPadding(new Insets(20));
        TextField n = new TextField(), a = new TextField();
        n.setPromptText("Ingredient");  a.setPromptText("Amount");
        box.getChildren().setAll(new Label("Name:"), n, new Label("Amount:"), a);
        dlg.getDialogPane().setContent(box);

        Node ok = dlg.getDialogPane().lookupButton(ButtonType.OK);
        ok.setDisable(true);
        n.textProperty().addListener((o,ov,nv) ->
                ok.setDisable(nv.trim().isEmpty() || a.getText().trim().isEmpty())
        );
        a.textProperty().addListener((o,ov,nv) ->
                ok.setDisable(nv.trim().isEmpty() || n.getText().trim().isEmpty())
        );

        dlg.setResultConverter(btn ->
                btn == ButtonType.OK
                        ? new InventoryItem(n.getText().trim(), a.getText().trim())
                        : null
        );
        Optional<InventoryItem> res = dlg.showAndWait();
        res.ifPresent(item -> ingredientsTable.getItems().add(item));
    }

    /** Return to the recipes list screen */
    @FXML
    private void onBackToList(ActionEvent evt) throws IOException {
        Parent list = FXMLLoader.load(
                getClass().getResource("RecipeList.fxml")
        );
        Stage stage = (Stage)((Node)evt.getSource()).getScene().getWindow();
        stage.getScene().setRoot(list);
    }
}
