package com.example.smart_navi;

/**
 * Created by manjari on 12/4/17.
 */

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

class GetPlaces extends AsyncTask<Void, Void, Void>{	

    private ProgressDialog dialog;
    private Context context;
    private String[] placeName;
    private String[] imageUrl;
    private ListView listView;
    private String Places;
    private double Lati;
    private double Longi;
    private List<Place> _Places;
    private Dialog _Dialog;
    private GoogleMap Map;

    public GetPlaces(Context context, String _Places, double Lat, double Long, GoogleMap _Map)
    {
        this.context = context;
        this.listView = listView;
        this.Places = _Places;
        this.Lati = Lat;
        this.Longi = Long;
        this.Map = _Map;
    }
    
    public GetPlaces(MapFragment context, String _Places, double Lat, double Long)
    {
        this.Places = _Places;
        this.Lati = Lat;
        this.Longi = Long;
    }

    @Override
    protected void onPostExecute(Void result)
    {
        super.onPostExecute(result);
        List<String> PlaceNames = new ArrayList<String>();
        
        for(Place plc : _Places)
        {
        	String str = "Name : "+plc.getName()+"\n";
        	str += "Address : "+plc.getVicinity()+"\n";
        	str += +plc.getLatitude()+"\n";
        	str += +plc.getLongitude();
        	PlaceNames.add(str);
        	
        	Map.addMarker(new MarkerOptions().position(new LatLng(plc.getLatitude(), plc.getLongitude())).title(plc.getName()));
        }
        
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... arg0)
    {
        PlacesService service = new PlacesService("API_KEY_PLACES");
        List<Place> findPlaces = service.findPlaces(Lati,Longi,Places);  // hospiral for hospital
                                                                                    // atm for ATM
        _Places= findPlaces;


        placeName = new String[findPlaces.size()];
        imageUrl = new String[findPlaces.size()];

        for (int i = 0; i < findPlaces.size(); i++)
        {
            Place placeDetail = findPlaces.get(i);
            placeDetail.getIcon();
            placeName[i] =placeDetail.getName();
            imageUrl[i] =placeDetail.getIcon();
        }
        return null;
    }
}
