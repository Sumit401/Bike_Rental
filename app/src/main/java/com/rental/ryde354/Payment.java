package com.rental.ryde354;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.paytm.pgsdk.PaytmPGService;

import java.util.Objects;

public class Payment extends AppCompatActivity {

    String bookid, cust_id, veh_name, from_date, to_date;
    float price, days, getPrice;
    TextView from, to, name, pricing;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        final PaytmPGService Service = PaytmPGService.getProductionService();

        bookid = intent.getStringExtra("book_id");
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
        pricing.setText("" + getPrice);


    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
}