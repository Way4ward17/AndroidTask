package com.theway4wardacademy.androidtask.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.theway4wardacademy.androidtask.Models.UserModels;
import com.theway4wardacademy.androidtask.R;
import com.theway4wardacademy.androidtask.Utils.Constant;
import com.theway4wardacademy.androidtask.Utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {


    ProgressBar progressBar;
    StringRequest stringRequestFirst;
    RequestQueue requestQueue;
    EditText email;
    int change = 1;
    TextInputEditText password;
    SharedPrefManager sharedPrefManager;
    TextInputLayout textInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        requestQueue = Volley.newRequestQueue(this);
        sharedPrefManager = new SharedPrefManager(this);
        email = findViewById(R.id.email);
        progressBar = findViewById(R.id.progressBar);
        password = findViewById(R.id.emailPassword);
        textInputLayout = findViewById(R.id.textinputlayout);

        if(sharedPrefManager.getToken() != null){
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }


        textInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(change == 1) {
                    textInputLayout.setEndIconDrawable(R.drawable.ic_baseline_visibility_24);
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                    change = 2;
                }else{
                    textInputLayout.setEndIconDrawable(R.drawable.ic_baseline_visibility_off_24);
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    change = 1;
                }
            }
        });


    }


    public void login(View view){
        progressBar.setVisibility(View.VISIBLE);
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();
        if (TextUtils.isEmpty(emailText) || TextUtils.isEmpty(passwordText)){
            progressBar.setVisibility(View.GONE);
            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout, "Field cant be empty .. ", Snackbar.LENGTH_SHORT).show();
        } else {

            getLogin(emailText, passwordText);
        }
    }
    private void getLogin(final String email, final String password){

        stringRequestFirst  = new StringRequest(Request.Method.POST,
                Constant.LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject product1 = new JSONObject(response);
                            String success = product1.getString("success");



                            if(success.equals("true")) {
                                progressBar.setVisibility(View.GONE);
                                String token = product1.getString("token");
                                sharedPrefManager.saveToken(token);
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(Login.this,"Success",Toast.LENGTH_LONG).show();
                            }else{
                                View parentLayout = findViewById(android.R.id.content);
                                Snackbar.make(parentLayout,  "Invalid Login Details", Snackbar.LENGTH_SHORT).show();
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

                    }
                }){

            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("email",email);
                params.put("password",password);
                return params;
            }


        };
        requestQueue.add(stringRequestFirst);


    }
}