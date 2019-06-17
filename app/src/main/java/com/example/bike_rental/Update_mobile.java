package com.example.bike_rental;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import org.json.JSONException;
import org.json.JSONObject;

public class Update_mobile extends AppCompatActivity {

    String url="https://gogoogol.in/android/load_mob.php";
    ImageButton button;
    EditText mobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_mobile);
        Intent intent=getIntent();
        final String id=intent.getStringExtra("id");
        mobile=findViewById(R.id.mobile_number);
        button=findViewById(R.id.send_mobile);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobile.getText().toString().trim().isEmpty()){
                    JSONObject object=new JSONObject();
                    try {
                        object.put("mobile",mobile.getText().toString().trim());
                        object.put("id",id);
                        Send_data send_data=new Send_data();
                        send_data.execute(object.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class Send_data extends AsyncTask<String,String,String> {
        ProgressDialog dialog=new ProgressDialog(Update_mobile.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Loading");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            JSONObject object=JsonFunction.GettingData(url,params[0]);
            if (object== null)
                return "Null";
            else
                return object.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
        }
    }
}
