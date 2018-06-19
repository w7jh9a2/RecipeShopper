package com.example.nam.recipeshopper;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.util.List;

import static com.example.nam.recipeshopper.BaseActivity.APP_DATA;

public class DataShareViewModel extends AndroidViewModel {
    private FileInputStream mFileInputStream;
    private ObjectInputStream mObjectInputStream;
    private List<RecipeEntry> savedRecipeEntryList;
    private List<Ingredient> savedShoppingList;

    private MutableLiveData<List<RecipeEntry>> mUpdatedRecipeEntryList = new MutableLiveData<>();
    private MutableLiveData<List<Ingredient>> mUpdatedShoppingList = new MutableLiveData<>();

    public void setUpdatedRecipeEntryList(List<RecipeEntry> updatedList) {
        mUpdatedRecipeEntryList.setValue(updatedList);
    }

    public void setUpdatedShoppingList(List<Ingredient> updatedList) {
        mUpdatedShoppingList.setValue(updatedList);
    }

    public LiveData<List<RecipeEntry>> getUpdatedRecipeEntryList() {
        if (mUpdatedRecipeEntryList == null) {
            mUpdatedRecipeEntryList = new MutableLiveData<List<RecipeEntry>>();
            loadRecipeEntryList();
        }
        return mUpdatedRecipeEntryList;
    }

    public LiveData<List<Ingredient>> getUpdatedShoppingList() {
        if (mUpdatedShoppingList == null) {
            mUpdatedShoppingList = new MutableLiveData<List<Ingredient>>();
            loadShoppingList();
        }
        return mUpdatedShoppingList;
    }

    public List<RecipeEntry> getSavedRecipeEntryList() {
        return mUpdatedRecipeEntryList.getValue();
    }

    public List<Ingredient> getSavedShoppingList() {
        return mUpdatedShoppingList.getValue();
    }

    private void loadRecipeEntryList() {
        try {
            mFileInputStream = new FileInputStream(getApplication().getFilesDir() + APP_DATA);
            mObjectInputStream = new ObjectInputStream(mFileInputStream);
            savedRecipeEntryList = (List<RecipeEntry>) mObjectInputStream.readObject();
            savedShoppingList = (List<Ingredient>) mObjectInputStream.readObject();
            mObjectInputStream.close();
            mFileInputStream.close();

            mUpdatedRecipeEntryList.setValue(savedRecipeEntryList);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ObjectStreamException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {        // ObjectInputStream Exception
            e.printStackTrace();
        } catch (IOException e) {                   // ObjectInputStream Exception
            e.printStackTrace();
        }
    }

    private void loadShoppingList() {
        try {
            mFileInputStream = new FileInputStream(getApplication().getFilesDir() + APP_DATA);
            mObjectInputStream = new ObjectInputStream(mFileInputStream);
            savedRecipeEntryList = (List<RecipeEntry>) mObjectInputStream.readObject();
            savedShoppingList = (List<Ingredient>) mObjectInputStream.readObject();
            mObjectInputStream.close();
            mFileInputStream.close();

            mUpdatedShoppingList.setValue(savedShoppingList);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ObjectStreamException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {        // ObjectInputStream Exception
            e.printStackTrace();
        } catch (IOException e) {                   // ObjectInputStream Exception
            e.printStackTrace();
        }
    }

    public DataShareViewModel(@NonNull Application application) {
        super(application);
    }
}
