package com.example.nam.recipeshopper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeRecyclerViewAdapter extends RecyclerView.Adapter<RecipeRecyclerViewAdapter.RecipeViewHolder> {
    private static final String TAG = "RecipeRecyclerViewAdapt";
    private List<RecipeEntry> mRecipeList;
    private Context mContext;

    public RecipeRecyclerViewAdapter(Context context, List<RecipeEntry> recipeList) {
        mContext = context;
        mRecipeList = recipeList;
    }

    @Override
    public RecipeRecyclerViewAdapter.RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_list, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeRecyclerViewAdapter.RecipeViewHolder holder, int position) {

        if((mRecipeList == null) || (mRecipeList.size() == 0)) {
            holder.thumbnail.setImageResource(R.drawable.placeholder);
            holder.title.setText("No recipes");
        } else {
            RecipeEntry recipeEntry = mRecipeList.get(position);
            Picasso.get().load(recipeEntry.getImageURL())
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.thumbnail);

            holder.title.setText(recipeEntry.getTitle());
            holder.yield.setText(recipeEntry.getYield());
            holder.totalTime.setText(recipeEntry.getTotalTime());
            holder.activeTime.setText(recipeEntry.getActiveTime());
        }
    }

    @Override
    public int getItemCount() {
        return ((mRecipeList != null) && (mRecipeList.size() != 0) ? mRecipeList.size() : 1);
    }

    void loadNewData(List<RecipeEntry> updatedList) {
        mRecipeList = updatedList;
        notifyDataSetChanged();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "RecipeViewHolder";
        ImageView thumbnail = null;
        TextView title = null;
        TextView yield = null;
        TextView activeTime = null;
        TextView totalTime = null;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "RecipeViewHolder: starts");
            this.thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.yield = (TextView) itemView.findViewById(R.id.yield);
            this.activeTime = (TextView) itemView.findViewById(R.id.activeTime);
            this.totalTime = (TextView) itemView.findViewById(R.id.totalTime);
        }
    }

    public RecipeEntry getRecipeEntry(int position) {
        if(mRecipeList.get(position) == null) Log.d(TAG, "getRecipeEntry: null" );
        return((mRecipeList != null) && (mRecipeList.size() != 0) ? mRecipeList.get(position) : null);
    }
}
