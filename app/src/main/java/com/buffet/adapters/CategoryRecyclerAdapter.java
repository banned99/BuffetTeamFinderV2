package com.buffet.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.app.FragmentManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.buffet.activities.ChooseBranchActivity;
import com.buffet.activities.MainActivity;
import com.buffet.fragments.CategoryFragment;
import com.buffet.fragments.CategoryPromotionFragment;
import com.buffet.fragments.CategoryRootFragment;
import com.buffet.models.Promotion;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import ggwp.caliver.banned.buffetteamfinderv2.R;

/**
 * Created by NattKS on 11/7/2016.
 */

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.ViewHolder>{

    private LayoutInflater inflater;
    List<String> categories = Collections.emptyList();


    public CategoryRecyclerAdapter(Context context, List<String> categories) {
        inflater = LayoutInflater.from(context);
        this.categories = categories;
    }

    @Override
    public CategoryRecyclerAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_recycler_child, parent, false);
        return new CategoryRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CategoryRecyclerAdapter.ViewHolder holder, int position) {

        holder.categoryLabel.setText("Category " + (position+1));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CategoryPromotionFragment categoryPromotionFragment = CategoryPromotionFragment.newInstance();
                FragmentTransaction transaction = ((AppCompatActivity) view.getContext()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.root_category, categoryPromotionFragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

    }


    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView categoryLabel;
        ImageView categoryImage;


        public ViewHolder(View itemView) {
            super(itemView);

            categoryLabel = (TextView) itemView.findViewById(R.id.category_name);
            categoryImage = (ImageView) itemView.findViewById(R.id.category_image);

        }
    }
}
