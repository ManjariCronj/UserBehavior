package com.example.smart_navi;
/**
 * Created by manjari on 12/4/17.
 */
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.os.ParcelFileDescriptor.MODE_WORLD_READABLE;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocationPredictionFragment extends Fragment
{

    EditText ETLoc;

    public LocationPredictionFragment()
    {
        // Required empty public constructor
    }

    public SharedPreferences Prefer;
    SharedPreferences.Editor PreEditor;

    private void IntilizePreferences()
    {
        Prefer = this.getActivity().getSharedPreferences("SmtPrefs", MODE_WORLD_READABLE);
        PreEditor = Prefer.edit();
    }

    private void getSavedDetails()
    {
        ETLoc.setText( Prefer.getString("Loc", ""));

        Toast.makeText(getActivity(),"You are in getSavedDetails() method",Toast.LENGTH_SHORT ).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        IntilizePreferences();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location_prediction, container, false);

        ETLoc= (EditText) view.findViewById(R.id.EtLoc);

        Button Btnsave = (Button) view.findViewById(R.id.BtnSave);

        Btnsave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                if(ETLoc.getText().toString().equals(""))
                {
                    PreEditor.putString("Loc", ETLoc.getText().toString());
                    PreEditor.commit();
                }

                else
                {
                    Geocoder coder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());

                    try {
                        List<Address> addressList = coder.getFromLocationName(ETLoc.getText().toString(), 10);

                        if (addressList != null && addressList.size() > 0)
                        {
                            PreEditor.putString("Loc", ETLoc.getText().toString());//sharedPreference

                            PreEditor.commit();

                            getActivity().getFragmentManager().popBackStack();

                            Toast.makeText(getActivity(),ETLoc.getText().toString()+"  Location Saved \n Thankyou",Toast.LENGTH_SHORT).show();

                            //getSavedDetails();
                            ETLoc.setText("");

                        }
                        else
                        {
                            Toast.makeText(getActivity().getApplicationContext(), "Invalid Destinations", Toast.LENGTH_SHORT).show();
                        }

                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                }

            }
        });

        return view;

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.find_location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}