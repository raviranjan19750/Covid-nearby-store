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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<HashMap<String, String>> placesList;
    private Context context;
    private GoogleMap mMap;


    public RecyclerViewAdapter(List<HashMap<String, String>> list, Context ctx, GoogleMap mMap) {
        placesList = list;
        context = ctx;
        this.mMap = mMap;
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
        final HashMap<String, String> place = placesList.get(position);
        if (place.get("place_name") != null) {

            holder.rowLinearLayout.setVisibility(View.VISIBLE);
            holder.titleTextView.setText(String.valueOf(place.get("place_name")));
            holder.addressTextView.setText(String.valueOf(place.get("vicinity")));
            holder.rowLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showOnMap(place);
                }
            });


            if (place.containsValue("icon"))
                Glide.with(context).asBitmap().load(String.valueOf(place.get("icon"))).into(holder.iconImageView);


        } else {
            holder.rowLinearLayout.setVisibility(View.GONE);
        }


    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;
        public TextView addressTextView;
        public ImageView iconImageView;
        public LinearLayout rowLinearLayout;

        public Button viewOnMap;

        public ViewHolder(View view) {

            super(view);

            titleTextView = view.findViewById(R.id.titleTextView);
            addressTextView = view.findViewById(R.id.addressTextView);
            iconImageView = view.findViewById(R.id.iconImageView);
            rowLinearLayout = view.findViewById(R.id.rowLinearLayout);

        }
    }


    @Override
    public int getItemCount() {
        return placesList.size();
    }

    private void showOnMap( HashMap<String, String> place){

        double lat = Double.parseDouble(place.get("lat"));
        double lng = Double.parseDouble(place.get("lng"));

        LatLng latLng = new LatLng(lat,lng);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18), 2500, null);

    }

}



