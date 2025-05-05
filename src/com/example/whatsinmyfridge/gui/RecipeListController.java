package com.example.whatsinmyfridge.gui;

import com.example.whatsinmyfridge.WhatsInMyFridgeApp;
import com.example.whatsinmyfridge.model.Ingredient;
import com.example.whatsinmyfridge.model.Recipe;
import com.example.whatsinmyfridge.model.RecipeChecker;
import com.example.whatsinmyfridge.model.UnitConverter;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class RecipeListController {
    @FXML private TextField searchField;
    @FXML private Button    searchButton;
    @FXML private Button    filterButton;
    @FXML private ListView<RecipeItem> recipeListView;
    @FXML private Button    addRecipeButton;
    @FXML private Button    backButton;

    private static ObservableList<RecipeItem> recipes = FXCollections.observableArrayList(
    );

    boolean filtering = false;
    RecipeChecker checker;
    UnitConverter converter;

    @FXML
    public void initialize() {
        recipeListView.setItems(recipes);
        // ensure scrolling
        recipeListView.setFocusTraversable(true);

        converter = new UnitConverter();
        checker = new RecipeChecker(converter);

        this.recipes.addListener((ListChangeListener<RecipeItem>) change -> {
            while (change.next()) {
                OnRecipeListUpdated();
                break;
            }
        });

        recipeListView.setCellFactory(lv -> new ListCell<>() {
            private final Label nameLabel = new Label();
            private final Region spacer    = new Region();
            private final ToggleButton delBtn = new ToggleButton("🗑");
            private final HBox hbox;

            {
                // grow label and spacer, style text larger
                nameLabel.setStyle("-fx-font-size: 14px;");
                HBox.setHgrow(spacer, Priority.ALWAYS);
                delBtn.setStyle("-fx-font-size:16px; -fx-background-color: transparent;");
                delBtn.setOnAction(e -> {
                    if(WhatsInMyFridgeApp.inventory.removeRecipe(getItem().getName()))
                    {
                        recipeListView.getItems().remove(getItem());
                    }
                });
                hbox = new HBox(10, nameLabel, spacer, delBtn);
                hbox.setStyle("-fx-padding: 5px;");
            }

            @Override
            protected void updateItem(RecipeItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    nameLabel.setText(item.getName());
                    setGraphic(hbox);
                }
            }
        });

        // double-click to open detail
        recipeListView.setOnMouseClicked(evt -> {
            if (evt.getClickCount() == 2) {
                RecipeItem selected = recipeListView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/whatsinmyfridgegui/RecipeDetail.fxml"));
                        Parent detailRoot = loader.load();
                        RecipeDetailController rc = loader.getController();
                        rc.setRecipe(selected);
                        Stage st = (Stage) recipeListView.getScene().getWindow();
                        st.getScene().setRoot(detailRoot);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        searchButton.setOnAction(this::onSearch);
        filterButton.setOnAction(e -> { onFilter(e); });
        addRecipeButton.setOnAction(this::onAddRecipe);
        backButton.setOnAction(e -> {
            try { onBack(e); }
            catch (IOException ex) { ex.printStackTrace(); }
        });

        filterButton.setText("✗");
    }

    public void SetLists()
    {
        ArrayList<RecipeItem> recipeItems = new ArrayList<>();
        ArrayList<Recipe> recs;
        try
        {
            recs = WhatsInMyFridgeApp.inventory.getRecipes();
        } catch (Exception e) { // Catch to ensure recipes is correctly pulled
            return;
        }

        for (Recipe r : recs)
        {
            recipeItems.add(new RecipeItem(r));
        }

        this.recipes = FXCollections.observableArrayList(recipeItems);
        recipeListView.setItems(this.recipes);
    }

    @FXML
    private void onSearch(ActionEvent evt) {
        String text = searchField.getText().trim().toLowerCase();
        if (text.isEmpty()) {
            recipeListView.setItems(recipes);
        } else {
            ObservableList<RecipeItem> filtered = FXCollections.observableArrayList();
            for (RecipeItem r : recipes) {
                if (r.getName().toLowerCase().contains(text)) {
                    filtered.add(r);
                }
            }
            recipeListView.setItems(filtered);
        }
    }

    @FXML
    private void onFilter(ActionEvent evt) {

        if(filtering)
        {
            filtering = false;
            recipeListView.setItems(recipes);
            filterButton.setText("✗");
        }
        else
        {
            filtering = true;
            ObservableList<RecipeItem> filtered = FXCollections.observableArrayList();
            ObservableList<RecipeItem> filtered2 = FXCollections.observableArrayList();

            ArrayList<Ingredient> ings = new ArrayList<>();

            for (RecipeItem r : recipes) {
                ings.clear();
                for (InventoryItem i : r.getIngredients())
                {
                    ings.add(new Ingredient(i.getName(), Double.parseDouble(i.getAmount()), i.getMeasurement()));
                }

                if(checker.canMakeRecipe(new Recipe(r.getName(), r.getDescription(), ings, r.getInstructions())))
                {
                    filtered.add(r);
                }
            }
            recipeListView.setItems(filtered);
            filterButton.setText("✓");
        }
    }

    @FXML
    private void onAddRecipe(ActionEvent evt) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/whatsinmyfridgegui/RecipeAdd.fxml"));
            Parent addRoot = loader.load();
            RecipeAddController ac = loader.getController();
            ac.setRecipeList(recipes);
            Stage st = (Stage)((Node)evt.getSource()).getScene().getWindow();
            st.getScene().setRoot(addRoot);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void onBack(ActionEvent evt) throws IOException {
        Parent home = FXMLLoader.load(getClass().getResource("/com/whatsinmyfridgegui/HomeScreen.fxml"));
        Stage st = (Stage)((Node)evt.getSource()).getScene().getWindow();
        st.getScene().setRoot(home);
    }

    public void OnRecipeListUpdated()
    {
        System.out.println("onRecipeListUpdated");
        ArrayList<RecipeItem> recipeItems = new ArrayList<>(recipes);
        ArrayList<Recipe> recipes1 = new ArrayList<>();

        for (RecipeItem r : recipeItems)
        {
            ArrayList<Ingredient> ingredients = new ArrayList<>();
            for (InventoryItem i : r.getIngredients())
            {
                ingredients.add(new Ingredient(i.getName(), Double.parseDouble(i.getAmount()), i.getMeasurement()));
            }
            recipes1.add(new Recipe(r.getName(), r.getDescription(), ingredients, r.getInstructions()));
        }
        WhatsInMyFridgeApp.inventory.updateRecipeList(recipes1);
    }

}
