package com.buffet.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buffet.activities.ChooseBranchActivity;
import com.buffet.activities.DealMemberActivity;
import com.buffet.models.Branch;
import com.buffet.models.Deal;
import com.buffet.models.Promotion;

import java.util.Collections;
import java.util.List;

import ggwp.caliver.banned.buffetteamfinderv2.R;

/**
 * Created by icespw on 12/2/2016 AD.
 */

public class MyCreateDealRecyclerAdapter extends RecyclerView.Adapter<MyCreateDealRecyclerAdapter.ViewHolder>{

    private LayoutInflater inflater;
    List<Deal> mydeals = Collections.emptyList();
    List<Branch> branchs = Collections.emptyList();
    List<Promotion> promotions = Collections.emptyList();


    public MyCreateDealRecyclerAdapter(Context context, List<Deal> mydeals, List<Branch> branchs, List<Promotion> promotions) {
        inflater = LayoutInflater.from(context);
        this.mydeals = mydeals;
        this.branchs = branchs;
        this.promotions = promotions;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_deal_recycler_child, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyCreateDealRecyclerAdapter.ViewHolder holder, int position) {

        holder.proname.setText(promotions.get(position).getProName());
        holder.branchname.setText(branchs.get(position).getBranchName());
        holder.ownername.setText(Integer.toString(mydeals.get(position).getDealOwner()));
        holder.date.setText(mydeals.get(position).getDate());
        holder.time.setText(mydeals.get(position).getTime());

        int current_person = mydeals.get(position).getCurrentPerson();
        int max_person = promotions.get(position).getMaxPerson();

        ImageView img_black, img_gray;

        holder.linearLayout.removeAllViews();

        for (int i = 1; i<= current_person; i++) {
            img_black = new ImageView(inflater.getContext());
            img_black.setImageResource(R.drawable.person);
            img_black.setLayoutParams(new ViewGroup.LayoutParams(40,40));
            holder.linearLayout.addView(img_black);
        }

        for (int i = 1; i<= max_person - current_person; i++) {
            img_gray = new ImageView(inflater.getContext());
            img_gray.setImageResource(R.drawable.person_gray);
            img_gray.setLayoutParams(new ViewGroup.LayoutParams(40,40));
            holder.linearLayout.addView(img_gray);
        }

        holder.memberbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DealMemberActivity.class);
                v.getContext().startActivity(intent);
            }
        });
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
