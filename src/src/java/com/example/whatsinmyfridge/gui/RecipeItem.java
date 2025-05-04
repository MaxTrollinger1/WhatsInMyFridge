package com.example.whatsinmyfridge.gui;

import com.example.whatsinmyfridge.model.Recipe;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

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

    /** Full constructor, pass in your own data */
    public RecipeItem(Recipe recipe) {
        this.name.set(recipe.getName());
        this.ingredients.setAll((InventoryItem)FXCollections.observableArrayList(recipe.getIngredients()));
        this.description.set(recipe.getDescription());
        this.instructions.set(recipe.getInstructions());
    }

    public StringProperty nameProperty()         { return name;        }
    public BooleanProperty flaggedProperty()     { return flagged;     }
    public ObservableList<InventoryItem> getIngredients() { return ingredients; }
    public StringProperty descriptionProperty()  { return description; }
    public StringProperty instructionsProperty() { return instructions;}

    public String getName()        { return name.get();         }
    public boolean isFlagged()     { return flagged.get();      }
    public String getDescription(){ return description.get();  }
    public String getInstructions(){ return instructions.get(); }

    public void setName(String n)           { name.set(n);           }
    public void setFlagged(boolean f)       { flagged.set(f);        }
    public void setDescription(String d)    { description.set(d);    }
    public void setInstructions(String i)   { instructions.set(i);   }

    @Override
    public String toString() { return getName(); }
}
