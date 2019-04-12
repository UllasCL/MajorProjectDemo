package com.ullas.majorproject.MapActivity;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ullas.majorproject.R;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    MqttAndroidClient client;
    LatLng current = null, prev = null;
    Marker marker;
    MarkerOptions markerOptions = new MarkerOptions();

    int initialised = 0;

    // LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(getApplicationContext(), "tcp://iot.eclipse.org:1883", clientId);


        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                //Toast.makeText(MapsActivity.this, "Hi", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {

                //  Toast.makeText(MapsActivity.this, topic+" "+message.toString(), Toast.LENGTH_SHORT).show();
                Log.e("msg", "Hello");

                topic = "mqtt/android321";


                String msg = new String(message.getPayload());
                //   Toast.makeText(MapsActivity.this, msg, Toast.LENGTH_SHORT).show();
                String[] splits = msg.split(" ");
                String[] lat = splits[0].split(":");
                String[] lng = splits[1].split(":");

                Double latitude = Double.parseDouble(lat[1]);
                Double longitude = Double.parseDouble(lng[1]);
                Toast.makeText(MapsActivity.this, latitude + " " + longitude, Toast.LENGTH_SHORT).show();


                if (initialised == 0) {
                    initialised = 1;
                    LatLng latLng = new LatLng(latitude, longitude);
                    prev = latLng;
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                        String str = addressList.get(0).getLocality();
                        str += addressList.get(0).getCountryName();
                        mMap.addMarker(new MarkerOptions().position(latLng).title("Start Loc: " + "" + str));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13.3f));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //  mMap.addMarker(new MarkerOptions().position(latLng).title("str"));
                    // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.2f));
                } else {
                    current = new LatLng(latitude, longitude);
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                        String str = addressList.get(0).getSubLocality();
                        str += addressList.get(0).getLocality();
                        //String str1=addressList.get(0).getAddressLine(1);
                        //  mMap.addMarker(new MarkerOptions().position(latLng).title(str));
                        //  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10.2f));
                        if (marker != null) {
                            marker.remove();
                        }
                        // marker = mMap.addMarker(new MarkerOptions()
                        // .position(
                        //       new LatLng(latitude,
                        //           longitude))
                        //  .draggable(true).visible(true));
                        marker = mMap.addMarker(new MarkerOptions().position(current).title("GPS LOC:" + " " + str));
                        mMap.addPolyline((new PolylineOptions())
                                .add(prev, current).width(6).color(Color.BLUE)
                                .visible(true));
                        //mMap.po
                        //  if (addressList.size() > 0) {
                        //     System.out.println(addressList.get(0).getLocality());
                        //  }
                        Toast.makeText(MapsActivity.this, "GPS Location:" + " " + str, Toast.LENGTH_SHORT).show();
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 13.3f));
                        prev = current;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //  mMap.addPolyline((new PolylineOptions())
                    //    .add(prev, current).width(6).color(Color.BLUE)
                    //   .visible(true));
                    //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 15.2f));
                    // prev=current;
                }


            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        try {
            IMqttToken token = client.connect();

            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                    Toast.makeText(MapsActivity.this, "Connected Successfully", Toast.LENGTH_SHORT).show();
                    String topic = "mqtt/android";
                    String payload = "Sample mqtt app for android";
                    byte[] encodedPayload = new byte[0];

                    try {
                        encodedPayload = payload.getBytes("UTF-8");
                        MqttMessage message = new MqttMessage(encodedPayload);
                        client.publish(topic, message);
                    } catch (UnsupportedEncodingException | MqttException e) {
                        e.printStackTrace();
                    }
                    try {
                        IMqttToken subToken =
                                client.subscribe("mqtt/android321", 1);
                        subToken.setActionCallback(new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {

                                Log.d("MQTT", "Message recd");
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                            }
                        });
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                    Toast.makeText(MapsActivity.this, "Access Denied", Toast.LENGTH_SHORT).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }


    }




     /*   locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    LatLng latLng = new LatLng(latitude, longitude);
                    //instantiate the class geocoder
                    Geocoder geocoder =new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressList=geocoder.getFromLocation(latitude,longitude,1);
                        String str=addressList.get(0).getLocality();
                        str+=addressList.get(0).getCountryName();
                        mMap.addMarker(new MarkerOptions().position(latLng).title(str));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10.2f));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });

        }
        else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    LatLng latLng = new LatLng(latitude, longitude);
                    //instantiate the class geocoder
                    Geocoder geocoder =new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressList=geocoder.getFromLocation(latitude,longitude,1);
                        String str=addressList.get(0).getLocality();
                        str+=addressList.get(0).getCountryName();
                        mMap.addMarker(new MarkerOptions().position(latLng).title(str));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10.2f));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        }*/
    // }


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

        // Add a marker in Sydney and move the camera
        // LatLng sydney = new LatLng(13.3260385, 77.1235277);
        //  mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //  mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

}