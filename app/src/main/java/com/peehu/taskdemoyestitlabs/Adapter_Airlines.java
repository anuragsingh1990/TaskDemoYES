package com.peehu.taskdemoyestitlabs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter_Airlines extends RecyclerView.Adapter<Adapter_Airlines.ProductViewHolder> {
    private Context mCtx;
    private List<ResponseModel> report_list;
    String flit_no;
    ViewGroup viewGroup;
    TextView flt_no, txt_price, txt_seat, txt_xurrcy, txt_from, txt_to;
    ProgressDialog progressDialog;


    public Adapter_Airlines(Context mCtx, List<ResponseModel> report_list) {
        this.mCtx = mCtx;
        this.report_list = report_list;

    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.row_flite_items, parent, false);
        return new ProductViewHolder(view);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {

        final ResponseModel reports = report_list.get(position);
        holder.jet_Name.setText(reports.getAirline().getName());
        holder.jet_slot.setText(reports.getStops().toString());
        holder.jet_dep.setText(reports.getDeparture());
        holder.jet_des.setText(reports.getArrival());
        holder.jet_price.setText(reports.getFlightNumber());
        holder.jet_time.setText(reports.getDuration());
        holder.jet_sheet.setText(reports.getInstructions());
        Glide.with(mCtx).load(reports.getAirline().getLogo()).into(holder.img);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flit_no = report_list.get(position).getFlightNumber();
                showListDialog();

            }
        });


    }

    private void showListDialog() {
        final View dialogView = LayoutInflater.from(mCtx).inflate(R.layout.dialod_layout, viewGroup, false);
        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(mCtx, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        flt_no = dialogView.findViewById(R.id.flt_no);
        txt_price = dialogView.findViewById(R.id.price);
        txt_seat = dialogView.findViewById(R.id.jet_seet);
        txt_xurrcy = dialogView.findViewById(R.id.cuurcy);
        txt_from = dialogView.findViewById(R.id.from);
        txt_to = dialogView.findViewById(R.id.to);
        progressDialog = new ProgressDialog(mCtx);
        progressDialog.setMessage("Loading Please Wait....");
        progressDialog.show();


        final API_Interface retrofitInterface = ApiClient.getClient().create(API_Interface.class);
        Call<Pojo_Details> call = retrofitInterface.GetDetails(flit_no, "DEL", "CHE");

        call.enqueue(new Callback<Pojo_Details>() {
            @Override
            public void onResponse(Call<Pojo_Details> call, Response<Pojo_Details> response) {

                if (response.body() != null) {
                    progressDialog.dismiss();
                    flt_no.setText(response.body().flight_number);
                    txt_price.setText("Price:  " + response.body().price);
                    txt_seat.setText("Seat:  " + response.body().seats);
                    txt_xurrcy.setText("Curr:  " + response.body().currency);
                    txt_from.setText("From:  " + response.body().from);
                    txt_to.setText("To:  " + response.body().to);


                }
            }


            @Override
            public void onFailure
                    (Call<Pojo_Details> call, Throwable t) {
                Log.e("Error_Anurag", call.request().url().toString());

                progressDialog.dismiss();
            }
        });


    }


    @Override
    public int getItemCount() {
        return report_list.size();
    }

    public void updateList(List<ResponseModel> list) {
        report_list = list;
        notifyDataSetChanged();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView jet_Name, jet_slot, jet_dep, jet_des, jet_price, jet_time, jet_sheet;
        CircleImageView img;
        CardView card;


        public ProductViewHolder(final View itemView) {
            super(itemView);
            jet_Name = itemView.findViewById(R.id.jet_name);
            jet_slot = itemView.findViewById(R.id.jet_slot);
            jet_dep = itemView.findViewById(R.id.jet_dep);
            jet_des = itemView.findViewById(R.id.jet_des);
            jet_price = itemView.findViewById(R.id.jet_price);
            jet_time = itemView.findViewById(R.id.jet_time);
            jet_sheet = itemView.findViewById(R.id.jet_sheets);
            img = itemView.findViewById(R.id.img_logo);
            card = itemView.findViewById(R.id.card);


        }

    }
}
