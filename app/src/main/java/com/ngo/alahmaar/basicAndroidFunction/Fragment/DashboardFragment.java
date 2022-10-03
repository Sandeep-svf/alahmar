package com.ngo.alahmaar.basicAndroidFunction.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ngo.alahmaar.MainActivity;
import com.ngo.alahmaar.R;
import com.ngo.alahmaar.basicAndroidFunction.donate.DonateFragment;
import com.ngo.alahmaar.communityPost.CommunityFragment;
import com.ngo.alahmaar.event.EventFragment;
import com.ngo.alahmaar.model.GetUserProfileModel;
import com.ngo.alahmaar.model.GetUserProfileResult;
import com.ngo.alahmaar.retrofit.API_Client;
import com.ngo.alahmaar.userForm.UserFormsFragment;

import org.json.JSONObject;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DashboardFragment extends Fragment {

    ConstraintLayout event_layout, community_layout, forms_layout, donate_layout, about_us_layout,about_team_layout;
    CircleImageView userDashboardImage;
    AppCompatTextView userDashboardName;
    private String userImage,userName;
    private String accessToken,finalAccessToken;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_dashboard, container, false);

        inits(view);


        //geting userID data
        SharedPreferences getUserIdData = getActivity().getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
       // accessToken = getUserIdData.getString("accessToken", "");
       // refreshToken = getUserIdData.getString("refreshToken", "");
        accessToken = getUserIdData.getString("accessToken", "");
        userName = getUserIdData.getString("userName", "");
        userName = getUserIdData.getString("userName", "");
        finalAccessToken = "Bearer "+accessToken;

        //geting userID data
        SharedPreferences getUserIdData2 = getActivity().getSharedPreferences("AUTHENTICATION_FILE_NAME2", Context.MODE_PRIVATE);

        userImage = getUserIdData.getString("userImageRefresh", "");

        Glide.with(getActivity()).load(API_Client.BASE_IMAGE_URL+userImage)
                .placeholder(R.drawable.ic_launcher_background)
                        .into(userDashboardImage);

        Log.e("checkImage",API_Client.BASE_IMAGE_URL+userImage+"/");
        userDashboardName.setText(userName);



        // layout
        event_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventFragment eventFragment = new EventFragment();
                FragmentManager fragmentManager = getFragmentManager();
                MainActivity.load_home_fragment(eventFragment, fragmentManager);
            }
        });
        community_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommunityFragment eventFragment = new CommunityFragment();
                FragmentManager fragmentManager = getFragmentManager();
                MainActivity.load_home_fragment(eventFragment, fragmentManager);
            }
        });
        forms_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserFormsFragment eventFragment = new UserFormsFragment();
                FragmentManager fragmentManager = getFragmentManager();
                MainActivity.load_home_fragment(eventFragment, fragmentManager);
            }
        });
        donate_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DonateFragment eventFragment = new DonateFragment();
                FragmentManager fragmentManager = getFragmentManager();
                MainActivity.load_home_fragment(eventFragment, fragmentManager);
            }
        });
        about_us_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutFragment eventFragment = new AboutFragment();
                FragmentManager fragmentManager = getFragmentManager();
                MainActivity.load_home_fragment(eventFragment, fragmentManager);
            }
        });


        about_team_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutTeamFragment aboutTeamFragment = new AboutTeamFragment();
                FragmentManager fragmentManager = getFragmentManager();
                MainActivity.load_home_fragment(aboutTeamFragment,fragmentManager);
            }
        });


        user_details_api();

        return  view;
    }
    public  void user_details_api() {


     /*   // show till load api data

        final ProgressDialog pd = new ProgressDialog(getActivity());
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

                            String userProfile = getUserProfileResult.getImage();

                            Glide.with(getActivity()).load(API_Client.BASE_IMAGE_URL+userProfile)
                                    .placeholder(R.drawable.ic_launcher_background)
                                    .into(userDashboardImage);

                        


                        } else {
                            Toast.makeText(getActivity(), message , Toast.LENGTH_LONG).show();
                            //   pd.dismiss();
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
                    // pd.dismiss();
                } else {
                    Log.e("conversion issue", t.getMessage());
                    Toast.makeText(getActivity(), "Please Check your Internet Connection...." + t.getMessage(), Toast.LENGTH_SHORT).show();
                    //  pd.dismiss();
                }
            }
        });

    }
    private void inits(View view) {

        donate_layout = view.findViewById(R.id.donate_layout);
        forms_layout = view.findViewById(R.id.forms_layout);
        community_layout = view.findViewById(R.id.community_layout);
        event_layout = view.findViewById(R.id.event_layout);
        about_us_layout = view.findViewById(R.id.about_us_layout);
        about_team_layout = view.findViewById(R.id.about_team_layout);
        userDashboardImage = view.findViewById(R.id.userDashboardImage);
        userDashboardName = view.findViewById(R.id.userDashboardName);

    }
}