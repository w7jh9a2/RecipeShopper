package com.example.nam.recipeshopper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckedTextView;

import java.util.List;

public class IngredientsActivity extends BaseActivity implements RecyclerItemClickListener.OnRecyclerClickListener{
    private static final String TAG = "IngredientsActivity";
    private List<Ingredient> mIngredientList;
    private IngredientRecyclerViewAdapter mIngredientRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        activateToolbar(true);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        // Retrieves information passed through the intent from MainActivity
        Intent intent = getIntent();
        RecipeEntry recipeEntry = (RecipeEntry) intent.getSerializableExtra(RECIPE_TRANSFER);
        if(recipeEntry != null) {
            Log.d(TAG, "onCreate: Ingredient Activity recipe: " + recipeEntry.getTitle());
            mIngredientList = recipeEntry.getIngredients();
        }

        // Sets RecyclerView to populate list of ingredients
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, this));

        mIngredientRecyclerViewAdapter = new IngredientRecyclerViewAdapter(this, mIngredientList);
        recyclerView.setAdapter(mIngredientRecyclerViewAdapter);

        // TODO: FAB programming
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        CheckedTextView ctView = (CheckedTextView) view.findViewWithTag("IngredientCheckedText");
        ctView.toggle();
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

}
