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

public class InstructionRecyclerViewAdapter extends RecyclerView.Adapter<InstructionRecyclerViewAdapter.InstructionViewHolder> {
    private static final String TAG = "InstructionRecyclerView";
    private List<String> mInstructionList;
    private Context mContext;

    public InstructionRecyclerViewAdapter(Context context, List<String> instructionList) {
        mContext = context;
        mInstructionList = instructionList;
    }

    @NonNull
    @Override
    public InstructionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: starts");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_checked_text, parent, false);
        return new InstructionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionViewHolder holder, int position) {
        if((mInstructionList == null) || (mInstructionList.size() == 0)) {
            holder.instruction.setText("There are no instructions.");
            holder.instruction.setChecked(false);
        } else {
            String instructionItem = mInstructionList.get(position);
            holder.instruction.setText(instructionItem);
            holder.instruction.setChecked(false);
        }

    }

    @Override
    public int getItemCount() {
        return ((mInstructionList != null) && (mInstructionList.size() != 0) ? mInstructionList.size() : 1);
    }

    static class InstructionViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "InstructionViewHolder";
        AppCompatCheckedTextView instruction = null;

        public InstructionViewHolder(View itemView) {
            super(itemView);
            this.instruction = (AppCompatCheckedTextView) itemView.findViewById(R.id.checkedTextView_listEntry);
        }
    }

}
