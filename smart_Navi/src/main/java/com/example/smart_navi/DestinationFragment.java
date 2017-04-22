/**
 * Created by manjari on 12/4/17.
 */

package com.example.smart_navi;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class DestinationFragment extends Fragment
{
    RadioGroup radioType;
    Button BtnStart;
    EditText EtDestination;

    public DestinationFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.fragment_destination, container, false);

        EtDestination = (EditText) view.findViewById(R.id.ETDestination);
        BtnStart = (Button) view.findViewById(R.id.BtnStart);
        radioType = (RadioGroup) view.findViewById(R.id.radioType);

        BtnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Geocoder coder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> addressList = coder.getFromLocationName(EtDestination.getText().toString(), 10);
                    if (addressList != null && addressList.size() > 0) {

                        RadioButton rb = (RadioButton) view.findViewById(radioType.getCheckedRadioButtonId());

                        MainActivity mainActivity = (MainActivity) getActivity();

                        mainActivity.catchData(EtDestination.getText().toString(),addressList.get(0).getLatitude(),addressList.get(0).getLongitude(),rb.getText());

                    }
                    else
                    {
                        Toast.makeText(getActivity().getApplicationContext(), "Invalid Destinations", Toast.LENGTH_SHORT).show();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
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
