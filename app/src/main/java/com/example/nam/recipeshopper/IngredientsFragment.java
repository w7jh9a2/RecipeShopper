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

import java.util.ArrayList;
import java.util.List;

import static com.example.nam.recipeshopper.BaseActivity.RECIPE_TRANSFER;

public class IngredientsFragment extends Fragment implements RecyclerItemClickListener.OnRecyclerClickListener{
    private static final String TAG = "IngredientsFragment";

    private IngredientRecyclerViewAdapter mIngredientRecyclerViewAdapter;
    private List<RecipeEntry> mRecipeEntryList;
    private List<Ingredient> mShoppingList;
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
        // Takes selected ingredient and adds/removes from the ShoppingList based on the boolean member in the ingredient
        CheckedTextView ctView = view.findViewWithTag("IngredientCheckedText");
        Ingredient ingredient = mIngredientRecyclerViewAdapter.getIngredient(position);
        boolean isChecked = ingredient.toggleChecked();
        ctView.setChecked(isChecked);
        if(!isChecked) {
            mShoppingList.add(ingredient);
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
