package com.example.bike_rental_safety365;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Booked_Vehicle extends AppCompatActivity {

    String url="https://gogoogol.in/android/booking_vehicle.php";
    TextView vehicle_name,book_from,bookto,booking_date,totalprice;
    ImageView vehicleicon,qrcode;
    String VehiclesTitle,BrandName,FromDate,ToDate,veh_image,userEmail,license_num,PostingDate,aadhaar_num,message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booked__vehicle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent=getIntent();
        String id2=intent.getStringExtra("book_id");
        /*float price = Float.parseFloat(intent.getStringExtra("price"));
        float days = Float.parseFloat(intent.getStringExtra("days"));*/
        vehicle_name = findViewById(R.id.vehiclesname1);
        book_from = findViewById(R.id.bookingfrom1);
        bookto = findViewById(R.id.Bookingto);
        booking_date = findViewById(R.id.bookingdate);
        vehicleicon = findViewById(R.id.vehicleicon);
        qrcode = findViewById(R.id.qrcode);
        /*totalprice = findViewById(R.id.total_price);*/

       /* totalprice.setText(""+price*days);
*/
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("id",id2);
            Get_Booking_Data get_booking_data=new Get_Booking_Data();
            get_booking_data.execute(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("StaticFieldLeak")
    private class Get_Booking_Data extends AsyncTask<String,String,String> {
        ProgressDialog dialog=new ProgressDialog(Booked_Vehicle.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Please Wait");
            dialog.setTitle("Loading");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            JSONObject jsonObject=JsonFunction.GettingData(url,params[0]);
            if (jsonObject==null)
                return "NULL";
            else
                return jsonObject.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            if (s.equalsIgnoreCase("NULL")){

            }else {
                try {
                    JSONObject object=new JSONObject(s);
                    String s1=object.getString("response");
                    if (s1.equalsIgnoreCase("success")) {
                        JSONArray jsonArray = object.getJSONArray("vehicleinfo");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject j2 = jsonArray.getJSONObject(i);
                            userEmail = j2.getString("userEmail");
                            FromDate = j2.getString("FromDate");
                            ToDate = j2.getString("ToDate");
                            message = j2.getString("message");
                            PostingDate=j2.getString("PostingDate");
                            VehiclesTitle=j2.getString("VehiclesTitle");
                            BrandName=j2.getString("BrandName");
                            veh_image=j2.getString("Vimage1");
                            Picasso.get().load("https://gogoogol.in/admin/img/vehicleimages/"+veh_image).resize(750,390).into(vehicleicon);
                            Picasso.get().load("https://chart.googleapis.com/chart?chs=300x300&cht=qr&chl=Vehicle Title: "+VehiclesTitle+"%0A"+"Vehicle Brand: "+BrandName+"%0A"+"Booking Date: "+PostingDate+"%0A"+"Booking From: "+FromDate+"%0A"+"Booking To: "+ToDate+"&choe=UTF-8" ).into(qrcode);
                            vehicle_name.setText(VehiclesTitle+", "+BrandName);
                            book_from.setText(FromDate);
                            bookto.setText(ToDate);
                            booking_date.setText(PostingDate);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
