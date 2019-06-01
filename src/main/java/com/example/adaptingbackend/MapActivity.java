package com.example.adaptingbackend;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.RequestClientOptions;
import com.example.adaptingbackend.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

	ArrayList<Ticket> ticketList = new ArrayList<Ticket>();
	String id;
	@Override
	public void onMapReady(GoogleMap googleMap) {
		Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
		Log.d(TAG, "onMapReady: map is ready");
		mMap = googleMap;

		new MapActivity.JSONBackgroundWorker().execute();
	}

	private static final String TAG = "MapActivity";

	private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
	private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
	private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
	private static final float DEFAULT_ZOOM = 15f;

	//vars
	private Boolean mLocationPermissionsGranted = false;
	private GoogleMap mMap;
	private FusedLocationProviderClient mFusedLocationProviderClient;
	public void startUp() {
		// before loop:
		List<Marker> markers = new ArrayList<Marker>();
		boolean found=false;

		for(int i=0; i<ticketList.size();i++) {
		    if(ticketList.get(i).getLatitude().equals("")){

            }
			else if(markers.size()==0){
				String markerName = ticketList.get(i).getName() + "   " + ticketList.get(i).getDate();
				LatLng shop = new LatLng(Double.parseDouble(ticketList.get(i).getLatitude()), Double.parseDouble(ticketList.get(i).getLongitude()));
				Marker marker = mMap.addMarker(new MarkerOptions().position(shop)
						.title(markerName));
				//CHange this to users location
				mMap.moveCamera(CameraUpdateFactory.newLatLng(shop));
				markers.add(marker);
			}
			else {
				for (int j = 0; j < markers.size(); j++) {
					if(markers.get(j).getPosition().latitude == Double.parseDouble(ticketList.get(i).getLatitude())
					&& markers.get(j).getPosition().longitude== Double.parseDouble(ticketList.get(i).getLongitude())){
							markers.get(j).setTitle( markers.get(j).getTitle()+"\n"+ticketList.get(i).getName()+"  "+ticketList.get(i).getDate());
							found=true;
							break;
					}

				}

				if(found==false){
					String markerName = ticketList.get(i).getName() + "   " + ticketList.get(i).getDate();
					LatLng shop = new LatLng(Double.parseDouble(ticketList.get(i).getLatitude()), Double.parseDouble(ticketList.get(i).getLongitude()));
					Marker marker = mMap.addMarker(new MarkerOptions().position(shop)
							.title(markerName));
					//CHange this to users location
					mMap.moveCamera(CameraUpdateFactory.newLatLng(shop));
					markers.add(marker);
				}
			}
		}

		if (mLocationPermissionsGranted) {
			getDeviceLocation();

			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
					!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
					Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				return;
			}
			mMap.setMyLocationEnabled(true);
			mMap.getUiSettings().setMyLocationButtonEnabled(false);

		}
	}


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);

		getLocationPermission();
	}

	private void getDeviceLocation(){
		Log.d(TAG, "getDeviceLocation: getting the devices current location");

		mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

		try{
			if(mLocationPermissionsGranted){

				final Task location = mFusedLocationProviderClient.getLastLocation();
				location.addOnCompleteListener(new OnCompleteListener() {
					@Override
					public void onComplete(@NonNull Task task) {
						if(task.isSuccessful()){
							Log.d(TAG, "onComplete: found location!");
							Location currentLocation = (Location) task.getResult();

							moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
									DEFAULT_ZOOM);

						}else{
							Log.d(TAG, "onComplete: current location is null");
							Toast.makeText(MapActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		}catch (SecurityException e){
			Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
		}
	}

	private void moveCamera(LatLng latLng, float zoom){
		Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
		mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapActivity.this));
	}

	private void initMap(){
		Log.d(TAG, "initMap: initializing map");
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

		mapFragment.getMapAsync(MapActivity.this);
	}

	private void getLocationPermission(){
		Log.d(TAG, "getLocationPermission: getting location permissions");
		String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
				Manifest.permission.ACCESS_COARSE_LOCATION};

		if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
				FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
			if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
					COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
				mLocationPermissionsGranted = true;
				initMap();
			}else{
				ActivityCompat.requestPermissions(this,
						permissions,
						LOCATION_PERMISSION_REQUEST_CODE);
			}
		}else{
			ActivityCompat.requestPermissions(this,
					permissions,
					LOCATION_PERMISSION_REQUEST_CODE);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		Log.d(TAG, "onRequestPermissionsResult: called.");
		mLocationPermissionsGranted = false;

		switch(requestCode){
			case LOCATION_PERMISSION_REQUEST_CODE:{
				if(grantResults.length > 0){
					for(int i = 0; i < grantResults.length; i++){
						if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
							mLocationPermissionsGranted = false;
							Log.d(TAG, "onRequestPermissionsResult: permission failed");
							return;
						}
					}
					Log.d(TAG, "onRequestPermissionsResult: permission granted");
					mLocationPermissionsGranted = true;
					//initialize our map
					initMap();
				}
			}
		}
	}

	public ArrayList<Ticket> parseJSON(String JSON_STRING) {


		try {
			JSONObject jsonObject = new JSONObject(JSON_STRING);
			JSONArray jsonArray = jsonObject.getJSONArray("server_response");
			int count = 0;
			String id, arena, date, name, time = "", value,longitude,latitude;
			boolean type = true;
			while (count < jsonArray.length()) {
				JSONObject JO = jsonArray.getJSONObject(count);
				id = JO.getString("id");
				name = JO.getString("name");
				arena = JO.getString("arena");
				date = JO.getString("date");
				value = JO.getString("price");
				longitude = JO.getString("longitude");
				latitude = JO.getString("latitude");

				double price = Double.parseDouble(value);
				date = date.replace("\"", "");
				Ticket ticket = new Ticket(id, name, arena, date, price, time,longitude,latitude);
//                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'");
//                Date d = format.parse(date);
//                if(date.contains("")) {
				ticketList.add(ticket);
//                }
//                else
//                {
//                    Log.d(null, "parseJSON: ");
//                }

				count++;
			}
			startUp();
			return ticketList;
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	public class JSONBackgroundWorker extends AsyncTask<Void, Void, String> {
		String JSON_URL;
		String JSON_STRING;

//        ArrayList<Product> list = new ArrayList<Product>();

		@Override
		protected void onPreExecute() {
//			JSON_URL = "http://147.252.148.154//viewUsersTickets.php?id=" + id;
            JSON_URL = "http://192.168.1.120//viewUsersTickets.php?id=" + id;
		}

		@Override
		protected String doInBackground(Void... params) {
			try {
				URL url = new URL(JSON_URL);
				HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
				httpURLConnection.setRequestMethod("GET");
				InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				StringBuilder stringBuilder = new StringBuilder();

				while ((JSON_STRING = bufferedReader.readLine()) != null) {
					stringBuilder.append(JSON_STRING + "\n");
				}
				bufferedReader.close();
				inputStream.close();
				httpURLConnection.disconnect();
				JSON_STRING = stringBuilder.toString().trim();
				return stringBuilder.toString().trim();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return " ERROR";
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(String result) {

			ticketList = parseJSON(result);

		}
	}


}