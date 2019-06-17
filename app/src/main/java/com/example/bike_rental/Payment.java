package com.example.bike_rental;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.ChecksumException;
import com.paytm.pgsdk.PaytmClientCertificate;
import com.paytm.pgsdk.PaytmMerchant;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.zip.Checksum;

import static com.paytm.pgsdk.PaytmConstants.CHECKSUM;
import static com.paytm.pgsdk.PaytmConstants.ORDER_ID;

public class Payment extends AppCompatActivity {

    String bookid,cust_id,veh_name,from_date,to_date;
    float price,days,getPrice;
    TextView from,to,name,pricing;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Intent intent=getIntent();
        final PaytmPGService Service = PaytmPGService.getProductionService();

        bookid=intent.getStringExtra("book_id");
        price= Float.parseFloat(intent.getStringExtra("price"));
        days= Float.parseFloat(intent.getStringExtra("days"));
        cust_id=intent.getStringExtra("cust_id");
        veh_name=intent.getStringExtra("veh_name");
        from_date=intent.getStringExtra("from");
        to_date=intent.getStringExtra("to");

        from=findViewById(R.id.booking_from);
        to=findViewById(R.id.booking_to);
        name=findViewById(R.id.vehicle_name);
        pricing=findViewById(R.id.price);
        submit=findViewById(R.id.book_now);


        getPrice=price*days;
        from.setText(from_date);
        to.setText(to_date);
        name.setText(veh_name);
        pricing.setText(""+getPrice);

    }
    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


}