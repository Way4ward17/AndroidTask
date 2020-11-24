package com.theway4wardacademy.androidtask.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.theway4wardacademy.androidtask.Adapter.UsersAdapter;
import com.theway4wardacademy.androidtask.Models.UserModels;
import com.theway4wardacademy.androidtask.R;
import com.theway4wardacademy.androidtask.Utils.Constant;
import com.theway4wardacademy.androidtask.Utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    StringRequest stringRequestFirst;
    RequestQueue requestQueue;
    LinearLayoutManager linearLayoutManager;
    UsersAdapter usersAdapter;
    List<UserModels> userModels;
    SharedPrefManager sharedPrefManager;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);
        sharedPrefManager = new SharedPrefManager(this);
        recyclerView = findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(this);
        userModels = new ArrayList<>();
        usersAdapter = new UsersAdapter(this, userModels);
        recyclerView.setAdapter(usersAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        progressBar = findViewById(R.id.progressBar);
        getUserInfo();
    }

    private void getUserInfo(){
                stringRequestFirst  = new StringRequest(Request.Method.GET,
                        Constant.GETUSER,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                            Log.d("Response - > ", response);

                                try {
                                    JSONObject product1 = new JSONObject(response);
                                    String success = product1.getString("success");
                                    JSONArray array = product1.getJSONArray("result");

                                    if(success.equals("true")) {
                                        for(int i=0;i<array.length();i++){
                                            JSONObject jo=array.getJSONObject(i);
                                            UserModels dataSet = new UserModels();
                                            dataSet.setFirstname(jo.getString("first_name"));
                                            dataSet.setLastname(jo.getString("last_name"));
                                            dataSet.setEmailaddress(jo.getString("email"));
                                            dataSet.setPhonenumber(jo.getString("phone"));
                                            userModels.add(dataSet);
                                        }
                                        usersAdapter.setFilter(userModels);
                                        progressBar.setVisibility(View.GONE);
                                    }
                                } catch (JSONException ex) {
                                    ex.printStackTrace();

                                }


                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                stringRequestFirst.setRetryPolicy(new DefaultRetryPolicy(5000,3,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                Log.d("Error Response", "onErrorResponse: Volley Error = " + error);
                            }
                        }){

                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("email","sandbox@grazac.com.ng");
                        params.put("password","tobiloba123");
                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        String credentials = "sandbox@grazac.com.ng:tobiloba123";
                        String auth = "Basic "
                                + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                        headers.put("Content-Type", "application/json");
                        //headers.put("Authorization", auth);
                        headers.put("Authorization", "Bearer" + " " + sharedPrefManager.getToken());
                        headers.put("accept-language","EN");
                        headers.put("access-control-allow-credentials","true");
                        return headers;
                    }
                };
                requestQueue.add(stringRequestFirst);


    }

    public void logout(View view) {
        sharedPrefManager.saveToken(null);
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
        finish();
    }
}