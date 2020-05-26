package com.iteyes.placesproject;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;


class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

    private String googlePlacesData;
    private GoogleMap mMap;
    String url;
    Context context;
    RecyclerView recyclerView;

    public GetNearbyPlacesData(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap) objects[0];
        url = (String) objects[1];

        DownloadURL downloadURL = new DownloadURL();
        try {
            googlePlacesData = downloadURL.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s) {

        List<HashMap<String, String>> nearbyPlaceList;
        DataParser parser = new DataParser();
        Log.e("TAG", "onPostExecute: " + s);
        nearbyPlaceList = parser.parse(s);
        Log.e("TAG", "onPostExecute 1: " + nearbyPlaceList.toString());
        Log.d("nearbyplacesdata", "called parse method");

        showNearbyPlaces(nearbyPlaceList);
    }

    private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlaceList) {
        Log.e("TAG", "onPostExecute 2: " + nearbyPlaceList.toString());

        for (int i = 0; i < nearbyPlaceList.size(); i++) {
            Log.e("TAG", "onPostExecute 3: " + i + " " + nearbyPlaceList.get(i).toString());

            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = nearbyPlaceList.get(i);

            if (googlePlace.get("place_name") != null) {

                String placeName = String.valueOf(googlePlace.get("place_name"));
                String vicinity = String.valueOf(googlePlace.get("vicinity"));

                double lat = Double.parseDouble(googlePlace.get("lat"));
                double lng = Double.parseDouble(googlePlace.get("lng"));

                LatLng latLng = new LatLng(lat, lng);
                markerOptions.position(latLng);
                markerOptions.title(placeName);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
            }


        }

        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(nearbyPlaceList, context);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);


    }
}
