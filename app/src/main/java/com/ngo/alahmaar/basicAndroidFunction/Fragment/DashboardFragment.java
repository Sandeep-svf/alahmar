package com.ngo.alahmaar.basicAndroidFunction.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.ngo.alahmaar.MainActivity;
import com.ngo.alahmaar.R;
import com.ngo.alahmaar.basicAndroidFunction.donate.DonateFragment;
import com.ngo.alahmaar.communityPost.CommunityFragment;
import com.ngo.alahmaar.event.EventFragment;
import com.ngo.alahmaar.retrofit.API_Client;
import com.ngo.alahmaar.userForm.UserFormsFragment;

import de.hdodenhof.circleimageview.CircleImageView;


public class DashboardFragment extends Fragment {

    ConstraintLayout event_layout, community_layout, forms_layout, donate_layout, about_us_layout,about_team_layout;
    CircleImageView userDashboardImage;
    AppCompatTextView userDashboardName;
    private String userImage,userName;

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
        userImage = getUserIdData.getString("userImage", "");
        userName = getUserIdData.getString("userName", "");

        Glide.with(getActivity()).load(API_Client.BASE_IMAGE_URL+userImage)
                .placeholder(R.drawable.ic_launcher_background)
                        .into(userDashboardImage);

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

        return  view;
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