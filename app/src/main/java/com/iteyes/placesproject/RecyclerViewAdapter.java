package com.iteyes.placesproject;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.libraries.places.api.model.Place;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<HashMap<String, String>> placesList;
    private Context context;


    public RecyclerViewAdapter(List<HashMap<String, String>> list, Context ctx) {
        placesList = list;
        context = ctx;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.places_item, parent, false);

        RecyclerViewAdapter.ViewHolder viewHolder = new RecyclerViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        final int itemPos = position;

        Boolean isOpen;
        HashMap<String, String> place = placesList.get(position);
        if (place.get("place_name") != null) {

            holder.rowLinearLayout.setVisibility(View.VISIBLE);
            holder.titleTextView.setText(String.valueOf(place.get("place_name")));
            holder.addressTextView.setText(String.valueOf(place.get("vicinity")));

            if (place.containsValue("icon"))
                Glide.with(context).asBitmap().load(String.valueOf(place.get("icon"))).into(holder.iconImageView);

            if (place.containsValue("isOpen")) {

                if (place.get("isOpen").equalsIgnoreCase("true")){
                    holder.isOpenTextView.setText(place.get("isOpen"));
                }else {
                    holder.isOpenTextView.setText(place.get("isOpen"));
                }

            }

        } else {
            holder.rowLinearLayout.setVisibility(View.GONE);
        }


    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;
        public TextView addressTextView;
        public TextView isOpenTextView;
        public ImageView iconImageView;
        public LinearLayout rowLinearLayout;

        public Button viewOnMap;

        public ViewHolder(View view) {

            super(view);

            titleTextView = view.findViewById(R.id.titleTextView);
            addressTextView = view.findViewById(R.id.addressTextView);
            isOpenTextView = view.findViewById(R.id.isOpenTextView);
            iconImageView = view.findViewById(R.id.iconImageView);
            rowLinearLayout = view.findViewById(R.id.rowLinearLayout);

        }
    }


    @Override
    public int getItemCount() {
        return placesList.size();
    }

}



