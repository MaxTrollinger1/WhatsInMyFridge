package com.whatsinmyfridgegui;

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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;

public class RecipeDetailController {

    @FXML private Button backButton;
    @FXML private Button toggleSaveButton;

    @FXML private Label nameLabel;
    @FXML private TextField nameField;

    @FXML private Label descriptionLabel;
    @FXML private TextArea descriptionArea;

    @FXML private Label instructionsLabel;
    @FXML private TextArea instructionsArea;

    @FXML private Button addIngredientButton;
    @FXML private TableView<InventoryItem> ingredientsTable;
    @FXML private TableColumn<InventoryItem,String> ingNameCol;
    @FXML private TableColumn<InventoryItem,String> ingAmountCol;
    @FXML private TableColumn<InventoryItem,String> ingMeasurementCol;
    @FXML private TableColumn<InventoryItem,InventoryItem> ingActionCol;

    private RecipeItem recipe;
    private boolean editMode = false;

    @FXML
    private void initialize() {
        // wire button handlers
        toggleSaveButton.setOnAction(this::onToggleSave);
        backButton.setOnAction(this::onBack);

        // table columns
        ingNameCol.setCellValueFactory(c -> c.getValue().nameProperty());
        ingAmountCol.setCellValueFactory(c -> c.getValue().amountProperty());
        ingMeasurementCol.setCellValueFactory(c -> c.getValue().measurementProperty());

        ingNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        ingAmountCol.setCellFactory(TextFieldTableCell.forTableColumn());
        ingMeasurementCol.setCellFactory(TextFieldTableCell.forTableColumn());

        ingNameCol.setOnEditCommit(e -> e.getRowValue().setName(e.getNewValue()));
        ingAmountCol.setOnEditCommit(e -> {
            String v = e.getNewValue();
            if (v.matches("\\d+")) {
                e.getRowValue().setAmount(v);
            } else {
                new Alert(Alert.AlertType.WARNING, "Amount must be integer.").showAndWait();
                ingredientsTable.refresh();
            }
        });
        ingMeasurementCol.setOnEditCommit(e -> e.getRowValue().setMeasurement(e.getNewValue()));

        ingActionCol.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue()));
        ingActionCol.setCellFactory(col -> new TableCell<>() {
            private final Button del = new Button("🗑");
            { del.setOnAction(evt -> getTableView().getItems().remove(getItem())); }
            @Override protected void updateItem(InventoryItem item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : del);
            }
        });

        ingredientsTable.setTableMenuButtonVisible(false);
        ingredientsTable.setFixedCellSize(24);
        ingredientsTable.setMinHeight(Region.USE_PREF_SIZE);

        // remove default placeholder text
        ingredientsTable.setPlaceholder(new Label(""));

        enterViewMode();
    }

    public void setRecipe(RecipeItem recipe) {
        this.recipe = recipe;
        nameLabel.setText(recipe.getName());
        nameField.setText(recipe.getName());

        ObservableList<InventoryItem> items = recipe.getIngredients();
        ingredientsTable.setItems(items);
        ingredientsTable.prefHeightProperty().bind(
                Bindings.size(items)
                        .multiply(ingredientsTable.getFixedCellSize())
                        .add(28)
        );

        descriptionLabel.setText(recipe.getDescription());
        descriptionArea.setText(recipe.getDescription());

        instructionsLabel.setText(recipe.getInstructions());
        instructionsArea.setText(recipe.getInstructions());
    }

    @FXML
    private void onToggleSave(ActionEvent evt) {
        if (!editMode) {
            editMode = true;
            toggleSaveButton.setText("Save");
            enterEditMode();
        } else {
            recipe.setName(nameField.getText().trim());
            recipe.setDescription(descriptionArea.getText().trim());
            recipe.setInstructions(instructionsArea.getText().trim());
            editMode = false;
            toggleSaveButton.setText("Edit");
            enterViewMode();
        }
    }

    private void enterViewMode() {
        nameLabel.setVisible(true);   nameLabel.setManaged(true);
        nameField.setVisible(false);  nameField.setManaged(false);

        descriptionLabel.setVisible(true);   descriptionLabel.setManaged(true);
        descriptionArea.setVisible(false);   descriptionArea.setManaged(false);

        instructionsLabel.setVisible(true);   instructionsLabel.setManaged(true);
        instructionsArea.setVisible(false);   instructionsArea.setManaged(false);

        ingredientsTable.setEditable(false);
        ingActionCol.setVisible(false);

        addIngredientButton.setVisible(false);
        addIngredientButton.setManaged(false);
    }

    private void enterEditMode() {
        nameLabel.setVisible(false);  nameLabel.setManaged(false);
        nameField.setVisible(true);   nameField.setManaged(true);

        descriptionLabel.setVisible(false);   descriptionLabel.setManaged(false);
        descriptionArea.setVisible(true);     descriptionArea.setManaged(true);

        instructionsLabel.setVisible(false);   instructionsLabel.setManaged(false);
        instructionsArea.setVisible(true);     instructionsArea.setManaged(true);

        ingredientsTable.setEditable(true);
        ingActionCol.setVisible(true);

        addIngredientButton.setVisible(true);
        addIngredientButton.setManaged(true);
    }

    @FXML
    private void onAddIngredient(ActionEvent evt) {
        if (!editMode) return;

        Dialog<InventoryItem> dlg = new Dialog<>();
        dlg.setTitle("Add Ingredient");
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20));
        TextField nm = new TextField(), amt = new TextField(), meas = new TextField();
        nm.setPromptText("Ingredient"); amt.setPromptText("Amount"); meas.setPromptText("Measurement (opt)");
        amt.setTextFormatter(new TextFormatter<>(c ->
                c.getControlNewText().matches("\\d*") ? c : null
        ));
        grid.add(new Label("Name:"),        0, 0);
        grid.add(nm,                        1, 0);
        grid.add(new Label("Amount:"),      0, 1);
        grid.add(amt,                       1, 1);
        grid.add(new Label("Measurement:"), 0, 2);
        grid.add(meas,                      1, 2);
        dlg.getDialogPane().setContent(grid);

        Node ok = dlg.getDialogPane().lookupButton(ButtonType.OK);
        ok.setDisable(true);
        nm.textProperty().addListener((o,ov,nv) ->
                ok.setDisable(nv.trim().isEmpty() || amt.getText().trim().isEmpty())
        );
        amt.textProperty().addListener((o,ov,nv) ->
                ok.setDisable(nv.trim().isEmpty() || nm.getText().trim().isEmpty())
        );

        dlg.setResultConverter(bt -> bt == ButtonType.OK
                ? new InventoryItem(nm.getText().trim(), amt.getText().trim(), meas.getText().trim())
                : null
        );
        dlg.showAndWait().ifPresent(item -> ingredientsTable.getItems().add(item));
    }

    @FXML
    private void onBack(ActionEvent evt) {
        try {
            if (editMode) {
                editMode = false;
                toggleSaveButton.setText("Edit");
                enterViewMode();
            } else {
                Parent list = FXMLLoader.load(getClass().getResource("RecipeList.fxml"));
                Stage st = (Stage)((Node)evt.getSource()).getScene().getWindow();
                st.getScene().setRoot(list);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
