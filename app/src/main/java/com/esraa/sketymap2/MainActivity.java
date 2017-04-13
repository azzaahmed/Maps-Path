package com.esraa.sketymap2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

//    private GoogleMap myGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//
//        mapFragment.getMapAsync(this);
    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//
//        myGoogleMap = googleMap;
////
////        myGoogleMap.addMarker(new MarkerOptions().position(new LatLng(31.205753, 29.924526)));
////
////        myGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.205753, 29.924526), 18));
//
//        // Creating a marker
//        MarkerOptions markerOptions = new MarkerOptions();
//
//        LatLng latLng = new LatLng(31.205753, 29.924526);
//        // Setting the position for the marker
//        markerOptions.position(latLng);
//
//        // Setting the title for the marker.
//        // This will be displayed on taping the marker
//        markerOptions.title(latLng.latitude + " : " + latLng.longitude);
//
//        // Clears the previously touched position
//        myGoogleMap.clear();
//
//
//
//        // Placing a marker on the touched position
//        myGoogleMap.addMarker(markerOptions);
//        //                choosenLatLng = latLng;
//        // Animating to the touched position
//        myGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
//    }
}
