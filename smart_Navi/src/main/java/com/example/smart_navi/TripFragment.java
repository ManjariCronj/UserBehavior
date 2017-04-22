package com.example.smart_navi;
/**
 * Created by manjari on 12/4/17.
 */

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TripFragment extends Fragment
{

    TableLayout Table;

    DatabaseHandler DB;

    public TripFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trip, container, false);

        Table = (TableLayout) view.findViewById(R.id.displayLinear);

        DB = new DatabaseHandler(getActivity().getApplicationContext());

        Bundle bundle = getArguments();

        String string = bundle.getString("data");

        if(string != null)
        {
            if(string.equals("Trip"))
            {
                TripHeader();
                PrintTrips(DB.getAllTrips());
            }
            else if(string.equals("coordinate"))
            {
                CoorHeader();
                PrintCoordinates(DB.GetAllCoordinates(""));
            }
            else if(string.equals("Street"))
            {
                StreetHeader();
                PrintStreets(DB.GetAllStreets(""));
            }
        }

        return view;

    }
    void PrintTrips(List<Trip> Trips)
    {
        for(Trip trp : Trips)
        {
            TableRow tbrow0 = new TableRow(getActivity());
            tbrow0.setBackgroundColor(Color.WHITE);
            tbrow0.addView(AddTextView(trp.ID));
            tbrow0.addView(AddTextView(trp.Day_Type));
            tbrow0.addView(AddTextView(trp.date));
            tbrow0.addView(AddTextView(trp.Time));
            tbrow0.addView(AddTextView(trp.Destinations));
            Table.addView(tbrow0);
        }
    }

    void PrintCoordinates(List<Coordinates> coors)
    {
        for(Coordinates coor : coors)
        {
            TableRow tbrow0 = new TableRow(getActivity());
            tbrow0.setBackgroundColor(Color.WHITE);
            tbrow0.addView(AddTextView(coor.CID));
            tbrow0.addView(AddTextView(coor.Lati));
            tbrow0.addView(AddTextView(coor.Longi));
            tbrow0.addView(AddTextView(coor.TID));
            Table.addView(tbrow0);
        }
    }

    void PrintStreets(List<Street> streets)
    {
        for(Street str : streets)
        {
            TableRow tbrow0 = new TableRow(getActivity());
            tbrow0.setBackgroundColor(Color.WHITE);
            tbrow0.addView(AddTextView(str.SID));
            tbrow0.addView(AddTextView(str.Street));
            tbrow0.addView(AddTextView(str.Area));
            tbrow0.addView(AddTextView(str.City));
            tbrow0.addView(AddTextView(str.State));
            tbrow0.addView(AddTextView(str.TID));
            Table.addView(tbrow0);
        }
    }

    TextView AddTextView(String Text)
    {
        TextView tv0 = new TextView(getActivity());
        tv0.setText("  "+Text+"  ");
        tv0.setTextColor(Color.BLACK);
        return tv0;
    }

    void TripHeader()
    {
        TableRow tbrow0 = new TableRow(getActivity());
        tbrow0.setBackgroundColor(Color.CYAN);
        tbrow0.addView(AddTextView("Trip ID"));
        tbrow0.addView(AddTextView("Day Type"));
        tbrow0.addView(AddTextView("Date"));
        tbrow0.addView(AddTextView("Time"));
        tbrow0.addView(AddTextView("Destinations"));
        Table.addView(tbrow0);
    }

    void CoorHeader()
    {
        TableRow tbrow0 = new TableRow(getActivity());
        tbrow0.setBackgroundColor(Color.CYAN);
        tbrow0.addView(AddTextView("CID"));
        tbrow0.addView(AddTextView("Latitude"));
        tbrow0.addView(AddTextView("Longitude"));
        tbrow0.addView(AddTextView("TripID"));
        Table.addView(tbrow0);
    }

    void StreetHeader()
    {
        TableRow tbrow0 = new TableRow(getActivity());
        tbrow0.setBackgroundColor(Color.CYAN);
        tbrow0.addView(AddTextView("SID"));
        tbrow0.addView(AddTextView("Street Name"));
        tbrow0.addView(AddTextView("Area"));
        tbrow0.addView(AddTextView("City"));
        tbrow0.addView(AddTextView("State"));
        tbrow0.addView(AddTextView("TripID"));
        Table.addView(tbrow0);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.trip_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

}
