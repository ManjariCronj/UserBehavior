package com.example.smart_navi;

import android.graphics.Color;
import android.os.AsyncTask;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by manjari on 12/4/17.
 */

public class PredefinedRoute
{
    ArrayList<LatLng> decodePath ;
    Mytask mytask;
    GoogleMap googleMap;

    public PredefinedRoute(String start, String destination, String mode, GoogleMap googleMap )
    {
        this.googleMap = googleMap;
        mytask=new Mytask();
        mytask.execute("https://maps.googleapis.com/maps/api/directions/json?origin="+start+"&destination="+destination+"&mode="+mode+"&key=YOUR_API_KEY");
    }

    public class Mytask extends AsyncTask<String, Void, String>
    {
        URL url;
        HttpURLConnection httpUrlConnection;
        InputStream inputStream;
        InputStreamReader inputStreamReader;
        BufferedReader bufferedReader;
        StringBuilder stringBuilder;
        String line;

        @Override
        protected String doInBackground(String... strings)
        {
            try {
                    url = new URL(strings[0]);
                    httpUrlConnection = (HttpURLConnection) url.openConnection();
                    inputStream = httpUrlConnection.getInputStream();
                    inputStreamReader = new InputStreamReader(inputStream);
                    bufferedReader = new BufferedReader(inputStreamReader);
                    line = bufferedReader.readLine();
                    stringBuilder = new StringBuilder();
                    while (line != null)
                    {
                        stringBuilder.append(line);
                        line = bufferedReader.readLine();
                    }
                    return stringBuilder.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            try
            {
                if (s != null && !s.isEmpty())
                {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("routes");
                    String string1 = jsonObject.getString("status");
                    if (string1.equals("OK") && jsonArray != null && jsonArray.length() > 0)
                    {
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject test = jsonArray.getJSONObject(i);
                            JSONObject overview_polyline = test.getJSONObject("overview_polyline");
                            String name = overview_polyline.getString("points");
                            decodePath = (ArrayList<LatLng>) PolyUtil.decode(name);
                        }
                    }

                    if (decodePath != null && decodePath.size() > 0)
                    {
                        googleMap.addPolyline(new PolylineOptions().addAll(decodePath).width(5).color(Color.BLUE));
                    }
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            super.onPostExecute(s);
        }
    }
}
