package com.example.nam.recipeshopper;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.List;

import static com.example.nam.recipeshopper.BaseActivity.APP_DATA;
import static com.example.nam.recipeshopper.BaseActivity.RECIPE_LIST_TRANSFER;
import static com.example.nam.recipeshopper.BaseActivity.SHOPPING_TRANSFER;

public class BaseFragment extends Fragment {
    private FileInputStream mFileInputStream;
    private ObjectInputStream mObjectInputStream;
    private List<RecipeEntry> savedRecipeEntryList;
    private List<Ingredient> savedShoppingList;
    private Bundle bundle = new Bundle();

    public boolean load() {
        boolean success = false;
        try {
            mFileInputStream = new FileInputStream(getActivity().getFilesDir() + APP_DATA);
            mObjectInputStream = new ObjectInputStream(mFileInputStream);
            savedRecipeEntryList = (List<RecipeEntry>) mObjectInputStream.readObject();
            savedShoppingList = (List<Ingredient>) mObjectInputStream.readObject();
            bundle.putSerializable(RECIPE_LIST_TRANSFER, (Serializable) savedRecipeEntryList);
            bundle.putSerializable(SHOPPING_TRANSFER, (Serializable) savedShoppingList);
            mObjectInputStream.close();
            mFileInputStream.close();
            success = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ObjectStreamException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {        // ObjectInputStream Exception
            e.printStackTrace();
        } catch (IOException e) {                   // ObjectInputStream Exception
            e.printStackTrace();
        }

        return success;
    }

    public List<RecipeEntry> getSavedRecipeEntryList() {
        return savedRecipeEntryList;
    }

    public void setSavedRecipeEntryList(List<RecipeEntry> savedRecipeEntryList) {
        this.savedRecipeEntryList = savedRecipeEntryList;
    }

    public List<Ingredient> getSavedShoppingList() {
        return savedShoppingList;
    }

    public void setSavedShoppingList(List<Ingredient> savedShoppingList) {
        this.savedShoppingList = savedShoppingList;
    }
}
