package com.example.smart_navi;


import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class Trip
{
	public String ID;
	public String date;
	public String Day_Type;
	public String Time;
	public String Destinations;
	public List<Coordinates> Coordinates;
	public List<Street> Streets;
	public Trip()
	{
		date = Day_Type= Time=Destinations="";
		Coordinates =  new ArrayList<Coordinates>();
		Streets =new ArrayList<Street>();
		
	}
	
	public Trip(String _date, String DType, String time, String des)
	{
		
		date = _date; Day_Type= DType; Time=time; Destinations=des;
		Coordinates =  new ArrayList<Coordinates>();
		Streets =new ArrayList<Street>();
		
	}
}

class Coordinates
{
	public String CID;
	public String Lati;
	public String Longi;
	public String TID;
	
	public Coordinates()
	{
		CID = Lati = Longi= TID = "";
	}
	
	public Coordinates(String lati, String longi, String tid)
	{
		Lati = lati;Longi= longi;TID = tid;
	}
}

class Street
{
	public String SID;
	public String Area;
	public String Street;
	public String City;
	public String State;
	public String TID;
	public Street()
	{
		Area = Street = City = State = TID = "";
	}
}

public class DatabaseHandler extends SQLiteOpenHelper {
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "SmartNavi.db";
	private static final String TABLE_Trip = "Trips";
	private static final String TABLE_Coor = "Coors";
	private static final String TABLE_Street = "Streets";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);		
	}
	
	public void UpdateQuery(String Verficode)
	{
		
		SQLiteDatabase db = this.getWritableDatabase();
		String strFilter = "VerficationCOde=" + Verficode;
		ContentValues args = new ContentValues();
		args.put("IsVerified", "True");
		db.update("SALES", args, strFilter, null);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE "+TABLE_Trip+"(TRIPID INTEGER PRIMARY KEY AUTOINCREMENT, DTYPE TEXT, DATE TEXT, TIME TEXT, DESTINATION TEXT)");
		db.execSQL("CREATE TABLE "+TABLE_Coor+"(CID INTEGER PRIMARY KEY AUTOINCREMENT, LATI TEXT, LONGI TEXT, TID TEXT)");
		db.execSQL("CREATE TABLE "+TABLE_Street+"(SID INTEGER PRIMARY KEY AUTOINCREMENT,STREET TEXT ,AREA TEXT, CITY TEXT, STATE TEXT, TID TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_Trip);
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_Coor);
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_Street);
		onCreate(db);
	}
	
	void addStreet(Street street)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("STREET", street.Street);
		values.put("AREA",street.Area);
		values.put("CITY", street.City);
		values.put("STATE", street.State);
		values.put("TID", street.TID);
		db.insert(TABLE_Street, null, values);
		db.close(); // Closing database connection
	}
	
	void addTrip(Trip trip)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("DTYPE", trip.Day_Type);
		values.put("DATE", trip.date);
		values.put("TIME", trip.Time);
		values.put("DESTINATION", trip.Destinations);
		db.insert(TABLE_Trip, null, values);
		db.close(); // Closing database connection
	}
	
	int GetMaxTripID()
	{
		String selectQuery = "SELECT Max(TRIPID) FROM " + TABLE_Trip;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
				return cursor.getInt(0);
		}
		return -1;
	}

	void addCoor(Coordinates coor)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("LATI", coor.Lati);
		values.put("LONGI", coor.Longi);
		values.put("TID", coor.TID);
		db.insert(TABLE_Coor, null, values);
		db.close(); // Closing database connection
	}
	
	public List<Trip> getAllTrips() {
		List<Trip> contactList = new ArrayList<Trip>();
		String selectQuery = "SELECT  * FROM " + TABLE_Trip;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Trip trip = new Trip();
				trip.ID = cursor.getString(0);
				trip.Day_Type = cursor.getString(1);
				trip.date = cursor.getString(2);
				trip.Time = cursor.getString(3);
				trip.Destinations = cursor.getString(4);
				trip.Coordinates = GetAllCoordinates(trip.ID);
				trip.Streets = GetAllStreets(trip.ID);
				contactList.add(trip);
			} while (cursor.moveToNext());
		}
		return contactList;
	}
	
	public List<Coordinates> GetAllCoordinates(String TID)
	{
		List<Coordinates> CoorList = new ArrayList<Coordinates>();
		String selectQuery ="";
		if(TID.equals(""))
			selectQuery = "SELECT  * FROM " + TABLE_Coor;
		else
			selectQuery = "SELECT  * FROM " + TABLE_Coor+" where TID = '"+TID+"'";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Coordinates coor = new Coordinates();
				coor.CID = cursor.getString(0);
				coor.Lati = cursor.getString(1);
				coor.Longi = cursor.getString(2);
				coor.TID = cursor.getString(3);
				CoorList.add(coor);
			} while (cursor.moveToNext());
		}
		return CoorList;
	}
	
	public List<Street> GetAllStreets(String TID)
	{
		List<Street> StreetList = new ArrayList<Street>();
		String selectQuery ="";
		if(TID.equals(""))
			selectQuery = "SELECT  * FROM " + TABLE_Street;
		else
			selectQuery = "SELECT  * FROM " + TABLE_Street+" where TID = '"+TID+"'";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Street street = new Street();
				street.SID = cursor.getString(0);
				street.Street = cursor.getString(1);
				street.Area = cursor.getString(2);
				street.City = cursor.getString(3);
				street.State = cursor.getString(4);
				street.TID = cursor.getString(5);
				StreetList.add(street);
			} while (cursor.moveToNext());
		}
		return StreetList;
	}
}