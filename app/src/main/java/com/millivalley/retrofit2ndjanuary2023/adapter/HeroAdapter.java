package com.millivalley.retrofit2ndjanuary2023.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.millivalley.retrofit2ndjanuary2023.R;
import com.millivalley.retrofit2ndjanuary2023.apis.RetrofitClient;
import com.millivalley.retrofit2ndjanuary2023.models.DeleteModel;
import com.millivalley.retrofit2ndjanuary2023.models.Hero;
import com.millivalley.retrofit2ndjanuary2023.models.Root;
import com.millivalley.retrofit2ndjanuary2023.models.update;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HeroAdapter extends RecyclerView.Adapter<HeroAdapter.Holder> {

    private List<Root> heroList;
    private Context context;

    public HeroAdapter(List<Root> heroList, Context context) {
        this.heroList = heroList;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_view_hero,parent,false);
        Holder h = new Holder(v);
        return h;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.name.setText(heroList.get(position).getName());
        holder.realname.setText(heroList.get(position).getId());
        holder.team.setText(heroList.get(position).getEmail());
        holder.firstapp.setText(heroList.get(position).getPassword());
        holder.createdby.setText(heroList.get(position).getPhone());

        holder.editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View v = LayoutInflater.from(context).inflate(R.layout.update_item,null,false);
                builder.setView(v);

                EditText name = v.findViewById(R.id.name);
                EditText email = v.findViewById(R.id.email);
                EditText password = v.findViewById(R.id.password);
                EditText phone = v.findViewById(R.id.phone);
                Button updatebtn = v.findViewById(R.id.btnAdd);
                Button cancelBtn = v.findViewById(R.id.btnCancel);

                name.setText(heroList.get(position).getName());
                email.setText(heroList.get(position).getEmail());
                phone.setText(heroList.get(position).getPhone());
                password.setText(heroList.get(position).getPassword());

                AlertDialog dialog = builder.create();
                dialog.show();

                updatebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("wow123", "name: "+name.getText().toString());
                        Log.d("wow123", "email: "+email.getText().toString());
                        Log.d("wow123", "password: "+password.getText().toString());
                        Log.d("wow123", "phone: "+phone.getText().toString());

                        Call<update> call = RetrofitClient.getInstance().getMyApi().UpdateCustomer(
                                heroList.get(position).getId(),
                                name.getText().toString(),
                                email.getText().toString(),
                                password.getText().toString(),
                                phone.getText().toString());

                        call.enqueue(new Callback<update>() {
                            @Override
                            public void onResponse(Call<update> call, Response<update> response) {
                                dialog.cancel();
                                Toast.makeText(context, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("wow123", "onResponse: "+response.body().getMessage());

                                Root root = new Root();
                                root.setId(heroList.get(position).getId());
                                root.setName(name.getText().toString());
                                root.setEmail(email.getText().toString());
                                root.setPassword(password.getText().toString());
                                root.setPhone(phone.getText().toString());

                                heroList.remove(position);
                                heroList.add(position,root);
                                notifyDataSetChanged();



                            }

                            @Override
                            public void onFailure(Call<update> call, Throwable t) {
                                dialog.cancel();
                                Toast.makeText(context, "Error:"+t.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("wow123", "onFailure: "+t.getMessage());

                            }
                        });

                    }
                });

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
            }
        });


        holder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "ID:"+heroList.get(position).getId(), Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete");
                builder.setMessage("Are You sure delete Customer");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Call<DeleteModel> modelCall = RetrofitClient.getInstance().getMyApi().DeleteCustomer(heroList.get(position).getId());
                        modelCall.enqueue(new Callback<DeleteModel>() {
                            @Override
                            public void onResponse(Call<DeleteModel> call, Response<DeleteModel> response) {
                                Toast.makeText(context, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("wow123", "onResponse: "+response.body().getMessage());

                                notifyItemRemoved(position);
                                heroList.remove(position);
                                notifyItemRangeChanged(position,heroList.size());

                            }

                            @Override
                            public void onFailure(Call<DeleteModel> call, Throwable t) {
                                Toast.makeText(context, "Error:"+t.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("wow123", "onFailure: "+t.getMessage());
                            }
                        });

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.create().show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return heroList.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        private TextView name,realname,team,firstapp,createdby,publisher,imgurl,bio;
        private ImageView deleteImg,editImage;

        public Holder(@NonNull View itemView) {
            super(itemView);
            deleteImg = itemView.findViewById(R.id.deleteImg);
            editImage = itemView.findViewById(R.id.updateImg);
            name = itemView.findViewById(R.id.name);
            realname = itemView.findViewById(R.id.realname);
            team = itemView.findViewById(R.id.team);
            firstapp = itemView.findViewById(R.id.firstAppearance);
            createdby = itemView.findViewById(R.id.createdby);
//            publisher = itemView.findViewById(R.id.publisher);
//            imgurl = itemView.findViewById(R.id.imgurl);
//            bio = itemView.findViewById(R.id.bio);
        }
    }
}
//    Root r = new Root();
//                                r.setId(heroList.get(position).getId());
//                                        r.setName(name.getText().toString());
//                                        r.setEmail(email.getText().toString());
//                                        r.setPassword(password.getText().toString());
//                                        r.setPhone(phone.getText().toString());
//
//                                        heroList.remove(position);
//                                        heroList.add(position,r);
//                                        notifyItemRemoved(position);
//                                        notifyItemRangeChanged(position,heroList.size());