package com.rental.ryde354;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LocationListener {

    ViewPager viewPager;
    TabLayout tabLayout;
    TextView profile_name,profile_email,profile_contact;
    LocationManager locationManager;
    Spinner location_spinner;
    ImageButton location_button;
    // String url="https://gogoogol.in/android/get_status.php";
    String url="https://gogoogol.in/android/sendloc.php";
    String url2="https://gogoogol.in/android/getloc.php";
    String id;
    double lat=0,lng=0;
    LatLng latLng;
    String city;
    boolean doubleBackToExitPressedOnce = false;
    LinearLayout header;
    ImageView acct_img;
    SharedPreferences preferences;
    ArrayList<String> city_served=new ArrayList<>();
    ArrayAdapter<String> dataAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        location_spinner=findViewById(R.id.location_spinner);
        location_button=findViewById(R.id.locationbtn);
        viewPager=findViewById(R.id.viewpager);
        tabLayout=findViewById(R.id.tabs);
        //getting shared preferences (Session)
        preferences=getApplicationContext().getSharedPreferences("Login",MODE_PRIVATE);

        GetLoc getLoc=new GetLoc();
        getLoc.execute();

        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View headerview = navigationView.getHeaderView(0);
        header = headerview.findViewById(R.id.header);

        // finding name,email,contact, image textview and imageview from header......
        // for setting values to these fields see onResume func. below.......
        profile_name =  headerview.findViewById(R.id.act2_username);
        profile_email = headerview.findViewById(R.id.act2_email);
        profile_contact = headerview.findViewById(R.id.act2_mobile);
        acct_img = headerview.findViewById(R.id.accountimage);

        location_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("city",city_served.get(position));
                editor.apply();
                setupViewPager(viewPager);
                tabLayout.setupWithViewPager(viewPager);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("city",city_served.get(0));
                editor.apply();
                setupViewPager(viewPager);
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        // Header Listener to update profile (Navigation Drawer Header Listener)
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity2.this,Update_profile.class);
                startActivity(intent);
            }
        });

        location_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(MainActivity2.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                final Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (city !=null) {
                            @SuppressLint("CommitPrefEdits")
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("city", city);
                            editor.apply();
                            setupViewPager(viewPager);
                            tabLayout.setupWithViewPager(viewPager);
                            int spinnerPosition = dataAdapter.getPosition(city);
                            if (spinnerPosition < 0){
                                city_served.add(city);
                                int spinnerPosition1 = dataAdapter.getPosition(city);
                                location_spinner.setSelection(spinnerPosition1);
                            }else {
                                location_spinner.setSelection(spinnerPosition);
                            }
                            return;
                        }
                        handler.postDelayed(this,1000);
                    }
                },1000);
            }
        });

        // Asking Runtime-Permission access to Various Permissions.......
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // setting name,email,contact no,image to header (Navigation Drawer Header)
        profile_email.setText(preferences.getString("Email",null));
        profile_name.setText(preferences.getString("Name",null));
        profile_contact.setText(preferences.getString("Mobile", null));
        Picasso.get().load(preferences.getString("pic",null)).placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                .error(R.drawable.com_facebook_profile_picture_blank_square).into(acct_img);

    }
    //  For Creating TABS as a fragment in Android.........
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Car_listing(),"Cars"); //adding Car_listing.java class in fragment......
        adapter.addFragment(new Bike_listing(),"Bikes"); //adding Bike_listing.java class in fragment......
        viewPager.setAdapter(adapter);
    }
    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>(); // for adding classes...
        private final List<String> mFragmentTitleList = new ArrayList<>(); // for adding names to the tabs.......
        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return mFragmentList.get(i);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
            // return mFragmentTitleList.size();
        }
        private void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    //end.... to create tabs in Android.....



    //  This below func. requires LocationListener for functioning.....
    //  This func. is called automatically every time your location is changed......
    //  You just need to access this location and insert(update) in database.....

    @Override
    public void onLocationChanged(Location location) {

        lat=location.getLatitude();
        lng=location.getLongitude();

        latLng=new LatLng(lat,lng);

        JSONObject object = new JSONObject();
        try {
            object.put("email",preferences.getString("Email",null));
            object.put("lat",lat);
            object.put("lng",lng);

            Update_db upd_db=new  Update_db();
            upd_db.execute(object.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Get address from lat/lng ...... like city,state,country,etc......
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());;
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            // getting address
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            // getting city
            city = addresses.get(0).getLocality();
            // getting state
            String state = addresses.get(0).getAdminArea();
            // getting country
            String country = addresses.get(0).getCountryName();


        } catch (IOException e) {
            e.printStackTrace();
        }

        // This piece of code runs only once........
        // Only after getting city.....
        // Bcoz... we require current city to book vehicles.....
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

    //sending lat,lng to database............ where status == 1........
    // Check sendloc.php........for more details.......................
    @SuppressLint("StaticFieldLeak")
    private class Update_db extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... params) {
            JSONObject jsonObject=JsonFunction.GettingData(url,params[0]);
            if (jsonObject==null)
                return "NULL";
            else
                return jsonObject.toString();
        }
    }

    // For getting city names from tblcity.........................
    @SuppressLint("StaticFieldLeak")
    private class GetLoc extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... params) {
            JSONObject object=JsonFunction.GettingData(url2,"");
            if (object==null)
                return "NULL";
            else
                return object.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object=new JSONObject(s);
                JSONArray jsonArray=object.getJSONArray("cityinfo");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject object1=jsonArray.getJSONObject(i);
                    String city=object1.getString("CityName");
                    city_served.add(city);
                    dataAdapter = new ArrayAdapter<String>(MainActivity2.this, android.R.layout.simple_spinner_item, city_served);

                    // Drop down layout style - list view with radio button
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    // attaching data adapter to spinner
                    location_spinner.setAdapter(dataAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.aboutus){
            //See aboutus activity for more details........
            Intent intent=new Intent(MainActivity2.this,About_us.class);
            startActivity(intent);

        }else if (id == R.id.my_booking){

            //See My_Booking activity for more details........
            Intent intent=new Intent(MainActivity2.this,My_Bookings.class);
            startActivity(intent);

        }else if (id == R.id.profile_verif){
            //See Profile_verfic activity for more details........
            Intent intent=new Intent(MainActivity2.this,Profile_verific.class);
            startActivity(intent);
        }
        else if (id == R.id.faq){

            //See FAQs activity for more details........
            Intent intent=new Intent(MainActivity2.this,Faqs.class);
            startActivity(intent);
        }
        else if (id == R.id.contactus){


        }else if (id == R.id.shareloc){

            //Intent to Share current Location to any one ......
            if (lat != 0 && lng != 0) // if lat,lng not equal to null............
            {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    String shareMessage = "RyDE 365\nHello, My Current Location is\n\n";
                    shareMessage = shareMessage + "http://maps.google.com/?q="+lat+","+lng;
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "Share via"));
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }else {
                // if lat/lng equal to null......
                // permission to location access....if permission not granted.....
                // Or you need to wait for few seconds to call that onLocationChanged Listener......
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        }else if (id == R.id.share){

            //Intent to Share this application ......
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareMessage= "RyDE 365\nLet me recommend you this application\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id="+"com.rental.ryde354";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            } catch(Exception e) {
                e.getStackTrace();
            }

        }else if (id == R.id.feedback){

        }
        // Finding drawer layout ..... and close the drawer layout..... After the an item is chosen in drawerlayout.....
        DrawerLayout drawer = findViewById(R.id.drawer_layout); // Finding....
        drawer.closeDrawer(GravityCompat.START); // Closing after selecting one of the items above.....
        return true;
    }

    @Override
    public void onBackPressed() {
        // This func. is called when back button is pressed...................
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            // If drawer is open Close Drawer
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // Else exit application..........
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }


    // This Listener is called when you press accept or deny button on Runtime-Permissions........

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted.......
                    // Now location access popup appears.....to grant high accuracy location.......
                    try {
                        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, this);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }

                    locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                    boolean gps_enabled = false;
                    try {
                        gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    } catch (Exception ignored) {
                    }
                    if (!gps_enabled){

                        LocationRequest locationRequest = LocationRequest.create();
                        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

                        Task<LocationSettingsResponse> result =
                                LocationServices.getSettingsClient(getApplicationContext()).checkLocationSettings(builder.build());

                        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
                            @Override
                            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                                try {
                                    LocationSettingsResponse response = task.getResult(ApiException.class);
                                    // All location settings are satisfied. The client can initialize location
                                    // requests here.
                                } catch (ApiException exception) {
                                    switch (exception.getStatusCode()) {
                                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                            // Location settings are not satisfied. But could be fixed by showing the
                                            // user a dialog.
                                            try {
                                                // Cast to a resolvable exception.
                                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                                // Show the dialog by calling startResolutionForResult(),
                                                // and check the result in onActivityResult().
                                                resolvable.startResolutionForResult(MainActivity2.this, LocationRequest.PRIORITY_HIGH_ACCURACY);
                                            } catch (IntentSender.SendIntentException e) {
                                                // Ignore the error.
                                            } catch (ClassCastException e) {
                                                // Ignore, should be an impossible error.
                                            }
                                            break;
                                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                            // Location settings are not satisfied. However, we have no way to fix the
                                            // settings so we won't show the dialog.
                                            break;
                                    }
                                }
                            }
                        });
                    }
                } else {
                    // permission denied.........
                    Toast.makeText(this, "Permission denied to Access Location", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}