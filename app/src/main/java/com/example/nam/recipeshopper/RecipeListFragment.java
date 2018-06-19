package com.example.nam.recipeshopper;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

import static com.example.nam.recipeshopper.BaseActivity.RECIPE_LIST_TRANSFER;
import static com.example.nam.recipeshopper.BaseActivity.RECIPE_TRANSFER;
import static com.example.nam.recipeshopper.BaseActivity.SHOPPING_TRANSFER;

public class RecipeListFragment extends Fragment implements GetRecipeHtmlData.OnDataAvailable,
                RecyclerItemClickListener.OnRecyclerClickListener {
    private static final String TAG = "RecipeListFragment";
    private RecipeRecyclerViewAdapter mRecipeRecyclerViewAdapter;
    private List<RecipeEntry> mRecipeEntryList;
    private List<Ingredient> mShoppingList;
    private boolean mVisible;
    private Bundle saveData = new Bundle();

    private DataShareViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Gets ViewModel from MainActivity, which is a single ViewModel instance shared between the fragments of Main Activity
        mViewModel = ViewModelProviders.of(getActivity()).get(DataShareViewModel.class);

        final Observer<List<RecipeEntry>> recipeObserver = new Observer<List<RecipeEntry>>() {
            @Override
            public void onChanged(@Nullable List<RecipeEntry> recipeEntries) {
                mRecipeEntryList = recipeEntries;
                mRecipeRecyclerViewAdapter.loadNewData(mRecipeEntryList);
                saveData.putSerializable(RECIPE_LIST_TRANSFER, (Serializable) mRecipeEntryList);
            }
        };

        final Observer<List<Ingredient>> shoppingObserver = new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable List<Ingredient> ingredients) {
                mShoppingList = ingredients;
                saveData.putSerializable(SHOPPING_TRANSFER, (Serializable) mShoppingList);
            }
        };

        if (mViewModel.getSavedRecipeEntryList() != null)
        {
            mRecipeEntryList = mViewModel.getSavedRecipeEntryList();
        }
        if(mViewModel.getSavedShoppingList() != null) {
            mShoppingList = mViewModel.getSavedShoppingList();
        }

        mViewModel.getUpdatedRecipeEntryList().observe(this, recipeObserver);
        mViewModel.getUpdatedShoppingList().observe(this, shoppingObserver);
        updateSaveData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_main, container, false);
        super.onCreate(savedInstanceState);


        // Creates RecyclerView and sets custom RecyclerViewAdapter to display listRecipes contents
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(v.getContext(), recyclerView, this));

        mRecipeRecyclerViewAdapter = new RecipeRecyclerViewAdapter(v.getContext(), mRecipeEntryList);
        recyclerView.setAdapter(mRecipeRecyclerViewAdapter);

        if(mRecipeEntryList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            v.findViewById(R.id.empty_text).setVisibility(View.VISIBLE);
            mVisible = false;
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            mVisible = true;
            mRecipeRecyclerViewAdapter.loadNewData(mRecipeEntryList);
        }

        // Sets onClickListener for addRecipeFAB
        final GetRecipeHtmlData.OnDataAvailable mCallback = this;
        FloatingActionButton addRecipeButton = v.findViewById(R.id.fab);
        addRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Inline dialog builder, if using this AddRecipeDialogFragment class can be removed
                LayoutInflater inflater = LayoutInflater.from(getContext());
                final View addRecipeView = inflater.inflate(R.layout.dialog_addrecipe, null);
                final TextView etName = (EditText) addRecipeView.findViewById(R.id.recipeURLText);
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setView(addRecipeView)
                        .setPositiveButton("Add recipe from URL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(!etName.getText().toString().equals("")){
                                    GetRecipeHtmlData getRecipeHtmlData = new GetRecipeHtmlData(mCallback);
                                    getRecipeHtmlData.execute(etName.getText().toString());
                                    updateSaveData();
                                } else {
                                    Toast.makeText(getContext(), "Type recipe URL above.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
            }
        });

        return v;
    }

    @Override
    public void onDataAvailable(RecipeEntry newEntry, DownloadStatus status) {

        if(status == DownloadStatus.OK) {
            if(!mVisible) {
                getView().findViewById(R.id.empty_text).setVisibility(View.GONE);
                getView().findViewById(R.id.recycler_view).setVisibility(View.VISIBLE);
            }

            mRecipeEntryList.add(newEntry);
            mViewModel.setUpdatedRecipeEntryList(mRecipeEntryList);

        } else {
            // TODO: download or processing failed
        }

    }

    private void updateSaveData() {

        // updateSaveData() updates data that is used to initialize RecipeActivity when data is changed
        saveData.putSerializable(RECIPE_LIST_TRANSFER, (Serializable) mRecipeEntryList);
        saveData.putSerializable(SHOPPING_TRANSFER, (Serializable) mShoppingList);
    }

    @Override
    public void onItemClick(View view, int position) {

        // Select a RecipeEntry to view recipe's ingredients and instructions
        Intent intent = new Intent(getContext(), RecipeActivity.class);
        intent.putExtra(RECIPE_TRANSFER, mRecipeRecyclerViewAdapter.getRecipeEntry(position));
        intent.putExtras(saveData);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}
