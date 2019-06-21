package com.rental.ryde365;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class About_us extends AppCompatActivity {

    String url="https://gogoogol.in/android/getpages.php";
    TextView aboutus;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        aboutus=findViewById(R.id.aboutus1);

        JSONObject object=new JSONObject();
        try {
            object.put("pages","About Us");
            Get_data data=new Get_data();
            data.execute(object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class Get_data extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... params) {
            JSONObject object=JsonFunction.GettingData(url,params[0]);
            return object.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object=new JSONObject(s);
                String s1=object.getString("response");
                if(s1.equalsIgnoreCase("success")){
                    String detail=object.getString("detail");
                    aboutus.setText(detail);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
