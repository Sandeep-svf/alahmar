package com.ngo.alahmaar.basicAndroidFunction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.ngo.alahmaar.R;
import com.ngo.alahmaar.model.RegistrationModel;
import com.ngo.alahmaar.model.UserAccountVerificationModel;
import com.ngo.alahmaar.retrofit.API_Client;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import in.aabhasjindal.otptextview.OtpTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    AppCompatButton register_button;
    AppCompatEditText mobile_number_Edittext_FP, new_password_Edittext_FP, confirm_new_password_Edittext_FP;
    private String userMobileData, userNewPassword, userConfirmPassword;
    private String countryCode="91",phoneVerificationId,userOtpData;
    private FirebaseAuth mAuth;
    private Context context;
    OtpTextView otpText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        FirebaseApp.initializeApp(ForgotPasswordActivity.this);
        context = ForgotPasswordActivity.this;
        FirebaseApp.getApps(context);
        mAuth = FirebaseAuth.getInstance();

        intis();




        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_form_data();
                if(validation()){

                    change_password_api();


                    //  user_account_verification();
                }
            }
        });


    }

    private void user_account_verification() {

        // show till load api data

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("loading...");
        pd.show();

        Call<UserAccountVerificationModel> call = API_Client.getClient().user_account_verification(userMobileData);
        call.enqueue(new Callback<UserAccountVerificationModel>() {
            @Override
            public void onResponse(Call<UserAccountVerificationModel> call, Response<UserAccountVerificationModel> response) {
                pd.dismiss();
                try {
                    //if api response is successful ,taking message and success
                    if (response.isSuccessful()) {
                        String message = response.body().getMsg();
                        String success = String.valueOf(response.body().getSuccess());

                        if (success.equals("true") || success.equals("True")) {


                            Log.e("OTPNUMBER","+"+countryCode + userMobileData);
                            // if account exist than send verification code to user..
                            sendVerificationCode("+"+countryCode + userMobileData);
                            Toast.makeText(ForgotPasswordActivity.this, message, Toast.LENGTH_SHORT).show();


                        } else {
                            Toast.makeText(getApplicationContext(), message , Toast.LENGTH_LONG).show();
                            pd.dismiss();
                        }

                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Toast.makeText(getApplicationContext(), jObjError.getString("User is not registered."), Toast.LENGTH_LONG).show();
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
            public void onFailure(Call<UserAccountVerificationModel> call, Throwable t) {
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

    private void sendVerificationCode(String s) {
        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                s, // first parameter is user's mobile number
                60, // second parameter is time limit for OTP
                // verification which is 60 seconds in our case.
                TimeUnit.SECONDS, // third parameter is for initializing units
                // for time period which is in seconds in our case.
                TaskExecutors.MAIN_THREAD, // this task will be excuted on Main thread.
                mCallBack // we are calling callback method when we recieve OTP for
                // auto verification of user.
        );

    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            // initializing our callbacks for on
            // verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // below method is used when
        // OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // when we receive the OTP it
            // contains a unique id which
            // we are storing in our string
            // which we have already created.
           /* Intent intent=new Intent(getApplicationContext(),Verification_Activity.class);
            startActivity(intent);*/
            phoneVerificationId = s;
            //viewFlipper.setDisplayedChild(1);

            popup();


        }

        // this method is called when user
        // receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            // below line is used for getting OTP code
            // which is sent in phone auth credentials.
            final String code = phoneAuthCredential.getSmsCode();
            signInWithPhoneAuthCredential(phoneAuthCredential);
            // checking if the code
            // is null or not.

            if (code != null) {
                //   Forget_Login();
                // if the code is not null then
                // we are setting that code to
                // our OTP edittext field.
//                edtOTP.setText(code);

                // after setting this code
                // to OTP edittext field we
                // are calling our verifycode method.
//                verifyCode(code);
            }
        }

        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.

         /*   Intent intent = new Intent(RegisterActivity.this, NameEmailActivity.class);
            startActivity(intent);*/
            Toast.makeText(ForgotPasswordActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            Toast.makeText(ForgotPasswordActivity.this,"Please enter valid number", Toast.LENGTH_LONG).show();
        }
    };

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(ForgotPasswordActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            Toast.makeText(ForgotPasswordActivity.this, "OTP Verification Successful", Toast.LENGTH_SHORT).show();

                            // forgot password api

                            change_password_api();

                        } else {
                            Toast.makeText(ForgotPasswordActivity.this, "Please enter valid OTP", Toast.LENGTH_SHORT).show();
                            if (task.getException() instanceof
                                    FirebaseAuthInvalidCredentialsException) {
                            }
                        }
                    }
                });
    }

    private void change_password_api() {


        // show till load api data

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("loading...");
        pd.show();

        Call<RegistrationModel> call = API_Client.getClient().reset_password( userMobileData,userNewPassword);

        call.enqueue(new Callback<RegistrationModel>() {
            @Override
            public void onResponse(Call<RegistrationModel> call, Response<RegistrationModel> response) {
                pd.dismiss();
                try {
                    //if api response is successful ,taking message and success
                    if (response.isSuccessful()) {
                        String message = response.body().getMsg();
                        String success = String.valueOf(response.body().getSuccess());



                        Log.e("djfklsdf",message+"ok");

                        if (success.equals("true") || success.equals("True")) {

                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
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
            public void onFailure(Call<RegistrationModel> call, Throwable t) {
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


    private void popup() {

        AlertDialog dialogs;


        final LayoutInflater inflater = ForgotPasswordActivity.this.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.test_dialog_xml_otp, null);
        final AppCompatButton continue_button = alertLayout.findViewById(R.id.continue_button);
        final OtpTextView otpText = alertLayout.findViewById(R.id.otpText);



        final AlertDialog.Builder alert = new AlertDialog.Builder(ForgotPasswordActivity.this);

        alert.setView(alertLayout);
        alert.setCancelable(false);

        dialogs = alert.create();
        dialogs.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogs.show();
        dialogs.setCanceledOnTouchOutside(true);


        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                dialogs.dismiss();
                // call varification function here......
                userOtpData = otpText.getOTP();

                if(userOtpData.equals("")){
                    Toast.makeText(ForgotPasswordActivity.this, "Please enter valid OTP", Toast.LENGTH_SHORT).show();
                }else if(userOtpData.length()!=6){
                    Toast.makeText(ForgotPasswordActivity.this, "Please enter valid OTP", Toast.LENGTH_SHORT).show();

                } else{
                    verifyCode();
                }
            }
        });
    }

    private void verifyCode() {

        String code = userOtpData;
        if ((!code.equals("")) && (code.length() == 6)) {

            PhoneAuthCredential credential =
                    PhoneAuthProvider.getCredential(phoneVerificationId, code);
            signInWithPhoneAuthCredential(credential);
        } else if (code.length() != 6) {
            Toast.makeText(this, "Please enter six digit valid otp", Toast.LENGTH_SHORT).show();

        }
    }

    private boolean validation() {

        if(userMobileData.equals("")){
            Toast.makeText(ForgotPasswordActivity.this, "Please enter mobile number", Toast.LENGTH_SHORT).show();
            return false;
        }else  if(userNewPassword.equals("")){
            Toast.makeText(ForgotPasswordActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
            return false;
        }else  if(userConfirmPassword.equals("")){
            Toast.makeText(ForgotPasswordActivity.this, "Please confirm password", Toast.LENGTH_SHORT).show();
            return false;
        }else  if(userNewPassword==userConfirmPassword){
            Toast.makeText(ForgotPasswordActivity.this, "Password did not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void get_form_data() {
        userMobileData = mobile_number_Edittext_FP.getText().toString();
        userNewPassword = new_password_Edittext_FP.getText().toString();
        userConfirmPassword = confirm_new_password_Edittext_FP.getText().toString();
    }

    private void intis() {
        mobile_number_Edittext_FP = findViewById(R.id.mobile_number_Edittext_FP);
        new_password_Edittext_FP = findViewById(R.id.new_password_Edittext_FP);
        confirm_new_password_Edittext_FP = findViewById(R.id.confirm_new_password_Edittext_FP);
        register_button = findViewById(R.id.register_button);
        otpText= findViewById(R.id.otpText);
    }
}
