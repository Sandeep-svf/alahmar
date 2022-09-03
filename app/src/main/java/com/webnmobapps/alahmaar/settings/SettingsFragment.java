package com.webnmobapps.alahmaar.settings;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.webnmobapps.alahmaar.R;
import com.webnmobapps.alahmaar.model.RegistrationModel;
import com.webnmobapps.alahmaar.retrofit.API_Client;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SettingsFragment extends Fragment {

    AppCompatEditText old_Password_edittext, new_password_edittext, confirm_new_password_edittext;
    AppCompatButton update_password_button_layout;
    private String oldPassword, newPassword, confirmNewPassword;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);

        inits(view);

        update_password_button_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               get_form_data();

               if(validaton()){

                   change_password_api();

               }
            }
        });


        return view;
    }

    private void change_password_api() {


            // show till load api data

            final ProgressDialog pd = new ProgressDialog(getActivity());
            pd.setCancelable(false);
            pd.setMessage("loading...");
            pd.show();

            Call<RegistrationModel> call = API_Client.getClient().reset_password("","");

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

                                Toast.makeText(getActivity(), message , Toast.LENGTH_LONG).show();
                                pd.dismiss();

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
                public void onFailure(Call<RegistrationModel> call, Throwable t) {
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

    private boolean validaton() {
        if(oldPassword.equals("")){
            Toast.makeText(getActivity(), "Please enter old password", Toast.LENGTH_SHORT).show();
        }else if(newPassword.equals("")){
            Toast.makeText(getActivity(), "Please enter new password", Toast.LENGTH_SHORT).show();
        }else if(confirmNewPassword.equals("")){
            Toast.makeText(getActivity(), "Please enter confirm password", Toast.LENGTH_SHORT).show();
        }else if(newPassword != confirmNewPassword){
            Toast.makeText(getActivity(), "Password did not match", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    private void get_form_data() {
        oldPassword = old_Password_edittext.getText().toString();
        newPassword = new_password_edittext.getText().toString();
        confirmNewPassword = confirm_new_password_edittext.getText().toString();
    }

    private void inits(View view) {
        update_password_button_layout = view.findViewById(R.id.update_password_button_layout);

    }
}