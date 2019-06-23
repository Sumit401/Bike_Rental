package com.rental.ryde354;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity1 extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    SignInButton signInButton;
    GoogleSignInOptions gso;
    GoogleApiClient mGoogleApiClient;
    int RC_SIGN_IN = 100;
    String url="https://gogoogol.in/android/reg.php";
    String url2="https://gogoogol.in/android/check_user.php";
    String url3="https://gogoogol.in/android/reg2.php";
    EditText loginemail,login_pass;
    ImageButton loginbtn,loginbtn2;
    TextInputLayout textInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        loginemail=findViewById(R.id.login_mobile);
        loginbtn=findViewById(R.id.loginbtn);
        loginbtn2=findViewById(R.id.loginbtn2);
        login_pass=findViewById(R.id.login_password);
        textInputLayout=findViewById(R.id.pass);
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setScopes(gso.getScopeArray());

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInButton.setOnClickListener(this);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmailValid(loginemail.getText().toString())){
                    JSONObject object=new JSONObject();
                    try {
                        object.put("email",loginemail.getText().toString().trim());
                        Get_data get_data=new Get_data();
                        get_data.execute(object.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    loginemail.setError("Enter Valid Email");
                    loginemail.requestFocus();
                }
            }
        });

    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    //This function will option signing intent
    private void signIn() {
        //Creating an intent
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

        //Starting intent for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //If signin
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //Calling a new function to handle signin
            handleSignInResult(result);
        }
    }

    //After the signing we are calling this function
    private void handleSignInResult(GoogleSignInResult result) {
        //If the login succeed
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            SharedPreferences preferences=getSharedPreferences("Login",MODE_PRIVATE);
            SharedPreferences.Editor editor=preferences.edit();
            if (acct.getPhotoUrl() != null) {
                editor.putString("pic",acct.getPhotoUrl().toString());
            }
            editor.apply();
            try {
                JSONObject object=new JSONObject();
                object.put("action","register_user");
                object.put("email",acct.getEmail());
                object.put("name",acct.getDisplayName());
                Load_data load_data=new Load_data();
                load_data.execute(object.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(this, "Login Failed"+result, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == signInButton) {
            signIn();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this,"No Internet", Toast.LENGTH_LONG).show();
    }

    @SuppressLint("StaticFieldLeak")
    private class Load_data extends AsyncTask<String,String,String> {
        ProgressDialog dialog=new ProgressDialog(MainActivity1.this);
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
                String s1=object.getString("response");
                if (s1.equalsIgnoreCase("success")){

                    SharedPreferences preferences=getSharedPreferences("Login",MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("id",object.getString("id"));
                    editor.putString("Name",object.getString("name"));
                    editor.putString("Email",object.getString("email"));
                    editor.apply();

                    Intent intent=new Intent(MainActivity1.this,Update_mobile.class);
                    intent.putExtra("id",""+object.getString("id"));
                    startActivity(intent);
                    finish();

                }else if (s1.equalsIgnoreCase("failed")){
                    SharedPreferences preferences=getSharedPreferences("Login",MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("id",object.getString("id"));
                    editor.putString("Name",object.getString("name"));
                    editor.putString("Email",object.getString("email"));
                    editor.putString("Mobile",object.getString("mobile"));
                    editor.apply();

                    Intent intent=new Intent(MainActivity1.this,MainActivity2.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class Get_data extends AsyncTask<String,String,String>{
        ProgressDialog dialog=new ProgressDialog(MainActivity1.this);
        @Override
        protected void onPreExecute() {
            dialog.setCancelable(false);
            dialog.setMessage("Loading");
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            JSONObject object=JsonFunction.GettingData(url2,params[0]);
            if (object == null)
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
                String s1=object.getString("response");
                if (s1.equalsIgnoreCase("success")){
                    textInputLayout.setVisibility(View.VISIBLE);
                    loginbtn.setVisibility(View.GONE);
                    loginbtn2.setVisibility(View.VISIBLE);
                    loginemail.setFocusable(false);
                    loginbtn2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                JSONObject object1=new JSONObject();
                                object1.put("email",loginemail.getText().toString().trim());
                                object1.put("pass",login_pass.getText().toString().trim());
                                object1.put("action","register_user");
                                Login_user user=new Login_user();
                                user.execute(object1.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else {
                    Intent intent=new Intent(MainActivity1.this,Login.class);
                    intent.putExtra("email",loginemail.getText().toString().trim());
                    startActivity(intent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class Login_user extends AsyncTask<String,String,String>{
        ProgressDialog dialog=new ProgressDialog(MainActivity1.this);
        @Override
        protected void onPreExecute() {
            dialog.setCancelable(false);
            dialog.setMessage("Loading");
            dialog.show();
            super.onPreExecute();
        }
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
            dialog.dismiss();
            try {
                JSONObject object=new JSONObject(s);
                String s1=object.getString("response");
                if (s1.equalsIgnoreCase("Success")){
                    SharedPreferences preferences=getSharedPreferences("Login",MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("id",object.getString("id"));
                    editor.putString("Name",object.getString("name"));
                    editor.putString("Email",object.getString("email"));
                    editor.putString("Mobile",object.getString("mobile"));
                    editor.apply();
                    Intent intent=new Intent(MainActivity1.this,MainActivity2.class);
                    startActivity(intent);
                    finish();

                }else {
                    Toast.makeText(MainActivity1.this,"Wrong Password",Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}