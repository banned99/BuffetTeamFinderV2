package com.buffet.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.buffet.activities.ChooseBranchActivity;
import com.buffet.models.Promotion;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.List;

import ggwp.caliver.banned.buffetteamfinderv2.R;

import static java.security.AccessController.getContext;

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
        final int promotion_id = current.getProId();
        final String promotion_name = current.getProName();
        final Double promotion_price = current.getPrice();
        final String promotion_date_start = current.getDateStart();
        final String promotion_expire = current.getExpire();
        final int promotion_max_person = current.getMaxPerson();
        final String promotion_image = current.getImage();
        final String promotion_catname = current.getCatName();
        final String promotion_description = current.getDescription();

        holder.promotionLabel.setText(promotion_name);

        holder.promotionPrice.setText("ราคา " + Double.toString(promotion_price) + " บาท");
        holder.promotionMax.setText("จำนวน " + Integer.toString(promotion_max_person) + " คน");
        holder.promotionExpire.setText("ถึง " + promotion_expire);
        holder.promotionCat.setText(promotion_catname);
        Picasso.with(holder.itemView.getContext()).load("http://api.tunacon.com/images/"+promotion_image).resize(1200, 650).into(holder.promotionImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = holder.promotionLabel.getText().toString();
                System.out.println("name = " + name + " id = " + promotion_id);

                Intent chooseProIntent = new Intent(v.getContext(), ChooseBranchActivity.class);
                chooseProIntent.putExtra("promotion_id", promotion_id);
                chooseProIntent.putExtra("promotion_name", promotion_name);
                chooseProIntent.putExtra("promotion_price", promotion_price);
                chooseProIntent.putExtra("promotion_date_start", promotion_date_start);
                chooseProIntent.putExtra("promotion_expire", promotion_expire);
                chooseProIntent.putExtra("promotion_max_person", promotion_max_person);
                chooseProIntent.putExtra("promotion_image", promotion_image);
                chooseProIntent.putExtra("promotion_catname", promotion_catname);
                chooseProIntent.putExtra("promotion_description", promotion_description);
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
        TextView promotionPrice;
        TextView promotionMax;
        TextView promotionExpire;
        ImageView promotionImage;
        TextView promotionCat;


        public ViewHolder(View itemView) {
            super(itemView);

            promotionLabel = (TextView) itemView.findViewById(R.id.promotionLabel);
            promotionPrice = (TextView) itemView.findViewById(R.id.promotionPrice);
            promotionMax = (TextView) itemView.findViewById(R.id.promotionMax);
            promotionExpire = (TextView) itemView.findViewById(R.id.promotionExpire);
            promotionImage = (ImageView) itemView.findViewById(R.id.promotionImage);
            promotionCat = (TextView) itemView.findViewById(R.id.pro_catname);
        }
    }
}
