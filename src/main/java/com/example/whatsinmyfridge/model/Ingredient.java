//import java.util.List;

public class Ingredient {
    private String name;
    private double amount;
    private String unit;

    public Ingredient(String name, double amount, String unit){
        this.name = name;
        this.amount = amount;
        this.unit = unit;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

}


