package com.buffet.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ggwp.caliver.banned.buffetteamfinderv2.R;

/**
 * Created by NattKS on 10/19/2016.
 */

public class NewPromotionRecyclerAdapter extends RecyclerView.Adapter<NewPromotionRecyclerAdapter.ViewHolder>{
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.new_promotion_recycler_child, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.promotionLabel.setText("Promotion " + Integer.toString(position+1));

        switch (position+1) {
            case 1:
                holder.promotionImage.setImageResource(R.drawable.promo_1);
                break;
            case 2:
                holder.promotionImage.setImageResource(R.drawable.promo_2);
                break;
            case 3:
                holder.promotionImage.setImageResource(R.drawable.promo_3);
                break;
            case 4:
                holder.promotionImage.setImageResource(R.drawable.promo_4);
                break;
            case 5:
                holder.promotionImage.setImageResource(R.drawable.promo_5);
                break;
            case 6:
                holder.promotionImage.setImageResource(R.drawable.promo_6);
                break;
            case 7:
                holder.promotionImage.setImageResource(R.drawable.promo_7);
                break;
            case 8:
                holder.promotionImage.setImageResource(R.drawable.promo_8);
                break;
        }
    }


    @Override
    public int getItemCount() {
        return 8;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView promotionLabel;
        ImageView promotionImage;

        public ViewHolder(View itemView) {
            super(itemView);

            promotionLabel = (TextView) itemView.findViewById(R.id.promotionLabel);
            promotionImage = (ImageView) itemView.findViewById(R.id.promotionImage);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(view.getContext(), ChooseDealActivity.class);
//                    view.getContext().startActivity(intent);
//                }
//            });

        }
    }
}
