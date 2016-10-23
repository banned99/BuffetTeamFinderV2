package com.buffet.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buffet.activities.ChooseDealActivity;
import com.buffet.models.Deal;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.List;

import ggwp.caliver.banned.buffetteamfinderv2.R;

/**
 * Created by YaYaTripleSix on 23-Oct-16.
 */

public class DealRecyclerAdapter extends RecyclerView.Adapter<DealRecyclerAdapter.ViewHolder>{

    private LayoutInflater inflater;
    List<Deal> deals = Collections.emptyList();

    public DealRecyclerAdapter(Context context , List<Deal> deals) {
        inflater = LayoutInflater.from(context);
        this.deals = deals;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deal_recycler_child, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DealRecyclerAdapter.ViewHolder holder, int position) {

        Deal current = deals.get(position);
        final int deal_id = current.getDealId();
//        holder.dealLabel.setText(current.getDealName());
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent chooseBranchIntent = new Intent(v.getContext(), ChooseDealActivity.class);
//                chooseBranchIntent.putExtra("deal_id", deal_id);
//                v.getContext().startActivity(chooseBranchIntent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return deals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

//        TextView dealLabel;

        public ViewHolder(View itemView) {
            super(itemView);

//            dealLabel = (TextView) itemView.findViewById(R.id.dealLabel);
        }
    }
}
