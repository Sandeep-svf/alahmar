package com.ngo.alahmaar.basicAndroidFunction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.ngo.alahmaar.MainActivity;
import com.ngo.alahmaar.R;
import com.ngo.alahmaar.model.LoginModel;
import com.ngo.alahmaar.model.TokenModel;
import com.ngo.alahmaar.retrofit.API_Client;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    AppCompatTextView register_layout,forgot_password_layout;
    AppCompatButton login_button;
    AppCompatEditText user_email_edittext,uesr_password_edittext;
    private String userEmailData, userPasswordData;
    private String accessToken, refreshToken, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inits();

        forgot_password_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });


        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_user_form_data();
                if(validation())
                {
                    login_api();
                   /* Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();*/
                }

            }
        });

        register_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    private void login_api() {


            // show till load api data

            final ProgressDialog pd = new ProgressDialog(this);
            pd.setCancelable(false);
            pd.setMessage("loading...");
            pd.show();

            Call<LoginModel> call = API_Client.getClient().login(userEmailData,userPasswordData);

            call.enqueue(new Callback<LoginModel>() {
                @Override
                public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                    pd.dismiss();
                    try {
                        //if api response is successful ,taking message and success
                        if (response.isSuccessful()) {
                            String message = response.body().getMsg();
                            String success = String.valueOf(response.body().getSuccess());
                            userId = String.valueOf(response.body().getUserId());


                            Log.e("djfklsdf",message+"ok");

                            if (success.equals("true") || success.equals("True")) {

                                LoginModel login_model =response.body();
                                TokenModel tokenModel = login_model.getToken();
                                accessToken = tokenModel.getAccess();
                                refreshToken = tokenModel.getRefresh();


                                String userImage = login_model.getImage();
                                String userName = login_model.getUsername();
                                String userEmail = login_model.getEmail();


                                try {
                                    Log.e("token", String.valueOf(accessToken));
                                    Log.e("token", String.valueOf(refreshToken));
                                    Log.e("token", String.valueOf(userEmail));
                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                }

                                // Using share preferance for setting userID data

                                SharedPreferences getUserIdData = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = getUserIdData.edit();
                                editor.putString("accessToken", String.valueOf(accessToken));
                                editor.putString("refreshToken", String.valueOf(refreshToken));
                                editor.putString("UserID", String.valueOf(userId));
                                editor.putString("userImage", String.valueOf(userImage));
                                editor.putString("userEmail", String.valueOf(userEmail));
                                editor.putString("userName", String.valueOf(userName));

                                editor.apply();

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(getApplicationContext(), message , Toast.LENGTH_LONG).show();
                                pd.dismiss();
                            }

                        } else {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Toast.makeText(getApplicationContext(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
                                switch (response.code()) {
                                    case 400:
                                        Toast.makeText(getApplicationContext(), "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 401:
                                        Toast.makeText(getApplicationContext(), "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 404:
                                        Toast.makeText(getApplicationContext(), "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 500:
                                        Toast.makeText(getApplicationContext(), "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 503:
                                        Toast.makeText(getApplicationContext(), "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 504:
                                        Toast.makeText(getApplicationContext(), "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 511:
                                        Toast.makeText(getApplicationContext(), "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(getApplicationContext(), "unknown error", Toast.LENGTH_SHORT).show();
                                        break;
                                }

                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (
                            Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<LoginModel> call, Throwable t) {
                    Log.e("conversion issue", t.getMessage());

                    if (t instanceof IOException) {
                        Toast.makeText(getApplicationContext(), "This is an actual network failure :( inform the user and possibly retry)", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    } else {
                        Log.e("conversion issue", t.getMessage());
                        Toast.makeText(getApplicationContext(), "Please Check your Internet Connection...." + t.getMessage(), Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            });

        }

    private void get_user_form_data() {
        userEmailData = user_email_edittext.getText().toString();
        userPasswordData = uesr_password_edittext.getText().toString();
    }

    private boolean validation() {
        if(userEmailData.equals(""))
        {
            Toast.makeText(LoginActivity.this, "Please enter user email.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(userPasswordData.equals(""))
        {
            Toast.makeText(LoginActivity.this, "Please enter user password.", Toast.LENGTH_SHORT).show();

            return false;
        }
        return true;
    }

    private void inits() {
        register_layout = findViewById(R.id.register_layout);
        login_button = findViewById(R.id.login_button);
        user_email_edittext = findViewById(R.id.user_email_edittext);
        uesr_password_edittext = findViewById(R.id.uesr_password_edittext);
        forgot_password_layout = findViewById(R.id.forgot_password_layout);
    }
}