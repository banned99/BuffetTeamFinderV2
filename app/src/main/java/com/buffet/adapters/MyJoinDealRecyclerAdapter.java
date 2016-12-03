package com.buffet.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import ggwp.caliver.banned.buffetteamfinderv2.R;

/**
 * Created by icespw on 12/2/2016 AD.
 */

public class MyJoinDealRecyclerAdapter extends RecyclerView.Adapter<MyJoinDealRecyclerAdapter.ViewHolder>{

    private LayoutInflater inflater;
    List<String> mydeals = Collections.emptyList();


    public MyJoinDealRecyclerAdapter(Context context, List<String> mydeals) {
        inflater = LayoutInflater.from(context);
        this.mydeals = mydeals;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_deal_recycler_child, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyJoinDealRecyclerAdapter.ViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return mydeals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView proname, branchname, ownername, date, time;
        ImageButton memberbtn, chatbtn;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            proname = (TextView) itemView.findViewById(R.id.pro_name);
            branchname = (TextView) itemView.findViewById(R.id.pro_branch_name);
            ownername = (TextView) itemView.findViewById(R.id.deal_owner);
            date = (TextView) itemView.findViewById(R.id.eat_date);
            time = (TextView) itemView.findViewById(R.id.eat_time);
            chatbtn = (ImageButton) itemView.findViewById(R.id.chat_button);
            memberbtn = (ImageButton) itemView.findViewById(R.id.member_button);

            linearLayout = (LinearLayout) itemView.findViewById(R.id.deal_member_img);


        }

    }
}
