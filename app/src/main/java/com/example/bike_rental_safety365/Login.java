package com.example.bike_rental_safety365;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class Login extends AppCompatActivity {

    EditText name,mobile,email,pass;
    Button signin;
    String s1;
    String url="https://gogoogol.in/android/login.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Intent intent=getIntent();
        s1=intent.getStringExtra("email");
        name=findViewById(R.id.login_name);
        email=findViewById(R.id.login_email);
        mobile=findViewById(R.id.login_no);
        pass=findViewById(R.id.login_pass);
        signin=findViewById(R.id.login_button);
        email.setText(s1);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name != null && email != null && mobile != null && pass != null){
                    try {
                        JSONObject object=new JSONObject();
                        object.put("email",s1);
                        object.put("mobile",mobile.getText().toString().trim());
                        object.put("name",name.getText().toString().trim());
                        object.put("pass",pass.getText().toString().trim());
                        object.put("action","register_user");
                        Put_data put_data=new Put_data();
                        put_data.execute(object.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Snackbar.make(v,"Field Can't be Empty",Snackbar.LENGTH_SHORT);
                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class Put_data extends AsyncTask<String,String,String> {
        ProgressDialog dialog=new ProgressDialog(Login.this);
        @Override
        protected void onPreExecute() {
            dialog.setCancelable(false);
            dialog.setMessage("Loading");
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            JSONObject object=JsonFunction.GettingData(url,params[0]);
            if (object==null)
                return "NULL";
            else
                return object.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            try {
                JSONObject object=new JSONObject(s);
                String s2=object.getString("response");
                if (s2.equalsIgnoreCase("success")){

                    SharedPreferences preferences=getSharedPreferences("Login",MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("id",object.getString("id"));
                    editor.putString("Name", object.getString("name"));
                    editor.putString("Email",object.getString("email"));
                    editor.putString("Mobile",object.getString("mobile"));
                    editor.apply();
                    Intent intent=new Intent(Login.this,MainActivity2.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(Login.this,"Failed",Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}