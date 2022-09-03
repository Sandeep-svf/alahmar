package com.webnmobapps.alahmaar.basicAndroidFunction.Fragment;

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


public class ContactUsFragment extends Fragment {
    AppCompatEditText user_name_CU,user_email_CF,user_messages_CF;
    private String personName, personEmail, aboutPerson;
    AppCompatButton submit_contact_us_form_button;
    private String regexEmail = "(?:[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?\\.)+[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[A-Za-z0-9]:(?:|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.activity_contact_us_form, container, false);
        intis(view);

        submit_contact_us_form_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_form_data();
                if(validation()){

                    contact_us_api();

                }

            }
        });


        return view;
    }

    private void contact_us_api() {

            // show till load api data

            final ProgressDialog pd = new ProgressDialog(getActivity());
            pd.setCancelable(false);
            pd.setMessage("loading...");
            pd.show();

            Call<RegistrationModel> call = API_Client.getClient().contact_us_form(personName,personEmail,aboutPerson,"123456789");

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

                                user_name_CU.setText("");
                                user_email_CF.setText("");
                                user_messages_CF.setText("");


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

    private void intis(View view) {

        user_name_CU = view.findViewById(R.id.user_name_CU);
        user_email_CF = view.findViewById(R.id.user_email_CF);
        user_messages_CF = view.findViewById(R.id.user_messages_CF);
        submit_contact_us_form_button = view.findViewById(R.id.submit_contact_us_form_button);
    }

    private void get_form_data() {
        personName = user_name_CU.getText().toString();
        personEmail = user_email_CF.getText().toString();
        aboutPerson = user_messages_CF.getText().toString();

    }

    private boolean validation() {

        if(personName.equals("")){
            Toast.makeText(getActivity(), "Please enter person name.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(personEmail.equals("")){
            Toast.makeText(getActivity(), "Please enter person email.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(!personEmail.matches(regexEmail)){
            Toast.makeText(getActivity(), "Please enter valid email.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(aboutPerson.equals("")){
            Toast.makeText(getActivity(), "Please enter message.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}