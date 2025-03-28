import java.util.*;

public class Fridge {
    private static Fridge instance;
    private final List<Ingredient> ingredients = new ArrayList<>();

    private Fridge() {}

    public static Fridge getInstance() {
        if (instance == null) {
            instance = new Fridge();
        }
        return instance;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    public void removeIngredient(Ingredient ingredient) {
        ingredients.remove(ingredient);
    }

    public Optional<Ingredient> findMatchingIngredient(String name, String unit) { // unit should be optional
        return ingredients.stream()
                .filter(i -> i.getName().equalsIgnoreCase(name) && i.getUnit().equalsIgnoreCase(unit))
                .findFirst();
    }

    public List<Ingredient> getMatchingIngredients(String name) {
        List<Ingredient> matches = new ArrayList<>();
        for (Ingredient i : ingredients) {
            if (i.getName().equalsIgnoreCase(name)) {
                matches.add(i);
            }
        }
        return matches;
    }
}