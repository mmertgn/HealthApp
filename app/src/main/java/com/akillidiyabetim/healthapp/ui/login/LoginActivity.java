package com.akillidiyabetim.healthapp.ui.login;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.akillidiyabetim.healthapp.ApiServices.VolleySingleton;
import com.akillidiyabetim.healthapp.Entities.Kullanici;
import com.akillidiyabetim.healthapp.MainActivity;
import com.akillidiyabetim.healthapp.R;
import com.akillidiyabetim.healthapp.RegisterActivity;
import com.akillidiyabetim.healthapp.Service.SharedPrefManager;
import com.akillidiyabetim.healthapp.Service.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
TextView mTxtEmail;
TextView mTxtPassword;
    ProgressBar loadingProgressBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(this);
        setContentView(R.layout.activity_login);
        //TODO logout sadece test aşaması için
        //SharedPrefManager.getInstance(this).logout();
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
        mTxtEmail = findViewById(R.id.txtEmail);
        mTxtPassword = findViewById(R.id.txtPassword);
        final Button loginButton = findViewById(R.id.btnLogin);
        loadingProgressBar = findViewById(R.id.loading);
        final Button mBtnRegister = findViewById(R.id.btnRegister);


        loginButton.setOnClickListener(v -> userLogin());

        mBtnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
        });
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.gradient);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }


    private void userLogin() {
        //first getting the values
        final String username = mTxtEmail.getText().toString();
        final String password = mTxtPassword.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(username)) {
            mTxtEmail.setError("Lütfen mail adresinizi giriniz.");
            mTxtEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            mTxtPassword.setError("Lütfen şifrenizi giriniz.");
            mTxtPassword.requestFocus();
            return;
        }
        loadingProgressBar.setVisibility(View.VISIBLE);
        String loginUrl = URLs.URL_LOGIN + "?request={\"Email\": \""+ username+"\",\"Sifre\": \""+password+"\"}";
        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, /*URLs.URL_LOGIN*/ loginUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loadingProgressBar.setVisibility(View.GONE);

                        try {
                            JSONObject obj = new JSONObject(response);

                            if (obj.getString("Status") == "true") {
                                Toast.makeText(getApplicationContext(), obj.getString("Message"), Toast.LENGTH_SHORT).show();

                                JSONObject userJson = new JSONObject(obj.getString("Data"));

                                Kullanici user = new Kullanici();
                                user.setEmail(userJson.getString("Email"));
                                user.setSifre(userJson.getString("Sifre"));
                                user.setAdi(userJson.getString("Adi"));
                                user.setSoyadi(userJson.getString("Soyadi"));
                                //user.setCinsiyet(userJson.getString("Cinsiyet")); TODO Düzeltilecek
                                user.setCinsiyet("Erkek");
                                user.setDogumTarihi(userJson.getString("DogumTarihi"));
                                user.setDiyabetTipi(userJson.getString("DiyabetTipi"));
                                user.setTeshisKondoguTarih(userJson.getString("TeshisKondoguTarih"));
                                user.setIl(userJson.getString("il"));
                                user.setIlce(userJson.getString("ilce"));
                                user.setId(userJson.getInt("Id"));
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                                finish();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("Message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("request", "{\"Email\": \""+ username+"\",\"Sifre\": \""+password+"\"}");
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
