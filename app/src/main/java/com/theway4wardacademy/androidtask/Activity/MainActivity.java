package com.theway4wardacademy.androidtask.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

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

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    StringRequest stringRequestFirst;
    RequestQueue requestQueue;
    LinearLayoutManager linearLayoutManager;
    UsersAdapter usersAdapter;
    List<UserModels> userModels;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);
    }



    private void getUserInfo(){


                stringRequestFirst  = new StringRequest(Request.Method.POST,
                        Constant.GETUSERPOST,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {



                                try {
                                    final Gson gson = new Gson();
                                    Type type = new TypeToken<ArrayList<Post>>() {

                                    }.getType();



                                    sqLiteManager.deleteOldUserpost();

                                    imagePostFirst = gson.fromJson(response, type);
                                    for (int a = 0; a < imagePostFirst.size(); a++) {
                                        sqLiteManager.addUserpost(imagePostFirst.get(a));

                                    }



                                } catch (Exception e) {
                                    Log.d("Error Caught", "onResponse: response = " + response);
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
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("page", String.valueOf(integers[0]));
                        params.put("userid", sharedPrefManager.getID());
                        params.put("useridd", sharedPrefManager.getID());
                        return params;
                    }
                };
                requestQueue.add(stringRequestFirst);


    }
}