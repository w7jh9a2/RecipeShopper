package com.example.nam.recipeshopper;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.example.nam.recipeshopper.BaseActivity.APP_DATA;
import static com.example.nam.recipeshopper.BaseActivity.RECIPE_LIST_TRANSFER;
import static com.example.nam.recipeshopper.BaseActivity.RECIPE_TRANSFER;
import static com.example.nam.recipeshopper.BaseActivity.SHOPPING_TRANSFER;

public class IngredientsFragment extends Fragment implements RecyclerItemClickListener.OnRecyclerClickListener{
    private static final String TAG = "IngredientsFragment";

    private IngredientRecyclerViewAdapter mIngredientRecyclerViewAdapter;
    private List<RecipeEntry> mRecipeEntryList;
    private List<Ingredient> mShoppingList;
    private FileOutputStream mFileOutputStream;
    private ObjectOutputStream mObjectOutputStream;
    private RecipeEntry mRecipeEntry;
    private int mRecipeEntryIndex;
    private DataShareViewModel mViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Validation of recipeEntry
        mRecipeEntry = (RecipeEntry) getArguments().getSerializable(RECIPE_TRANSFER);

        // Gets ViewModel from MainActivity, which is a single ViewModel instance shared between the fragments of Main Activity
        mViewModel = ViewModelProviders.of(getActivity()).get(DataShareViewModel.class);
        Log.d(TAG, "onCreate: " + mViewModel);

        final Observer<List<RecipeEntry>> recipeObserver = new Observer<List<RecipeEntry>>() {
            @Override
            public void onChanged(@Nullable List<RecipeEntry> recipeEntries) {
                mRecipeEntryList = recipeEntries;
                mIngredientRecyclerViewAdapter.loadNewData(mRecipeEntryList.get(mRecipeEntryIndex).getIngredients());
            }
        };

        final Observer<List<Ingredient>> shoppingObserver = new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable List<Ingredient> ingredients) {
                mShoppingList = ingredients;
                Log.d(TAG, "onChanged: mshoppinglist changed");
            }
        };

        mViewModel.getUpdatedRecipeEntryList().observe(this, recipeObserver);
        mViewModel.getUpdatedShoppingList().observe(this, shoppingObserver);

        // TODO properly validate lists after opening
        mRecipeEntryList = mViewModel.getSavedRecipeEntryList();
        mShoppingList = mViewModel.getSavedShoppingList();
        Log.d(TAG, "onCreate: savedshoppinglist post mviewmodel " + mViewModel.getSavedShoppingList() + " " + mShoppingList);
        if(mShoppingList == null) {
            mShoppingList = new ArrayList<>();
        }
        Log.d(TAG, "onCreate: mshoppinglist" + mShoppingList);

        Log.d(TAG, "onCreate: recipeEntry = " + (mRecipeEntryList.indexOf(mRecipeEntry)));

        // TODO: Check if necessary, potentially move mRecipeEntry to RecipeActivity
        mRecipeEntryIndex = mRecipeEntryList.indexOf(mRecipeEntry);
        mRecipeEntry = mRecipeEntryList.get(mRecipeEntryIndex);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recipe, container, false);

        // Sets RecyclerView to populate list of ingredients
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(v.getContext(), recyclerView, this));



        // TODO: Testing globally accessed lists
       // mRecipeEntry =
        mIngredientRecyclerViewAdapter = new IngredientRecyclerViewAdapter(getContext(), mRecipeEntry.getIngredients());
        recyclerView.setAdapter(mIngredientRecyclerViewAdapter);

        // TODO: FAB programming
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        return v;
    }

    @Override
    public void onItemClick(View view, int position) {

        Log.d(TAG, "onItemClick: " + mShoppingList + " " + mViewModel.getSavedShoppingList());

        //Log.d(TAG, "onItemClick: Checkbox procedure");
        CheckedTextView ctView = view.findViewWithTag("IngredientCheckedText");
        Ingredient ingredient = mIngredientRecyclerViewAdapter.getIngredient(position);
        //int ingredientIndex = mRecipeEntry.getIngredients().indexOf(mIngredientRecyclerViewAdapter.getIngredient(position));
        //Ingredient ingredient = mRecipeEntry.getIngredients().get(ingredientIndex);
        Log.d(TAG, "onItemClick: " + ingredient.getChecked());
        boolean isChecked = ingredient.toggleChecked();
        Log.d(TAG, "onItemClick: " + isChecked + " " + mIngredientRecyclerViewAdapter.getIngredient(position).getChecked());
        ctView.setChecked(isChecked);
        Log.d(TAG, "onItemClick: " + mShoppingList + " " + mViewModel.getSavedShoppingList());
        if(!isChecked) {
            mShoppingList.add(ingredient);
            Log.d(TAG, "onItemClick: adds to list" + mShoppingList);
            mViewModel.setUpdatedShoppingList(mShoppingList);
        } else {

            //TODO: Remove ingredients from shoppinglist based on owner
            int sIndex = mShoppingList.indexOf(ingredient);
            int fIndex = mShoppingList.lastIndexOf(ingredient);

            if(fIndex >= sIndex && sIndex != -1) {
                while(sIndex <= fIndex) {
                    if(mShoppingList.get(sIndex).getOwner() == ingredient.getOwner() && mShoppingList.get(sIndex).equals(ingredient)) {
                        mShoppingList.remove(sIndex);
                        mViewModel.setUpdatedShoppingList(mShoppingList);
                        break;
                    }
                    else {
                        sIndex++;
                    }
                }
            }
        }


//        try{
//            Log.d(TAG, "onItemClick: save data");
//            mFileOutputStream = new FileOutputStream(getContext().getFilesDir() + APP_DATA);
//            mObjectOutputStream = new ObjectOutputStream(mFileOutputStream);
//            mObjectOutputStream.writeObject(mRecipeEntryList);
//            mObjectOutputStream.writeObject(mShoppingList);
//            mObjectOutputStream.flush();
//            mObjectOutputStream.close();
//            mFileOutputStream.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO: Make sure exception on OOS does not require close on FOS, maybe implement finally block
//            e.printStackTrace();
//        }
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    @Override
    public void onPause() {
        if (mViewModel.getSavedRecipeEntryList() != mRecipeEntryList) {
            mViewModel.setUpdatedRecipeEntryList(mRecipeEntryList);
        }
        if (mViewModel.getSavedShoppingList() != mShoppingList) {
            mViewModel.setUpdatedShoppingList(mShoppingList);
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
