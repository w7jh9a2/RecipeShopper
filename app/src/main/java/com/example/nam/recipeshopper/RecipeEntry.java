package com.example.nam.recipeshopper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nam on 3/5/2018.
 */

public class RecipeEntry implements Serializable {

    private static final long serialVersionUID = 1L;
    private String mTitle;
    private String mLink;
    private String mImageURL;
    private String mYield;
    private String mTotalTime;
    private String mActiveTime;
    private List<Ingredient> mIngredients;
    private List<String> mInstructions;

    public RecipeEntry() {
        mTitle = null;
        mLink = null;
        mImageURL = null;
        mYield = null;
        mTotalTime = null;
        mIngredients = new ArrayList<>();
        mInstructions = new ArrayList<>();
    };

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        this.mLink = link;
    }

    public String getImageURL() {
        return mImageURL;
    }

    public void setImageURL(String imageURL) {
        this.mImageURL = imageURL;
    }

    public String getYield() {
        return mYield;
    }

    public void setYield(String yield) {
        this.mYield = yield;
    }

    public String getTotalTime() {
        return mTotalTime;
    }

    public void setTotalTime(String totalTime) {
        this.mTotalTime = totalTime;
    }

    public List<Ingredient> getIngredients() {
        return mIngredients;
    }

    public void addIngredients(Ingredient ingredients) {
        this.mIngredients.add(ingredients);
    }

    public List<String> getInstructions() {
        return mInstructions;
    }

    public void addInstructions(String instructions) {
        this.mInstructions.add(instructions);
    }

    public String getActiveTime() {
        return mActiveTime;
    }

    public void setActiveTime(String activeTime) {
        mActiveTime = activeTime;
    }
}
