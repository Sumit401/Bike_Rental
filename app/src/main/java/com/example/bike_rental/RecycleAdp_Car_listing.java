package com.example.bike_rental;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecycleAdp_Car_listing extends RecyclerView.Adapter<RecycleAdp_Car_listing.MyViewHolder> {

    private Context context;
    private ArrayList<String> veh_id;
    private ArrayList<String> veh_title;
    private ArrayList<String> veh_brand;
    private ArrayList<String> veh_overview;
    private ArrayList<String> veh_seating;
    private ArrayList<String> veh_pricing;
    private ArrayList<String> veh_model;
    private ArrayList<String> veh_fuel;
    private ArrayList<String> veh_image;

    RecycleAdp_Car_listing(Context context1, ArrayList<String> vehicleid, ArrayList<String> vehicletitle, ArrayList<String> vehiclebrand,
                           ArrayList<String> vehicleoverview, ArrayList<String> vehicleseating, ArrayList<String> vehiclepricing,
                           ArrayList<String> vehiclemodelyear, ArrayList<String> vehiclefuel, ArrayList<String> vimage) {
        context=context1;
        veh_id=vehicleid;
        veh_title=vehicletitle;
        veh_brand=vehiclebrand;
        veh_overview=vehicleoverview;
        veh_seating=vehicleseating;
        veh_model=vehiclemodelyear;
        veh_pricing=vehiclepricing;
        veh_fuel=vehiclefuel;
        veh_image=vimage;

    }

    @NonNull
    @Override
    public RecycleAdp_Car_listing.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vehicle_listing_adapter,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final RecycleAdp_Car_listing.MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i) {
        Picasso.get().load(veh_image.get(i)).resize(300,180).into(myViewHolder.vehicleimage);
        myViewHolder.vehicleimage.setBackgroundResource(R.drawable.backgrounddetails);
        myViewHolder.brand.setText(veh_title.get(i)+", "+veh_brand.get(i));
        myViewHolder.seat.setText(veh_seating.get(i)+" Seater");
        myViewHolder.model.setText(veh_model.get(i)+" Model");
        myViewHolder.fuel.setText(veh_fuel.get(i)+" Vehicle");
        myViewHolder.pricing.setText("INR "+veh_pricing.get(i)+" Per Day");
        myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent=new Intent(context, MainActivity3.class);
                intent.putExtra("id",""+veh_id.get(i));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return veh_id.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView brand,seat,model,fuel,pricing;
        LinearLayout linearLayout;
        ImageView vehicleimage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            vehicleimage=itemView.findViewById(R.id.bikeimg);
            brand=itemView.findViewById(R.id.brand);
            seat=itemView.findViewById(R.id.seating);
            model=itemView.findViewById(R.id.model);
            fuel=itemView.findViewById(R.id.fuel);
            pricing=itemView.findViewById(R.id.pricing);
            linearLayout=itemView.findViewById(R.id.layoutinfo);
        }
    }
}
