package com.example.nam.recipeshopper;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.io.File;

/**
 * Created by Nam on 3/22/2018.
 */

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    static final String RECIPE_TRANSFER = "RECIPE_TRANSFER";
    static final String SHOPPING_TRANSFER = "SHOPPING_TRANSFER";
    static final String RECIPE_LIST_TRANSFER = "RECIPE_LIST_TRANSFER";
    static final String APP_DATA = "data";

    @Override
    public File getFilesDir() {
        return super.getFilesDir();
    }

    // TODO: Put save/load functions here

    void activateToolbar(boolean enableHome) {
        Log.d(TAG, "activateToolbar: starts");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar == null) {
            // TODO: What to implement when actionbar is not on activity
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

            if(toolbar != null) {
                setSupportActionBar(toolbar);
                actionBar = getSupportActionBar();
            }
        }

        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(enableHome);
        }
    }
}
