    package com.webnmobapps.alahmaar.event;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.webnmobapps.alahmaar.R;
import com.webnmobapps.alahmaar.adapter.EventAdapter;
import com.webnmobapps.alahmaar.model.EventListModel;
import com.webnmobapps.alahmaar.model.EventModel;
import com.webnmobapps.alahmaar.model.EventResult;
import com.webnmobapps.alahmaar.retrofit.API_Client;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

    public class EventFragment extends Fragment {

    RecyclerView rcv_event;
    List<EventModel> eventModelList = new ArrayList<>();
    ArrayList<EventResult> eventListModelList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_event, container, false);
        rcv_event = view.findViewById(R.id.rcv_event);
        add_data_in_model();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        rcv_event.setLayoutManager(linearLayoutManager);

        event_list_api();




        return view;
    }

        private void event_list_api() {


                final ProgressDialog pd = new ProgressDialog(getActivity());
                pd.setCancelable(false);
                pd.setMessage("loading...");
                pd.show();

                Call<EventListModel> call = API_Client.getClient().event_list();

                call.enqueue(new Callback<EventListModel>() {
                    @Override
                    public void onResponse(Call<EventListModel> call, Response<EventListModel> response) {
                        pd.dismiss();
                        try {
                            //if api response is successful ,taking message and success
                            if (response.isSuccessful()) {

                                String success = String.valueOf(response.body().getSuccess());
                                String message = String.valueOf(response.body().getMsg());


                                if (success.equals("true") || success.equals("True")) {

                                    eventListModelList = response.body().getData();


                                    Log.e("fsfsdf", String.valueOf(eventListModelList.size()));
                                    // Log.e("SAM","List size: "+formListModelList.size());
                                    // Toast.makeText(getActivity(), formListModelList.size(), Toast.LENGTH_SHORT).show();
                                    EventAdapter eventAdapter = new EventAdapter(getActivity(),eventListModelList);
                                    rcv_event.setAdapter(eventAdapter);

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
                    public void onFailure(Call<EventListModel> call, Throwable t) {
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

        private void add_data_in_model() {
            EventModel eventModel = new EventModel();
            eventModel.setEventname("Envirounmental Awareness");
            eventModel.setEventDesc("an awareness around the natural environment and the choices that either promote its well-being or cause it more harm.");
            eventModel.setEventCreatedAt("SAT, JUN 15 07:30 PM");
            eventModel.setEventProfile(R.drawable.group_image1);
            eventModelList.add(eventModel);

            eventModel = new EventModel();
            eventModel.setEventname("Educational Awareness");
            eventModel.setEventDesc("Education broadens the horizons of the mind and prepares young children to face challenges.");
            eventModel.setEventCreatedAt("SAT, JUN 15 07:30 PM");
            eventModel.setEventProfile(R.drawable.group_image2);
            eventModelList.add(eventModel);

            eventModel = new EventModel();
            eventModel.setEventname("Envirounmental Awareness");
            eventModel.setEventDesc("an awareness around the natural environment and the choices that either promote its well-being or cause it more harm.");
            eventModel.setEventCreatedAt("SAT, JUN 15 07:30 PM");
            eventModel.setEventProfile(R.drawable.group_image1);
            eventModelList.add(eventModel);

            eventModel = new EventModel();
            eventModel.setEventname("Educational Awareness");
            eventModel.setEventDesc("Education broadens the horizons of the mind and prepares young children to face challenges.");
            eventModel.setEventCreatedAt("SAT, JUN 15 07:30 PM");
            eventModel.setEventProfile(R.drawable.group_image2);
            eventModelList.add(eventModel);

            eventModel = new EventModel();
            eventModel.setEventname("Envirounmental Awareness");
            eventModel.setEventDesc("an awareness around the natural environment and the choices that either promote its well-being or cause it more harm.");
            eventModel.setEventCreatedAt("SAT, JUN 15 07:30 PM");
            eventModel.setEventProfile(R.drawable.group_image1);
            eventModelList.add(eventModel);

            eventModel = new EventModel();
            eventModel.setEventname("Educational Awareness");
            eventModel.setEventDesc("Education broadens the horizons of the mind and prepares young children to face challenges.");
            eventModel.setEventCreatedAt("SAT, JUN 15 07:30 PM");
            eventModel.setEventProfile(R.drawable.group_image2);
            eventModelList.add(eventModel);

            eventModel = new EventModel();
            eventModel.setEventname("Envirounmental Awareness");
            eventModel.setEventDesc("an awareness around the natural environment and the choices that either promote its well-being or cause it more harm.");
            eventModel.setEventCreatedAt("SAT, JUN 15 07:30 PM");
            eventModel.setEventProfile(R.drawable.group_image1);
            eventModelList.add(eventModel);

            eventModel = new EventModel();
            eventModel.setEventname("Educational Awareness");
            eventModel.setEventDesc("Education broadens the horizons of the mind and prepares young children to face challenges.");
            eventModel.setEventCreatedAt("SAT, JUN 15 07:30 PM");
            eventModel.setEventProfile(R.drawable.group_image2);
            eventModelList.add(eventModel);

            eventModel = new EventModel();
            eventModel.setEventname("Envirounmental Awareness");
            eventModel.setEventDesc("an awareness around the natural environment and the choices that either promote its well-being or cause it more harm.");
            eventModel.setEventCreatedAt("SAT, JUN 15 07:30 PM");
            eventModel.setEventProfile(R.drawable.group_image1);
            eventModelList.add(eventModel);

            eventModel = new EventModel();
            eventModel.setEventname("Educational Awareness");
            eventModel.setEventDesc("Education broadens the horizons of the mind and prepares young children to face challenges.");
            eventModel.setEventCreatedAt("SAT, JUN 15 07:30 PM");
            eventModel.setEventProfile(R.drawable.group_image2);
            eventModelList.add(eventModel);

            eventModel = new EventModel();
            eventModel.setEventname("Envirounmental Awareness");
            eventModel.setEventDesc("an awareness around the natural environment and the choices that either promote its well-being or cause it more harm.");
            eventModel.setEventCreatedAt("SAT, JUN 15 07:30 PM");
            eventModel.setEventProfile(R.drawable.group_image1);
            eventModelList.add(eventModel);

            eventModel = new EventModel();
            eventModel.setEventname("Educational Awareness");
            eventModel.setEventDesc("Education broadens the horizons of the mind and prepares young children to face challenges.");
            eventModel.setEventCreatedAt("SAT, JUN 15 07:30 PM");
            eventModel.setEventProfile(R.drawable.group_image2);
            eventModelList.add(eventModel);

            eventModel = new EventModel();
            eventModel.setEventname("Envirounmental Awareness");
            eventModel.setEventDesc("an awareness around the natural environment and the choices that either promote its well-being or cause it more harm.");
            eventModel.setEventCreatedAt("SAT, JUN 15 07:30 PM");
            eventModel.setEventProfile(R.drawable.group_image1);
            eventModelList.add(eventModel);

            eventModel = new EventModel();
            eventModel.setEventname("Educational Awareness");
            eventModel.setEventDesc("Education broadens the horizons of the mind and prepares young children to face challenges.");
            eventModel.setEventCreatedAt("SAT, JUN 15 07:30 PM");
            eventModel.setEventProfile(R.drawable.group_image2);
            eventModelList.add(eventModel);

            eventModel = new EventModel();
            eventModel.setEventname("Envirounmental Awareness");
            eventModel.setEventDesc("an awareness around the natural environment and the choices that either promote its well-being or cause it more harm.");
            eventModel.setEventCreatedAt("12 July 2022");
            eventModel.setEventProfile(R.drawable.group_image1);
            eventModelList.add(eventModel);

            eventModel = new EventModel();
            eventModel.setEventname("Educational Awareness");
            eventModel.setEventDesc("Education broadens the horizons of the mind and prepares young children to face challenges.");
            eventModel.setEventCreatedAt("SAT, JUN 15 07:30 PM");
            eventModel.setEventProfile(R.drawable.group_image2);
            eventModelList.add(eventModel);

            eventModel = new EventModel();
            eventModel.setEventname("Envirounmental Awareness");
            eventModel.setEventDesc("an awareness around the natural environment and the choices that either promote its well-being or cause it more harm.");
            eventModel.setEventCreatedAt("SAT, JUN 15 07:30 PM");
            eventModel.setEventProfile(R.drawable.group_image1);
            eventModelList.add(eventModel);

            eventModel = new EventModel();
            eventModel.setEventname("Educational Awareness");
            eventModel.setEventDesc("Education broadens the horizons of the mind and prepares young children to face challenges.");
            eventModel.setEventCreatedAt("13 July 2022");
            eventModel.setEventProfile(R.drawable.group_image2);
            eventModelList.add(eventModel);

            eventModel = new EventModel();
            eventModel.setEventname("Envirounmental Awareness");
            eventModel.setEventDesc("an awareness around the natural environment and the choices that either promote its well-being or cause it more harm.");
            eventModel.setEventCreatedAt("12 July 2022");
            eventModel.setEventProfile(R.drawable.group_image1);
            eventModelList.add(eventModel);

            eventModel = new EventModel();
            eventModel.setEventname("Educational Awareness");
            eventModel.setEventDesc("Education broadens the horizons of the mind and prepares young children to face challenges.");
            eventModel.setEventCreatedAt("SAT, JUN 15 07:30 PM");
            eventModel.setEventProfile(R.drawable.group_image2);
            eventModelList.add(eventModel);





        }
    }