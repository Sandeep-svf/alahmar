package com.webnmobapps.alahmaar.communityPost;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.webnmobapps.alahmaar.R;
import com.webnmobapps.alahmaar.adapter.CommunityAdapter;
import com.webnmobapps.alahmaar.model.CommunityPostModel;
import com.webnmobapps.alahmaar.model.CommuntyPostResult;
import com.webnmobapps.alahmaar.retrofit.API_Client;
import com.webnmobapps.alahmaar.utility.RefreshInterface;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunityFragment extends Fragment implements RefreshInterface {

    RecyclerView rcv_community;
    List<CommuntyPostResult> communtyPostResultList= new ArrayList<>();
    private String accessToken, finalAccessToken,userID;
    RefreshInterface refreshInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_community, container, false);
        //geting userID data
        SharedPreferences getUserIdData = getActivity().getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
        accessToken = getUserIdData.getString("accessToken", "");
        userID = getUserIdData.getString("UserID", "");
        finalAccessToken = "Bearer "+accessToken;



        rcv_community = view.findViewById(R.id.rcv_community);



        //community_list_api();

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            refreshInterface = this;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshInterface.onRefresh();
    }

    private void community_list_api() {

            final ProgressDialog pd = new ProgressDialog(getActivity());
            pd.setCancelable(false);
            pd.setMessage("loading...");
            pd.show();

            Call<CommunityPostModel> call = API_Client.getClient().community_post_list(finalAccessToken);

            call.enqueue(new Callback<CommunityPostModel>() {
                @Override
                public void onResponse(Call<CommunityPostModel> call, Response<CommunityPostModel> response) {
                    pd.dismiss();
                    try {
                        //if api response is successful ,taking message and success
                        if (response.isSuccessful()) {

                            String success = String.valueOf(response.body().getSuccess());


                            if (success.equals("true") || success.equals("True")) {

                                communtyPostResultList = response.body().getData();
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
                                rcv_community.setLayoutManager(linearLayoutManager);
                                // Log.e("SAM","List size: "+formListModelList.size());
                                // Toast.makeText(getActivity(), "community list api calling sucessfuly....", Toast.LENGTH_SHORT).show();
                                CommunityAdapter communityAdapter = new CommunityAdapter(getActivity(),communtyPostResultList,userID,finalAccessToken,refreshInterface);
                                rcv_community.setAdapter(communityAdapter);

                            } else {
                                Toast.makeText(getActivity(), "Something went wrong." , Toast.LENGTH_LONG).show();
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
                public void onFailure(Call<CommunityPostModel> call, Throwable t) {
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

    @Override
    public void onRefresh() {
        community_list_api();
        Log.e("onrefreshinterface","on refresh interface call from fragment");
    }
}