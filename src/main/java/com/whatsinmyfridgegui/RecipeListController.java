package com.example.whatsinmyfridgegui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;

public class RecipeListController {
    @FXML private TextField searchField;
    @FXML private Button    filterButton;
    @FXML private ListView<RecipeItem> recipeListView;
    @FXML private Button    addRecipeButton;

    // Shared list of recipes
    private static final ObservableList<RecipeItem> recipes =
            FXCollections.observableArrayList(
                    new RecipeItem("Butter Chicken"),
                    new RecipeItem("Pico De Gallo"),
                    new RecipeItem("Salisbury Steak")
            );

    @FXML
    public void initialize() {
        // 1) Populate the ListView
        recipeListView.setItems(recipes);

        // 2) Custom cell: show name + spacer + flag icon
        recipeListView.setCellFactory(lv -> new ListCell<>() {
            private final Label  nameLabel = new Label();
            private final Region spacer    = new Region();
            private final Button flagBtn   = new Button("⚑");
            private final HBox   box        = new HBox(10, nameLabel, spacer, flagBtn);

            {
                HBox.setHgrow(spacer, Priority.ALWAYS);
                flagBtn.getStyleClass().add("flag-icon");
                flagBtn.setOnAction(e -> {
                    RecipeItem item = getItem();
                    if (item != null) {
                        boolean nowFlagged = !item.isFlagged();
                        item.setFlagged(nowFlagged);
                        ObservableList<String> styles = flagBtn.getStyleClass();
                        if (nowFlagged) {
                            if (!styles.contains("flagged")) styles.add("flagged");
                        } else {
                            styles.remove("flagged");
                        }
                    }
                });
            }

            @Override
            protected void updateItem(RecipeItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    nameLabel.setText(item.getName());
                    ObservableList<String> styles = flagBtn.getStyleClass();
                    styles.remove("flagged");
                    if (item.isFlagged()) styles.add("flagged");
                    setGraphic(box);
                }
            }
        });

        // 3) Double‐click → detail page
        recipeListView.setOnMouseClicked(evt -> {
            if (evt.getClickCount() == 2) {
                RecipeItem sel = recipeListView.getSelectionModel().getSelectedItem();
                if (sel != null) {
                    try {
                        FXMLLoader loader = new FXMLLoader(
                                getClass().getResource("RecipeDetail.fxml")
                        );
                        Parent detailRoot = loader.load();
                        RecipeDetailController dc = loader.getController();
                        dc.setRecipe(sel);
                        Stage stage = (Stage) recipeListView.getScene().getWindow();
                        stage.getScene().setRoot(detailRoot);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        // 4) Wire buttons
        addRecipeButton.setOnAction(this::onAddRecipe);
        filterButton   .setOnAction(this::onFilter);
    }

    /** Filter recipes by the searchField text */
    @FXML
    private void onFilter(ActionEvent evt) {
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

    /** Show the Add‐Recipe page */
    @FXML
    private void onAddRecipe(ActionEvent evt) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("RecipeAdd.fxml")
            );
            Parent addRoot = loader.load();
            RecipeAddController ac = loader.getController();
            ac.setRecipeList(recipes);
            Stage st = (Stage)((Node)evt.getSource()).getScene().getWindow();
            st.getScene().setRoot(addRoot);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /** Back to the Home screen */
    @FXML
    private void onBack(ActionEvent evt) throws IOException {
        Parent home = FXMLLoader.load(getClass().getResource("Homescreen.fxml"));
        Stage stage = (Stage)((Node)evt.getSource()).getScene().getWindow();
        stage.getScene().setRoot(home);
    }
}
