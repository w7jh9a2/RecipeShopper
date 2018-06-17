package com.example.nam.recipeshopper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class IngredientRecyclerViewAdapter extends RecyclerView.Adapter<IngredientRecyclerViewAdapter.IngredientViewHolder> {
    private static final String TAG = "IngredientRecyclerViewA";
    private List<Ingredient> mIngredientList;
    private Context mContext;

    public IngredientRecyclerViewAdapter(Context context, List<Ingredient> ingredientList) {
        mContext = context;
        mIngredientList = ingredientList;
    }

    @NonNull
    @Override
    public IngredientRecyclerViewAdapter.IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Log.d(TAG, "onCreateViewHolder: starts");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_checked_text, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return ((mIngredientList != null) && (mIngredientList.size() != 0) ? mIngredientList.size() : 1);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        if((mIngredientList == null) || (mIngredientList.size() == 0)) {
            holder.ingredient.setText("No ingredients listed");
            holder.ingredient.setChecked(false);
        } else {
            Ingredient ingredientItem = mIngredientList.get(position);
            //Log.d(TAG, "onBindViewHolder: " + ingredientItem.getChecked());
            //Log.d(TAG, "onBindViewHolder: " + ingredientItem.getName() + " ---> " + position);
            holder.ingredient.setText(ingredientItem.getMeasurement() + " " + ingredientItem.getUnit() + " " + ingredientItem.getName());
            holder.ingredient.setChecked(ingredientItem.getChecked());
            holder.ingredient.setTag("IngredientCheckedText");
        }

    }

    public Ingredient getIngredient(int position) {
        return((mIngredientList != null) && (mIngredientList.size() != 0) ? mIngredientList.get(position) : null);
    }

    void loadNewData(List<Ingredient> newIngredients) {
        mIngredientList = newIngredients;
        notifyDataSetChanged();
    }

    static class IngredientViewHolder extends RecyclerView.ViewHolder {
        //private static final String TAG = "IngredientViewHolder";
        AppCompatCheckedTextView ingredient = null;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            this.ingredient = (AppCompatCheckedTextView) itemView.findViewById(R.id.checkedTextView_listEntry);
        }
    }
}
