package com.example.simplelogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.simplelogin.util.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivitySentCode extends AppCompatActivity {

    AppCompatEditText edtCode;
    AppCompatButton btnSuccess, btnResend;
    AppCompatTextView txtTimer;

    String getCode;
    String getGetCodeEdt;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    long currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_code);
        edtCode = findViewById(R.id.edt_code);
        btnSuccess = findViewById(R.id.btn_success);
        btnResend = findViewById(R.id.btn_resend);
        txtTimer = findViewById(R.id.text_count_time);

        getCode = ActivityGetNumber.random;
        Toast.makeText(getApplicationContext(), "getCode " + getCode, Toast.LENGTH_LONG).show();

        btnSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getGetCodeEdt = edtCode.getText().toString().trim();
                int firstTempCode = Integer.parseInt(getCode);
                int secondTempCode = Integer.parseInt(getGetCodeEdt);
                if (firstTempCode == secondTempCode) {
                    Toast.makeText(ActivitySentCode.this, "با موفقیت وارد شدید", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ActivitySentCode.this, ActivityHome.class);
                    startActivity(intent);
                    preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    editor = preferences.edit();
                    editor.putBoolean("hasSignUp", true);
                    editor.putString("PhoneNumber", ActivityGetNumber.getPhone);
                    editor.commit();
                    finish();

                } else {
                    Toast.makeText(ActivitySentCode.this, "کد تایید نا معتبر است", Toast.LENGTH_SHORT).show();
                }


            }
        });

        CountDownTimer downTimer = new CountDownTimer(120000, 1000) {
            @Override
            public void onTick(long l) {
                txtTimer.setText(String.valueOf(l / 1000) + " " + "ثانیه تا ارسال مجدد کد");
//                txtTimer.setText( "ثانیه تا ارسال مجدد کد");

            }

            @Override
            public void onFinish() {
                Toast.makeText(ActivitySentCode.this, "زمان شما به پایان رسید", Toast.LENGTH_SHORT).show();
                btnResend.setVisibility(View.VISIBLE);

            }

        };
        downTimer.start();
        btnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendRequest();
            }
        });

    }

    private void resendRequest() {
        String random = String.valueOf((int) Math.random() * 1000000);
        String getPhone = ActivityGetNumber.getPhone;
        String URL = "https://api.kavenegar.com/v1/6B706E4D704850397238756A51784C576B5973474F3048563951484A4F4A6D7639776E77444F43365863413D" +
                "/verify/lookup.json?receptor="+getPhone+"&token="+random+"&template=hamed";
        ProgressDialog progressDialog = new ProgressDialog(ActivitySentCode.this);
        progressDialog.setCancelable(false);
        progressDialog.show();
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject object = response.getJSONObject("return");
                    int result = object.getInt("status");
                    if (result == 200) {
                        Toast.makeText(ActivitySentCode.this, "عملیات با موفقیت انجام شد", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ActivitySentCode.this, ActivitySentCode.class));
                        progressDialog.dismiss();
                    } else {
                        Toast.makeText(ActivitySentCode.this, "خطا در اتصال", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ActivitySentCode.this, "عملیات انجام نشد", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        };
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, listener, errorListener);
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}