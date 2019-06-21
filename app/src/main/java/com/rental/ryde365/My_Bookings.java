package com.rental.ryde365;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class My_Bookings extends AppCompatActivity {
    RecyclerView recyclerView_mybooking;
    LinearLayoutManager layoutManager;
    ArrayList<String> vehicleid=new ArrayList<>();
    ArrayList<String> vehicletitle=new ArrayList<>();
    ArrayList<String> vehiclebrand=new ArrayList<>();
    ArrayList<String> todate_booking=new ArrayList<>();
    ArrayList<String> fromDate_booking=new ArrayList<>();
    ArrayList<String> booking_status=new ArrayList<>();
    ArrayList<String> book_message=new ArrayList<>();
    ArrayList<String> vimage=new ArrayList<>();
    ArrayList<String> booking_date=new ArrayList<>();
    String url="https://gogoogol.in/android/mybookings.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mybooking);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        layoutManager = new LinearLayoutManager(My_Bookings.this);
        recyclerView_mybooking=findViewById(R.id.recyclermybooking);
        SharedPreferences preferences=this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        try {
            JSONObject object=new JSONObject();
            object.put("email",preferences.getString("Email",null));
            Get_booking_details details=new Get_booking_details();
            details.execute(object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class Get_booking_details extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            JSONObject object=JsonFunction.GettingData(url,params[0]);
            if (object==null)
                return "NULL";
            else
                return object.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equalsIgnoreCase("NULL")){
                Toast.makeText(getApplicationContext(),"No Internet", Toast.LENGTH_SHORT).show();
            }else {
                try {
                    JSONObject object=new JSONObject(s);
                    String s1=object.getString("response");
                    if (s1.equalsIgnoreCase("success")){
                        JSONArray jsonArray = object.getJSONArray("vehicleinfo");
                        for (int i =0; i < jsonArray.length(); i++) {
                            JSONObject j2 = jsonArray.getJSONObject(i);
                            String id=j2.getString("bookid");
                            String title = j2.getString("VehiclesTitle");
                            String brand = j2.getString("BrandName");
                            String fromDate=j2.getString("FromDate");
                            String toDate=j2.getString("ToDate");
                            String image=j2.getString("Vimage1");
                            String message=j2.getString("message");
                            String status = j2.getString("Status");
                            String posting_date=j2.getString("PostingDate");
                            String img2="https://gogoogol.in/admin/img/vehicleimages/"+image;
                            vehicleid.add(id);
                            vehicletitle.add(title);
                            vehiclebrand.add(brand);
                            todate_booking.add(toDate);
                            fromDate_booking.add(fromDate);
                            booking_status.add(status);
                            book_message.add(message);
                            vimage.add(img2);
                            booking_date.add(posting_date);
                        }
                    }else {
                        Toast.makeText(getApplicationContext(),"Failed", Toast.LENGTH_SHORT).show();
                    }
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView_mybooking.setLayoutManager(layoutManager);
                    recyclerView_mybooking.setAdapter(new RecycleAdp_mybooking(My_Bookings.this,vehicleid,vehicletitle,vehiclebrand,
                            todate_booking,fromDate_booking,booking_date,booking_status,book_message,vimage));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
