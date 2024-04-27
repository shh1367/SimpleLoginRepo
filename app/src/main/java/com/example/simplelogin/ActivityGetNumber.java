package com.example.simplelogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.simplelogin.commands.UserLogin;
import com.example.simplelogin.databinding.ActivityMainBinding;
import com.example.simplelogin.model.User;
import com.example.simplelogin.util.MySingleton;
import com.example.simplelogin.viewmodel.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityGetNumber extends AppCompatActivity {
    private ActivityMainBinding activityMainBinding;
    AppCompatEditText edtPhone;
    AppCompatButton btnSend;

    static String random;
    static String getPhone;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_number);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean login = sharedPreferences.getBoolean("hasSignUp", false);
        if (login) {
            Toast.makeText(this, "شما قبلا ثبت نام کرده اید", Toast.LENGTH_LONG).show();
            startActivity(new Intent(ActivityGetNumber.this, ActivityHome.class));
            finish();
        }

        edtPhone = findViewById(R.id.edt_phone);
        btnSend = findViewById(R.id.btn_send);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredPhone = edtPhone.getText().toString();
                if (!enteredPhone.matches("(\\+98|0)?9\\d{9}")) {
                    Toast.makeText(ActivityGetNumber.this, "شماره موبایل معتبر نمی باشد", Toast.LENGTH_SHORT).show();


                } else {
                    UserModel userModel = new UserModel(new User(enteredPhone));
//
//                    Intent intent = new Intent(ActivityGetNumber.this, ActivitySentCode.class);
//                    startActivity(intent);
                    sendRequest();

                }

            }

            private void sendRequest() {

                random = String.valueOf((int)( Math.random() * 1000000));
                getPhone = edtPhone.getText().toString().trim();
                String URL = "https://api.kavenegar.com/v1/6B706E4D704850397238756A51784C576B5973474F3048563951484A4F4A6D7639776E77444F43365863413D/verify/lookup.json?receptor="+getPhone+"&token="+random +"&template=hamed";
                ProgressDialog progressDialog = new ProgressDialog(ActivityGetNumber.this);
                progressDialog.setCancelable(false);
                progressDialog.show();
                Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject object = response.getJSONObject("return");
                            int result = object.getInt("status");
                            if (result == 200) {
                                Toast.makeText(ActivityGetNumber.this, "عملیات با موفقیت انجام شد", Toast.LENGTH_SHORT).show();
                                Intent intent = new  Intent (ActivityGetNumber.this, ActivitySentCode.class);
                                startActivity(intent);
                                progressDialog.dismiss();
                                finish();
                            } else {
                                Toast.makeText(ActivityGetNumber.this, "خطا در اتصال", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivityGetNumber.this, "عملیات انجام نشد", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                };
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, listener, errorListener);
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

            }
        });


    }

}
