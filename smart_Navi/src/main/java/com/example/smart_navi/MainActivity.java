package com.example.smart_navi;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
/**
 * Created by manjari on 12/4/17.
 */
public class MainActivity extends AppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		FragmentManager fragmentManager = getSupportFragmentManager();

		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

		HomeFragment homeFragment = new HomeFragment();

		fragmentTransaction.replace(R.id.container,homeFragment);

		fragmentTransaction.commit();

	}

	public void catchData(String data)
	{

		if(data.equals("Service"))
		{

			FragmentManager fragmentManager = getSupportFragmentManager();

			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

			DestinationFragment destinationFragment = new DestinationFragment();

			fragmentTransaction.replace(R.id.container,destinationFragment);

			fragmentTransaction.addToBackStack(null);

			fragmentTransaction.commit();

		}

		if(data.equals("Trip")|| data.equals("Street")||data.equals("coordinate"))
		{

			FragmentManager fragmentManager = getSupportFragmentManager();

			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

			TripFragment tripFragment= new TripFragment();

			Bundle bundle = new Bundle();

			bundle.putString("data",data);

			tripFragment.setArguments(bundle);

			fragmentTransaction.replace(R.id.container,tripFragment);

			fragmentTransaction.addToBackStack(null);

			fragmentTransaction.commit();

		}
		if(data.equals("Location"))
		{

			FragmentManager fragmentManager = getSupportFragmentManager();

			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

			LocationPredictionFragment locationPredictionFragment = new LocationPredictionFragment();

			fragmentTransaction.replace(R.id.container,locationPredictionFragment);

			fragmentTransaction.addToBackStack(null);

			fragmentTransaction.commit();

		}


	}

	public void catchData(String data, double lat, double longi, CharSequence radioButton)
	{

		FragmentManager fragmentManager = getSupportFragmentManager();

		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

		Bundle bundle = new Bundle();

		bundle.putString("Des" ,data);

		bundle.putDouble("Lati",lat);

		bundle.putDouble("Longi",longi);

		bundle.putCharSequence("Type",radioButton);

		MapFragment mapFragment = new MapFragment();

		mapFragment.setArguments(bundle);

		fragmentTransaction.replace(R.id.container,mapFragment);

		fragmentTransaction.addToBackStack(null);

		fragmentTransaction.commit();

	}

}