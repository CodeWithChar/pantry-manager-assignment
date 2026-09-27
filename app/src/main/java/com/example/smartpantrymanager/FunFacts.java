package com.example.smartpantrymanager;

import java.util.HashMap;
import java.util.Map;

//this is a list of hand-picked did you know facts for ingredients
//that show up often across the recipes//
public class FunFacts {

    private static final Map<String, String> FACTS = new HashMap<>();

    static {
        FACTS.put("onion", "Did you know: chilling an onion in the fridge before cutting it reduces the gas that makes your eyes water.");
        FACTS.put("garlic", "Did you know: garlic releases more of its flavor-boosting compounds the finer it's chopped or crushed.");
        FACTS.put("egg", "Did you know: a fresh egg sinks in water, while an older egg floats due to air building up inside the shell over time.");
        FACTS.put("rice", "Did you know: rinsing rice before cooking removes excess surface starch, giving you fluffier, less sticky grains.");
        FACTS.put("carrot", "Did you know: carrots were originally purple - the familiar orange variety was bred in the Netherlands centuries ago.");
        FACTS.put("tomato", "Did you know: tomatoes are botanically a fruit, but were legally classified as a vegetable in the US in 1893.");
    }

    //this returns a fun fact for the recipe that was given
    //if there is no fact it will return as null
    public static String getFact(String ingredientName) {
        return FACTS.get(ingredientName.toLowerCase().trim());
    }
}