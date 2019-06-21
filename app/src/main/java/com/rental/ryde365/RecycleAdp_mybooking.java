package com.rental.ryde365;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;


public class RecycleAdp_mybooking extends RecyclerView.Adapter<RecycleAdp_mybooking.MyViewHolder> {

    private Context context;
    private ArrayList<String> book_id;
    private ArrayList<String> veh_title;
    private ArrayList<String> veh_brand;
    private ArrayList<String> todate;
    private ArrayList<String> fromdate;
    private ArrayList<String> bookingdate;
    private ArrayList<String> bookingstats;
    private ArrayList<String> veh_image;
    private ArrayList<String> msg;

    RecycleAdp_mybooking(Context context1, ArrayList<String> vehicleid, ArrayList<String> vehicletitle, ArrayList<String> vehiclebrand,
                         ArrayList<String> todate_booking, ArrayList<String> fromDate_booking, ArrayList<String> booking_date,
                         ArrayList<String> booking_status, ArrayList<String> book_message, ArrayList<String> vimage) {
        context=context1;
        book_id=vehicleid;
        veh_title=vehicletitle;
        veh_brand=vehiclebrand;
        todate=todate_booking;
        fromdate=fromDate_booking;
        bookingstats=booking_status;
        msg=book_message;
        veh_image=vimage;
        bookingdate=booking_date;
    }

    @NonNull
    @Override
    public RecycleAdp_mybooking.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_booking_adapter,viewGroup,false);
        return new RecycleAdp_mybooking.MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i) {
        Picasso.get().load(veh_image.get(i)).resize(400,200).into(myViewHolder.vehicleimage);
        myViewHolder.brand.setText(veh_title.get(i)+", "+veh_brand.get(i));
        myViewHolder.fromdate.setText(fromdate.get(i));
        myViewHolder.todate.setText(todate.get(i));
        myViewHolder.bookingdate.setText(bookingdate.get(i));
        if (bookingstats.get(i).equalsIgnoreCase("1")){
            myViewHolder.veh_status.setText("Active");
        }else if (bookingstats.get(i).equalsIgnoreCase("0")){
            myViewHolder.veh_status.setText("InActive");
        }
        myViewHolder.messages.setText(msg.get(i));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            myViewHolder.messages.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }
        myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(context, Booked_Vehicle.class);
                intent.putExtra("book_id",""+ book_id.get(i));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return book_id.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView brand,fromdate,todate,bookingdate,veh_status,messages,pricing;
        LinearLayout linearLayout;
        ImageView vehicleimage;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            vehicleimage=itemView.findViewById(R.id.vehicleimg);
            brand=itemView.findViewById(R.id.veh_brand);
            fromdate=itemView.findViewById(R.id.booking_fromdate);
            todate=itemView.findViewById(R.id.booking_todate);
            bookingdate=itemView.findViewById(R.id.veh_bookingdate);
            veh_status=itemView.findViewById(R.id.veh_status);
            messages=itemView.findViewById(R.id.booking_message);
            pricing=itemView.findViewById(R.id.pricing);
            linearLayout=itemView.findViewById(R.id.layoutinfo);
        }
    }
}