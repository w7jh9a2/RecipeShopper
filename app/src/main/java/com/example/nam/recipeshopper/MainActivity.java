package com.example.nam.recipeshopper;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity{

    private static final String TAG = "MainActivity";
    private FileInputStream mFileInputStream;
    private ObjectInputStream mObjectInputStream;
    private FileOutputStream mFileOutputStream;
    private ObjectOutputStream mObjectOutputStream;
    private List<RecipeEntry> savedRecipeEntryList;
    private List<Ingredient> savedShoppingList;
    private DataShareViewModel mViewModelReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager_main);
        activateToolbar(true);

        load();

        // MainActivity's ViewModel is shared between the RecipeListFragment and ShoppingListFragment
        // The data is initialized with what is saved to the disk from load(), where new lists are created if
        // load returns empty.
        final DataShareViewModel mViewModel = ViewModelProviders.of(this).get(DataShareViewModel.class);
        mViewModelReference = mViewModel;

        if (savedRecipeEntryList != null) {
            mViewModel.setUpdatedRecipeEntryList(savedRecipeEntryList);
        } else {
            mViewModel.setUpdatedRecipeEntryList(new ArrayList<RecipeEntry>());
        }
        if (savedShoppingList != null) {
            mViewModel.setUpdatedShoppingList(savedShoppingList);
        } else {
            mViewModel.setUpdatedShoppingList(new ArrayList<Ingredient>());
        }


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
            }
        };

        mViewModel.getUpdatedRecipeEntryList().observe(this, recipeObserver);
        mViewModel.getUpdatedShoppingList().observe(this, shoppingObserver);

        ViewPager pager = (ViewPager) findViewById(R.id.viewPager_Main);
        pager.setAdapter(new ListPagerAdapter(getSupportFragmentManager()));

    }

    private class ListPagerAdapter extends FragmentPagerAdapter{
        public ListPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return new RecipeListFragment();
                case 1:
                    return new ShoppingListFragment();
                default:
                    return new RecipeListFragment();

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
        if (savedRecipeEntryList != mViewModelReference.getSavedRecipeEntryList()) {
            mViewModelReference.setUpdatedRecipeEntryList(savedRecipeEntryList);
        }
        if (savedShoppingList != mViewModelReference.getSavedShoppingList()) {
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