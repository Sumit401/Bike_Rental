package com.example.bike_rental_safety365;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class Bike_listing extends Fragment {
    String url="https://gogoogol.in/android/vehicle_listing.php";
    RecyclerView recyclerView;
    ArrayList<String> vehicleid=new ArrayList<>();
    ArrayList<String> vehicletitle=new ArrayList<>();
    ArrayList<String> vehiclebrand=new ArrayList<>();
    ArrayList<String> vehicleoverview=new ArrayList<>();
    ArrayList<String> vehiclepricing=new ArrayList<>();
    ArrayList<String> vehiclemodelyear=new ArrayList<>();
    ArrayList<String> vehicleseating=new ArrayList<>();
    ArrayList<String> vehiclefuel=new ArrayList<>();
    ArrayList<String> vimage=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.vehicle_listing,container,false);
        recyclerView=view.findViewById(R.id.recycler);
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("type","bike");
            jsonObject.put("action","get_all_links");
            LoadData loadData=new LoadData();
            loadData.execute(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }
    @SuppressLint("StaticFieldLeak")
    private class LoadData extends AsyncTask<String,String,String> {
        ProgressDialog progressDialog=new ProgressDialog(getContext());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            JSONObject jsonObject=JsonFunction.GettingData(url,params[0]);
            if (jsonObject==null)
                return "NULL";
            else
                return jsonObject.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if (s.equalsIgnoreCase("NULL")){
                Toast.makeText(getContext(),"No Internet",Toast.LENGTH_SHORT).show();
            }else {
                try {
                    JSONObject j1=new JSONObject(s);
                    String res=j1.getString("response");
                    if(res.equalsIgnoreCase("success")) {
                        JSONArray jsonArray = j1.getJSONArray("vehicleinfo");
                        for (int i =0; i < jsonArray.length(); i++) {
                            JSONObject j2 = jsonArray.getJSONObject(i);
                            String id=j2.getString("id");
                            String title = j2.getString("VehiclesTitle");
                            String brand = j2.getString("BrandName");
                            String overview =j2.getString("VehiclesOverview");
                            String pricing =j2.getString("PricePerDay");
                            String fuelType =j2.getString("FuelType");
                            String model_yr=j2.getString("ModelYear");
                            String seating=j2.getString("SeatingCapacity");
                            String image=j2.getString("Vimage1");
                            String img2="https://gogoogol.in/admin/img/vehicleimages/"+image;
                            vehicleid.add(id);
                            vehicletitle.add(title);
                            vehiclebrand.add(brand);
                            vehicleoverview.add(overview);
                            vehicleseating.add(seating);
                            vehiclepricing.add(pricing);
                            vehiclemodelyear.add(model_yr);
                            vehiclefuel.add(fuelType);
                            vimage.add(img2);
                        }

                        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(),
                                LinearLayoutManager.VERTICAL,false);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(new RecycleAdp_Car_listing(getContext(),vehicleid,vehicletitle,vehiclebrand,vehicleoverview,vehicleseating,vehiclepricing,vehiclemodelyear,vehiclefuel,vimage));
                    }else {
                        Snackbar.make(Objects.requireNonNull(getView()),"No Data",Snackbar.LENGTH_SHORT);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
