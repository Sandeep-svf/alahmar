package com.ngo.alahmaar.basicAndroidFunction;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
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

import com.ngo.alahmaar.retrofit.API_Client;
import com.ngo.alahmaar.utility.Permission;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import in.aabhasjindal.otptextview.OtpTextView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_R = 178500;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int CAMERA_PIC_REQUEST = 1457;
    CircleImageView user_profile_image,add_image_layout;
    AppCompatTextView back_to_login;
    AppCompatButton register_button;
    AppCompatEditText user_email_edittext_register,user_name_edittext_register,confirm_password_Edittext_register,
            password_Edittext_register,phone_number_Edittext_register,city_Edittext_register;
    private String userNameData, userEmailData, userPasswordData, userConfirmPasswordData, userPhoneNumberData
            ,userCityData,userOtpData;
    private File userProfileImageData;
    private String countryCode="91";
    private String phoneVerificationId,device_token;
    public FirebaseAuth mAuth;
    public Context context;
    AlertDialog dialogs;
    OtpTextView otpText;
    private ContentValues values5;
    private Uri imageUri5;
    AppCompatImageView hiden_password_image, visibale_password_image
            , hiden_c_password_image, visibale_c_password_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        intis();


        FirebaseApp.initializeApp(RegisterActivity.this);

        mAuth = FirebaseAuth.getInstance(FirebaseApp.initializeApp(RegisterActivity.this));


        hiden_password_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visibale_password_image.setVisibility(View.VISIBLE);
                hiden_password_image.setVisibility(View.GONE);
                password_Edittext_register.setTransformationMethod(null);
                password_Edittext_register.setSelection(password_Edittext_register.getText().length());

            }
        });
        visibale_password_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiden_password_image.setVisibility(View.VISIBLE);
                visibale_password_image.setVisibility(View.GONE);

                password_Edittext_register.setTransformationMethod(new PasswordTransformationMethod());
                password_Edittext_register.setSelection(password_Edittext_register.getText().length());
            }
        });

        hiden_c_password_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visibale_c_password_image.setVisibility(View.VISIBLE);
                hiden_c_password_image.setVisibility(View.GONE);
                confirm_password_Edittext_register.setTransformationMethod(null);
                confirm_password_Edittext_register.setSelection(confirm_password_Edittext_register.getText().length());

            }
        });
        visibale_c_password_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiden_c_password_image.setVisibility(View.VISIBLE);
                visibale_c_password_image.setVisibility(View.GONE);

                confirm_password_Edittext_register.setTransformationMethod(new PasswordTransformationMethod());
                confirm_password_Edittext_register.setSelection(confirm_password_Edittext_register.getText().length());
            }
        });

        add_image_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog addProfileUpdate = new Dialog(RegisterActivity.this);
                addProfileUpdate.requestWindowFeature(Window.FEATURE_NO_TITLE);
                addProfileUpdate.setContentView(R.layout.add_profile_update_dialog);
                TextView gallaryDialog = addProfileUpdate.findViewById(R.id.gallaryDialog);
                TextView cameraDialog = addProfileUpdate.findViewById(R.id.cameraDialog);

                addProfileUpdate.show();
                Window window = addProfileUpdate.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                gallaryDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /* openGallery(PICK_IMAGE);*/
                        gallery();
                        addProfileUpdate.dismiss();
                    }
                });

                cameraDialog.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View v) {

                        // profile_camera_open();

                        PackageManager packageManager = getApplicationContext().getPackageManager();

                        boolean readExternal = Permission.checkPermissionReadExternal(getApplicationContext());
                        boolean writeExternal = Permission.checkPermissionReadExternal2(getApplicationContext());
                        boolean camera = Permission.checkPermissionCamera(getApplicationContext());

                        if (camera && writeExternal && readExternal ) {
                            if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                                values5 = new ContentValues();
                                values5.put(MediaStore.Images.Media.TITLE, "New Picture");
                                values5.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                                imageUri5 = getApplicationContext().getContentResolver().insert(
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values5);
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri5);
                                startActivityForResult(intent, CAMERA_PIC_REQUEST);
                                addProfileUpdate.dismiss();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "camera permission required", Toast.LENGTH_LONG).show();
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                            addProfileUpdate.dismiss();
                        }

                    }
                });

            }
        });


        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                get_form_data();
                Log.e("dfsdf","userPasswordData: "+userPasswordData);
                Log.e("dfsdf","userConfirmPasswordData: "+userConfirmPasswordData);
                if(validation())
                {

                    // Mobile number verification using firebase.....


               /*     Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);*/

                   check_user_exist();


                    // register_api();
                }


            }
        });

        back_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void gallery() {


            boolean readExternal = Permission.checkPermissionReadExternal(getApplicationContext());
            boolean writeExternal = Permission.checkPermissionReadExternal2(getApplicationContext());
            boolean camera = Permission.checkPermissionCamera(getApplicationContext());
            if (readExternal && camera) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_R);
            }else
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                }

            }

        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if (requestCode == PICK_IMAGE_R) {

                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                Cursor cursor = managedQuery(selectedImageUri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                userProfileImageData = new File(cursor.getString(column_index));
                Log.e("userImage1", String.valueOf(userProfileImageData));
                String selectedImagePath1 = getPath(selectedImageUri);

                Log.v("Deatils_path","***"+selectedImagePath1);
                Glide.with(getApplicationContext()).load(selectedImagePath1).placeholder(R.drawable.ic_launcher_background).into(user_profile_image);
                Log.e("userImage1", "BB");


            }else if(requestCode==CAMERA_PIC_REQUEST){



                File file = new File(getRealPathFromURIs(imageUri5));

                Glide.with(getApplicationContext())
                        .load(file)
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(user_profile_image);

            }
        }

    }

    public String getRealPathFromURIs(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);

    }

    public String getPath(Uri uri)
    {
        Cursor cursor=null;
        try {
            String[] projection = {MediaStore.Images.Media.DATA};
            cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, null);
            if (cursor == null) return null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();


            return cursor.getString(column_index);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return "";
    }
    private void check_user_exist() {

            // show till load api data

            final ProgressDialog pd = new ProgressDialog(this);
            pd.setCancelable(false);
            pd.setMessage("loading...");
            pd.show();

            Call<RegistrationModel> call = API_Client.getClient().verify_account_by_phone(userPhoneNumberData);

            call.enqueue(new Callback<RegistrationModel>() {
                @Override
                public void onResponse(Call<RegistrationModel> call, Response<RegistrationModel> response) {
                    pd.dismiss();
                    try {
                        //if api response is successful ,taking message and success
                        if (response.isSuccessful()) {
                            String message = response.body().getMsg();
                            String success = String.valueOf(response.body().getSuccess());


                            if (success.equals("true") || success.equals("True")) {

                                Toast.makeText(getApplicationContext(), "Mobile number already register" , Toast.LENGTH_LONG).show();
                               // register_api();

                            } else {
                                sendVerificationCode("+"+countryCode + userPhoneNumberData);
                                Log.e("userPhoneNumber","+"+countryCode + userPhoneNumberData);


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




    private void sendVerificationCode(String mobileNumber) {

        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobileNumber, // first parameter is user's mobile number
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

            Log.e("firebase_status","on coade send called........");
            popup();


        }

        // this method is called when user
        // receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            // below line is used for getting OTP code
            // which is sent in phone auth credentials.
            Log.e("firebase_status","on verification complete called........");
            final String code = phoneAuthCredential.getSmsCode();
            signInWithPhoneAuthCredential(phoneAuthCredential);
            // checking if the code
            // is null or not.

        }

        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.

            Log.e("firebase_status","on verification failed called........");
         /*   Intent intent = new Intent(RegisterActivity.this, NameEmailActivity.class);
            startActivity(intent);*/
            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            Toast.makeText(RegisterActivity.this,"Please enter valid number", Toast.LENGTH_LONG).show();
        }
    };

    private void popup() {


            final LayoutInflater inflater = RegisterActivity.this.getLayoutInflater();
            View alertLayout = inflater.inflate(R.layout.test_dialog_xml_otp, null);
        final AppCompatButton continue_button = alertLayout.findViewById(R.id.continue_button);
        OtpTextView otpText = alertLayout.findViewById(R.id.otpText);



            final AlertDialog.Builder alert = new AlertDialog.Builder(RegisterActivity.this);

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
                    verifyCode(userOtpData);



                }
            });
        }

    private void verifyCode(String code) {
        //String code = userOtpData;

        Log.e("otp","OTP: "+code);

        if ((!code.equals("")) && (code.length() == 6)) {

            PhoneAuthCredential credential =
                    PhoneAuthProvider.getCredential(phoneVerificationId, code);
            signInWithPhoneAuthCredential(credential);
        } else if (code.length() != 6) {
            Toast.makeText(this, "Please enter six digit valid otp", Toast.LENGTH_SHORT).show();

        }
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.e("firebase_status","signInWithPhoneAuthCredential called........");

                        if (task.isSuccessful()) {


                            Toast.makeText(RegisterActivity.this, "OTP Verification Successful", Toast.LENGTH_SHORT).show();
                            
                            register_api();

                        } else {
                            Toast.makeText(RegisterActivity.this, "Please enter valid OTP", Toast.LENGTH_SHORT).show();
                            if (task.getException() instanceof
                                    FirebaseAuthInvalidCredentialsException) {
                            }
                        }
                    }
                });
    }

    private void register_api() {

            RequestBody UserNameData = RequestBody.create(MediaType.parse("text/plain"), userNameData);
            RequestBody UserEmailData = RequestBody.create(MediaType.parse("text/plain"), userEmailData);
            RequestBody UserMobileData = RequestBody.create(MediaType.parse("text/plain"), userPhoneNumberData);
            RequestBody UserPasswordData = RequestBody.create(MediaType.parse("text/plain"), userPasswordData);
            RequestBody city_name = RequestBody.create(MediaType.parse("text/plain"),userCityData );
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("image", userProfileImageData.getName(), RequestBody.create(MediaType.parse("image/*"), userProfileImageData));
//          MultipartBody.Part filePart1 = MultipartBody.Part.createFormData("nationalId", idImage.getName(), RequestBody.create(MediaType.parse("image/*"), idImage));


            // show till load api data

            final ProgressDialog pd = new ProgressDialog(this);
            pd.setCancelable(false);
            pd.setMessage("loading...");
            pd.show();

            Call<RegistrationModel> call = API_Client.getClient().registration(UserNameData,
                    UserEmailData,
                    UserMobileData,
                    UserPasswordData,
                    filePart);

            call.enqueue(new Callback<RegistrationModel>() {
                @Override
                public void onResponse(Call<RegistrationModel> call, Response<RegistrationModel> response) {
                    pd.dismiss();
                    try {
                        //if api response is successful ,taking message and success
                        if (response.isSuccessful()) {
                            String message = response.body().getMsg();
                            String success = response.body().getSuccess();


                            if (success.equals("true") || success.equals("True")) {
                                Toast.makeText(getApplicationContext(), message , Toast.LENGTH_LONG).show();

                                userProfileImageData = null;
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);

                            } else {
                                Toast.makeText(getApplicationContext(), message +  "\n" + "Please Try Again", Toast.LENGTH_LONG).show();
                                pd.dismiss();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
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
                                Log.e("conversion issue", e.getMessage());
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (
                            Exception e) {
                        Log.e("conversion issue", e.getMessage());
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
    private boolean validation() {
        if(userNameData.equals("")){
            Toast.makeText(RegisterActivity.this, "Please enter user name.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(userEmailData.equals("")){
            Toast.makeText(RegisterActivity.this, "Please enter user email.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(userPhoneNumberData.equals("")){
            Toast.makeText(RegisterActivity.this, "Please enter user phone number.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(userPasswordData.equals("")){
            Toast.makeText(RegisterActivity.this, "Please enter user password.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(userConfirmPasswordData.equals("")){
            Toast.makeText(RegisterActivity.this, "Please enter confirm password.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(!userPasswordData.equals( userConfirmPasswordData)){
            Toast.makeText(RegisterActivity.this, "Password did not match.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(userProfileImageData==null){
            Toast.makeText(RegisterActivity.this, "Please add user image.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void get_form_data() {

        userNameData = user_name_edittext_register.getText().toString();
        userEmailData = user_email_edittext_register.getText().toString();
        userPhoneNumberData = phone_number_Edittext_register.getText().toString();
        userPasswordData = password_Edittext_register.getText().toString();
        userConfirmPasswordData = confirm_password_Edittext_register.getText().toString();
        userCityData = city_Edittext_register.getText().toString();

    }

    private void intis() {
        visibale_c_password_image = findViewById(R.id.visibale_c_password_image);
        visibale_password_image = findViewById(R.id.visibale_password_image);
        hiden_c_password_image = findViewById(R.id.hiden_c_password_image);
        hiden_password_image = findViewById(R.id.hiden_password_image);
        back_to_login = findViewById(R.id.back_to_login);
        user_profile_image = findViewById(R.id.user_profile_image);
        register_button = findViewById(R.id.register_button);
        user_email_edittext_register = findViewById(R.id.user_email_edittext_register);
        user_name_edittext_register = findViewById(R.id.user_name_edittext_register);
        confirm_password_Edittext_register = findViewById(R.id.confirm_password_Edittext_register);
        password_Edittext_register = findViewById(R.id.password_Edittext_register);
        phone_number_Edittext_register = findViewById(R.id.phone_number_Edittext_register);
        city_Edittext_register = findViewById(R.id.city_Edittext_register);
        add_image_layout = findViewById(R.id.add_image_layout);
        otpText = findViewById(R.id.otpText);
    }
}