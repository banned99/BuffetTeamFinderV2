package com.buffet.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buffet.activities.ChooseBranchActivity;

import com.buffet.activities.MyDealActivity;
import com.buffet.models.Deal;

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
        final String owner_name = current.getDealOwner();
        final String date = current.getDate();
        final String time = current.getTime();
        final int current_person = current.getCurrentPerson();

        ImageView img_black, img_gray;

        holder.owner_name.setText("Deal Owner : "+owner_name);
        holder.eat_date.setText("Date : " + date);
        holder.eat_time.setText("Time : " + time);

        holder.linearLayout.removeAllViews();

        for (int i = 1; i<= current_person; i++) {
            img_black = new ImageView(inflater.getContext());
            img_black.setImageResource(R.drawable.person);
            img_black.setLayoutParams(new ViewGroup.LayoutParams(40,40));
            holder.linearLayout.addView(img_black);
        }

        for (int i = 1; i<= ChooseBranchActivity.promotion_max_person - current_person; i++) {
            img_gray = new ImageView(inflater.getContext());
            img_gray.setImageResource(R.drawable.person_gray);
            img_gray.setLayoutParams(new ViewGroup.LayoutParams(40,40));
            holder.linearLayout.addView(img_gray);
        }


        holder.join_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Join Event");
                builder.setMessage("Are you sure to join this event?");
                builder.setPositiveButton("Join", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(v.getContext(), MyDealActivity.class);
                        v.getContext().startActivity(intent);
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return deals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView owner_name;
        TextView eat_time, eat_date;
        Button join_button;

        LinearLayout linearLayout;



        public ViewHolder(View itemView) {
            super(itemView);

            owner_name = (TextView) itemView.findViewById(R.id.deal_owner);
            eat_time = (TextView) itemView.findViewById(R.id.eat_time);
            join_button = (Button) itemView.findViewById(R.id.join_button);
            eat_date = (TextView) itemView.findViewById(R.id.eat_date);

            linearLayout = (LinearLayout) itemView.findViewById(R.id.deal_member_img);

        }
    }
}
