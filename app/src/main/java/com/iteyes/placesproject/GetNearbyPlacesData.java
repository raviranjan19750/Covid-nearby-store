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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

    private ArrayList<String> googlePlacesData = new ArrayList<>();
    private GoogleMap mMap;
    ArrayList<String> url = new ArrayList<>();
    Context context;


    ArrayList<List<HashMap<String, String>>> nearbyPlaceList ;
    List<HashMap<String, String>> currentList  ;

    RecyclerViewAdapter recyclerViewAdapter;



    public GetNearbyPlacesData(Context context, RecyclerViewAdapter recyclerViewAdapter , ArrayList<List<HashMap<String, String>>> nearbyPlaceList,  List<HashMap<String, String>> currentList ) {
        this.context = context;
        this.recyclerViewAdapter = recyclerViewAdapter;
        this.nearbyPlaceList = nearbyPlaceList;
        this.currentList = currentList;

    }

    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap) objects[0];

        url.clear();
        for (int i = 1; i <objects.length ; i++) {

            url.add((String) objects[i]);
        }

        DownloadURL downloadURL = new DownloadURL();

        try {

            for (int i = 0; i <url.size() ; i++) {

                googlePlacesData.add(downloadURL.readUrl(url.get(i)));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    protected void onPostExecute(String s) {

        DataParser parser = new DataParser();

        for (int i = 0; i <googlePlacesData.size() ; i++) {

            nearbyPlaceList.add(parser.parse(googlePlacesData.get(i)));

        }

        currentList.clear();

        showNearbyPlaces();
    }

    private void showNearbyPlaces() {

        for (int i = 0; i < nearbyPlaceList.size(); i++) {

            for (int j = 0; j <nearbyPlaceList.get(i).size() ; j++) {

                MarkerOptions markerOptions = new MarkerOptions();

                HashMap<String, String> googlePlace = nearbyPlaceList.get(i).get(j);

                if (googlePlace.get("place_name") != null) {

                    currentList.add(nearbyPlaceList.get(i).get(j));

                    String placeName = String.valueOf(googlePlace.get("place_name"));

                    double lat = Double.parseDouble(googlePlace.get("lat"));
                    double lng = Double.parseDouble(googlePlace.get("lng"));

                    LatLng latLng = new LatLng(lat, lng);
                    markerOptions.position(latLng);
                    markerOptions.title(placeName);
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                    mMap.addMarker(markerOptions);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15), 5000, null);

                }

            }

        }

        recyclerViewAdapter.notifyDataSetChanged();

    }
}
