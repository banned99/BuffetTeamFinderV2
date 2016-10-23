package com.buffet.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.buffet.activities.ChooseBranchActivity;
import com.buffet.models.Promotion;

import java.util.Collections;
import java.util.List;

import ggwp.caliver.banned.buffetteamfinderv2.R;

/**
 * Created by NattKS on 10/19/2016.
 */

public class NewPromotionRecyclerAdapter extends RecyclerView.Adapter<NewPromotionRecyclerAdapter.ViewHolder>{

    private LayoutInflater inflater;
    List<Promotion> promotions = Collections.emptyList();

    public NewPromotionRecyclerAdapter(Context context, List<Promotion> promotions) {
        inflater = LayoutInflater.from(context);
        this.promotions = promotions;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_promotion_recycler_child, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Promotion current = promotions.get(position);
        final int promotion_id = current.getPromotionID();
        holder.promotionLabel.setText(current.getPromotionName());
        holder.promotionImage.setImageResource(current.getImage());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = holder.promotionLabel.getText().toString();
                System.out.println("name = " + name + " id = " + promotion_id);
                Toast.makeText(v.getContext(),name,Toast.LENGTH_LONG);

                Intent chooseProIntent = new Intent(v.getContext(), ChooseBranchActivity.class);
                chooseProIntent.putExtra("promotion_id", promotion_id);
                v.getContext().startActivity(chooseProIntent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return promotions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView promotionLabel;
        ImageView promotionImage;

        public ViewHolder(View itemView) {
            super(itemView);

            promotionLabel = (TextView) itemView.findViewById(R.id.promotionLabel);
            promotionImage = (ImageView) itemView.findViewById(R.id.promotionImage);


        }
    }
}
