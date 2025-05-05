package com.example.whatsinmyfridge.gui;

import com.example.whatsinmyfridge.model.Recipe;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * View model wrapping a Recipe for display in the UI.
 * <p>
 * Exposes observable properties for use in JavaFX controls,
 * including name, flagged state, ingredients, description, and instructions.
 */
public class RecipeItem {
    private final StringProperty name         = new SimpleStringProperty();
    private final BooleanProperty flagged     = new SimpleBooleanProperty(false);
    private final ObservableList<InventoryItem> ingredients
            = FXCollections.observableArrayList();
    private final StringProperty description  = new SimpleStringProperty();
    private final StringProperty instructions = new SimpleStringProperty();

    /** Constructor for an EMPTY recipe (used on “Add”) */
    public RecipeItem(String name) {
        this(new Recipe(name));
    }

    /**
     * Constructs a RecipeItem from an existing Recipe model.
     * Populates all observable properties from the Recipe data.
     * @param recipe the Recipe to wrap
     */
    public RecipeItem(Recipe recipe) {
        this.name.set(recipe.getName());
        for (var ingredient : recipe.getIngredients()) {
            InventoryItem item = new InventoryItem(
                    ingredient.getName(),
                    String.valueOf(ingredient.getAmount()),
                    ingredient.getUnit()
            );
            this.ingredients.add(item);
        }
        this.description.set(recipe.getDescription());
        this.instructions.set(recipe.getInstructions());
    }

    /** @return observable name property */
    public StringProperty nameProperty() { return name; }
    /** @return observable flagged property */
    public BooleanProperty flaggedProperty() { return flagged; }
    /** @return observable list of ingredients */
    public ObservableList<InventoryItem> getIngredients() { return ingredients; }
    /** @return observable description property */
    public StringProperty descriptionProperty() { return description; }
    /** @return observable instructions property */
    public StringProperty instructionsProperty() { return instructions; }

    /** @return recipe name */
    public String getName() { return name.get(); }
    /** @return flagged state */
    public boolean isFlagged() { return flagged.get(); }
    /** @return recipe description */
    public String getDescription() { return description.get(); }
    /** @return recipe instructions */
    public String getInstructions() { return instructions.get(); }

    /** @param n sets the recipe name */
    public void setName(String n) { name.set(n); }
    /** @param f sets the flagged state */
    public void setFlagged(boolean f) { flagged.set(f); }
    /** @param d sets the description */
    public void setDescription(String d) { description.set(d); }
    /** @param i sets the instructions */
    public void setInstructions(String i) { instructions.set(i); }

    /**
     * Returns the recipe name for display in lists.
     * @return recipe name string
     */
    @Override
    public String toString() { return getName(); }
}
