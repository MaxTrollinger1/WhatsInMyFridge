package com.example.whatsinmyfridge.gui;

import com.example.whatsinmyfridge.model.Ingredient;
import com.example.whatsinmyfridge.model.Recipe;
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
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * Controller for the "Add Recipe" dialog.
 * <p>
 * Manages input fields for recipe name, description, ingredients, and instructions,
 * and handles adding ingredients and saving or canceling the recipe.
 */
public class RecipeAddController {
    @FXML private TextField nameField;
    @FXML private TextArea  descriptionField;
    @FXML private TableView<Ingredient> ingredientsTable;
    @FXML private TableColumn<Ingredient,String> ingNameCol;
    @FXML private TableColumn<Ingredient,String> ingAmountCol;
    @FXML private TableColumn<Ingredient,String> ingMeasurementCol;
    @FXML private TableColumn<Ingredient,Ingredient> ingActionCol;
    @FXML private TextArea  instructionsField;
    @FXML private Button    addIngredientButton;

    private ObservableList<RecipeItem> recipes;

    /**
     * Supplies the list where new recipes will be added.
     * @param recipes observable list of RecipeItem
     */
    public void setRecipeList(ObservableList<RecipeItem> recipes) {
        this.recipes = recipes;
    }

    /**
     * Initializes table columns, factories, and the "Add Ingredient" button.
     * Called after FXML is loaded.
     */
    @FXML
    private void initialize() {
        ingNameCol.setCellValueFactory(c -> c.getValue().nameProperty());
        ingAmountCol.setCellValueFactory(c -> c.getValue().amountProperty());
        ingMeasurementCol.setCellValueFactory(c -> c.getValue().unitProperty());
        ingActionCol.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue()));
        ingActionCol.setCellFactory(col -> new TableCell<>() {
            private final Button del = new Button("🗑");
            { del.setOnAction(e -> getTableView().getItems().remove(getItem())); }
            @Override protected void updateItem(Ingredient item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : del);
            }
        });

        ingredientsTable.setItems(FXCollections.observableArrayList());
        ingredientsTable.setFixedCellSize(24);
        ingredientsTable.setMinHeight(Region.USE_PREF_SIZE);

        addIngredientButton.setOnAction(this::onAddIngredient);
    }

    /**
     * Shows a dialog to enter a new Ingredient and adds it to the ingredients table.
     * @param evt action event from the "Add Ingredient" button
     */
    @FXML
    private void onAddIngredient(ActionEvent evt) {
        Dialog<Ingredient> dlg = new Dialog<>();
        dlg.setTitle("Add Ingredient");
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        VBox box = new VBox(10);
        box.setPadding(new Insets(20));
        TextField nm  = new TextField();
        TextField amt = new TextField();
        TextField meas = new TextField();
        nm.setPromptText("Ingredient");
        amt.setPromptText("Amount");
        meas.setPromptText("Measurement (optional)");
        amt.setTextFormatter(new TextFormatter<>(c ->
                c.getControlNewText().matches("\\d*") ? c : null
        ));
        box.getChildren().addAll(
                new Label("Name:"), nm,
                new Label("Amount:"), amt,
                new Label("Measurement:"), meas
        );
        dlg.getDialogPane().setContent(box);

        Node ok = dlg.getDialogPane().lookupButton(ButtonType.OK);
        ok.setDisable(true);
        nm.textProperty().addListener((o,a,b) ->
                ok.setDisable(b.trim().isEmpty() || amt.getText().trim().isEmpty())
        );
        amt.textProperty().addListener((o,a,b) ->
                ok.setDisable(b.trim().isEmpty() || nm.getText().trim().isEmpty())
        );

        dlg.setResultConverter(bt -> bt == ButtonType.OK
                ? new Ingredient(nm.getText().trim(),
                Double.parseDouble(amt.getText().trim()),
                meas.getText().trim())
                : null
        );
        Optional<Ingredient> res = dlg.showAndWait();
        res.ifPresent(item -> ingredientsTable.getItems().add(item));
    }

    /**
     * Validates input and saves the new recipe to the shared list, then returns to list view.
     * @param evt action event from the "Save" button
     * @throws IOException if list FXML fails to load
     */
    @FXML
    private void onSave(ActionEvent evt) throws IOException {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Recipe name is required.").showAndWait();
            return;
        }
        RecipeItem r = new RecipeItem(new Recipe(
                name,
                descriptionField.getText().trim(),
                ingredientsTable.getItems(),
                instructionsField.getText().trim())
        );
        recipes.add(r);
        onCancel(evt);
    }

    /**
     * Cancels and returns to the recipe list view without saving.
     * @param evt action event from the "Cancel" or after save
     * @throws IOException if list FXML fails to load
     */
    @FXML
    private void onCancel(ActionEvent evt) throws IOException {
        Parent list = FXMLLoader.load(getClass().getResource("/com/whatsinmyfridgegui/RecipeList.fxml"));
        Stage st = (Stage)((Node)evt.getSource()).getScene().getWindow();
        st.getScene().setRoot(list);
    }
}
