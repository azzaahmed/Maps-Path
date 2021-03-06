package com.esraa.sketymap2;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dell on 18/10/16.
 */
public class PathJSONParser {

    public List<List<HashMap<String, String>>> parse(JSONObject jObject) {
        List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String, String>>>();
        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;
        try {
            jRoutes = jObject.getJSONArray("routes");
            /** Traversing all routes */
            for (int i = 0; i < jRoutes.length(); i++) {
                jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                List<HashMap<String, String>> path = new ArrayList<HashMap<String, String>>();

                /** Traversing all legs */
                for (int j = 0; j < jLegs.length(); j++) {
                    jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");

                    /** Traversing all steps */
                    for (int k = 0; k < jSteps.length(); k++) {
                        String polyline = "";
                        polyline = (String) ((JSONObject) ((JSONObject) jSteps
                                .get(k)).get("polyline")).get("points");
                        List<LatLng> list = decodePoly(polyline);

                        /** Traversing all points */
                        for (int l = 0; l < list.size(); l++) {
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("lat",
                                    Double.toString(((LatLng) list.get(l)).latitude));
                            hm.put("lng",
                                    Double.toString(((LatLng) list.get(l)).longitude));
                            path.add(hm);
                        }
                    }
                    routes.add(path);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }
        return routes;
    }


    public ArrayList<LatLng> getStepsLatLng(JSONObject jObject) {

//        List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String, String>>>();
        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;
        ArrayList<LatLng> latLngArrayList = new ArrayList<>();
        try {
            jRoutes = jObject.getJSONArray("routes");
            /** Traversing all routes */
            for (int i = 0; i < jRoutes.length(); i++) {
                jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                List<HashMap<String, String>> path = new ArrayList<HashMap<String, String>>();
                /** Traversing all legs */

                for (int j = 0; j < jLegs.length(); j++) {
                    jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");

                    for (int k = 0; k < jSteps.length(); k++) {
                        double latStart ;
                        latStart = (double) ((JSONObject) ((JSONObject) jSteps
                                .get(k)).get("start_location")).get("lat");
                        double LngStrat ;
                        LngStrat = (double) ((JSONObject) ((JSONObject) jSteps
                                .get(k)).get("start_location")).get("lng");
                        double latEnd ;
                        latEnd = (double) ((JSONObject) ((JSONObject) jSteps
                                .get(k)).get("end_location")).get("lat");
                        double LngEnd ;
                        LngEnd = (double) ((JSONObject) ((JSONObject) jSteps
                                .get(k)).get("end_location")).get("lng");

                        latLngArrayList.add(new LatLng(latStart,LngStrat));
                        latLngArrayList.add(new LatLng(latEnd,LngEnd));

                    }

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }
        return latLngArrayList;
    }

    /**
     * Method Courtesy :
     * jeffreysambells.com/2010/05/27
     * /decoding-polylines-from-google-maps-direction-api-with-java
     * */
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }


    public Integer getDist(JSONObject jObject){

        List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String, String>>>();
        List<Integer> allDist= new ArrayList<>();
        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONObject jTotDist = null;
        Integer totDist=null;
        try {
            jRoutes = jObject.getJSONArray("routes");
            /** Traversing all routes */
            for (int i = 0; i < jRoutes.length(); i++) {
                jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");

                /** Traversing all legs */
                for (int j = 0; j < jLegs.length(); j++) {
                    //jTotDist = ((JSONObject) jLegs.get(j)).getJSONObject("distance").getJSONObject("value");
                    totDist=(int)((JSONObject) ((JSONObject) jLegs
                            .get(j)).get("distance")).get("value");
                    //allDist.add((Integer.parseInt(jTotDist.toString())));
                    allDist.add(totDist);
                }
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }catch(Exception e){
        }
        Integer min=allDist.get(0);
        for (int i=1;i<allDist.size()-1;i++){
            if(allDist.get(i)<min){
                min=allDist.get(i);
            }
        }
        return min;
    }
}
