package com.ngo.alahmaar.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.ngo.alahmaar.MainActivity;
import com.ngo.alahmaar.R;
import com.ngo.alahmaar.model.CommonSuccessMsgModel;
import com.ngo.alahmaar.model.NotificationListResult;
import com.ngo.alahmaar.retrofit.API_Client;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationViewHolder> {

    private Context context;
    List<NotificationListResult> notificationListResultList = new ArrayList<>();
    private String finalAccessToken;

    public NotificationAdapter(Context context, List<NotificationListResult> notificationListResultList, String finalAccessToken) {
        this.context = context;
        this.notificationListResultList = notificationListResultList;
        this.finalAccessToken = finalAccessToken;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_design, parent, false);
        NotificationViewHolder myViewHolder = new NotificationViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.notification_heading.setText(notificationListResultList.get(position).getNotificationHeadding());
        holder.notificatin_subheading.setText(notificationListResultList.get(position).getNotificationMessage());
        holder.notification_date.setText(notificationListResultList.get(position).getRecievedDate());

        holder.notification_delete_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // open popup
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.delete_dialog);
                LinearLayout noDialogLogout = dialog.findViewById(R.id.noDialogLogout);
                LinearLayout yesDialogLogout = dialog.findViewById(R.id.yesDialogLogout);


                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


                yesDialogLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //code
                        String notificationId = String.valueOf(notificationListResultList.get(position).getId());
                        notificaiton_single_delete_api(notificationId, position);
                        dialog.dismiss();
                    }
                });

                noDialogLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


            }
        });
    }

    private void notificaiton_single_delete_api(String position, int currentPosition) {

          

            final ProgressDialog pd = new ProgressDialog(context);
            pd.setCancelable(false);
            pd.setMessage("loading...");
            pd.show();

            Call<CommonSuccessMsgModel> call = API_Client.getClient().COMMON_SUCCESS_MSG_MODEL_CALL_NOTIFICAITON(finalAccessToken, String.valueOf(position));

            call.enqueue(new Callback<CommonSuccessMsgModel>() {
                @Override
                public void onResponse(Call<CommonSuccessMsgModel> call, Response<CommonSuccessMsgModel> response) {
                    pd.dismiss();
                    try {
                        //if api response is successful ,taking message and success
                        if (response.isSuccessful()) {

                            String success = String.valueOf(response.body().getSuccess());
                            String message = String.valueOf(response.body().getMsg());


                            if (success.equals("true") || success.equals("True")) {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                                try {
                                    notificationListResultList.remove(currentPosition);
                                    notifyDataSetChanged();
                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                }


                            } else {
                                Toast.makeText(context, "Something went wrong." , Toast.LENGTH_LONG).show();
                                pd.dismiss();
                            }

                        } else {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Toast.makeText(context, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                                switch (response.code()) {
                                    case 400:
                                        Toast.makeText(context, "The server did not understand the request.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 401:
                                        Toast.makeText(context, "Unauthorized The requested page needs a username and a password.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 404:
                                        Toast.makeText(context, "The server can not find the requested page.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 500:
                                        Toast.makeText(context, "Internal Server Error..", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 503:
                                        Toast.makeText(context, "Service Unavailable..", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 504:
                                        Toast.makeText(context, "Gateway Timeout..", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 511:
                                        Toast.makeText(context, "Network Authentication Required ..", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(context, "unknown error", Toast.LENGTH_SHORT).show();
                                        break;
                                }

                            } catch (Exception e) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (
                            Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<CommonSuccessMsgModel> call, Throwable t) {
                    Log.e("conversion issue", t.getMessage());

                    if (t instanceof IOException) {
                        Toast.makeText(context, "This is an actual network failure :( inform the user and possibly retry)", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    } else {
                        Log.e("conversion issue", t.getMessage());
                        Toast.makeText(context, "Please Check your Internet Connection...." + t.getMessage(), Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            });

        }

    @Override
    public int getItemCount() {
        return notificationListResultList.size();
    }
}
class NotificationViewHolder extends RecyclerView.ViewHolder {

    AppCompatTextView notification_heading , notificatin_subheading,notification_date;
    AppCompatImageView notification_delete_image;

    public NotificationViewHolder(@NonNull View itemView) {
        super(itemView);

        notification_heading = itemView.findViewById(R.id.event_heading);
        notificatin_subheading = itemView.findViewById(R.id.notificatin_subheading);
        notification_date = itemView.findViewById(R.id.notification_date);
        notification_delete_image = itemView.findViewById(R.id.notification_delete_image);
    }
}