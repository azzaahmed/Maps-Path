package com.esraa.sketymap2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MatchFragment extends Fragment {

    TextView from;
    TextView to;
    TextView steps;

    RoutInformation myRoutInformation;

    public MatchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match, container, false);

        from =(TextView) view.findViewById(R.id.latlng_from);
        to =(TextView) view.findViewById(R.id.latlng_to);
        steps =(TextView) view.findViewById(R.id.latlng_steps);


        Intent intent = getActivity().getIntent();
        if (null != intent) { //Null Checking
            myRoutInformation=(RoutInformation)intent.getSerializableExtra("routs");

            from.setText("lat: "+myRoutInformation.getSource().latitude+"lng: "+myRoutInformation.getSource().longitude+"name: "+myRoutInformation.getSource_name());
            to.setText("lat: "+myRoutInformation.getDestination().latitude+"lng: "+myRoutInformation.getDestination().longitude+"name: "+myRoutInformation.getDest_name());

            for (int i=0;i<myRoutInformation.points_lat.size();i++){
                steps.setText(steps.getText() + "lat: "+myRoutInformation.points_lat.get(i)+"Lng :"+myRoutInformation.points_lng.get(i));
            }
        }



        return view;
    }
}
