package com.example.nam.recipeshopper;

import android.util.Log;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Nam on 3/22/2018.
 */

enum Unit{TEASPOON, TABLESPOON, CUP, QUART, GALLON, LITER, MILLILITER, OUNCE, POUND, GRAM, KILOGRAM}

public class Ingredient implements Serializable{
    private static final String TAG = "Ingredient";
    private static final long serialVersionUID = 1L;
    final String Digits = "(\\p{Digit}+)";

    // TODO: Find better way to parse measurements to appropriate units
    final static List<Unit> Defaults = Arrays.asList(Unit.CUP, Unit.POUND);
    final static List<String> Teaspoons = Arrays.asList("teaspoon", "teaspoons", "tsp", "tsps");
    final static List<String> Tablespoons = Arrays.asList("tablespoon", "tablespoons", "tbsp", "tbsps");
    final static List<String> Cups = Arrays.asList("cup", "cups", "c", "c.");
    final static List<String> Quarts = Arrays.asList("quart", "quarts");
    final static List<String> Gallons = Arrays.asList("gallon", "gallons");
    final static List<String> Liters = Arrays.asList("liter", "liters", "l");
    final static List<String> Milliliters = Arrays.asList("milliliter", "milliliters", "ml");
    final static List<String> Ounces = Arrays.asList("ounce", "ounces", "oz", "ozs");
    final static List<String> Pounds = Arrays.asList("pound", "pounds", "lbs");
    final static List<String> Grams = Arrays.asList("gram", "grams", "g");
    final static List<String> Kilograms = Arrays.asList("kilogram", "kilograms", "kg");
    final static List<String> Units = Arrays.asList("teaspoon", "teaspoons", "tsp", "tsps",
            "tablespoon", "tablespoons", "tbsp", "tbsps", "cup", "cups", "c", "quart", "quarts",
            "gallon", "gallons", "liter", "liters", "l", "ml", "milliliter", "milliliters",
            "ounce", "ounces", "oz", "ozs", "pound", "pounds", "lbs", "gram", "grams", "g",
            "kilogram", "kilograms", "kg");

    double mMeasurement = 0;
    String mName = "";
    Unit mUnit = null;
    private RecipeEntry mOwner = null;
    private boolean mChecked = true;

    public RecipeEntry getOwner() {
        return mOwner;
    }

    public void setOwner(RecipeEntry owner) {
        mOwner = owner;
    }



    @Override
    public boolean equals(Object obj) {
        return (this.getName().equalsIgnoreCase(((Ingredient) obj).getName()));
    }

    public void convert() {
        // Default units are pound for mass and cup for volume.
        if(this.getUnit() != null) {
            switch (this.getUnit()) {
                case TEASPOON:
                    defaultConvert(Unit.CUP, .0208333, this);
                    break;
                case TABLESPOON:
                    defaultConvert(Unit.CUP, 0.0625, this);
                    break;
                case CUP:           //Set when doing metric
                    break;
                case QUART:
                    defaultConvert(Unit.CUP, 4.0, this);
                    break;
                case GALLON:
                    defaultConvert(Unit.CUP, 16.0, this);
                    break;
                case LITER:
                    defaultConvert(Unit.CUP, 4.22675, this);
                    break;
                case MILLILITER:
                    defaultConvert(Unit.CUP, 0.00422675, this);
                    break;

                case OUNCE:
                    defaultConvert(Unit.POUND, 0.0625, this);
                    break;
                case POUND:         //Set when doing metric
                    break;
                case GRAM:
                    defaultConvert(Unit.POUND, 0.00220462, this);
                    break;
                case KILOGRAM:
                    defaultConvert(Unit.CUP, 2.20462, this);
                    break;
            }
        }
    }

    private void defaultConvert(Unit unit, double factor, Ingredient ingredient) {
        ingredient.setUnit(unit);
        ingredient.setMeasurement(ingredient.getMeasurement() * factor);
        return;
    }

    public void add(Ingredient ingredient) {
        if(this.getUnit() == ingredient.getUnit()) {
            this.setMeasurement(this.getMeasurement() + ingredient.getMeasurement());
        } else {
            ingredient.convert();
            this.setMeasurement(this.getMeasurement() + ingredient.getMeasurement());
        }
    }

    @Override
    public int hashCode() {
        return mName.hashCode();
    }

    public Ingredient(String unparsed, RecipeEntry owner){
        mOwner = owner;
        mChecked = true;
        String delims = "[ ]+";
        String[] tokens = unparsed.split(delims);
        double runningTotal = 0;

        for(String token : tokens) {
            if (Pattern.matches(Digits, token)) {
                runningTotal += Double.parseDouble(token);
                continue;
            }

            // TODO: implement checks with regex instead
            String delim = "/";
            String[] fractions = token.split(delim);
            if(fractions.length > 1 && Pattern.matches(Digits, fractions[0]) && Pattern.matches(Digits, fractions[1])) {
                runningTotal += Double.valueOf(fractions[0]) / Double.valueOf(fractions[1]);
                continue;
            }

            mMeasurement = runningTotal;

//            if(token.matches("(?i)teaspoon|teaspoons|tsp|tsps|tablespoon|tablespoons|tbsp|tbsps|" +
//                    "cup|cups|c|quart|quarts|gallon|gallons|liter|liters|l|ml|milliliter|milliliters|" +
//                    "ounce|ounces|oz|ozs|pound|pounds|lbs|gram|grams|g|kilogram|kilograms|kg")) {
//                switch(token) {
//                    case "teaspoon" || "teaspoons"
//                }
//            }

            // TODO: Currently using locale dependent solution
            if(Units.contains(token.toLowerCase())) {
                mUnit = parseUnit(token.toLowerCase());
            }

            else {
                mName += (token + " ");
            }
        }
        Log.d(TAG, "Ingredient: " + mMeasurement + " " + mUnit + " " + mName + " " + mChecked);
    }

    public Ingredient(String unparsed) {
        this(unparsed, null);
    }

    public Ingredient (Ingredient ingredient) {
        this(ingredient.mName, ingredient.mOwner);
        this.setMeasurement(ingredient.getMeasurement());
        this.setUnit(ingredient.getUnit());
        this.setChecked(ingredient.getChecked());
    }

    public double getMeasurement() {
        return mMeasurement;
    }

    public void setMeasurement(double measurement) {
        mMeasurement = measurement;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Unit getUnit() {
        return mUnit;
    }

    public void setUnit(Unit unit) {
        mUnit = unit;
    }

    public boolean getChecked() {
        return mChecked;
    }

    public void setChecked(boolean isChecked) {
        mChecked = isChecked;
    }

    public boolean toggleChecked() {
        mChecked = !mChecked;
        return mChecked;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "Measurement=" + mMeasurement +
                ", Name='" + mName + '\'' +
                ", Unit='" + mUnit + '\'' +
                '}';
    }

    private Unit parseUnit(String token) {
        if(Teaspoons.contains(token)){
            return Unit.TEASPOON;
        } else if(Tablespoons.contains(token)) {
            return Unit.TABLESPOON;
        } else if(Cups.contains(token)) {
            return Unit.CUP;
        } else if(Ounces.contains(token)) {
            return Unit.OUNCE;
        } else if(Pounds.contains(token)) {
            return Unit.POUND;
        } else if(Milliliters.contains(token)) {
            return Unit.MILLILITER;
        } else if(Grams.contains(token)) {
            return Unit.GRAM;
        } else if(Quarts.contains(token)) {
            return Unit.QUART;
        } else if(Gallons.contains(token)) {
            return Unit.GALLON;
        } else if(Liters.contains(token)) {
            return Unit.LITER;
        } else if(Kilograms.contains(token)) {
            return Unit.KILOGRAM;
        } else {
            return null;
        }
    }
}
