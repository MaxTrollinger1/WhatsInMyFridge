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

    public void setRecipeList(ObservableList<RecipeItem> recipes) {
        this.recipes = recipes;
    }

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

    @FXML
    private void onCancel(ActionEvent evt) throws IOException {
        Parent list = FXMLLoader.load(getClass().getResource("/com/whatsinmyfridgegui/RecipeList.fxml"));
        Stage st = (Stage)((Node)evt.getSource()).getScene().getWindow();
        st.getScene().setRoot(list);
    }
}
