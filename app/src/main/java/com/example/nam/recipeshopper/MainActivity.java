package com.example.nam.recipeshopper;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements GetRecipeHtmlData.OnDataAvailable {
    private static final String TAG = "MainActivity";
    private RecipeRecyclerViewAdapter mRecipeRecyclerViewAdapter;
    private List<RecipeEntry> listRecipes = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activateToolbar(false);

        // TODO: create RecyclerViewAdapter.
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecipeRecyclerViewAdapter = new RecipeRecyclerViewAdapter(this, listRecipes);
        recyclerView.setAdapter(mRecipeRecyclerViewAdapter);
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: starts");
        super.onResume();

        GetRecipeHtmlData getRecipeHtmlData = new GetRecipeHtmlData(this);
        getRecipeHtmlData.execute("https://www.seriouseats.com/recipes/2011/07/real-deal-mapo-dofu-tofu-chinese-sichuan-recipe.html");
    }

    @Override
    public void onDataAvailable(RecipeEntry newEntry, DownloadStatus status) {
        Log.d(TAG, "onDataAvailable: starts");

        if(status == DownloadStatus.OK) {
            listRecipes.add(newEntry);
            mRecipeRecyclerViewAdapter.loadNewData(listRecipes);
            Log.d(TAG, "onDataAvailable: adds entry to existing list here");
        } else {
            // download or processing failed
            Log.e(TAG, "onDataAvailable: failed with status " + status);
        }

        Log.d(TAG, "onDataAvailable: ends");
    }
}
