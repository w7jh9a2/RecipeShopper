package com.example.nam.recipeshopper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.List;

public class RecipeActivity extends BaseActivity {
    private static final String TAG = "RecipeActivity";
    private List<Ingredient> mIngredientList;
    private RecipeEntry mRecipeEntry;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        activateToolbar(true);

        Intent intent = getIntent();
        RecipeEntry recipeEntry = (RecipeEntry) intent.getSerializableExtra(RECIPE_TRANSFER);
        if(recipeEntry != null) {
            Log.d(TAG, "onCreate: Ingredient Activity recipe: " + recipeEntry.getTitle());
            mIngredientList = recipeEntry.getIngredients();
            mRecipeEntry = recipeEntry;
        }

        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new RecipePagerAdapter((getSupportFragmentManager())));
    }


    private class RecipePagerAdapter extends FragmentPagerAdapter {
        public RecipePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("RecipeEntry", mRecipeEntry);
            switch(position) {
                case 0:
                    IngredientsFragment ingredientsFragment = new IngredientsFragment();
                    ingredientsFragment.setArguments(bundle);
                    return ingredientsFragment;
                case 1:
                    InstructionsFragment instructionsFragment = new InstructionsFragment();
                    instructionsFragment.setArguments(bundle);
                    return instructionsFragment;
                default:
                    return new IngredientsFragment();

            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
