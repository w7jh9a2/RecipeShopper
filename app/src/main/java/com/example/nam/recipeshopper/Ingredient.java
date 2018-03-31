package com.example.nam.recipeshopper;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Created by Nam on 3/22/2018.
 */

enum Unit{TEASPOON, TABLESPOON, CUP, QUART, GALLON, LITER, MILLILITER, OUNCE, POUND, GRAM, KILOGRAM}

public class Ingredient {
    private static final String TAG = "Ingredient";
    final String Digits = "(\\p{Digit}+)";

    // TODO: Find better way to parse measurements to appropriate units
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

    public Ingredient(String unparsed){
        double whole = 0;
        String delims = "[ ]+";
        String[] tokens = unparsed.split(delims);
        double runningTotal = 0;

        for(String token : tokens) {
            if (Pattern.matches(Digits, token)) {
                runningTotal += Double.parseDouble(token);
                continue;
            }
//            try {
//                whole = Double.parseDouble(token);
//                continue;
//            } catch (NumberFormatException e) {
//                Log.e(TAG, "Token does not match number format");
//            }

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
        Log.d(TAG, "Ingredient: " + mMeasurement + " " + mUnit + " " + mName);
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
