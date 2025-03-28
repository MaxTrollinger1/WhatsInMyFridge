import java.util.List;

public class Recipe {
    private String name;
    private String description;
    private List<Ingredient> ingredients;

    public Recipe(String name, String description, List<Ingredient> ingredients) {
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
