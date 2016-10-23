package com.buffet.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.buffet.activities.ChooseDealActivity;
import com.buffet.models.Branch;

import java.util.Collections;
import java.util.List;

import ggwp.caliver.banned.buffetteamfinderv2.R;

/**
 * Created by YaYaTripleSix on 23-Oct-16.
 */

public class BranchRecyclerAdapter extends RecyclerView.Adapter<BranchRecyclerAdapter.ViewHolder> {

    private LayoutInflater inflater;
    List<Branch> branches = Collections.emptyList();

    public BranchRecyclerAdapter(Context context, List<Branch> branches) {
        inflater = LayoutInflater.from(context);
        this.branches = branches;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.branch_recycler_child, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BranchRecyclerAdapter.ViewHolder holder, int position) {

        Branch current = branches.get(position);
        final int branch_id = current.getBranchId();
        holder.branchLabel.setText(current.getBranchName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseBranchIntent = new Intent(v.getContext(), ChooseDealActivity.class);
                chooseBranchIntent.putExtra("branch_id", branch_id);
                v.getContext().startActivity(chooseBranchIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return branches.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView branchLabel;

        public ViewHolder(View itemView) {
            super(itemView);

            branchLabel = (TextView) itemView.findViewById(R.id.branchLabel);
        }
    }
}
