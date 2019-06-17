package com.example.bike_rental;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.paytm.pgsdk.PaytmClientCertificate;
import com.paytm.pgsdk.PaytmMerchant;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

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
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Intent intent=getIntent();
        final PaytmPGService Service = PaytmPGService.getStagingService();
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


        SharedPreferences preferences=getSharedPreferences("Login",MODE_PRIVATE);

        Map<String, String> paramMap = new HashMap<String,String>();
        paramMap.put( "MID" , "jbehFg51291260255104");
// Key in your staging and production MID available in your dashboard
        paramMap.put( "ORDER_ID" , bookid);
        paramMap.put( "CUST_ID" , cust_id);
        paramMap.put( "MOBILE_NO" , preferences.getString("Mobile",null));
        paramMap.put( "EMAIL" , preferences.getString("Email",null));
        paramMap.put( "CHANNEL_ID" , "WAP");
        paramMap.put( "TXN_AMOUNT" , String.valueOf(getPrice));
        paramMap.put( "WEBSITE" , "DEFAULT");
// This is the staging value. Production value is available in your dashboard
        paramMap.put( "INDUSTRY_TYPE_ID" , "Retail");
// This is the staging value. Production value is available in your dashboard
        paramMap.put( "CALLBACK_URL", "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=order1");
        paramMap.put( "CHECKSUMHASH" , "w2QDRMgp1234567JEAPCIOmNgQvsi+BhpqijfM9KvFfRiPmGSt3Ddzw+oTaGCLneJwxFFq5mqTMwJXdQE2EzK4px2xruDqKZjHupz9yXev4=");

        PaytmOrder Order = new PaytmOrder((HashMap<String, String>) paramMap);

        PaytmMerchant Merchant = new PaytmMerchant(
                "https://pguat.paytm.com/paytmchecksum/paytmCheckSumGenerator.jsp",
                "https://pguat.paytm.com/paytmchecksum/paytmCheckSumVerify.jsp");


        Service.initialize(Order,null);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Service.startPaymentTransaction(Payment.this, true, true, new PaytmPaymentTransactionCallback() {

                            @Override
                            public void someUIErrorOccurred(String inErrorMessage) {
                                Toast.makeText(getApplicationContext(),""+inErrorMessage,Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onTransactionResponse(Bundle inResponse) {
                                Toast.makeText(getApplicationContext(),"Response",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void networkNotAvailable() {
                                Toast.makeText(getApplicationContext(),"No network",Toast.LENGTH_SHORT).show();
                            }

                            @Override

                            public void clientAuthenticationFailed(String inErrorMessage) {
                                Toast.makeText(getApplicationContext(),""+inErrorMessage,Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                                Toast.makeText(getApplicationContext(),""+inErrorMessage+""+inFailingUrl,Toast.LENGTH_SHORT).show();
                            }


                            @Override
                            public void onBackPressedCancelTransaction() {
                                Toast.makeText(getApplicationContext(),"back pressed",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                                Toast.makeText(getApplicationContext(),""+inErrorMessage,Toast.LENGTH_SHORT).show();
                            }
                        });

            }


        });
    }


}