package com.example.smart_navi;
/**
 * Created by manjari on 12/4/17.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment
{
    Button Btnexit, BtnService, Btntrip,BtnCoor,Btnstreet,BtnLoc;

    public HomeFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Btnexit = (Button) view.findViewById(R.id.BtnExit);
        BtnService = (Button) view.findViewById(R.id.BtnService);
        Btntrip = (Button) view.findViewById(R.id.BtnTrips);
        BtnCoor = (Button) view.findViewById(R.id.BtnCoor);
        Btnstreet = (Button) view.findViewById(R.id.BtnStreets);
        BtnLoc = (Button) view.findViewById(R.id.BtnLoc);



        BtnService.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {

                MainActivity mainActivity = (MainActivity) getActivity();

                mainActivity.catchData("Service");
            }
        });

        Btntrip.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                MainActivity mainActivity = (MainActivity) getActivity();

                mainActivity.catchData("Trip");
            }
        });

        Btnstreet.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                MainActivity mainActivity = (MainActivity) getActivity();

                mainActivity.catchData("Street");
            }
        });

        BtnCoor.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                MainActivity mainActivity = (MainActivity) getActivity();

                mainActivity.catchData("coordinate");
            }
        });

        BtnLoc.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                MainActivity mainActivity = (MainActivity) getActivity();

                mainActivity.catchData("Location");
            }
        });


        Btnexit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                getActivity().finish();
            }
        });

        return view;
    }


    public boolean onCreateOptionsMenu(Menu menu)
    {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        return super.onOptionsItemSelected(item);
    }
}


