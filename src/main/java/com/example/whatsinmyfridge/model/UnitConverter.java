import java.util.*;

public class UnitConverter {
    // Map: Ingredient Name -> Conversion Map: (FromUnit -> ToUnit -> Ratio)
    private final Map<String, Map<String, Map<String, Double>>> conversions = new HashMap<>();
    private final List<UnitConversionSubscriber> subscribers = new ArrayList<>();

    public void addConversion(String ingredient, String fromUnit, String toUnit, double ratio) {
        conversions
                // at some point after first add, could addConversion(same, same-1, same+1, 1/same)
                .computeIfAbsent(ingredient.toLowerCase(), k -> new HashMap<>())
                .computeIfAbsent(fromUnit.toLowerCase(), k -> new HashMap<>())
                .put(toUnit.toLowerCase(), ratio);

        notifySubscribers(ingredient);
    }

    public Optional<Double> convert(String ingredient, double amount, String fromUnit, String toUnit) {
        ingredient = ingredient.toLowerCase();
        fromUnit = fromUnit.toLowerCase();
        toUnit = toUnit.toLowerCase();

        if (!conversions.containsKey(ingredient)) return Optional.empty();
        Map<String, Map<String, Double>> unitMap = conversions.get(ingredient);
        if (!unitMap.containsKey(fromUnit)) return Optional.empty();
        Map<String, Double> targets = unitMap.get(fromUnit);
        if (!targets.containsKey(toUnit)) return Optional.empty();

        double ratio = targets.get(toUnit);
        return Optional.of(amount * ratio);
    }

    public void subscribe(UnitConversionSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    private void notifySubscribers(String ingredientName) {
        for (UnitConversionSubscriber subscriber : subscribers) {
            subscriber.onConversionUpdated(ingredientName);
        }
    }
}