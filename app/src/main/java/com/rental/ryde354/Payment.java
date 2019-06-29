package com.rental.ryde354;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Payment extends AppCompatActivity {

    String orderid, cust_id, veh_name, from_date, to_date;
    float price, days, getPrice;
    TextView from, to, name, pricing;
    Button submit;
    SharedPreferences preferences;
    PaytmPGService service;
    String checksum;
    String url = "https://gogoogol.in/android/paytm/generateChecksum.php";
    public static final String MID = "mjMzoe42717843068462";
    public static final String INDUSTRY_TYPE = "Retail";
    public static final String CHANNEL_ID = "WAP";
    public static final String WEBSITE = "DEFAULT";
    public static final String CALLBACK_URL = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";
    ArrayList<String> strings = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        if (ContextCompat.checkSelfPermission(Payment.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Payment.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        }
        service = PaytmPGService.getProductionService();
        preferences = getSharedPreferences("Login", MODE_PRIVATE);
        Intent intent = getIntent();
        orderid = intent.getStringExtra("book_id");
        price = Float.parseFloat(intent.getStringExtra("price"));
        days = Float.parseFloat(intent.getStringExtra("days"));
        cust_id = intent.getStringExtra("cust_id");
        veh_name = intent.getStringExtra("veh_name");
        from_date = intent.getStringExtra("from");
        to_date = intent.getStringExtra("to");

        from = findViewById(R.id.booking_from);
        to = findViewById(R.id.booking_to);
        name = findViewById(R.id.vehicle_name);
        pricing = findViewById(R.id.price);
        submit = findViewById(R.id.book_now);

        getPrice = price * days;
        from.setText(from_date);
        to.setText(to_date);
        name.setText(veh_name);
        pricing.setText(""+getPrice);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, String> params = new HashMap<String, String>();
                params.put("orderid", orderid);
                params.put("cust_id", cust_id);
                params.put("amt", "" + getPrice);
                params.put("ind_type", INDUSTRY_TYPE);
                params.put("channel", CHANNEL_ID);
                params.put("mobile",preferences.getString("Mobile",null));
                params.put("callbackurl",CALLBACK_URL);

                JSONObject param = new JSONObject(params);
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, param, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        checksum=response.optString("CHECKSUMHASH");
                        if (checksum.trim().length() !=0) {
                            HashMap<String, String> paramMap = new HashMap<>();
                            paramMap.put("MID", MID);
                            paramMap.put("ORDER_ID", orderid);
                            paramMap.put("CALLBACK_URL", CALLBACK_URL);
                            paramMap.put("CHANNEL_ID", CHANNEL_ID);
                            paramMap.put("CHECKSUMHASH",checksum);
                            paramMap.put("CUST_ID", cust_id);
                            paramMap.put("INDUSTRY_TYPE_ID", INDUSTRY_TYPE);
                            paramMap.put("WEBSITE", WEBSITE);
                            paramMap.put("TXN_AMOUNT", ""+getPrice);
                            paramMap.put("MOBILE_NO",preferences.getString("Mobile",null));
                            PaytmOrder Order = new PaytmOrder(paramMap);

                            service.initialize(Order, null);
                            service.startPaymentTransaction(Payment.this, true,
                                    true,
                                    new PaytmPaymentTransactionCallback() {
                                @Override
                                public void someUIErrorOccurred(String inErrorMessage) {
                                }

                                @Override
                                public void onTransactionResponse(Bundle inResponse) {
                                    Log.d("LOG", "Payment Transaction is successful " + inResponse);
                                    Toast.makeText(getApplicationContext(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void networkNotAvailable() {
                                    // If network is not available, then this method gets called.
                                }

                                @Override
                                public void clientAuthenticationFailed(String inErrorMessage) {
                                }

                                @Override
                                public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String
                                        inFailingUrl) {
                                    Toast.makeText(getApplicationContext(), "" + inErrorMessage + "" + inFailingUrl, Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onBackPressedCancelTransaction() {
                                    Toast.makeText(Payment.this, "Back pressed. Transaction cancelled", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                                    Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
                                    Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
                Volley.newRequestQueue(Payment.this).add(request);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
}