package com.theway4wardacademy.androidtask.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.theway4wardacademy.androidtask.Models.UserModels;
import com.theway4wardacademy.androidtask.R;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    Context mContext;
    List<UserModels> mUsers;



    public UsersAdapter(Context mContext, List<UserModels> mUsers){
        this.mUsers = mUsers;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view;
           view = LayoutInflater.from(mContext).inflate(R.layout.item_user, parent, false);
           return new UserInfo(view);


    }

    public void setFilterClear(List<UserModels> data) {
        this.mUsers.clear();
        notifyDataSetChanged();
    }
    public void setFilter(List<UserModels> data) {
        this.mUsers.addAll(data);
        notifyDataSetChanged();
    }



    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final UserModels user = mUsers.get(position);

        ((UserInfo) holder).firstname.setText(user.getFirstname());
        ((UserInfo) holder).lastname.setText(user.getLastname());
        ((UserInfo) holder).email.setText(user.getEmailaddress());
        ((UserInfo) holder).phonenumber.setText(user.getPhonenumber());

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    class UserInfo extends RecyclerView.ViewHolder {

        TextView firstname, lastname, email, phonenumber;



        public UserInfo(View itemView) {
            super(itemView);

            firstname = itemView.findViewById(R.id.first_name);
            lastname = itemView.findViewById(R.id.last_name);
            email = itemView.findViewById(R.id.email_address);
            phonenumber = itemView.findViewById(R.id.phone_number);




        }


    }



}