package com.webnmobapps.alahmaar.basicAndroidFunction.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.webnmobapps.alahmaar.R;
import com.webnmobapps.alahmaar.model.GetUserProfileModel;
import com.webnmobapps.alahmaar.model.GetUserProfileResult;
import com.webnmobapps.alahmaar.retrofit.API_Client;
import com.webnmobapps.alahmaar.utility.Permission;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {


    private static final int PICK_IMAGE_R = 178500;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int CAMERA_PIC_REQUEST = 1457;
    private Uri img;
    private File  profileImage;
    private ContentValues values5;
    private Uri imageUri5;
    List<GetUserProfileResult> getUserProfileResultList = new ArrayList<>();
    CircleImageView add_user_profile_layout,add_user_profile_image;
    private String userName, userEmail, userMobileNumber, userProfile, userId,accessToken, finalAccessToken,userID;
    private Bitmap thumbnail5;
    AppCompatEditText user_name, user_email, user_mobile_number, user_city_town;
    AppCompatButton update_button_layout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        intis(view);

        //geting userID data
        SharedPreferences getUserIdData = getActivity().getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
        accessToken = getUserIdData.getString("accessToken", "");
        userID = getUserIdData.getString("UserID", "");
        finalAccessToken = "Bearer "+accessToken;


        user_details_api();

        update_button_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                get_form_data();
                if(validation()){
                    user_update_api();
                }

            }
        });




        add_user_profile_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog addProfileUpdate = new Dialog(getActivity());
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

                        PackageManager packageManager = getActivity().getPackageManager();

                        boolean readExternal = Permission.checkPermissionReadExternal(getActivity());
                        boolean writeExternal = Permission.checkPermissionReadExternal2(getActivity());
                        boolean camera = Permission.checkPermissionCamera(getActivity());

                        if (camera && writeExternal && readExternal ) {
                            if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                                values5 = new ContentValues();
                                values5.put(MediaStore.Images.Media.TITLE, "New Picture");
                                values5.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                                imageUri5 = getActivity().getContentResolver().insert(
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values5);
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri5);
                                startActivityForResult(intent, CAMERA_PIC_REQUEST);
                                addProfileUpdate.dismiss();
                            }
                        } else {
                            Toast.makeText(getActivity(), "camera permission required", Toast.LENGTH_LONG).show();
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                            addProfileUpdate.dismiss();
                        }





                    }
                });
            }
        });

        return  view;
    }

    private boolean validation() {

        if(userName.equals("")){
            Toast.makeText(getActivity(), "Please enter user name", Toast.LENGTH_SHORT).show();
            return false;
        }else  if(userEmail.equals("")){
            Toast.makeText(getActivity(), "Please enter user email", Toast.LENGTH_SHORT).show();
            return false;
        }else  if(userMobileNumber.equals("")){
            Toast.makeText(getActivity(), "Please enter user mobile number", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void get_form_data() {
        userName = user_name.getText().toString();
        userEmail = user_email.getText().toString();
        userMobileNumber = user_mobile_number.getText().toString();
        userName = user_name.getText().toString();

    }

    public void user_update_api(){

            // show till load api data

            final ProgressDialog pd = new ProgressDialog(getActivity());
            pd.setCancelable(false);
            pd.setMessage("loading...");
            pd.show();

        RequestBody userNameRB = RequestBody.create(MediaType.parse("text/plain"), userName);
        RequestBody userEmailRB = RequestBody.create(MediaType.parse("text/plain"), userEmail);
        RequestBody userMobileNumberRB = RequestBody.create(MediaType.parse("text/plain"), userMobileNumber);
        RequestBody userIDRB = RequestBody.create(MediaType.parse("text/plain"), userID);
//        RequestBody IdImage = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(idImage));


        MultipartBody.Part profile;


        if (profileImage == null) {

            profile = MultipartBody.Part.createFormData("profile", "", RequestBody.create(MediaType.parse("image/*"), ""));
            Log.e("Photo", String.valueOf(profileImage));

        } else {


            profile = MultipartBody.Part.createFormData("profile", profileImage.getName(), RequestBody.create(MediaType.parse("image/*"), profileImage));
            Log.e("Photo", String.valueOf(profileImage));
        }

       // profile = MultipartBody.Part.createFormData("profile", profileImage.getName(), RequestBody.create(MediaType.parse("image/*"), profileImage));



        Call<GetUserProfileModel> call = API_Client.getClient().update_user_profile_details(finalAccessToken,
                userIDRB,
                userNameRB,
                userEmailRB ,
                userMobileNumberRB,
                profile);

            call.enqueue(new Callback<GetUserProfileModel>() {
                @Override
                public void onResponse(Call<GetUserProfileModel> call, Response<GetUserProfileModel> response) {
                    pd.dismiss();
                    try {
                        //if api response is successful ,taking message and success
                        if (response.isSuccessful()) {
                            String message = response.body().getMsg();
                            String success = String.valueOf(response.body().getSuccess());


                            if (success.equals("true") || success.equals("True")) {

                                Toast.makeText(getActivity(), message , Toast.LENGTH_LONG).show();
                                user_details_api();

                            } else {
                                Toast.makeText(getActivity(), message , Toast.LENGTH_LONG).show();
                                pd.dismiss();
                            }

                        } else {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Toast.makeText(getActivity(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
                                switch (response.code()) {
                                    case 400:
                                        Toast.makeText(getActivity(), "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 401:
                                        Toast.makeText(getActivity(), "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 404:
                                        Toast.makeText(getActivity(), "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 500:
                                        Toast.makeText(getActivity(), "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 503:
                                        Toast.makeText(getActivity(), "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 504:
                                        Toast.makeText(getActivity(), "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 511:
                                        Toast.makeText(getActivity(), "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(getActivity(), "unknown error", Toast.LENGTH_SHORT).show();
                                        break;
                                }

                            } catch (Exception e) {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (
                            Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<GetUserProfileModel> call, Throwable t) {
                    Log.e("conversion issue", t.getMessage());

                    if (t instanceof IOException) {
                        Toast.makeText(getActivity(), "This is an actual network failure :( inform the user and possibly retry)", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    } else {
                        Log.e("conversion issue", t.getMessage());
                        Toast.makeText(getActivity(), "Please Check your Internet Connection...." + t.getMessage(), Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            });

        }

    private void user_details_api() {


            // show till load api data

            final ProgressDialog pd = new ProgressDialog(getActivity());
            pd.setCancelable(false);
            pd.setMessage("loading...");
            pd.show();

            Call<GetUserProfileModel> call = API_Client.getClient().get_user_profile_details(finalAccessToken);

            call.enqueue(new Callback<GetUserProfileModel>() {
                @Override
                public void onResponse(Call<GetUserProfileModel> call, Response<GetUserProfileModel> response) {
                    pd.dismiss();
                    try {
                        //if api response is successful ,taking message and success
                        if (response.isSuccessful()) {
                            String message = response.body().getMsg();
                            String success = String.valueOf(response.body().getSuccess());


                            if (success.equals("true") || success.equals("True")) {


                                GetUserProfileModel getUserProfileModel = response.body();
                                GetUserProfileResult getUserProfileResult = getUserProfileModel.getData();

                                //get value from api
                                userName = getUserProfileResult.getUsername();
                                userEmail = getUserProfileResult.getEmail();
                                userMobileNumber = getUserProfileResult.getPhoneNumber();
                                userProfile = getUserProfileResult.getImage();
                                userId = String.valueOf(getUserProfileResult.getId());
                                
                                Log.e("api_details",userEmail+userName+userMobileNumber+userProfile);

                                // set value ....
                                user_name.setText(userName);
                                user_email.setText(userEmail);
                                user_mobile_number.setText(userMobileNumber);
                               // Glide.with(getActivity()).load(API_Client.BASE_IMAGE_URL+userProfile).placeholder(R.drawable.ic_launcher_background).into(add_user_profile_image);


                                try {
                                    Glide.with(getActivity()).load(API_Client.BASE_IMAGE_URL+userProfile).placeholder(R.drawable.ic_launcher_background).into(add_user_profile_image);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } else {
                                Toast.makeText(getActivity(), message , Toast.LENGTH_LONG).show();
                                pd.dismiss();
                            }

                        } else {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Toast.makeText(getActivity(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
                                switch (response.code()) {
                                    case 400:
                                        Toast.makeText(getActivity(), "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 401:
                                        Toast.makeText(getActivity(), "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 404:
                                        Toast.makeText(getActivity(), "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 500:
                                        Toast.makeText(getActivity(), "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 503:
                                        Toast.makeText(getActivity(), "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 504:
                                        Toast.makeText(getActivity(), "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 511:
                                        Toast.makeText(getActivity(), "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(getActivity(), "unknown error", Toast.LENGTH_SHORT).show();
                                        break;
                                }

                            } catch (Exception e) {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (
                            Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<GetUserProfileModel> call, Throwable t) {
                    Log.e("conversion issue", t.getMessage());

                    if (t instanceof IOException) {
                        Toast.makeText(getActivity(), "This is an actual network failure :( inform the user and possibly retry)", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    } else {
                        Log.e("conversion issue", t.getMessage());
                        Toast.makeText(getActivity(), "Please Check your Internet Connection...." + t.getMessage(), Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            });

        }

    private void gallery() {

        boolean readExternal = Permission.checkPermissionReadExternal(getActivity());
        boolean writeExternal = Permission.checkPermissionReadExternal2(getActivity());
        boolean camera = Permission.checkPermissionCamera(getActivity());
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
    private void intis(View view) {


        user_mobile_number = view.findViewById(R.id.user_mobile_number);
        user_email = view.findViewById(R.id.user_email);
        update_button_layout = view.findViewById(R.id.update_button_layout);
        user_name = view.findViewById(R.id.user_name);
        add_user_profile_layout = view.findViewById(R.id.add_user_profile_layout);
        add_user_profile_image = view.findViewById(R.id.add_user_profile_image);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            if (requestCode == PICK_IMAGE_R) {

                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                Cursor cursor = getActivity().managedQuery(selectedImageUri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                profileImage = new File(cursor.getString(column_index));
                Log.e("userImage1", String.valueOf(profileImage));
                String selectedImagePath1 = getPath(selectedImageUri);

                Log.v("Deatils_path","***"+selectedImagePath1);
                Glide.with(getActivity()).load(selectedImagePath1).placeholder(R.drawable.ic_launcher_background).into(add_user_profile_image);
                Log.e("userImage1", "BB");


            }else if(requestCode==CAMERA_PIC_REQUEST){

              try {
                    thumbnail5 = MediaStore.Images.Media.getBitmap(
                            getActivity().getContentResolver(), imageUri5);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                File file = new File(getRealPathFromURIs(imageUri5));

                Glide.with(getActivity())
                        .load(file)
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(add_user_profile_image);

            }
        }


    }

    public String getRealPathFromURIs(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
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
            cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
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


}