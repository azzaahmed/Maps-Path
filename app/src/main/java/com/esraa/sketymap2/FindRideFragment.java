package com.esraa.sketymap2;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FindRideFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    RoutInformation myRoutInformation;

    private FragmentActivity myContext;

    public static final String TAG = "FindRideFragment";
    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    public static final int RESULT_OK = -1;
    public static final int RESULT_CANCELED = 0;
    Activity activity;

    private GoogleMap myGoogleMap;
    MarkerOptions markerOptions_from;
    MarkerOptions markerOptions_to;

    EditText mFromLocation;
    EditText mWhereTo;
    private Button findTawsela;

    boolean from_place_boolean=false;
    boolean to_place_boolean=false;

    LatLng fromLatLng;
    LatLng toLatLng;
    private GoogleApiClient mGoogleApiClient;
    boolean mapReady = false;
    protected Location mlocation;
    private final String LOG_TAG = "Map app";

    public FindRideFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_find_ride, container, false);
        activity = getActivity();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager()
//                .findFragmentById(R.id.map);
        //    MapFragment mapFragment=(MapFragment) view.findViewById(R.id.map);
//        FragmentManager fragManager = myContext.getSupportFragmentManager(); //If using fragments from support v4
//        SupportMapFragment mapFragment = (SupportMapFragment)fragManager.findFragmentById(R.id.map);


        //  mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFromLocation = (EditText) view.findViewById(R.id.search_from);
        mWhereTo = (EditText) view.findViewById(R.id.search_to);
        findTawsela = (Button) view.findViewById(R.id.match);

        mFromLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(getActivity());
                    from_place_boolean = true;
                    to_place_boolean = false;
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });

        mWhereTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(getActivity());
                    from_place_boolean = false;
                    to_place_boolean = true;
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });

       /* findTawsela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                LatLng Alex1 = new LatLng(31.1293737,29.924526);
//                LatLng Alex2 = new LatLng(31.2,29.9);
//                String url = getMapsApiDirectionsUrl(Alex1,Alex2);

                if(isOnline()) {
                    if(!mFromLocation.getText().equals("") && !mWhereTo.getText().equals("")) {
                        myRoutInformation = new RoutInformation(fromLatLng, toLatLng, mFromLocation.getText() + "", mWhereTo.getText() + "");
//                        pointsTextView.setText("from LatLng: " + fromLatLng + "to LatLng: " + toLatLng);
                        Log.v(TAG, "LatLng: " + fromLatLng + "," + toLatLng);
                        String url = getMapsApiDirectionsUrl(fromLatLng, toLatLng);
                        ReadURLTask readURLTask = new ReadURLTask(getActivity());
                        readURLTask.execute(url);
                    }
                    else{
                        Toast toast = Toast.makeText(getActivity(), "Please Enter both source and destination !", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
                else{
                    Toast toast = Toast.makeText(getActivity(), "Please check your Internet connection !", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
*/
        return view;
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

//            CameraPosition current = CameraPosition.builder()
//                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
//                    .zoom(18)
//                    .bearing(0)
//                    .tilt(45)
//                    .build();
//            m_map.moveCamera(CameraUpdateFactory.newCameraPosition(current));

        myGoogleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        myGoogleMap.setMyLocationEnabled(true);
        mapReady=true;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                Log.i(TAG, resultCode + " Place: " + place.getName());
                if (from_place_boolean && !to_place_boolean) {
                    mFromLocation.setText(place.getName());
                    fromLatLng = place.getLatLng();

                    pinOnMap();
                } else if (to_place_boolean && !from_place_boolean) {
                    mWhereTo.setText(place.getName());
                    toLatLng = place.getLatLng();

                    pinOnMap();
                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }/////////////////////////////////
        }
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    private void pinOnMap() {

        markerOptions_from.position(fromLatLng);
        markerOptions_to.position(toLatLng);
        // Setting the title for the marker.
        // This will be displayed on taping the marker
        markerOptions_from.title(fromLatLng.latitude + " : " + fromLatLng.longitude);
        markerOptions_to.title(toLatLng.latitude + " : " + toLatLng.longitude);

        // Clears the previously touched position
        myGoogleMap.clear();

        // Placing a marker on the touched position
        myGoogleMap.addMarker(markerOptions_from);
        myGoogleMap.addMarker(markerOptions_to);



        // Animating to the touched position
        if (from_place_boolean && !to_place_boolean) {
            myGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(fromLatLng, 18));
        } else if (to_place_boolean && !from_place_boolean) {
            myGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(toLatLng, 18));

            getPathPoints();

         //   myGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(fromLatLng, 18));


        }

//        else if(from_place_boolean&&to_place_boolean){
//            PolylineOptions poption = new PolylineOptions().add(fromLatLng).add(toLatLng).width(5).color(Color.BLUE).geodesic(true);
//            myGoogleMap.addPolyline(poption);
//            myGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(fromLatLng, 18));
//        }
    }
public void getPathPoints(){
    if(isOnline()) {
        if(!mFromLocation.getText().equals("") && !mWhereTo.getText().equals("")) {
            myRoutInformation = new RoutInformation(fromLatLng, toLatLng, mFromLocation.getText() + "", mWhereTo.getText() + "");
//                        pointsTextView.setText("from LatLng: " + fromLatLng + "to LatLng: " + toLatLng);
            Log.v(TAG, "LatLng: " + fromLatLng + "," + toLatLng);
            String url = getMapsApiDirectionsUrl(fromLatLng, toLatLng);
            ReadURLTask readURLTask = new ReadURLTask(getActivity());
            readURLTask.execute(url);
        }
        else{
            Toast toast = Toast.makeText(getActivity(), "Please Enter both source and destination !", Toast.LENGTH_LONG);
            toast.show();
        }
    }
    else{
        Toast toast = Toast.makeText(getActivity(), "Please check your Internet connection !", Toast.LENGTH_LONG);
        toast.show();
    }
}
    public void drawpath(){

        MarkerOptions borders[]=new MarkerOptions[2];
        borders[0]=markerOptions_from;
        borders[1]=markerOptions_to;

        ArrayList<LatLng> pointsOfRout=myRoutInformation.getPointsOfRout();

//        ArrayList <LatLng> markers[]=new LatLng[pointsOfRout.size()];
//     // MarkerOptions temp= new MarkerOptions();
//        for(int i=0;i<pointsOfRout.size();i++){
//            markers[i]=pointsOfRout.get(i);
//
//        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (MarkerOptions marker : borders) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        PolylineOptions poption = new PolylineOptions().addAll(pointsOfRout).width(5).color(Color.BLUE).geodesic(true);
        myGoogleMap.addPolyline(poption);
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);
        myGoogleMap.moveCamera(cu);
        myGoogleMap.animateCamera(cu);
    }
    @Override
    public void onStart() {
        super.onStart();
        //connect the client
        mGoogleApiClient.connect();

    }

    @Override
    public void onStop() {
        //disconnect the client
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
        super.onStop();

    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mlocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mapReady){
            updatemap();
        }
            /*   if (mlocation != null) {
            txtOutput.setText(Double.toString(mlocation.getLatitude()));
            txtOutput2.setText(Double.toString(mlocation.getLongitude()));
        }*/
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(LOG_TAG, "GoogleApiClient connection has been suspend");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(LOG_TAG, "GoogleApiClient connection has failed  " + connectionResult.getErrorCode());

    }

public void updatemap(){
    markerOptions_from = new MarkerOptions();
    markerOptions_to = new MarkerOptions();


    fromLatLng =new LatLng(mlocation.getLatitude(),mlocation.getLongitude());
    toLatLng = new LatLng(mlocation.getLatitude(),mlocation.getLongitude());

    // Setting the position for the marker
    markerOptions_from.position(fromLatLng);
    markerOptions_to.position(toLatLng);
    // Setting the title for the marker.
    // This will be displayed on taping the marker
    markerOptions_from.title(fromLatLng.latitude + " : " + fromLatLng.longitude);
    markerOptions_to.title(toLatLng.latitude + " : " + toLatLng.longitude);

    // Clears the previously touched position
    myGoogleMap.clear();

    // Placing a marker on the touched position
    myGoogleMap.addMarker(markerOptions_from);
    myGoogleMap.addMarker(markerOptions_to);

    // Animating to the touched position
    myGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(fromLatLng, 18));
}


       private String getMapsApiDirectionsUrl(LatLng src,LatLng dest) {
   //https://maps.googleapis.com/maps/api/directions/json?origin=31.1293737,%2029.924526&destination=31.2,29.99&key=AIzaSyAcOtMU7qwUHhsGROcmP5J4G2cv5ME8nDI
           String params = "origin="+src.latitude + "," + src.longitude+"&destination="+dest.latitude + ","
                   + dest.longitude + "&key=AIzaSyAcOtMU7qwUHhsGROcmP5J4G2cv5ME8nDI";
           String output = "json";
           String url = "https://maps.googleapis.com/maps/api/directions/"
                   + output + "?" + params;
           return url;
       }

       private class ReadURLTask extends AsyncTask<String,Void, String> {

           private Context context;

           public ReadURLTask(Context context){
               this.context=context;
           }

           @Override
           protected String doInBackground(String... param) {
               String data = "";
               try {
                   HttpConnection http = new HttpConnection();
                   data = http.readUrl(param[0]);
               } catch (Exception e) {
                   Log.d("Background Task", e.toString());
               }
               return data;
           }

           @Override
           protected void onPostExecute(String result) {
               super.onPostExecute(result);
               new ParserTask(context).execute(result);
           }
       }

    private class ParserTask extends
            AsyncTask<String, ArrayList<LatLng>,  ArrayList<LatLng>> {

        private Context context;
        public ParserTask(Context context){
            this.context=context;
        }
        @Override
        protected  ArrayList<LatLng> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            ArrayList<LatLng> latLngArrayList = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                latLngArrayList = parser.getStepsLatLng(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return latLngArrayList;
        }

        @Override
        protected void onPostExecute( ArrayList<LatLng> points) {
            super.onPostExecute(points);

            myRoutInformation.setPointsOfRout(points);

            Log.v(TAG, "Done points for test: " + points.size() );

//            Intent myIntent = new Intent(context, Match.class);
////                myIntent.putExtra("routs",ArrayList<RoutInformation>newRouts);
//            Bundle myBundle = new Bundle();
//            myBundle.putSerializable("routs",myRoutInformation);
//            myIntent.putExtras(myBundle);
//            context.startActivity(myIntent);
//            //********************** end of update *******************************//

            drawpath();
        }
    }



}
