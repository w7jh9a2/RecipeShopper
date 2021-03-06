package com.example.nam.recipeshopper;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.List;

public class RecipeActivity extends BaseActivity {
    private static final String TAG = "RecipeActivity";
    private List<Ingredient> mIngredientList;
    private RecipeEntry mRecipeEntry;
    private Bundle saveData;

    private FileInputStream mFileInputStream;
    private ObjectInputStream mObjectInputStream;
    private FileOutputStream mFileOutputStream;
    private ObjectOutputStream mObjectOutputStream;
    private List<RecipeEntry> savedRecipeEntryList;
    private List<Ingredient> savedShoppingList;
    private DataShareViewModel mViewModelReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        activateToolbar(true);

        Intent intent = getIntent();
        Log.d(TAG, "onCreate: " + intent.getSerializableExtra(RECIPE_TRANSFER));
        RecipeEntry recipeEntry = (RecipeEntry) intent.getSerializableExtra(RECIPE_TRANSFER);
        saveData = intent.getExtras();

        if(recipeEntry != null) {
            Log.d(TAG, "onCreate: Ingredient Activity recipe: " + recipeEntry.getTitle());
            mIngredientList = recipeEntry.getIngredients();
            mRecipeEntry = recipeEntry;
        }

        if(saveData != null) {
            Log.d(TAG, "onCreate: Loaded save data for updating save");
            savedRecipeEntryList = (List<RecipeEntry>) saveData.getSerializable(RECIPE_LIST_TRANSFER);
            savedShoppingList = (List<Ingredient>) saveData.getSerializable(SHOPPING_TRANSFER);
            Log.d(TAG, "onCreate: SaveData:" + savedRecipeEntryList + " " + savedShoppingList);
        }

        final DataShareViewModel mViewModel = ViewModelProviders.of(this).get(DataShareViewModel.class);
        mViewModelReference = mViewModel;

        if(savedRecipeEntryList != null) {
            mViewModel.setUpdatedRecipeEntryList(savedRecipeEntryList);
        } else {
            mViewModel.setUpdatedRecipeEntryList(new ArrayList<RecipeEntry>());
        }

        if (savedShoppingList != null) {
            mViewModel.setUpdatedShoppingList(savedShoppingList);
        } else {
            mViewModel.setUpdatedShoppingList(new ArrayList<Ingredient>());
        }
        Log.d(TAG, "onCreate: savedShoppingList post viewModel " + mViewModel.getSavedShoppingList());
        Log.d(TAG, "onCreate: " + mViewModel);

        final Observer<List<RecipeEntry>> recipeObserver = new Observer<List<RecipeEntry>>() {
            @Override
            public void onChanged(@Nullable List<RecipeEntry> recipeEntries) {
                savedRecipeEntryList = recipeEntries;
            }
        };

        final Observer<List<Ingredient>> shoppingObserver = new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable List<Ingredient> ingredients) {
                savedShoppingList = ingredients;
                Log.d(TAG, "onChanged: savedshoppinglist changed");
                Log.d(TAG, "onChanged: " + mViewModel.getSavedShoppingList());
            }
        };

        mViewModel.getUpdatedRecipeEntryList().observe(this, recipeObserver);
        mViewModel.getUpdatedShoppingList().observe(this, shoppingObserver);

        ViewPager pager = findViewById(R.id.viewPager);
        pager.setAdapter(new RecipePagerAdapter((getSupportFragmentManager())));


    }


    private class RecipePagerAdapter extends FragmentPagerAdapter {
        public RecipePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(RECIPE_TRANSFER, mRecipeEntry);
            switch(position) {
                case 0:
                    IngredientsFragment ingredientsFragment = new IngredientsFragment();
                    ingredientsFragment.setArguments(bundle);
                    return ingredientsFragment;
                case 1:
                    InstructionsFragment instructionsFragment = new InstructionsFragment();
                    instructionsFragment.setArguments(bundle);
                    return instructionsFragment;
                default:
                    return new IngredientsFragment();

            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @Override
    protected void onPause() {
        save();
        super.onPause();

    }

    @Override
    protected void onResume() {
        load();
        if (savedRecipeEntryList != mViewModelReference.getSavedRecipeEntryList() && savedRecipeEntryList != null) {
            mViewModelReference.setUpdatedRecipeEntryList(savedRecipeEntryList);
        }
        if (savedShoppingList != mViewModelReference.getSavedShoppingList() && savedShoppingList != null) {
            mViewModelReference.setUpdatedShoppingList(savedShoppingList);
        }
        super.onResume();
    }

    public void save() {
        try{
            mFileOutputStream = new FileOutputStream(getFilesDir() + APP_DATA);
            mObjectOutputStream = new ObjectOutputStream(mFileOutputStream);
            mObjectOutputStream.writeObject(savedRecipeEntryList);
            mObjectOutputStream.writeObject(savedShoppingList);
            mObjectOutputStream.flush();
            mObjectOutputStream.close();
            mFileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO: Make sure exception on OOS does not require close on FOS, maybe implement finally block
            e.printStackTrace();
        }
    }

    public void load() {
        try {
            mFileInputStream = new FileInputStream(getFilesDir() + APP_DATA);
            mObjectInputStream = new ObjectInputStream(mFileInputStream);
            savedRecipeEntryList = (List<RecipeEntry>) mObjectInputStream.readObject();
            savedShoppingList = (List<Ingredient>) mObjectInputStream.readObject();
            mObjectInputStream.close();
            mFileInputStream.close();
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

}
