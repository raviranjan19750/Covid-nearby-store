package com.iteyes.placesproject;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FilterIssueGridViewAdapter extends RecyclerView.Adapter<FilterIssueGridViewAdapter.MyHolder> {


    Context context;
    ArrayList<String> filterTitle;
    GoogleMap mMap;
    private int selectedPosition = -1;
    List<HashMap<String, String>> list;
    ArrayList<List<HashMap<String, String>>> nearbyPlaceList;
    RecyclerViewAdapter recyclerViewAdapter;




    public FilterIssueGridViewAdapter(Context context, ArrayList<String> filterTitle, GoogleMap mMap, List<HashMap<String, String>> list, ArrayList<List<HashMap<String, String>>> nearbyPlaceList, RecyclerViewAdapter recyclerViewAdapter) {

        this.context = context;
        this.filterTitle = filterTitle;
        this.mMap = mMap;
        this.list = list;
        this.nearbyPlaceList = nearbyPlaceList;
        this.recyclerViewAdapter = recyclerViewAdapter;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_row, parent, false);


        return new MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {

        holder.mapFilterTitleTextView.setText(filterTitle.get(position));

        if (position == selectedPosition) {

            holder.mapFilterTitleTextView.setBackground(context.getResources().getDrawable(R.drawable.text_bg_round_fill));
            holder.mapFilterTitleTextView.setTextColor(context.getResources().getColor(R.color.white));

        } else {

            holder.mapFilterTitleTextView.setBackground(context.getResources().getDrawable(R.drawable.text_bg_round));
            holder.mapFilterTitleTextView.setTextColor(context.getResources().getColor(R.color.Green_XD));
        }

        holder.mapFilterTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                list.clear();
                mMap.clear();
                selectedPosition = position;

                for (int i = 0; i < nearbyPlaceList.get(selectedPosition).size(); i++) {

                    MarkerOptions markerOptions = new MarkerOptions();

                    HashMap<String, String> googlePlace = nearbyPlaceList.get(selectedPosition).get(i);

                    if (googlePlace.get("place_name") != null) {

                        list.add(nearbyPlaceList.get(selectedPosition).get(i));

                        String placeName = String.valueOf(googlePlace.get("place_name"));

                        double lat = Double.parseDouble(googlePlace.get("lat"));
                        double lng = Double.parseDouble(googlePlace.get("lng"));

                        LatLng latLng = new LatLng(lat, lng);
                        markerOptions.position(latLng);
                        markerOptions.title(placeName);
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                        mMap.addMarker(markerOptions);

                    }

                }

              //  mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15), 5000, null);


                recyclerViewAdapter.notifyDataSetChanged();
                notifyDataSetChanged();

                Toast.makeText(context, holder.mapFilterTitleTextView.getText().toString(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView mapFilterTitleTextView;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            mapFilterTitleTextView = itemView.findViewById(R.id.mapFilterTitleTextView);
        }
    }

    @Override
    public int getItemCount() {

        return filterTitle.size();
    }


}
