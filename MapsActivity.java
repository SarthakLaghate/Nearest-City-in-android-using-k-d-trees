package com.example.dsa;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static com.example.dsa.KDT.getNearestA;



public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    public String getName(double lat,double lng){
        JSONParser parser = new JSONParser();


        JSONObject mJsonObject=null;
        JSONArray mJsonArray = null;
        try {
            mJsonArray = new JSONArray((parser.parse(new BufferedReader
                    (new InputStreamReader(this.getAssets().open("cities.json"), "UTF-8")))).toString());
        } catch (Exception e) {
            Log.d("DEBUG",e.getMessage()+ "  2");
            e.printStackTrace();
        }
        double[] podoubles=new double[2];
        //Log.d("DEBUG", String.valueOf(mJsonArray.length()));
        for(int i=0;i<mJsonArray.length();i++){
            try {
                mJsonObject = mJsonArray.getJSONObject(i);
                podoubles[0]=Double.parseDouble(mJsonObject.getString("lat"));
                podoubles[1]=Double.parseDouble(mJsonObject.getString("lng"));
                if(podoubles[0]==lat && podoubles[1]==lng){
                    return mJsonObject.getString("city");
                }
            } catch (JSONException e) {
                Log.d("DEBUG",e.getMessage());
                e.printStackTrace();
            }
        }
        return "Not found";
    }
    Marker marker=null;


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                Log.d("DEBUG","Map clicked [" + point.latitude + " / " + point.longitude + "]");

                double[] input={point.latitude, point.longitude};


                double[] ans=getNearestA(input,MapsActivity.this);
                Log.d("DEBUG","Ans:" + ans[0] + " / " + ans[1] + "]");

                LatLng nearest = new LatLng(ans[0], ans[1]);
                if(marker!=null)
                    marker.remove();
                marker = mMap.addMarker(new MarkerOptions().position(nearest).title(getName(ans[0],ans[1])));
            }
        });
        LatLng delhi = new LatLng(28.651952, 77.231495);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(delhi));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 5.0f ) );
    }
}
