package com.example.nam.recipeshopper;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class InstructionsFragment extends Fragment implements RecyclerItemClickListener.OnRecyclerClickListener {
    private static final String TAG = "InstructionsFragment";
    private InstructionRecyclerViewAdapter mInstructionRecyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_ingredients, container, false);

        // Sets RecyclerView to populate list of ingredients
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(v.getContext(), recyclerView, this));

        // TODO: Validation of recipeEntry
        RecipeEntry recipeEntry = (RecipeEntry) getArguments().getSerializable("RecipeEntry");
        mInstructionRecyclerViewAdapter = new InstructionRecyclerViewAdapter(getContext(), recipeEntry.getInstructions());
        recyclerView.setAdapter(mInstructionRecyclerViewAdapter);

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

    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}
