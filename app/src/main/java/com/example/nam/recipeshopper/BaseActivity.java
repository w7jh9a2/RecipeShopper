package com.example.nam.recipeshopper;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Nam on 3/22/2018.
 */

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";

    void activateToolbar(boolean enableHome) {
        Log.d(TAG, "activateToolbar: starts");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar == null) {
            // TODO: What to implement when actionbar is not on activity
//            Toolbar toolbar = (Toolbar) findViewById(R.id.)
        }

        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(enableHome);
        }
    }
}
