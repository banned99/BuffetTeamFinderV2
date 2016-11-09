package com.buffet.customs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ggwp.caliver.banned.buffetteamfinderv2.R;

/**
 * Created by NattKS on 11/9/2016.
 */

public class CategoryCustomListView extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    List<String> categories;

    public CategoryCustomListView(Context context, List<String> categories) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.categories = categories;
    }
    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int i) {
        return categories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;

        if (view == null) {
            view = inflater.inflate(R.layout.categories_recycler_child, null);
            holder = new ViewHolder();
            holder.categoryLabel = (TextView) view.findViewById(R.id.category_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.categoryLabel.setText(" Category " + i);
        return view;
    }

    public class ViewHolder {

        TextView categoryLabel;
        ImageView categoryImage;

    }


}
