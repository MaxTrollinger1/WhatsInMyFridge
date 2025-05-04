package com.example.whatsinmyfridge.gui;

import com.example.whatsinmyfridge.storage.IDataPersistence;
import com.example.whatsinmyfridge.storage.data.Data;
import com.example.whatsinmyfridge.storage.data.PantryWrapper;
import com.example.whatsinmyfridge.storage.data.RecipeWrapper;
import javafx.collections.FXCollections;
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

public class RecipeListController implements IDataPersistence {
    @FXML private TextField searchField;
    @FXML private Button    searchButton;
    @FXML private Button    filterButton;
    @FXML private ListView<RecipeItem> recipeListView;
    @FXML private Button    addRecipeButton;
    @FXML private Button    backButton;

    private static ObservableList<RecipeItem> recipes = FXCollections.observableArrayList(
    );

    @FXML
    public void initialize() {
        recipeListView.setItems(recipes);
        // ensure scrolling
        recipeListView.setFocusTraversable(true);

        recipeListView.setCellFactory(lv -> new ListCell<>() {
            private final Label nameLabel = new Label();
            private final Region spacer    = new Region();
            private final ToggleButton flagBtn = new ToggleButton("⚑");
            private final HBox hbox;

            {
                // grow label and spacer, style text larger
                nameLabel.setStyle("-fx-font-size: 14px;");
                HBox.setHgrow(spacer, Priority.ALWAYS);
                flagBtn.setStyle("-fx-font-size:16px; -fx-background-color: transparent;");
                flagBtn.setOnAction(e -> {
                    if (flagBtn.isSelected()) {
                        flagBtn.setStyle("-fx-font-size:16px; -fx-text-fill: gold; -fx-background-color: transparent;");
                    } else {
                        flagBtn.setStyle("-fx-font-size:16px; -fx-text-fill: black; -fx-background-color: transparent;");
                    }
                });
                hbox = new HBox(10, nameLabel, spacer, flagBtn);
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
        filterButton.setOnAction(e -> { /* no-op */ });
        addRecipeButton.setOnAction(this::onAddRecipe);
        backButton.setOnAction(e -> {
            try { onBack(e); }
            catch (IOException ex) { ex.printStackTrace(); }
        });
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
        // stub: filter logic
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

    @Override
    public void loadData(Data data) {
        if (data instanceof RecipeWrapper recipeWrapper) {
            System.out.println("Recipe Information: " + recipeWrapper.toString());
            recipes = FXCollections.observableArrayList(recipeWrapper.recipes);
        }
    }

    @Override
    public void saveData(Data data) {
        if (data instanceof RecipeWrapper recipeWrapper) {
            System.out.println("Saving PantryWrapper data...");
            recipeWrapper.recipes = new ArrayList<>(recipes);
        }
    }
}
