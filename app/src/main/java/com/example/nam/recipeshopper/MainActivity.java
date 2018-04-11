package com.example.nam.recipeshopper;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements GetRecipeHtmlData.OnDataAvailable,
        AddRecipeDialogFragment.AddRecipeDialogListener, RecyclerItemClickListener.OnRecyclerClickListener {

    private static final String TAG = "MainActivity";
    private RecipeRecyclerViewAdapter mRecipeRecyclerViewAdapter;
    private List<RecipeEntry> listRecipes = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activateToolbar(false);

        // Creates RecyclerView and sets custom RecyclerViewAdapter to display listRecipes contents
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, this));

        mRecipeRecyclerViewAdapter = new RecipeRecyclerViewAdapter(this, listRecipes);
        recyclerView.setAdapter(mRecipeRecyclerViewAdapter);

        // Sets onClickListener for addRecipeFAB
        final GetRecipeHtmlData.OnDataAvailable mCallback = this;
        FloatingActionButton addRecipeButton = findViewById(R.id.fab);
        addRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: starts");

                // Inline dialog builder, if using this AddRecipeDialogFragment class can be removed
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                final View addRecipeView = inflater.inflate(R.layout.dialog_addrecipe, null);

                final TextView etName = (EditText) addRecipeView.findViewById(R.id.recipeURLText);
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setView(addRecipeView)
                        .setPositiveButton("Add recipe from URL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(!etName.getText().toString().equals("")){
                                    GetRecipeHtmlData getRecipeHtmlData = new GetRecipeHtmlData(mCallback);
                                    getRecipeHtmlData.execute(etName.getText().toString());
                                } else {
                                    Toast.makeText(MainActivity.this, "Type recipe URL above.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();

                // Create an instance of the AddRecipeDialogFragment and show it
//                AddRecipeDialogFragment addRecipeDialogFragment = new AddRecipeDialogFragment();
//                addRecipeDialogFragment.show(getSupportFragmentManager(), "AddRecipeDialogFragment");
            }
        });
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

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick: starts");
        Intent intent = new Intent(this, IngredientsActivity.class);
        intent.putExtra(RECIPE_TRANSFER, mRecipeRecyclerViewAdapter.getRecipeEntry(position));
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Log.d(TAG, "onItemLongClick: starts");
        Toast.makeText(MainActivity.this, "Long tap at position " + position, Toast.LENGTH_SHORT).show();
    }

    // TODO: Decide on which implementation of FAB to use
    // Implementation of add recipe dialog button clicks
    @Override
    public void onDialogPositiveClick(AppCompatDialogFragment dialog, String recipeURL) {
        Log.d(TAG, "onDialogPositiveClick: returned to main");
        if(!recipeURL.equals("")){
                GetRecipeHtmlData getRecipeHtmlData = new GetRecipeHtmlData(this);
                getRecipeHtmlData.execute(recipeURL);
        } else {
            Toast.makeText(MainActivity.this, "Type recipe URL above.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDialogNegativeClick(AppCompatDialogFragment dialog) {
        Log.d(TAG, "onDialogNegativeClick: returned to main");
    }
}
