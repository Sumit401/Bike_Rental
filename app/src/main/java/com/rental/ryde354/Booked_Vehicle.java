package com.rental.ryde354;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Booked_Vehicle extends AppCompatActivity {

    String url="https://gogoogol.in/android/booking_vehicle.php";
    TextView vehicle_name,book_from,bookto,booking_date,TXN_ID,TXN_AMOUNT,PAYMENT_MODE,Status,BANKTXN_ID;
    ImageView vehicleicon,qrcode;
    String VehiclesTitle,BrandName,FromDate,ToDate,veh_image,userEmail,PostingDate,message;
    String TXNID,TXNAMOUNT,PAYMENTMODE,TXNDATE,STATUS,BANKTXNID;
    LinearLayout txnidlayout,txnamountlayout,paymentmodelayout,bankidlayout,booked_vehicle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booked__vehicle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent=getIntent();
        String order_id=intent.getStringExtra("order_id");
        vehicle_name = findViewById(R.id.vehiclesname1);
        book_from = findViewById(R.id.bookingfrom1);
        bookto = findViewById(R.id.Bookingto);
        booking_date = findViewById(R.id.bookingdate);
        vehicleicon = findViewById(R.id.vehicleicon);
        qrcode = findViewById(R.id.qrcode);
        TXN_ID = findViewById(R.id.txn_id);
        TXN_AMOUNT=findViewById(R.id.txn_amt);
        PAYMENT_MODE = findViewById(R.id.payment_mode);
        BANKTXN_ID=findViewById(R.id.bankid);
        Status=findViewById(R.id.txn_status);
        txnidlayout=findViewById(R.id.txnidlayout);
        booked_vehicle=findViewById(R.id.booked_vehicle);
        txnamountlayout=findViewById(R.id.txnamountlayout);
        paymentmodelayout=findViewById(R.id.txnmodelayout);
        bankidlayout=findViewById(R.id.banktxnidlayout);
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("id",order_id);
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

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            if (s.equalsIgnoreCase("NULL")){
                Toast.makeText(getApplicationContext(),"No Internet Access",Toast.LENGTH_SHORT).show();
            }else {
                booked_vehicle.setVisibility(View.VISIBLE);
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
                            TXNID=j2.getString("TXNID");
                            TXNAMOUNT=j2.getString("TXNAMOUNT");
                            TXNDATE=j2.getString("TXNDATE");
                            BANKTXNID=j2.getString("BANKTXNID");
                            STATUS=j2.getString("STATUS");
                            PAYMENTMODE= j2.getString("PAYMENTMODE");

                            Picasso.get().load("https://gogoogol.in/admin/img/vehicleimages/"+veh_image).resize(750,390).into(vehicleicon);
                            Picasso.get().load("https://chart.googleapis.com/chart?chs=300x300&cht=qr&chl=Vehicle Title: "+VehiclesTitle+"%0A"+"Vehicle Brand: "+BrandName+"%0A"+"Booking Date: " +
                                    ""+PostingDate+"%0A"+"Booking From: "+FromDate+"%0A"+"Booking To: "+ToDate+"TXN_ID:"+TXNID+"%0A"+"Transaction Amount:"+TXNAMOUNT+"%0A"+"Payment Mode:"
                                    +PAYMENTMODE+"%0A"+"BANK TXN ID:"+BANKTXNID+"%0A"+"Status:"+STATUS+"%0A"+"&choe=UTF-8" ).into(qrcode);
                            vehicle_name.setText(VehiclesTitle+", "+BrandName);
                            book_from.setText(FromDate);
                            bookto.setText(ToDate);
                            booking_date.setText(TXNDATE);
                            TXN_ID.setText(TXNID);
                            TXN_AMOUNT.setText(TXNAMOUNT);
                            BANKTXN_ID.setText(BANKTXNID);
                            PAYMENT_MODE.setText(PAYMENTMODE);
                            if (STATUS.equalsIgnoreCase("TXN_SUCCESS"))
                                Status.setText("Success");
                            else
                                Status.setText("Failed");
                        }

                    }else if (s1.equalsIgnoreCase("failed")){

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
                            String price=j2.getString("price");

                            Picasso.get().load("https://gogoogol.in/admin/img/vehicleimages/"+veh_image).resize(750,390).into(vehicleicon);
                            Picasso.get().load("https://chart.googleapis.com/chart?chs=300x300&cht=qr&chl=Vehicle Title: "+VehiclesTitle+"%0A"+"Vehicle Brand: "+BrandName+"%0A"+"Booking Date: " +
                                    ""+PostingDate+"%0A"+"Booking From: "+FromDate+"%0A"+"Booking To: "+ToDate+"&choe=UTF-8" ).into(qrcode);
                            txnidlayout.setVisibility(View.GONE);
                            bankidlayout.setVisibility(View.GONE);
                            paymentmodelayout.setVisibility(View.GONE);
                            vehicle_name.setText(VehiclesTitle+", "+BrandName);
                            TXN_AMOUNT.setText(price);
                            book_from.setText(FromDate);
                            bookto.setText(ToDate);
                            booking_date.setText(PostingDate);
                            Status.setText("Failed");
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
