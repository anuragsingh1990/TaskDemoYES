package com.peehu.taskdemoyestitlabs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.peehu.taskdemoyestitlabs.Method.isNetworkAvailable;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<ResponseModel> responseModels_list;
    List<ResponseModel> lst;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.rv);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        responseModels_list = new ArrayList<>();
        if (isNetworkAvailable(MainActivity.this)) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please Wait Loading....");
            progressDialog.show();
            getFliteList();
        } else {
            Toast.makeText(this, "No Connection Available", Toast.LENGTH_SHORT).show();
        }

    }

    private void getFliteList() {
        final API_Interface retrofitInterface = ApiClient.getClient().create(API_Interface.class);
        Call<List<ResponseModel>> call = retrofitInterface.GetJetLists("DEL", "CHE");
        responseModels_list.clear();
        call.enqueue(new Callback<List<ResponseModel>>() {
            @Override
            public void onResponse(Call<List<ResponseModel>> call, Response<List<ResponseModel>> response) {

                if (response.body() != null) {
                    progressDialog.dismiss();
                    lst = response.body();
                    responseModels_list.addAll(lst);
                    Adapter_Airlines adapter_user = new Adapter_Airlines(MainActivity.this, responseModels_list);
                    recyclerView.setAdapter(adapter_user);

                }
            }


            @Override
            public void onFailure
                    (Call<List<ResponseModel>> call, Throwable t) {
                Log.e("Error_Anurag", call.request().url().toString());

                progressDialog.dismiss();
            }
        });
    }
}
