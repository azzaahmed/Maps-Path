package com.esraa.sketymap2;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell on 19/10/16.
 */
public class RoutInformation implements Serializable {

//    private LatLng source;
//    private LatLng destination;
    double lat_source;
    double lng_source;
    double lat_dest;
    double lng_dest;
    private String source_name;
    private String dest_name;

//    ArrayList<LatLng> pointsOfRout;
    ArrayList<Double> points_lat;
    ArrayList<Double> points_lng;


//    public RoutInformation(LatLng source,LatLng destination){
//        this.source = source;
//        this.destination = destination;
//    }

    public RoutInformation(String source_name, String dest_name){
        this.source_name = source_name;
        this.dest_name = dest_name;
    }

    public RoutInformation(LatLng source, LatLng destination, String source_name, String dest_name){
        this.lat_source = source.latitude;
        this.lng_source = source.longitude;
        this.lat_dest = destination.latitude;
        this.lng_dest = destination.longitude;

        this.dest_name = dest_name;
        this.source_name = source_name;
    }

    public void setPointsOfRout(ArrayList<LatLng> pointsOfRout){
//        this.pointsOfRout=pointsOfRout;
        this.points_lat = new ArrayList<>();
        this.points_lng = new ArrayList<>();
        for(int i = 0 ; i<pointsOfRout.size();i++){
            this.points_lat.add(pointsOfRout.get(i).latitude);
            this.points_lng.add(pointsOfRout.get(i).longitude);
        }
    }

    public ArrayList<LatLng> getPointsOfRout(){
        ArrayList<LatLng> pointsOfRout = new ArrayList<>();
        for(int i = 0 ; i<points_lng.size();i++){
            pointsOfRout.add(new LatLng(points_lat.get(i),points_lng.get(i)));
        }
        return pointsOfRout;
    }

//    public void setSource_name(String source_name){
//        this.source_name = source_name;
//    }
//
//    public void setDest_name(String dest_name){
//        this.dest_name = dest_name;
//    }

    public double getLat_source(){
        return lat_source;
    }
    public double getLng_source(){
        return lng_source;
    }
    public double getLat_dest(){
        return lat_dest;
    }
    public double getLng_dest(){
        return lng_dest;
    }

    public LatLng getSource(){
        return new LatLng(this.lat_source,this.lng_source);
    }
    public LatLng getDestination(){
        return new LatLng(this.lat_dest,this.lng_dest);
    }

    public String getSource_name(){
        return this.source_name;
    }
    public String getDest_name(){
        return dest_name;
    }
}
