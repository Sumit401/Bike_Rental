package com.rental.ryde354;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Payment extends AppCompatActivity {

    String orderid, veh_id, veh_name, from_date, to_date, message, image1, delivery;
    float price, days;
    int getPrice;
    TextView from, to, name, total_price, price_per_day, doorstep_payment;
    Button submit;
    SharedPreferences preferences;
    PaytmPGService service;
    String checksum;
    ImageView image_vehicle;
    ProgressDialog dialog;
    String url = "https://gogoogol.in/android/paytm/generateChecksum.php";
    String url2="https://gogoogol.in/android/booking.php";
    String url3="https://gogoogol.in/android/transaction.php";
    public static final String MID = "OvmBvc95844887436372";
    public static final String INDUSTRY_TYPE = "Retail";
    public static final String CHANNEL_ID = "WAP";
    public static final String WEBSITE = "DEFAULT";
    public static final String CALLBACK_URL = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";

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
        dialog=new ProgressDialog(Payment.this);
        preferences = getSharedPreferences("Login", MODE_PRIVATE);
        Intent intent = getIntent();
        price = Float.parseFloat(intent.getStringExtra("price"));
        days = Float.parseFloat(intent.getStringExtra("days"));
        veh_id = intent.getStringExtra("veh_id");
        veh_name = intent.getStringExtra("veh_name");
        from_date = intent.getStringExtra("from");
        to_date = intent.getStringExtra("to");
        message = intent.getStringExtra("message");
        image1=intent.getStringExtra("image_vehicle");
        delivery = intent.getStringExtra("delivery");

        from = findViewById(R.id.booking_from);
        to = findViewById(R.id.booking_to);
        name = findViewById(R.id.vehicle_name);
        total_price = findViewById(R.id.total_price);
        submit = findViewById(R.id.pay_now);
        image_vehicle=findViewById(R.id.image_vehicle_payment);
        price_per_day=findViewById(R.id.price_per_day);
        doorstep_payment=findViewById(R.id.doorstep_payment);

        Picasso.get().load(image1).placeholder(R.drawable.ic_directions_car_black_24dp)
                .error(R.drawable.ic_directions_car_black_24dp).resize(500,290).into(image_vehicle);
        getPrice = (int)(price * days);

        if (delivery.equalsIgnoreCase("true")){
            doorstep_payment.setText("Yes");
            total_price.setText(""+getPrice+" + 300 (Delivery Charges)");
            getPrice=getPrice+300;
        } else if (delivery.equalsIgnoreCase("false")){
            doorstep_payment.setText("No");
            total_price.setText(""+getPrice);
        }
        from.setText(from_date);
        to.setText(to_date);
        name.setText(veh_name);
        price_per_day.setText(""+(int)price);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.setMessage("Loading");
                dialog.setTitle("Please Wait");
                dialog.setCancelable(false);
                dialog.show();
                SharedPreferences preferences = getSharedPreferences("Login", MODE_PRIVATE);
                JSONObject obj1 = new JSONObject();
                try {
                    obj1.put("email", preferences.getString("Email", null));
                    obj1.put("vehicleid", veh_id);
                    obj1.put("fromdate", from_date);
                    obj1.put("todate", to_date);
                    obj1.put("message", message);
                    obj1.put("days",String.valueOf((int)days));
                    obj1.put("price",String.valueOf(getPrice));
                    obj1.put("door_delivery",delivery);
                    SubmitData submitData = new SubmitData();
                    submitData.execute(obj1.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
    @SuppressLint("StaticFieldLeak")
    private class SubmitData extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            JSONObject object=JsonFunction.GettingData(url2,params[0]);
            if (object ==null)
                return "NULL";
            else
                return object.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equalsIgnoreCase("NULL")){
                Snackbar.make(getWindow().getDecorView().getRootView(),"No Internet",Snackbar.LENGTH_SHORT);
            }else{
                try {
                    JSONObject object=new JSONObject(s);
                    String s1=object.getString("response");
                    if (s1.equalsIgnoreCase("Success")) {
                        //Toast.makeText(Payment.this,"Successfully Done",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        orderid=object.getString("id");
                        Map<String,String> params =new HashMap<>();
                            params.put("orderid", object.getString("id"));
                            params.put("cust_id", Objects.requireNonNull(preferences.getString("custom_id", null)));
                            params.put("amt", "" + getPrice);
                            params.put("ind_type", INDUSTRY_TYPE);
                            params.put("channel", CHANNEL_ID);
                            params.put("mobile", Objects.requireNonNull(preferences.getString("Mobile", null)));
                            params.put("callbackurl", CALLBACK_URL);

                            JSONObject param = new JSONObject(params);
                            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, url, param, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    checksum=response.optString("CHECKSUMHASH");
                                    if (checksum.trim().length() >0){
                                        HashMap<String, String> paramMap = new HashMap<>();
                                        paramMap.put("MID", MID);
                                        paramMap.put("ORDER_ID", orderid);
                                        paramMap.put("CALLBACK_URL", CALLBACK_URL);
                                        paramMap.put("CHANNEL_ID", CHANNEL_ID);
                                        paramMap.put("CHECKSUMHASH", checksum);
                                        paramMap.put("CUST_ID", preferences.getString("custom_id", null));
                                        paramMap.put("INDUSTRY_TYPE_ID", INDUSTRY_TYPE);
                                        paramMap.put("WEBSITE", WEBSITE);
                                        paramMap.put("TXN_AMOUNT", "" + getPrice);
                                        paramMap.put("MOBILE_NO", preferences.getString("Mobile", null));
                                        PaytmOrder Order = new PaytmOrder(paramMap);

                                        service.initialize(Order, null);
                                        service.startPaymentTransaction(Payment.this, true,
                                                true,
                                                new PaytmPaymentTransactionCallback() {
                                                    @Override
                                                    public void someUIErrorOccurred(String inErrorMessage) {
                                                        Toast.makeText(getApplicationContext(),"Some Error Occurred "+inErrorMessage,Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }

                                                    @Override
                                                    public void onTransactionResponse(Bundle inResponse) {
                                                        Log.d("LOG", "Payment Transaction is successful " + inResponse);
                                                        //Toast.makeText(getApplicationContext(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();

                                                        JSONObject object1=new JSONObject();
                                                        try {
                                                            object1.put("ORDERID",inResponse.getString("ORDERID"));
                                                            object1.put("MID",inResponse.getString("MID"));
                                                            object1.put("TXNID",inResponse.getString("TXNID"));
                                                            object1.put("TXNAMOUNT",inResponse.getString("TXNAMOUNT"));
                                                            object1.put("PAYMENTMODE",inResponse.getString("PAYMENTMODE"));
                                                            object1.put("CURRENCY",inResponse.getString("CURRENCY"));
                                                            object1.put("TXNDATE",inResponse.getString("TXNDATE"));
                                                            object1.put("STATUS",inResponse.getString("STATUS"));
                                                            object1.put("RESPCODE",inResponse.getString("RESPCODE"));
                                                            object1.put("RESPMSG",inResponse.getString("RESPMSG"));
                                                            object1.put("GATEWAYNAME",inResponse.getString("GATEWAYNAME"));
                                                            object1.put("BANKTXNID",inResponse.getString("BANKTXNID"));
                                                            object1.put("CHECKSUMHASH",inResponse.getString("CHECKSUMHASH"));

                                                            Send_Transaction_data transactionData = new Send_Transaction_data();
                                                            transactionData.execute(object1.toString());
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }

                                                    @Override
                                                    public void networkNotAvailable() {
                                                        // If network is not available, then this method gets called.
                                                        Toast.makeText(getApplicationContext(),"No Network Available",Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }

                                                    @Override
                                                    public void clientAuthenticationFailed(String inErrorMessage) {
                                                        Toast.makeText(getApplicationContext(),"Client Authentication Failed "+inErrorMessage,Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }

                                                    @Override
                                                    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String
                                                            inFailingUrl) {
                                                        Toast.makeText(getApplicationContext(), "" + inErrorMessage + "" + inFailingUrl, Toast.LENGTH_LONG).show();
                                                        finish();
                                                    }

                                                    @Override
                                                    public void onBackPressedCancelTransaction() {
                                                        Toast.makeText(getApplicationContext(), "Back pressed. Transaction cancelled", Toast.LENGTH_LONG).show();
                                                        finish();
                                                    }

                                                    @Override
                                                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                                                        Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
                                                        Toast.makeText(getApplicationContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
                                                        finish();
                                                    }
                                                });
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });
                            Volley.newRequestQueue(Payment.this).add(jsonObjectRequest);

                    } else {
                        Toast.makeText(Payment.this,"Failed",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class Send_Transaction_data extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... params) {
            JSONObject object=JsonFunction.GettingData(url3,params[0]);
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
                String s1=object.getString("response");
                if (s1.equalsIgnoreCase("success")){
                    Intent intent=new Intent(Payment.this,Booked_Vehicle.class);
                    intent.putExtra("order_id",orderid);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}