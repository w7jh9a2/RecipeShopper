package com.example.nam.recipeshopper;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.nam.recipeshopper.Ingredient.Defaults;

public class ShoppingListFragment extends BaseFragment implements RecyclerItemClickListener.OnRecyclerClickListener {
    private static final String TAG = "ShoppingListFragment";

    private IngredientRecyclerViewAdapter mIngredientRecyclerViewAdapter;
    private List<RecipeEntry> mRecipeEntryList;
    private List<Ingredient> mShoppingList;
    private static List<Ingredient> mConsolidatedList = new ArrayList<>();
    private DataShareViewModel mViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Gets ViewModel from MainActivity, which is a single ViewModel instance shared between the fragments of Main Activity
        // Observers observe when data is changed and ensures synchronization between fragments accessing MainActivity's viewModel
        mViewModel = ViewModelProviders.of(getActivity()).get(DataShareViewModel.class);

        final Observer<List<RecipeEntry>> recipeObserver = new Observer<List<RecipeEntry>>() {
            @Override
            public void onChanged(@Nullable List<RecipeEntry> recipeEntries) {
                mRecipeEntryList = recipeEntries;
            }
        };

        final Observer<List<Ingredient>> shoppingObserver = new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable List<Ingredient> ingredients) {
                mShoppingList = ingredients;
                if (mShoppingList != null) {
                    consolidate(mShoppingList);
                }
            }
        };

        mViewModel.getUpdatedRecipeEntryList().observe(this, recipeObserver);
        mViewModel.getUpdatedShoppingList().observe(this, shoppingObserver);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recipe, container, false);

        // Sets RecyclerView to populate list of ingredients
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(v.getContext(), recyclerView, this));

        // TODO: Validation of mShoppingList
        mIngredientRecyclerViewAdapter = new IngredientRecyclerViewAdapter(getContext(), mConsolidatedList);
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
        // TODO: Create function from here
        CheckedTextView ctView = view.findViewWithTag("IngredientCheckedText");
        Ingredient ingredient = mIngredientRecyclerViewAdapter.getIngredient(position);
        boolean isChecked = ingredient.toggleChecked();
        ctView.setChecked(isChecked);
        // to here
        if (isChecked) {
            int index = mShoppingList.indexOf(ingredient);
            while (index != -1) {
                // TODO: Need to find object in mIngredient List in the owning RecipeEntry to toggle to CT to true
                int recipeIndex = mRecipeEntryList.indexOf(mShoppingList.get(index).getOwner());
                if (recipeIndex != -1) {
                    for (Ingredient ingredientIterator : mRecipeEntryList.get(recipeIndex).getIngredients()) {
                        if (ingredientIterator.equals(ingredient) && ingredientIterator.getChecked() == false) {
                            ingredientIterator.toggleChecked();
                        }
                    }
                }

                mShoppingList.remove(index);
                index = mShoppingList.indexOf(ingredient);
            }

            // Update mViewModel to synchronize data
            mViewModel.setUpdatedShoppingList(mShoppingList);
            mViewModel.setUpdatedRecipeEntryList(mRecipeEntryList);

        }
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    private void consolidate(List<Ingredient> list) {
        // TODO: Multiple shopping list implementation: List<Ingredient> tempList = mConsolidatedList;
        mConsolidatedList = new ArrayList<>();
        for (Ingredient ingredient : list) {
            int index = mConsolidatedList.indexOf(ingredient);
            if (index != -1) {
                mConsolidatedList.get(index).add(new Ingredient(ingredient));

            } else {
                Ingredient tempIngredient = new Ingredient(ingredient);
                if (!Defaults.contains(ingredient.getUnit())) {
                    tempIngredient.convert();
                }
                mConsolidatedList.add(tempIngredient);
            }
        }

        if (mConsolidatedList != null) {
            mIngredientRecyclerViewAdapter.loadNewData(mConsolidatedList);
        }
    }

}
