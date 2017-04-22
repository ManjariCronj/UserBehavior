package com.example.smart_navi;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.os.ParcelFileDescriptor.MODE_WORLD_READABLE;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements LocationListener, TextToSpeech.OnInitListener, OnMapReadyCallback {


    public MapFragment() {
        // Required empty public constructor
    }

    private TextToSpeech tts;
    protected static final int RESULT_SPEECH = 1;

    private GoogleMap googleMap;
    Location CurrentLoc;

    String Destination, Keyword;
    Context con;
    Document doc = null;
    List<Coordinates> coordinates;
    Trip CurrTrip;
    DatabaseHandler DB;
    boolean isclosed = false;

    public SharedPreferences Prefer;
    SharedPreferences.Editor PreEditor;

    String PLoc = "";
    double Plat = 0, Plon = 0;
    boolean isspeak = false;
    boolean isrouteAdd = false;
    PolylineOptions rectLine = null;
    PredefinedRoute predefinedRoute;


    private void IntilizePreferences() {

        Prefer = this.getActivity().getSharedPreferences("SmtPrefs", MODE_WORLD_READABLE);

        PreEditor = Prefer.edit();
    }


    private void GetSavedDetails() {
        PLoc = Prefer.getString("Loc", "");
        if (PLoc != "") {
            Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());

            String result = null;
            try {
                List<android.location.Address> addressList = geocoder.getFromLocationName(PLoc, 1);
                if (addressList != null && addressList.size() > 0) {
                    android.location.Address address = addressList.get(0);
                    Plat = address.getLatitude();
                    Plon = address.getLongitude();
                    //address.getLatitude()
                }

            } catch (IOException e) {
            }

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        initilizeMap();
        IntilizePreferences();
        GetSavedDetails();

        tts = new TextToSpeech(this.getActivity(), this);

        DB = new DatabaseHandler(getActivity().getApplicationContext());
        con = getActivity();
        //MakeCollection();

        // Inflate the layout for this fragment
        return view;
    }

    void MakeCollection() {

        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (CurrentLoc != null) {
                    if (coordinates == null) {
                        coordinates = new ArrayList<Coordinates>();
                        Street str = Utility.GetStreet(getActivity().getApplicationContext(), CurrentLoc.getLatitude(), CurrentLoc.getLongitude());
                        str.TID = CurrTrip.ID;
                        DB.addStreet(str);
                        //Toast.makeText(getApplicationContext(), "New Street Detected",Toast.LENGTH_SHORT).show();
                    }
                    Coordinates coor = new Coordinates(String.valueOf(CurrentLoc.getLatitude()), String.valueOf(CurrentLoc.getLongitude()), CurrTrip.ID);
                    coordinates.add(coor);
                    DB.addCoor(coor);

                    if (Utility.IsAreaChanged(coordinates, CurrentLoc.getLatitude(), CurrentLoc.getLongitude())) {

                        Toast.makeText(getActivity().getApplicationContext(), "New Street Detected", Toast.LENGTH_SHORT).show();

                        Street str = Utility.GetStreet(getActivity().getApplicationContext(), CurrentLoc.getLatitude(), CurrentLoc.getLongitude());
                        str.TID = CurrTrip.ID;
                        DB.addStreet(str);

                        new GetPlaces(con, "bank", CurrentLoc.getLatitude(), CurrentLoc.getLongitude(), googleMap).execute();
                    }
                    if (!isclosed)
                        MakeCollection();
                }
            }
        }, 5000);
    }

    void Route_Prediction() {

        Date d = new Date();
        CharSequence s = DateFormat.format("MM-dd-yyyy ", d.getTime());
        CharSequence t = DateFormat.format("HH:mm:ss", d.getTime());
        String day = d.getDay() <= 5 ? "WEEKDAY" : "WEEKEND";
        boolean isroutefound = false;

        List<Trip> Trips = DB.getAllTrips();

        for (Trip trp : Trips) {
            if (trp.Destinations != null && trp.Destinations.toUpperCase().equals(Destination.toUpperCase())) {
                Street street = Utility.GetStreet(con, CurrentLoc.getLatitude(), CurrentLoc.getLongitude());
                if (trp.Streets != null)
                    for (Street str : trp.Streets) {
                        if (str.Street != null && str.Area != null && str.City != null && str.Street.equals(street.Street) && str.Area.equals(street.Area) && str.City.equals(street.City)) {
                            Toast.makeText(getActivity().getApplicationContext(), "Route Detected", Toast.LENGTH_SHORT).show();
                            //Draw Route.
                            isroutefound = true;
                            if (trp.Coordinates != null)

                                DrawRoute(trp.Coordinates);
                        }
                    }
            }
        }

        if (!isroutefound) {
            if (!isroutefound)
                AddRoute();
            Toast.makeText(getActivity().getApplicationContext(), "Route Not Found", Toast.LENGTH_SHORT).show();
        }
    }

    void AddRoute() {
        String day = "";
        Date d = new Date();
        CharSequence s = DateFormat.format("MM-dd-yyyy ", d.getTime());
        CharSequence t = DateFormat.format("HH:mm:ss", d.getTime());
        day = d.getDay() <= 5 ? "WEEKDAY" : "WEEKEND";
        CurrTrip = new Trip(s.toString(), day, t.toString(), Destination);
        DB.addTrip(CurrTrip);
        if (DB.GetMaxTripID() != -1)
            CurrTrip.ID = String.valueOf(DB.GetMaxTripID());
    }

    void DrawRoute(List<Coordinates> directionPoint) {
        //rectLine.add(new LatLng(CurrentLoc.getLatitude(), CurrentLoc.getLongitude()));
        ArrayList<  LatLng> latLong = new ArrayList<LatLng>();
        for (int i = 0; i < directionPoint.size(); i++) {
            latLong.add(new LatLng(Double.parseDouble(directionPoint.get(i).Lati), Double.parseDouble(directionPoint.get(i).Longi)));
        }
        rectLine.addAll(latLong);
        googleMap.addPolyline(rectLine);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initilizeMap()
    {
        if (googleMap == null)
        {
            SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                    .findFragmentById(R.id.map);

            mapFragment.getMapAsync(this);


            if (googleMap == null) {
                Toast.makeText(getActivity().getApplicationContext(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    public void onProviderDisabled(String provider) {

    }

    public void onProviderEnabled(String provider) {

    }

    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    public void onBackPressed() {
        isclosed = true;
        getActivity().getFragmentManager().popBackStack();
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap)
    {
        this.googleMap = googleMap;

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        final Bundle bundle = getArguments();

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

            @Override
            public void onMyLocationChange(Location arg0)
            {
                if(CurrentLoc != null)
                {
                    Route_Prediction();
                }
                CurrentLoc = arg0;

                if (predefinedRoute == null & CurrentLoc != null)
                {
                    CameraPosition camPos = new CameraPosition.Builder()
                            .target(new LatLng(CurrentLoc.getLatitude(), CurrentLoc.getLongitude()))
                            .zoom(8)
                            .bearing(CurrentLoc.getBearing())
                            .build();
                    CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);

                    googleMap.animateCamera(camUpd3);

                    String start = Double.toString(CurrentLoc.getLatitude()) + "," + Double.toString(CurrentLoc.getLongitude());

                    predefinedRoute = new PredefinedRoute(start,Destination, bundle.getString("Type"),googleMap);
                }

                float[] distance = new float[1];
                arg0.distanceBetween(arg0.getLatitude(), arg0.getLongitude(), Plat, Plon, distance);
                //Toast.makeText(getApplicationContext(), String.valueOf(distance[0]), Toast.LENGTH_SHORT).show();
                if(((distance[0]*0.621371) < 1000.0) && !isspeak)
                {
                    isspeak = true;
                    speakOut("you are near to "+PLoc );
                }
            }});

            if(bundle != null)
            {
                Destination = bundle.getString("Des");
                googleMap.addMarker(new MarkerOptions().position(new LatLng(bundle.getDouble("Lati"), bundle.getDouble("Longi"))).title(bundle.getString("Des")));
                Toast.makeText(getActivity().getApplicationContext(), bundle.getString("Des")+bundle.getString("Type"),Toast.LENGTH_SHORT).show();
            }

            if (rectLine == null)
                rectLine = new PolylineOptions().width(13).color(Color.GREEN).zIndex(100);
    }

    class GetDoc extends AsyncTask<String, String, String>
    {
        final GMapV2Direction md = new GMapV2Direction();

        String response="";
        @Override
        protected String doInBackground(String... Vals) {
            try{

            }catch(Exception ex){}
            return "";
        }

        @Override
        protected void onPostExecute(String response) {
            try{
                ArrayList<LatLng> directionPoint = md.getDirection(doc);

                PolylineOptions rectLine = new PolylineOptions().width(3).color(Color.RED);
                for (int i = 0; i < directionPoint.size(); i++) {
                    rectLine.add(directionPoint.get(i));
                }
                if (googleMap !=null)
                    googleMap.addPolyline(rectLine);


            }catch(Exception ex){}
        }

    }

    @Override
    public void onInit(int arg0) {

    }

    private void speakOut(String SpeakVal) {
        tts.speak(SpeakVal, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}
