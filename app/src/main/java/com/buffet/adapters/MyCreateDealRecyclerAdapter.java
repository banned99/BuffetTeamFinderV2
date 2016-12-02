package com.buffet.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import ggwp.caliver.banned.buffetteamfinderv2.R;

/**
 * Created by icespw on 12/2/2016 AD.
 */

public class MyCreateDealRecyclerAdapter extends RecyclerView.Adapter<MyCreateDealRecyclerAdapter.ViewHolder>{

    private LayoutInflater inflater;
    List<String> mydeals = Collections.emptyList();


    public MyCreateDealRecyclerAdapter(Context context, List<String> mydeals) {
        inflater = LayoutInflater.from(context);
        this.mydeals = mydeals;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_deal_recycler_child, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyCreateDealRecyclerAdapter.ViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return mydeals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(View itemView) {
            super(itemView);

        }

    }
}
