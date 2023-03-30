package com.millivalley.retrofit2ndjanuary2023.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.millivalley.retrofit2ndjanuary2023.R;
import com.millivalley.retrofit2ndjanuary2023.adapter.HeroAdapter;
import com.millivalley.retrofit2ndjanuary2023.apis.RetrofitClient;
import com.millivalley.retrofit2ndjanuary2023.models.Hero;
import com.millivalley.retrofit2ndjanuary2023.models.Insert;
import com.millivalley.retrofit2ndjanuary2023.models.Root;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private String TAG = "wow123";
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // call the API
        addDataApi();
        getDataApi();
    }

    private void addDataApi() {
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View v = getLayoutInflater().inflate(R.layout.custom_item, null, false);
                EditText name = v.findViewById(R.id.name);
                EditText email = v.findViewById(R.id.email);
                EditText password = v.findViewById(R.id.password);
                EditText phone = v.findViewById(R.id.phone);
                Button addbtn = v.findViewById(R.id.btnAdd);
                Button cancelBtn = v.findViewById(R.id.btnCancel);


                builder.setView(v);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();


                addbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(TAG, "name: "+name.getText().toString());
                        Log.d(TAG, "email: "+email.getText().toString());
                        Log.d(TAG, "password: "+password.getText().toString());
                        Log.d(TAG, "phone: "+phone.getText().toString());
//                        api calling
                        Call<Insert> call = RetrofitClient.getInstance().getMyApi().AddCustomer(
                                name.getText().toString(),
                                email.getText().toString(),
                                password.getText().toString(),
                                phone.getText().toString());

                        call.enqueue(new Callback<Insert>() {
                            @Override
                            public void onResponse(Call<Insert> call, Response<Insert> response) {
                                Toast.makeText(MainActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Insert> call, Throwable t) {
                                Toast.makeText(MainActivity.this, "Error:"+t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        alertDialog.cancel();
                    }
                });

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                    }
                });
            }
        });
    }

    private void setupRecycler(List<Root> heroList) {
        recyclerView = findViewById(R.id.recyclerViewHeroes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new HeroAdapter(heroList, this));

    }

    private void getDataApi() {

        Call<List<Root>> call = RetrofitClient.getInstance().getMyApi().getCustomer();

        call.enqueue(new Callback<List<Root>>() {
            @Override
            public void onResponse(Call<List<Root>> call, Response<List<Root>> response) {
                Log.d(TAG, "onResponse: " + response.body().get(0).getName());
                setupRecycler(response.body());
            }

            @Override
            public void onFailure(Call<List<Root>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

        //        Call<List<Hero>> call = RetrofitClient.getInstance().getMyApi().getHeroes();
//
//        call.enqueue(new Callback<List<Hero>>() {
//            @Override
//            public void onResponse(Call<List<Hero>> call, Response<List<Hero>> response) {
//                Log.d("wow123", "onResponse: " + response.body().get(0).getName());
//                setupRecycler(response.body());
//
//            }
//
//            @Override
//            public void onFailure(Call<List<Hero>> call, Throwable t) {
//                Log.d("wow123", "onFailure: ");
//            }
//        });
    }
}
