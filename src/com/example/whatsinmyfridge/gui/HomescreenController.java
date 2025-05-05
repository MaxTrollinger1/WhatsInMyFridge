package com.example.whatsinmyfridge.gui;

import com.example.whatsinmyfridge.WhatsInMyFridgeApp;
import com.example.whatsinmyfridge.model.Ingredient;
import com.example.whatsinmyfridge.model.Recipe;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Controller for the home screen, showing pantry inventory, grocery list, and recipes.
 * <p>
 * Manages table and list view population and user interactions for adding/removing items
 * and navigating to the recipe list screen.
 */
public class HomescreenController {

    @FXML private TableView<InventoryItem> inventoryTable;
    @FXML private TableColumn<InventoryItem,String> invItemCol;
    @FXML private TableColumn<InventoryItem,String> invAmountCol;
    @FXML private TableColumn<InventoryItem,String> invMeasurementCol;
    @FXML private TableColumn<InventoryItem,InventoryItem> invActionCol;
    @FXML private Button addInventoryButton;
    @FXML private Button themeToggleButton;

    @FXML private ListView<String> recipesList;
    @FXML private Button viewAllButton;

    @FXML private TableView<InventoryItem> groceryTable;
    @FXML private TableColumn<InventoryItem,String> groItemCol;
    @FXML private TableColumn<InventoryItem,String> groAmountCol;
    @FXML private TableColumn<InventoryItem,String> groMeasurementCol;
    @FXML private TableColumn<InventoryItem,InventoryItem> groActionCol;
    @FXML private Button addGroceryButton;

    /**
     * Initializes table columns, cell factories, and populates views after FXML load.
     */
    @FXML
    public void initialize() {
        inventoryTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        groceryTable .setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        invItemCol        .setCellValueFactory(c -> c.getValue().nameProperty());
        invAmountCol      .setCellValueFactory(c -> c.getValue().amountProperty());
        invMeasurementCol .setCellValueFactory(c -> c.getValue().measurementProperty());
        invActionCol      .setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue()));
        invActionCol      .setCellFactory(col -> new QuantityCell(inventoryTable));


        groItemCol        .setCellValueFactory(c -> c.getValue().nameProperty());
        groAmountCol      .setCellValueFactory(c -> c.getValue().amountProperty());
        groMeasurementCol .setCellValueFactory(c -> c.getValue().measurementProperty());
        groActionCol      .setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue()));
        groActionCol      .setCellFactory(col -> new QuantityCell(groceryTable));

        try
        {
            SetLists(); // Reset List on entry
        } catch (Exception e) {}

        // Add tooltips for buttons
        addInventoryButton.setTooltip(new Tooltip("Add a new item to your pantry inventory"));
        addGroceryButton.setTooltip(new Tooltip("Add a new item to your grocery list"));
        viewAllButton.setTooltip(new Tooltip("View all saved recipes"));

        // Tooltips for tables
        inventoryTable.setTooltip(new Tooltip("Your current pantry items. Use +/− to adjust or 🗑 to remove."));
        groceryTable.setTooltip(new Tooltip("Your grocery list. Use +/− to adjust or 🗑 to remove."));

        // Tooltip for recipe list
        recipesList.setTooltip(new Tooltip("Recipes you can make with current ingredients"));
    }

    /**
     * Refreshes pantry, grocery, and recipe views from the inventory.
     */
    public void SetLists()
    {
        set_ingredients();

        set_groceries();

        set_recipes();
    }

    /**
     * Populates the inventory table with current pantry items.
     */
    void set_ingredients()
    {
        ArrayList<Ingredient> ingredients = WhatsInMyFridgeApp.inventory.getIngredients();
        ArrayList<InventoryItem> i_items = new ArrayList<>();
        inventoryTable.getItems().clear();

        for (int i = 0; i < ingredients.size(); i++)
        {
            i_items.add(new InventoryItem(
                    ingredients.get(i).getName(),
                    String.valueOf(ingredients.get(i).getAmount()),
                    ingredients.get(i).getUnit()));
        }

        inventoryTable.setItems(FXCollections.observableArrayList(i_items));
    }

    /**
     * Populates the grocery table with current grocery list items.
     */
    void set_groceries()
    {
        ArrayList<Ingredient> groceries = WhatsInMyFridgeApp.inventory.getGroceries();
        ArrayList<InventoryItem> i_items = new ArrayList<>();
        groceryTable.getItems().clear();

        for (int i = 0; i < groceries.size(); i++)
        {
            i_items.add(new InventoryItem(
                    groceries.get(i).getName(),
                    String.valueOf(groceries.get(i).getAmount()),
                    groceries.get(i).getUnit()));
        }

        groceryTable.setItems(FXCollections.observableArrayList(i_items));
    }

    /**
     * Populates the recipe list view with saved recipe names.
     */
    void set_recipes()
    {
        ArrayList<Recipe> recipeSave = WhatsInMyFridgeApp.inventory.getRecipes();
        ArrayList<String> recipeNames = new ArrayList<>();

        for (Recipe r : recipeSave) {
            recipeNames.add(r.getName());
        }

        recipesList.setItems(FXCollections.observableArrayList(recipeNames));
    }


    /**
     * Handles add-inventory button click by showing an input dialog.
     * @param evt action event
     */
    @FXML public void onAddInventory(ActionEvent evt) {
        showAddDialog("Add Inventory Item", inventoryTable);
    }

    /**
     * Handles add-grocery button click by showing an input dialog.
     * @param evt action event
     */
    @FXML public void onAddGrocery(ActionEvent evt) {
        showAddDialog("Add Grocery Item", groceryTable);
    }

    /**
     * shows a dialog to enter a new InventoryItem and updates the given table and inventory.
     * @param title dialog title
     * @param table target table view (inventory or grocery)
     */
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
        res.ifPresent(result -> {
            // Create Ingredient from the InventoryItem
            Ingredient ingredient = new Ingredient(result.getName(),
                    Double.parseDouble(result.getAmount()),
                    result.getMeasurement());
            // Add Ingredient to the inventory or grocery list depending on table
            if (table == inventoryTable) {
                WhatsInMyFridgeApp.inventory.addIngredient(ingredient);
            } else if (table == groceryTable) {
                WhatsInMyFridgeApp.inventory.addGrocery(ingredient);
            }
            // Add InventoryItem to the table
            table.getItems().add(result);
        });
    }

    /**
     * Navigates to the recipe list view.
     * @param evt action event from the "View All" button
     * @throws IOException if FXML load fails
     */
    @FXML public void onViewAllRecipes(ActionEvent evt) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/whatsinmyfridgegui/RecipeList.fxml"));
        Stage st = (Stage)((Node)evt.getSource()).getScene().getWindow();
        st.getScene().setRoot(root);
    }

    /** Icons‐only +/− and 🗑 for delete; larger size and transparent chrome */
    private class QuantityCell extends TableCell<InventoryItem,InventoryItem> {
        private final Button plus  = new Button("+");
        private final Button minus = new Button("−");
        private final Button del   = new Button("🗑");
        private final TableView<InventoryItem> parentTable;

        /**
         * Initializes cell with action buttons and handlers.
         * @param table parent table view (inventory or grocery)
         */
        QuantityCell(TableView<InventoryItem> table) {
            this.parentTable = table;
            String style = "-fx-background-color: transparent; -fx-border-color: transparent; -fx-padding: 0; -fx-font-size: 16px;";
            plus .setStyle(style);
            minus.setStyle(style);
            del  .setStyle(style);

            plus .setFocusTraversable(false);
            minus.setFocusTraversable(false);
            del  .setFocusTraversable(false);

            plus .setOnAction(e -> adjust(1));
            minus.setOnAction(e -> adjust(-1));
            del  .setOnAction(e -> {
                InventoryItem item = getItem();
                if (item == null) return;

                if (parentTable == inventoryTable) {
                    WhatsInMyFridgeApp.inventory.removeIngredient(item.getName());
                } else if (parentTable == groceryTable) {
                    WhatsInMyFridgeApp.inventory.removeGrocery(item.getName());
                }

                parentTable.getItems().remove(item);
            });
        }

        private void adjust(int delta) {
            InventoryItem item = getItem();
            Ingredient ingredient = new Ingredient(item.getName(),
                    Double.parseDouble(item.getAmount()),
                    item.getMeasurement());
            if (item == null) return;
            String t = item.getAmount();
            Double v = Double.parseDouble(t);
            item.setAmount(String.valueOf(Math.max(0, v + delta)));

            if (parentTable == inventoryTable) {
                WhatsInMyFridgeApp.inventory.editIngredient(ingredient);
            } else if (parentTable == groceryTable) {
                WhatsInMyFridgeApp.inventory.editGrocery(ingredient);
            }
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
