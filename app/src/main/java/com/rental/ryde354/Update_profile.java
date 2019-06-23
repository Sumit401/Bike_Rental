package com.rental.ryde354;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class Update_profile extends AppCompatActivity {

    Button profile_verif,update_btn;
    EditText name,email,mobile;
    ImageView profileimg;
    RadioGroup radioGroup;
    RadioButton male,female;
    int gender;
    String url="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        name=findViewById(R.id.upd_name);
        email=findViewById(R.id.upd_email);
        mobile=findViewById(R.id.upd_mobile);
        profile_verif=findViewById(R.id.profile_verific);
        profileimg=findViewById(R.id.profileimage);
        male=findViewById(R.id.radio_male);
        female=findViewById(R.id.radio_female);
        radioGroup=findViewById(R.id.radiogroup);

        SharedPreferences preferences=getSharedPreferences("Login",MODE_PRIVATE);
        email.setText(preferences.getString("Email",null));
        name.setText(preferences.getString("Name",null));
        mobile.setText(preferences.getString("Mobile", null));
        Picasso.get().load(preferences.getString("pic",null)).placeholder(R.drawable.com_facebook_profile_picture_blank_portrait).error(R.drawable.com_facebook_profile_picture_blank_portrait).into(profileimg);
        profile_verif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Update_profile.this,Profile_verific.class);
                startActivity(intent);
                finish();
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
               if (checkedId == R.id.radio_male){
                   gender=1;
               }else if (checkedId == R.id.radio_female){
                   gender=2;
               }
            }
        });
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!name.getText().toString().isEmpty() && !mobile.getText().toString().isEmpty()){
                    try {
                        JSONObject object=new JSONObject();
                        object.put("name",name.getText().toString().trim());
                        object.put("mobile",mobile.getText().toString().trim());
                        object.put("gender",gender);

                        Send_data sendData=new Send_data();
                        sendData.execute(object.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            SharedPreferences settings = getSharedPreferences("Login", MODE_PRIVATE);
            settings.edit().clear().apply();
            final ProgressDialog progressDialog=new ProgressDialog(Update_profile.this);
            progressDialog.setMessage("Wait");
            progressDialog.setTitle("Logging Out");
            progressDialog.setCancelable(false);
            progressDialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    Intent intent=new Intent(Update_profile.this,MainActivity1.class);
                    startActivity(intent);
                    finish();
                }
            },1000);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class Send_data extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            JSONObject object=JsonFunction.GettingData(url,params[0]);
            return object.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }
}
