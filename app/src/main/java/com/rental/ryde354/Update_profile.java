package com.rental.ryde354;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class Update_profile extends AppCompatActivity {

    Button profile_verif,update_btn;
    EditText name,email,mobile,dob;
    ImageView profileimg;
    RadioGroup radioGroup;
    RadioButton male,female;
    String gender;
    String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    String url="https://gogoogol.in/android/update_profile.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        name=findViewById(R.id.upd_name);
        email=findViewById(R.id.upd_email);
        mobile=findViewById(R.id.upd_mobile);
        dob=findViewById(R.id.dob);
        profile_verif=findViewById(R.id.profile_verific);
        profileimg=findViewById(R.id.profileimage);
        male=findViewById(R.id.radio_male);
        female=findViewById(R.id.radio_female);
        radioGroup=findViewById(R.id.radiogroup);
        update_btn=findViewById(R.id.update_profile_btn);

        final SharedPreferences preferences=getSharedPreferences("Login",MODE_PRIVATE);
        Picasso.get().load(preferences.getString("pic",null))
                .placeholder(R.drawable.com_facebook_profile_picture_blank_portrait)
                .error(R.drawable.com_facebook_profile_picture_blank_portrait).into(profileimg);

        email.setText(preferences.getString("Email",null));
        name.setText(preferences.getString("Name",null));
        mobile.setText(preferences.getString("Mobile", null));

        if (Objects.requireNonNull(preferences.getString("dob", null)).equalsIgnoreCase("null")){
            dob.setText("01 Jan 2001");
        }else {
            dob.setText(preferences.getString("dob",null));
        }
        String gender1 = preferences.getString("gender",null);
        if (gender1.equalsIgnoreCase("f"))
            female.setChecked(true);
        else
            male.setChecked(true);

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int mday = calendar.get(Calendar.DATE);
                int mmonth = calendar.get(Calendar.MONTH) + 1;
                int myear = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Update_profile.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("DefaultLocale")
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                dob.setText(String.format("%02d %s %d", dayOfMonth, MONTHS[month], year));
                            }
                        }, myear, mmonth, mday);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        profile_verif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Update_profile.this,Profile_verific.class);
                startActivity(intent);
                finish();
            }
        });

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!name.getText().toString().isEmpty() && !mobile.getText().toString().isEmpty()){
                    if (radioGroup.getCheckedRadioButtonId() == R.id.radio_male){
                        gender="m";
                    }else{
                        gender="f";
                    }
                    try {
                        JSONObject object=new JSONObject();
                        object.put("id",preferences.getString("id",null));
                        object.put("name",name.getText().toString().trim());
                        object.put("mobile",mobile.getText().toString().trim());
                        object.put("gender",gender);
                        object.put("dob",dob.getText().toString().trim());
                        Send_data sendData=new Send_data();
                        sendData.execute(object.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                        Toast.makeText(getApplicationContext(),"Fields can't be Empty",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @SuppressLint("StaticFieldLeak")
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
            try {
                JSONObject object=new JSONObject(s);
                Toast.makeText(getApplicationContext(),object.getString("response"),Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SharedPreferences preferences=getSharedPreferences("Login",MODE_PRIVATE);
            SharedPreferences.Editor editor=preferences.edit();
            editor.putString("dob",dob.getText().toString().trim());
            editor.putString("gender",gender);
            editor.apply();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

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
}