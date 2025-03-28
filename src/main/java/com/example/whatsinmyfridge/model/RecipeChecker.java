import java.util.*;

public class RecipeChecker {
    private final UnitConverter converter;

    public RecipeChecker(UnitConverter converter) {
        this.converter = converter;
    }

    public boolean canMakeRecipe(Recipe recipe) {
        Fridge fridge = Fridge.getInstance();

        for (Ingredient needed : recipe.getIngredients()) {
            double totalAvailable = 0.0;

            for (Ingredient owned : fridge.getMatchingIngredients(needed.getName())) {
                if (owned.getUnit().equalsIgnoreCase(needed.getUnit())) {
                    totalAvailable += owned.getAmount();
                } else {
                    Optional<Double> converted = converter.convert(
                            needed.getName(), owned.getAmount(), owned.getUnit(), needed.getUnit()
                    );
                    if (converted.isPresent()) {
                        totalAvailable += converted.get();
                    }
                }
            }

            if (totalAvailable < needed.getAmount()) {
                return false;
            }
        }

        return true;
    }

    public List<Recipe> getMakeableRecipes(List<Recipe> allRecipes) {
        List<Recipe> makeable = new ArrayList<>();
        for (Recipe recipe : allRecipes) {
            if (canMakeRecipe(recipe)) {
                makeable.add(recipe);
            }
        }
        return makeable;
    }
}
