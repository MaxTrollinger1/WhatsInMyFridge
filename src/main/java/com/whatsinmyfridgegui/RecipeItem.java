package com.example.whatsinmyfridgegui;

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

    /** Constructor for a brand-new (empty) recipe */
    public RecipeItem(String name) {
        this(name, List.of(), "", "");
    }

    /** Full constructor */
    public RecipeItem(String name,
                      List<InventoryItem> ingredients,
                      String description,
                      String instructions)
    {
        this.name.set(name);
        this.ingredients.addAll(ingredients);
        this.description.set(description);
        this.instructions.set(instructions);
    }

    public StringProperty nameProperty()        { return name;        }
    public BooleanProperty flaggedProperty()   { return flagged;     }
    public ObservableList<InventoryItem> getIngredients() { return ingredients; }
    public StringProperty descriptionProperty(){ return description; }
    public StringProperty instructionsProperty(){ return instructions; }

    public String getName()       { return name.get();        }
    public boolean isFlagged()    { return flagged.get();     }
    public String getDescription(){ return description.get(); }
    public String getInstructions(){ return instructions.get(); }

    public void setName(String newName)           { name.set(newName);             }
    public void setFlagged(boolean f)             { flagged.set(f);                }
    public void setDescription(String desc)       { description.set(desc);         }
    public void setInstructions(String instr)     { instructions.set(instr);       }

    @Override
    public String toString() { return getName(); }
}
