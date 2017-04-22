package com.example.smart_navi;
/**
 * Created by manjari on 12/4/17.
 */
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Utility {

	
	public static Street GetStreet(Context con, double Lati, double Longi)
	{
		Street street = new Street();
		 Geocoder geocoder = new Geocoder(con, Locale.getDefault());

		            String result = null;
		            try {
		                List<Address> addressList = geocoder.getFromLocation(Lati, Longi, 1);
		                if (addressList != null && addressList.size() > 0) {
		                    Address address = addressList.get(0);

		                    street.Street = address.getSubLocality();
		                    street.Area = address.getLocality();
		                    street.City = address.getSubAdminArea();
		                    street.State = address.getAdminArea();
		                }
		            } catch (IOException e) {} 
		return street;
	}
	
	public static boolean IsAreaChanged(List<Coordinates> Values, double CurLati, double CurLongi)
	{
		double Distance = 0, MinDis=0.05;
		double SumX=0,SumY=0,SumXY=0,SumX2=0,SumY2=0,SXSY=0,A=0,B=0;
		
		int N=Values.size();
		
		for(Coordinates coor : Values)
		{
			SumX += Double.valueOf(coor.Lati);
			SumY += Double.valueOf(coor.Longi);
			SumXY += Double.valueOf(coor.Lati)*Double.valueOf(coor.Longi); 
			SumX2 += Double.valueOf(coor.Lati)*Double.valueOf(coor.Lati);
			SumY2 += Double.valueOf(coor.Longi)*Double.valueOf(coor.Longi);
		}
		SXSY = SumX * SumY;
		
		B = ((N*SumXY) - (SumX*SumY))/((N*SumX2)-(SumX*SumX));
		A = (SumY/N)-(B*(SumX/N));
		
		Distance = ((-B*CurLati)+CurLongi-A)/Math.sqrt((B*B)+1);
		
		return MinDis < Distance;

	}
	
}
