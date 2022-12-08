package com.ngo.alahmaar;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.ngo.alahmaar.basicAndroidFunction.Fragment.AboutFragment;
import com.ngo.alahmaar.basicAndroidFunction.Fragment.ContactUsFragment;
import com.ngo.alahmaar.basicAndroidFunction.Fragment.DashboardFragment;
import com.ngo.alahmaar.basicAndroidFunction.Fragment.ProfileFragment;
import com.ngo.alahmaar.basicAndroidFunction.LoginActivity;
import com.ngo.alahmaar.basicAndroidFunction.NotificationFragment;
import com.ngo.alahmaar.basicAndroidFunction.donate.DonateFragment;
import com.ngo.alahmaar.communityPost.CommunityFragment;
import com.ngo.alahmaar.event.EventFragment;
import com.ngo.alahmaar.model.GetUserProfileModel;
import com.ngo.alahmaar.model.GetUserProfileResult;
import com.ngo.alahmaar.model.RegistrationModel;
import com.ngo.alahmaar.model.TotalNumberNotification;
import com.ngo.alahmaar.retrofit.API_Client;
import com.ngo.alahmaar.settings.SettingsFragment;
import com.ngo.alahmaar.transactionHistory.TransactionHistoryFragment;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends SlidingFragmentActivity implements View.OnClickListener  {

    AppCompatImageView btnMenu,img_notification;
    public static AppCompatTextView title;
    AppCompatTextView text_notification;
    RelativeLayout dashboard_layout,contact_us_layout,check_price,community_layout,donate_layout,
            about_layout,transection_layout,setting_layout,logout_layout;
    ConstraintLayout container,edit_profile_layout;
    RelativeLayout relative_notification;
    private String accessToken,finalAccessToken,refreshToken,userImage,userName,userEmail;
    ShapeableImageView userImageLayout;
    AppCompatTextView user_name_layout, user_email_layout;
    private String userProfile;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setBehindContentView(R.layout.menu);

        inits();

        logout_layout.setOnClickListener(this::onClick);
        btnMenu.setOnClickListener(this::onClick);
        edit_profile_layout.setOnClickListener(this::onClick);
        contact_us_layout.setOnClickListener(this::onClick);
        dashboard_layout.setOnClickListener(this::onClick);
        check_price.setOnClickListener(this::onClick);
        community_layout.setOnClickListener(this::onClick);
        donate_layout.setOnClickListener(this::onClick);
        about_layout.setOnClickListener(this::onClick);
        relative_notification.setOnClickListener(this::onClick);
        setting_layout.setOnClickListener(this::onClick);
        transection_layout.setOnClickListener(this::onClick);

        my_sliding_window();


        //geting userID data
        SharedPreferences getUserIdData = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
        accessToken = getUserIdData.getString("accessToken", "");
        refreshToken = getUserIdData.getString("refreshToken", "");
        userImage = getUserIdData.getString("userImage", "");
        userName = getUserIdData.getString("userName", "");
        userEmail = getUserIdData.getString("userEmail", "");
        finalAccessToken = "Bearer "+accessToken;


        Log.e("Dfsdfasd",userEmail+userName+"ok");



        user_name_layout.setText(userName);
        user_email_layout.setText(userEmail);


        List<Character> list = new ArrayList<Character>();
        Set<Character> unique = new HashSet<Character>();
        for(char c : "abc".toCharArray()) {
            list.add(c);
            unique.add(c);
        }
        Log.e("fsdfs",list.get(0).toString());

        total_number_notification();
        user_details_api();
        
        
        // 
        

    }
    public  void user_details_api() {


     /*   // show till load api data

        final ProgressDialog pd = new ProgressDialog(MainActivity.this);
        pd.setCancelable(false);
        pd.setMessage("loading...");
        pd.show();*/

        Call<GetUserProfileModel> call = API_Client.getClient().get_user_profile_details(finalAccessToken);

        call.enqueue(new Callback<GetUserProfileModel>() {
            @Override
            public void onResponse(Call<GetUserProfileModel> call, Response<GetUserProfileModel> response) {
              //  pd.dismiss();
                try {
                    //if api response is successful ,taking message and success
                    if (response.isSuccessful()) {
                        String message = response.body().getMsg();
                        String success = String.valueOf(response.body().getSuccess());


                        if (success.equals("true") || success.equals("True")) {


                            GetUserProfileModel getUserProfileModel = response.body();
                            GetUserProfileResult getUserProfileResult = getUserProfileModel.getData();

                            //get value from api
                           /* userName = getUserProfileResult.getUsername();
                            userEmail = getUserProfileResult.getEmail();
                            userMobileNumber = getUserProfileResult.getPhoneNumber();*/
                              userProfile = getUserProfileResult.getImage();
                              user_name_layout.setText(getUserProfileResult.getUsername());
                            user_email_layout.setText(getUserProfileResult.getEmail());

                            SharedPreferences getUserIdData = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = getUserIdData.edit();
                            editor.putString("userName", String.valueOf(getUserProfileResult.getUsername()));

                            Glide.with(MainActivity.this).load(API_Client.BASE_IMAGE_URL+userProfile)
                                    .placeholder(R.drawable.ic_launcher_background)
                                    .into(userImageLayout);




                          //  Log.e("api_details",userEmail+userName+userMobileNumber+userProfile);

                       /*     // set value ....
                            user_name.setText(userName);
                            user_email.setText(userEmail);
                            user_mobile_number.setText(userMobileNumber);
                            // Glide.with(getActivity()).load(API_Client.BASE_IMAGE_URL+userProfile).placeholder(R.drawable.ic_launcher_background).into(add_user_profile_image);



                            Glide.with(getActivity()).load(API_Client.BASE_IMAGE_URL+getUserProfileResult.getImage())
                                    .placeholder(R.drawable.ic_launcher_background)
                                    .into(add_user_profile_image);

*/

                        } else {
                            Toast.makeText(MainActivity.this, message , Toast.LENGTH_LONG).show();
                         //   pd.dismiss();
                        }

                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Toast.makeText(MainActivity.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                            switch (response.code()) {
                                case 400:
                                    Toast.makeText(MainActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    Toast.makeText(MainActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    Toast.makeText(MainActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(MainActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 503:
                                    Toast.makeText(MainActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 504:
                                    Toast.makeText(MainActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                    break;
                                case 511:
                                    Toast.makeText(MainActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(MainActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                                    break;
                            }

                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
                    Toast.makeText(MainActivity.this, "This is an actual network failure :( inform the user and possibly retry)", Toast.LENGTH_SHORT).show();
                   // pd.dismiss();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(MainActivity.this, "Please Check your Internet Connection...." + t.getMessage(), Toast.LENGTH_SHORT).show();
                  //  pd.dismiss();
                }
            }
        });

    }
    private void total_number_notification() {
       


            // comment list api...........

            final ProgressDialog pd = new ProgressDialog(MainActivity.this);
            pd.setCancelable(false);
            pd.setMessage("loading...");
            pd.show();

            Call<TotalNumberNotification> call = API_Client.getClient().TOTAL_NUMBER_NOTIFICATION_CALL(finalAccessToken);

            call.enqueue(new Callback<TotalNumberNotification>() {
                @Override
                public void onResponse(Call<TotalNumberNotification> call, Response<TotalNumberNotification> response) {
                    pd.dismiss();
                    try {
                        //if api response is successful ,taking message and success
                        if (response.isSuccessful()) {

                            String success = String.valueOf(response.body().getSuccess());
                            String message = String.valueOf(response.body().getMsg());


                            if (success.equals("true") || success.equals("True")) {
                                //Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                                TotalNumberNotification totalNumberNotification = response.body();

                                text_notification.setText( String.valueOf(totalNumberNotification.getTotalNotification()));

                                Log.e("shkjdhgf", String.valueOf(totalNumberNotification.getTotalNotification()));
                               

                            } else {
                                Toast.makeText(MainActivity.this, "Something went wrong." , Toast.LENGTH_LONG).show();
                                pd.dismiss();
                            }

                        } else {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Toast.makeText(MainActivity.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                                switch (response.code()) {
                                    case 400:
                                        Toast.makeText(MainActivity.this, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 401:
                                        Toast.makeText(MainActivity.this, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 404:
                                        Toast.makeText(MainActivity.this, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 500:
                                        Toast.makeText(MainActivity.this, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 503:
                                        Toast.makeText(MainActivity.this, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 504:
                                        Toast.makeText(MainActivity.this, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 511:
                                        Toast.makeText(MainActivity.this, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(MainActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                                        break;
                                }

                            } catch (Exception e) {
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (
                            Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<TotalNumberNotification> call, Throwable t) {
                    Log.e("conversion issue", t.getMessage());

                    if (t instanceof IOException) {
                        Toast.makeText(MainActivity.this, "This is an actual network failure :( inform the user and possibly retry)", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    } else {
                        Log.e("conversion issue", t.getMessage());
                        Toast.makeText(MainActivity.this, "Please Check your Internet Connection...." + t.getMessage(), Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            });

        }

    private void my_sliding_window() {
        try {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

            final Display display = getWindowManager().getDefaultDisplay();
            int screenWidth = display.getWidth();
            //int screenWidth = getScreenWidthInPixel();
            final int slidingmenuWidth = (int) (screenWidth - (screenWidth / 3.7) + 20);
            final int offset = Math.max(0, screenWidth - slidingmenuWidth);
            getSlidingMenu().setBehindOffset(offset);
            getSlidingMenu().toggle();
            getSlidingMenu().isVerticalFadingEdgeEnabled();
            getSlidingMenu().isHorizontalFadingEdgeEnabled();
            getSlidingMenu().setMode(SlidingMenu.LEFT);
            getSlidingMenu().setFadeEnabled(true);
            getSlidingMenu().setFadeDegree(0.5f);
            getSlidingMenu().setFadingEdgeLength(10);
            getSlidingMenu().setEnabled(false);

            MainActivity mainActivity = MainActivity.this;

        } catch (Exception e) {
            e.printStackTrace();
        }
        DashboardFragment dashboardFragment = new DashboardFragment();
        FragmentManager fragmentManager_two = getSupportFragmentManager();
        ((ConstraintLayout) findViewById(R.id.container)).removeAllViews();
        FragmentTransaction fragmentTransaction2 = fragmentManager_two.beginTransaction();
        fragmentTransaction2.setCustomAnimations(R.anim.slid_in_right, R.anim.slide_in_left);
        fragmentTransaction2.add(R.id.container, dashboardFragment);
        fragmentTransaction2.commit();
        getSlidingMenu().toggle();
    }

    private void inits() {
        btnMenu= (AppCompatImageView) findViewById(R.id.btnMenu);
        img_notification= (AppCompatImageView) findViewById(R.id.img_notification);
        title= (AppCompatTextView) findViewById(R.id.title);
        text_notification= (AppCompatTextView) findViewById(R.id.text_notification);
        dashboard_layout= (RelativeLayout) findViewById(R.id.dashboard_layout);
        contact_us_layout= (RelativeLayout) findViewById(R.id.contact_us_layout);
        community_layout= (RelativeLayout) findViewById(R.id.community_layout);
        donate_layout= (RelativeLayout) findViewById(R.id.donate_layout);
        about_layout= (RelativeLayout) findViewById(R.id.about_layout);
        dashboard_layout= (RelativeLayout) findViewById(R.id.dashboard_layout);
        relative_notification= (RelativeLayout) findViewById(R.id.relative_notification);
        setting_layout= (RelativeLayout) findViewById(R.id.setting_layout);
        transection_layout= (RelativeLayout) findViewById(R.id.transection_layout);
        edit_profile_layout= (ConstraintLayout) findViewById(R.id.edit_profile_layout);
        logout_layout= (RelativeLayout) findViewById(R.id.logout_layout);
        user_name_layout= (AppCompatTextView) findViewById(R.id.user_name_layout);
        user_email_layout= (AppCompatTextView) findViewById(R.id.user_email_layout);
        userImageLayout= (ShapeableImageView) findViewById(R.id.userImageLayout);
        check_price= (RelativeLayout) findViewById(R.id.check_price);

    }

    public static void load_home_fragment(Fragment homeFragment, FragmentManager fragmentManager)
    {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, homeFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();


    }
    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btnMenu:
                showMenu();
                user_details_api();

                break;


            case R.id.relative_notification:
                text_notification.setText("0");
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                ((ConstraintLayout) findViewById(R.id.container)).removeAllViews();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new NotificationFragment()).commit();
                break;

            case R.id.contact_us_layout:
                title.setText("Contact Us");
            ContactUsFragment contactUsFragment = new ContactUsFragment();
            FragmentManager fragmentManager_one = getSupportFragmentManager();
            ((ConstraintLayout) findViewById(R.id.container)).removeAllViews();
            FragmentTransaction fragmentTransaction1 = fragmentManager_one.beginTransaction();
            fragmentTransaction1.setCustomAnimations(R.anim.slid_in_right, R.anim.slide_in_left);
            fragmentTransaction1.add(R.id.container, contactUsFragment);
            fragmentTransaction1.commit();
            getSlidingMenu().toggle();
            break;

            case R.id.dashboard_layout:
                title.setText("Home");
                DashboardFragment dashboardFragment = new DashboardFragment();
                FragmentManager fragmentManager_two = getSupportFragmentManager();
                ((ConstraintLayout) findViewById(R.id.container)).removeAllViews();
                FragmentTransaction fragmentTransaction2 = fragmentManager_two.beginTransaction();
                fragmentTransaction2.setCustomAnimations(R.anim.slid_in_right, R.anim.slide_in_left);
                fragmentTransaction2.add(R.id.container, dashboardFragment);
                fragmentTransaction2.commit();
                getSlidingMenu().toggle();
                break;

            case R.id.event_layout:
                title.setText("Event");
                EventFragment eventFragment = new EventFragment();
                FragmentManager fragmentManager_three = getSupportFragmentManager();
                ((ConstraintLayout) findViewById(R.id.container)).removeAllViews();
                FragmentTransaction fragmentTransaction3 = fragmentManager_three.beginTransaction();
                fragmentTransaction3.setCustomAnimations(R.anim.slid_in_right, R.anim.slide_in_left);
                fragmentTransaction3.add(R.id.container, eventFragment);
                fragmentTransaction3.commit();
                getSlidingMenu().toggle();
                break;
            case R.id.community_layout:
                title.setText("Community");
                CommunityFragment communityFragment = new CommunityFragment();
                FragmentManager fragmentManager_four = getSupportFragmentManager();
                ((ConstraintLayout) findViewById(R.id.container)).removeAllViews();
                FragmentTransaction fragmentTransaction4 = fragmentManager_four.beginTransaction();
                fragmentTransaction4.setCustomAnimations(R.anim.slid_in_right, R.anim.slide_in_left);
                fragmentTransaction4.add(R.id.container, communityFragment);
                fragmentTransaction4.commit();
                getSlidingMenu().toggle();
                break;
            case R.id.donate_layout:
                title.setText("Donate");
                DonateFragment donateFragment = new DonateFragment();
                FragmentManager fragmentManager_five = getSupportFragmentManager();
                ((ConstraintLayout) findViewById(R.id.container)).removeAllViews();
                FragmentTransaction fragmentTransaction5 = fragmentManager_five.beginTransaction();
                fragmentTransaction5.setCustomAnimations(R.anim.slid_in_right, R.anim.slide_in_left);
                fragmentTransaction5.add(R.id.container, donateFragment);
                fragmentTransaction5.commit();
                getSlidingMenu().toggle();
                break;
            case R.id.about_layout:
                title.setText("About Us");
                AboutFragment aboutFragment = new AboutFragment();
                FragmentManager fragmentManager_six = getSupportFragmentManager();
                ((ConstraintLayout) findViewById(R.id.container)).removeAllViews();
                FragmentTransaction fragmentTransaction6 = fragmentManager_six.beginTransaction();
                fragmentTransaction6.setCustomAnimations(R.anim.slid_in_right, R.anim.slide_in_left);
                fragmentTransaction6.add(R.id.container, aboutFragment);
                fragmentTransaction6.commit();
                getSlidingMenu().toggle();
                break;
            case R.id.setting_layout:
                title.setText("Chane Password");
                SettingsFragment settingsFragment = new SettingsFragment();
                FragmentManager fragmentManager_seven = getSupportFragmentManager();
                ((ConstraintLayout) findViewById(R.id.container)).removeAllViews();
                FragmentTransaction fragmentTransaction7 = fragmentManager_seven.beginTransaction();
                fragmentTransaction7.setCustomAnimations(R.anim.slid_in_right, R.anim.slide_in_left);
                fragmentTransaction7.add(R.id.container, settingsFragment);
                fragmentTransaction7.commit();
                getSlidingMenu().toggle();
                break;

            case R.id.transection_layout:
                title.setText("Transaction History");
                TransactionHistoryFragment transactionHistoryFragment = new TransactionHistoryFragment();
                FragmentManager fragmentManager_eight = getSupportFragmentManager();
                ((ConstraintLayout) findViewById(R.id.container)).removeAllViews();
                FragmentTransaction fragmentTransaction8 = fragmentManager_eight.beginTransaction();
                fragmentTransaction8.setCustomAnimations(R.anim.slid_in_right, R.anim.slide_in_left);
                fragmentTransaction8.add(R.id.container, transactionHistoryFragment);
                fragmentTransaction8.commit();
                getSlidingMenu().toggle();
                break;

            case R.id.edit_profile_layout:
                title.setText("Profile");
                ProfileFragment profileFragment1 = new ProfileFragment();
                FragmentManager fragmentManager_nine = getSupportFragmentManager();
                ((ConstraintLayout) findViewById(R.id.container)).removeAllViews();
                FragmentTransaction fragmentTransaction9 = fragmentManager_nine.beginTransaction();
                fragmentTransaction9.setCustomAnimations(R.anim.slid_in_right, R.anim.slide_in_left);
                fragmentTransaction9.add(R.id.container, profileFragment1);
                fragmentTransaction9.commit();
                getSlidingMenu().toggle();
                break;
                
            case R.id.logout_layout:
                //logout_api();

                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.logout_dialog);
                LinearLayout noDialogLogout = dialog.findViewById(R.id.noDialogLogout);
                LinearLayout yesDialogLogout = dialog.findViewById(R.id.yesDialogLogout);


                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


                yesDialogLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences getUserIdData = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = getUserIdData.edit();
                        editor.putString("UserID", "");
                        editor.putString("accessToken", "");
                        editor.putString("refreshToken", "");
                        editor.apply();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("finish", true);
                        startActivity(intent);

                        Toast.makeText(MainActivity.this, "User logout successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });


                noDialogLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });



                break;
        }
    }

    private void logout_api() {

            // show till load api data

            final ProgressDialog pd = new ProgressDialog(MainActivity.this);
            pd.setCancelable(false);
            pd.setMessage("loading...");
            pd.show();

            Call<RegistrationModel> call = API_Client.getClient().logout(finalAccessToken,refreshToken);

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

                                Toast.makeText(getApplicationContext(), message , Toast.LENGTH_LONG).show();
                                pd.dismiss();


                                SharedPreferences getUserIdData = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = getUserIdData.edit();
                                editor.putString("UserID", "");
                                editor.putString("accessToken", "");
                                editor.putString("refreshToken", "");
                                editor.apply();
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtra("finish", true);
                                startActivity(intent);


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
}