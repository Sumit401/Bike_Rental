package com.rental.ryde354;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Objects;

public class MainActivity3 extends AppCompatActivity {
    String url="https://gogoogol.in/android/getid.php";
    String s1;
    ImageView imageView1,imageView2,imageView3,imageView4,imageView5;
    TextView name,price,seat,fuel,reg_year;
    String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    ImageView air_cond,antilockbr,powersteering,powerwindow,power_door_lock,centrallock,cdplayer,leatherseat,crashsensor,driverairbag,passairbag,break_assist;
    String veh_id,brand,overview,fuelType,pricing,ac,modelyr,title,seating,powerdoorlock,abs,breakass, PowerSteering,DriverAirbag,
            PassengerAirbag,PowerWindows,CDPlayer,CrashSensor,CentralLocking,LeatherSeats;
    String image1,image2,image3,image4,image5;
    Button accessories,over_view;
    Button share,booknow;
    TextView contents_data;
    LinearLayout accessories_layout,mainlayout,layoutbooknow;
    ProgressBar bar;
    Dialog dialog;
    String s2=null;
    int k=0;
    float dayCount;
    int from_date_dd,from_date_mm,from_date_yy,to_date_dd,to_date_mm,to_date_yy,from_time_hh,from_time_mm,to_time_mm,to_time_hh;
    Calendar to_date_time,from_date_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog = new Dialog(MainActivity3.this);
        layoutbooknow=findViewById(R.id.layoutbooknow);
        imageView1 = findViewById(R.id.img1);
        imageView2 = findViewById(R.id.img2);
        imageView3 = findViewById(R.id.img3);
        imageView4 = findViewById(R.id.img4);
        imageView5 = findViewById(R.id.img5);
        name = findViewById(R.id.veh_name);
        price = findViewById(R.id.veh_price);
        seat = findViewById(R.id.seats);
        fuel = findViewById(R.id.fueltype);
        reg_year = findViewById(R.id.regyear);
        air_cond = findViewById(R.id.ac);
        antilockbr = findViewById(R.id.abs);
        powersteering = findViewById(R.id.ps);
        powerwindow = findViewById(R.id.pw);
        power_door_lock = findViewById(R.id.power_door_lock);
        centrallock = findViewById(R.id.central_locking);
        cdplayer = findViewById(R.id.cdp);
        leatherseat = findViewById(R.id.leather_seat);
        crashsensor = findViewById(R.id.crash_sensor);
        driverairbag = findViewById(R.id.driver_airbag);
        passairbag = findViewById(R.id.pass_airbag);
        break_assist = findViewById(R.id.break_assist);
        contents_data = findViewById(R.id.overview_content);
        accessories = findViewById(R.id.accessories);
        over_view = findViewById(R.id.overview);
        accessories_layout = findViewById(R.id.accessories_layout);
        mainlayout = findViewById(R.id.mainlayout);
        bar = findViewById(R.id.progressbar);
        booknow = findViewById(R.id.bookvehicle);
        share = findViewById(R.id.sharevehicle);
        final Intent intent = getIntent();
        s1 = intent.getStringExtra("veh_id");

        from_date_time = Calendar.getInstance();
        to_date_time =  Calendar.getInstance();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id",""+s1);
            jsonObject.put("action", "get_all_links");
            Get_data get_data = new Get_data();
            get_data.execute(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        booknow.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View v) {
                dialog.setContentView(R.layout.booknow);
                dialog.show();
                Window window=dialog.getWindow();
                Objects.requireNonNull(window).setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                final EditText fromdate = dialog.findViewById(R.id.fromdate);
                final EditText fromtime = dialog.findViewById(R.id.fromtime);
                final EditText todate = dialog.findViewById(R.id.todate);
                final EditText totime = dialog.findViewById(R.id.totime);
                final EditText message = dialog.findViewById(R.id.messagedata);
                final CheckBox terms_cond = dialog.findViewById(R.id.terms_cond);
                final CheckBox doorstep_delivery = dialog.findViewById(R.id.doorstep_delivery);
                Button submit_form = dialog.findViewById(R.id.submit_form);

                fromdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        int mday = calendar.get(Calendar.DATE);
                        int mmonth = calendar.get(Calendar.MONTH);
                        int myear = calendar.get(Calendar.YEAR);
                        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity3.this,R.style.DialogTheme,
                                new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("DefaultLocale")
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                from_date_dd=dayOfMonth;
                                from_date_mm=month;
                                from_date_yy=year;
                                fromdate.setText(String.format("%02d %s %d", dayOfMonth, MONTHS[month], year));
                            }
                        }, myear, mmonth, mday);
                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                        datePickerDialog.show();
                    }
                });

                fromtime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        int mhour = calendar.get(Calendar.HOUR);
                        int mmin = calendar.get(Calendar.MINUTE);

                        TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity3.this,R.style.DialogTheme,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @SuppressLint("DefaultLocale")
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        from_time_hh=hourOfDay;
                                        from_time_mm=minute;

                                        String AM_PM ;
                                        if(hourOfDay < 12) {
                                            AM_PM = "AM";
                                            if (hourOfDay==00){
                                                hourOfDay=12;
                                            }
                                        } else {
                                            AM_PM = "PM";
                                            hourOfDay=hourOfDay-12;
                                            if (hourOfDay==00){
                                                hourOfDay=12;
                                            }
                                        }
                                        fromtime.setText(String.format("%02d:%02d %s", hourOfDay, minute,AM_PM));
                                    }
                                }, mhour, mmin, false);
                        timePickerDialog.show();
                    }
                });

                todate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        int mday = calendar.get(Calendar.DATE);
                        int mmonth = calendar.get(Calendar.MONTH) + 1;
                        int myear = calendar.get(Calendar.YEAR);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity3.this,R.style.DialogTheme,
                                new DatePickerDialog.OnDateSetListener() {
                                    @SuppressLint("DefaultLocale")
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                                        to_date_dd=dayOfMonth;
                                        to_date_mm=month;
                                        to_date_yy=year;
                                        todate.setText(String.format("%02d %s %d", dayOfMonth, MONTHS[month], year));
                                    }
                                }, myear, mmonth, mday);


                        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis() - 1000);
                        datePickerDialog.show();
                    }
                });

                totime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        int mhour = calendar.get(Calendar.HOUR);
                        int mmin = calendar.get(Calendar.MINUTE);

                        TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity3.this,R.style.DialogTheme,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                        to_time_hh=hourOfDay;
                                        to_time_mm=minute;

                                        String AM_PM ;
                                        if(hourOfDay < 12) {
                                            AM_PM = "AM";
                                            if (hourOfDay==00){
                                                hourOfDay=12;
                                            }
                                        } else {
                                            AM_PM = "PM";
                                            hourOfDay=hourOfDay-12;
                                            if (hourOfDay==00){
                                                hourOfDay=12;
                                            }
                                        }
                                        totime.setText(String.format("%02d:%02d %s", hourOfDay, minute,AM_PM));
                                    }
                                }, mhour, mmin, false);
                        timePickerDialog.show();
                    }
                });
                submit_form.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (todate.getText().toString().trim().isEmpty() || totime.getText().toString().trim().isEmpty() || fromdate.getText().toString().trim().isEmpty() || fromtime.getText().toString().trim().isEmpty() ) {
                            Toast.makeText(MainActivity3.this, "Fields Can't be Empty", Toast.LENGTH_SHORT).show();
                        } else {
                            if (terms_cond.isChecked()){
                            from_date_time.set(from_date_yy, from_date_mm, from_date_dd);
                            to_date_time.set(to_date_yy, to_date_mm, to_date_dd);
                            long diff = -from_date_time.getTimeInMillis() + to_date_time.getTimeInMillis();
                            dayCount = (float) diff / (24 * 60 * 60 * 1000);
                            boolean doorstepDeliveryChecked=doorstep_delivery.isChecked();
                            if (dayCount > 0) {

                                Intent intent1 = new Intent(MainActivity3.this, Payment.class);
                                intent1.putExtra("price", "" + pricing);
                                intent1.putExtra("days", "" + dayCount);
                                intent1.putExtra("veh_id", "" + veh_id);
                                intent1.putExtra("veh_name", "" + title + "," + brand);
                                intent1.putExtra("from", fromdate.getText().toString().trim() + " - " + fromtime.getText().toString().trim());
                                intent1.putExtra("to", todate.getText().toString().trim() + " - " + totime.getText().toString().trim());
                                intent1.putExtra("message", message.getText().toString().trim());
                                intent1.putExtra("image_vehicle", image1);
                                intent1.putExtra("delivery",""+doorstepDeliveryChecked);
                                startActivity(intent1);
                                finish();
                            }else {
                                Toast.makeText(getApplicationContext(),"Invalid date",Toast.LENGTH_SHORT).show();
                            }
                            }else {
                                Toast.makeText(getApplicationContext(),"Accept Terms and Conditions to Proceed",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class Get_data extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mainlayout.setVisibility(View.GONE);
            layoutbooknow.setVisibility(View.GONE);
            bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            JSONObject jsonObject=JsonFunction.GettingData(url,params[0]);
            if (jsonObject==null)
                return "NULL";
            else
                return jsonObject.toString();
        }
        @SuppressLint("SetTextI18n")
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mainlayout.setVisibility(View.VISIBLE);
            layoutbooknow.setVisibility(View.VISIBLE);
            bar.setVisibility(View.GONE);
            if (s!=null){
                try {
                    JSONObject j1=new JSONObject(s);
                    String res=j1.getString("response");
                    if(res.equalsIgnoreCase("success")) {
                        JSONArray jsonArray = j1.getJSONArray("vehicleinfo");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject j2 = jsonArray.getJSONObject(i);
                            veh_id = j2.getString("id");
                            title = j2.getString("VehiclesTitle");
                            brand = j2.getString("BrandName");
                            overview = j2.getString("VehiclesOverview");
                            pricing = j2.getString("PricePerDay");
                            fuelType = j2.getString("FuelType");
                            modelyr = j2.getString("ModelYear");
                            seating = j2.getString("SeatingCapacity");
                            ac=j2.getString("AirConditioner");
                            powerdoorlock=j2.getString("PowerDoorLocks");
                            abs= j2.getString("AntiLockBrakingSystem");
                            breakass=j2.getString("BrakeAssist");
                            PowerSteering=j2.getString("PowerSteering");
                            DriverAirbag=j2.getString("DriverAirbag");
                            PassengerAirbag=j2.getString("PassengerAirbag");
                            PowerWindows=j2.getString("PowerWindows");
                            CDPlayer=j2.getString("CDPlayer");
                            CentralLocking=j2.getString("CentralLocking");
                            CrashSensor=j2.getString("CrashSensor");
                            LeatherSeats=j2.getString("LeatherSeats");
                            image1="https://gogoogol.in/admin/img/vehicleimages/"+j2.getString("Vimage1");
                            image2="https://gogoogol.in/admin/img/vehicleimages/"+j2.getString("Vimage2");
                            image3="https://gogoogol.in/admin/img/vehicleimages/"+j2.getString("Vimage3");
                            image4="https://gogoogol.in/admin/img/vehicleimages/"+j2.getString("Vimage4");
                            image5="https://gogoogol.in/admin/img/vehicleimages/"+j2.getString("Vimage5");

                            Picasso.get().load(image1).resize(750,390).into(imageView1);
                            Picasso.get().load(image2).resize(750,390).into(imageView2);
                            Picasso.get().load(image3).resize(750,390).into(imageView3);
                            Picasso.get().load(image4).resize(750,390).into(imageView4);
                            Picasso.get().load(image5).resize(750,390).into(imageView5);
                        }
                        name.setText(title+", "+brand);
                        price.setText("INR "+pricing+" Per Day");
                        fuel.setText(fuelType+"\nFuel Type");
                        seat.setText(seating+"\nSeats");
                        reg_year.setText(modelyr+"\nReg. Year");
                        contents_data.setText(overview);
                        if (ac.equalsIgnoreCase("1")){
                            air_cond.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_done_black_24dp));
                        }else {
                            air_cond.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_clear_black_24dp));
                        }
                        if (abs.equalsIgnoreCase("1")){
                            antilockbr.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_done_black_24dp));
                        }else {
                            antilockbr.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_clear_black_24dp));
                        }
                        if (PowerSteering.equalsIgnoreCase("1")){
                            powersteering.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_done_black_24dp));
                        }else {
                            powersteering.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_clear_black_24dp));
                        }
                        if (DriverAirbag.equalsIgnoreCase("1")){
                            driverairbag.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_done_black_24dp));
                        }else {
                            driverairbag.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_clear_black_24dp));
                        }
                        if (PassengerAirbag.equalsIgnoreCase("1")){
                            passairbag.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_done_black_24dp));
                        }else {
                            passairbag.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_clear_black_24dp));
                        }
                        if (PowerWindows.equalsIgnoreCase("1")){
                            powerwindow.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_done_black_24dp));
                        }else {
                            powerwindow.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_clear_black_24dp));
                        }
                        if (CDPlayer.equalsIgnoreCase("1")){
                            cdplayer.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_done_black_24dp));
                        }else {
                            cdplayer.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_clear_black_24dp));
                        }
                        if (CentralLocking.equalsIgnoreCase("1")){
                            centrallock.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_done_black_24dp));
                        }else {
                            centrallock.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_clear_black_24dp));
                        }
                        if (CrashSensor.equalsIgnoreCase("1")){
                            crashsensor.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_done_black_24dp));
                        }else {
                            crashsensor.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_clear_black_24dp));
                        }
                        if (LeatherSeats.equalsIgnoreCase("1")){
                            leatherseat.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_done_black_24dp));
                        }else {
                            leatherseat.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_clear_black_24dp));
                        }
                        if (powerdoorlock.equalsIgnoreCase("1")){
                            power_door_lock.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_done_black_24dp));
                        }else {
                            power_door_lock.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_clear_black_24dp));
                        }
                        if (breakass.equalsIgnoreCase("1")){
                            break_assist.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_done_black_24dp));
                        }else {
                            break_assist.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_clear_black_24dp));
                        }
                        over_view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contents_data.setVisibility(View.VISIBLE);
                                accessories_layout.setVisibility(View.GONE);
                                over_view.setBackgroundColor(getResources().getColor(R.color.blue2));
                                accessories.setBackgroundColor(getResources().getColor(R.color.brown));
                            }
                        });
                        accessories.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                accessories_layout.setVisibility(View.VISIBLE);
                                contents_data.setVisibility(View.GONE);
                                over_view.setBackgroundColor(getResources().getColor(R.color.brown));
                                accessories.setBackgroundColor(getResources().getColor(R.color.blue2));
                            }
                        });
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }


}
