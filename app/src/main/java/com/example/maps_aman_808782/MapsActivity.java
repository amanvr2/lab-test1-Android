package com.example.maps_aman_808782;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    private static final int REQUEST_CODE = 1;
    private Marker homeMarker;
    private Marker destMarker;

    ArrayList<LatLng> locations = new ArrayList<LatLng>();


    public static Integer count = 0;

    public static Double destLat = 0.0;
    public static Double destLong = 0.0;


    public static Double homeLat = 0.0;
    public static Double homeLong = 0.0;


    Polyline line;
    Polygon shape;
    private static final int POLYGON_SIDES = 4;
    List<Marker> markers = new ArrayList();

    // location with location manager and listener
    LocationManager locationManager;
    LocationListener locationListener;


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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                setHomeMarker(location);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (!hasLocationPermission())
            requestLocationPermission();
        else
            startUpdateLocation();


        // apply long press gesture
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                // set marker
                count += 1;
                locations.add(latLng);
                setMarker(latLng);



            }

            private void setMarker(LatLng latLng) {

                MarkerOptions options = null;

                if(count > 4){
                    count = 0;
                    clearMap();
                }

                else {

                    if (count == 1) {

                        float results[] = new float[1];
                        Location.distanceBetween(homeLat, homeLong, latLng.latitude, latLng.longitude, results);

                        options = new MarkerOptions().position(latLng)
                                .title("A").draggable(true).snippet("Distance from Source is " + String.valueOf(results[0]));
                        markers.add(mMap.addMarker(options));

                    } else if (count == 2) {
                        float results[] = new float[1];
                        Location.distanceBetween(homeLat, homeLong, latLng.latitude, latLng.longitude, results);

                        options = new MarkerOptions().position(latLng)
                                .title("B").draggable(true).snippet("Distance from Source is " + String.valueOf(results[0]));
                        markers.add(mMap.addMarker(options));
                    } else if (count == 3) {

                        float results[] = new float[1];
                        Location.distanceBetween(homeLat, homeLong, latLng.latitude, latLng.longitude, results);

                        options = new MarkerOptions().position(latLng)
                                .title("C").draggable(true).snippet("Distance from Source is " + String.valueOf(results[0]));
                        markers.add(mMap.addMarker(options));
                    } else if (count == 4) {
                        float results[] = new float[1];
                        Location.distanceBetween(homeLat, homeLong, latLng.latitude, latLng.longitude, results);

                        options = new MarkerOptions().position(latLng)
                                .title("D").draggable(true).snippet("Distance from Source is " + String.valueOf(results[0]));
                        markers.add(mMap.addMarker(options));
                    }

//                if (destMarker != null) clearMap();
//
//                destMarker = mMap.addMarker(options);
//
//                drawLine();

                    // check if there are already the same number of markers, we clear the map.
//                if (markers.size() == POLYGON_SIDES)
//                    clearMap();


                    if (markers.size() == POLYGON_SIDES)
                        drawShape();



                    markers.get(0).getPosition();
                }
            }

            private void drawShape() {
                PolygonOptions options = new PolygonOptions()
                        .fillColor(0x5F00FF00)
                        .strokeColor(Color.RED)
                        .strokeWidth(5);


                for (int i=0; i<POLYGON_SIDES; i++) {
                    options.add(markers.get(i).getPosition());
                }

                shape = mMap.addPolygon(options);
                shape.setClickable(true);

            }


//            private void drawLine() {
//                PolylineOptions options = new PolylineOptions()
//                        .color(Color.BLACK)
//                        .width(10)
//                        .add(homeMarker.getPosition(), destMarker.getPosition());
//                line = mMap.addPolyline(options);
//            }

            private void clearMap() {

                /*if (destMarker != null) {
                    destMarker.remove();
                    destMarker = null;
                }

                line.remove();*/

                for (Marker marker: markers)
                    marker.remove();

                markers.clear();
                shape.remove();
                shape = null;
            }


        });

//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(@NonNull LatLng latLng) {
//                destLat = latLng.latitude;
//                destLong = latLng.longitude;
//
//                showAddress(latLng);
//
//
//            }
//        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {

                showAddress(marker.getPosition());

                return false;
            }
        });

        mMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
            @Override
            public void onPolygonClick(@NonNull Polygon polygon) {

                getTotal();

            }
        });

//        mMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
//            @Override
//            public void onPolylineClick(@NonNull Polyline polyline) {
//                Toast.makeText(getApplicationContext(),"line click : ",Toast.LENGTH_LONG).show();
//            }
//        });
    }

    private void startUpdateLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);

        /*Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        setHomeMarker(lastKnownLocation);*/
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    private boolean hasLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void setHomeMarker(Location location) {
        homeLat = location.getLatitude();
        homeLong = location.getLongitude();

        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions options = new MarkerOptions().position(userLocation)
                .title("You are here")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("Your Location");
        homeMarker = mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (REQUEST_CODE == requestCode) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
            }
        }
    }
    public void showAddress(LatLng latLng){

        destLat = latLng.latitude;
        destLong = latLng.longitude;

        String address = "Could not find the address";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(destLat, destLong, 1);
            if (addressList != null && addressList.size() > 0) {
                address = "\n";

                // street name
                if (addressList.get(0).getThoroughfare() != null)
                    address += addressList.get(0).getThoroughfare() + "\n";
                if (addressList.get(0).getLocality() != null)
                    address += addressList.get(0).getLocality() + " ";
                if (addressList.get(0).getPostalCode() != null)
                    address += addressList.get(0).getPostalCode() + " ";
                if (addressList.get(0).getAdminArea() != null)
                    address += addressList.get(0).getAdminArea();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        Toast.makeText(getApplicationContext(),"Address : "+address,Toast.LENGTH_LONG).show();

    }

    public void getTotal(){

        float results1[] = new float[1];
        float results2[] = new float[1];
        float results3[] = new float[1];
        float results4[] = new float[1];

        float total;

        Location.distanceBetween(locations.get(0).latitude,locations.get(0).longitude,locations.get(1).latitude,locations.get(1).longitude,results1);
        Location.distanceBetween(locations.get(1).latitude,locations.get(1).longitude,locations.get(2).latitude,locations.get(2).longitude,results2);
        Location.distanceBetween(locations.get(2).latitude,locations.get(2).longitude,locations.get(3).latitude,locations.get(3).longitude,results3);
        Location.distanceBetween(locations.get(3).latitude,locations.get(3).longitude,locations.get(0).latitude,locations.get(0).longitude,results4);

        total = (results1[0]+results2[0]+results3[0]+results4[0])/1000;

        Toast.makeText(getApplicationContext(),"Total Distance : "+String.valueOf(total)+" Kms",Toast.LENGTH_LONG).show();

    }


}