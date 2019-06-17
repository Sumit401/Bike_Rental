package com.example.bike_rental;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

public class Payment extends AppCompatActivity {

    String bookid,price,days,cust_id,veh_name;
    TextView from,to,name,pricing;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Intent intent=getIntent();
        bookid=intent.getStringExtra("book_id");
        price=intent.getStringExtra("price");
        days=intent.getStringExtra("days");
        cust_id=intent.getStringExtra("cust_id");
        veh_name=intent.getStringExtra("veh_name");

        from=findViewById(R.id.booking_from);
        to=findViewById(R.id.booking_to);
        name=findViewById(R.id.vehicle_name);
        pricing=findViewById(R.id.price);
        submit=findViewById(R.id.book_now);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}