package com.example.assignment1.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.assignment1.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Fragment_Map extends Fragment {
    private GoogleMap myMap;
    SupportMapFragment mapFragment;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            LatLng sydney = new LatLng( 13.736717, 100.523186); //The initial map
            myMap = googleMap;
            myMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            myMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_map, container, false);
        mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        return view;
    }

    public void setLocationOnMap(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        if (myMap != null) {
            myMap.addMarker(new MarkerOptions().position(latLng).title("Player position"));
            myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}