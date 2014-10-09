package com.hackathon.playground.app.activity;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.hackathon.playground.app.R;
import com.hackathon.playground.app.model.Amenity;
import com.hackathon.playground.app.model.PointOfInterest;
import com.hackathon.playground.app.orm.PlaygroundResolver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Author: Dave
 */
public class AmenitiesListFragment extends ListFragment {

    private PlaygroundResolver resolver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.amenities_list, container, false);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        resolver = new PlaygroundResolver(activity);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        PointOfInterest pointOfInterest = getActivity().getIntent().getParcelableExtra(PointOfInterestDetailsActivity.POINT_OF_INTEREST);

        try {
            List<Amenity> amenities = resolver.getAmenitiesForGroup(pointOfInterest.getAmenityGroup());

            Set<String> amenityNames = new HashSet<>();
            for(Amenity amenity : amenities) {
                amenityNames.add(amenity.getAmenity());
            }

            setListAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, new ArrayList<>(amenityNames)));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
