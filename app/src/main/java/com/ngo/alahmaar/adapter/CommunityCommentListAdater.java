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
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ngo.alahmaar.R;
import com.ngo.alahmaar.model.AddCommentModel;
import com.ngo.alahmaar.model.CommonSuccessMsgModel;
import com.ngo.alahmaar.model.CommunityCommentListResult;
import com.ngo.alahmaar.retrofit.API_Client;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunityCommentListAdater extends RecyclerView.Adapter<CommunityCommentListViewHolder> {

    List<CommunityCommentListResult> communityCommentListResultList = new ArrayList<>();
    Context context;
    String userId, finalAccessToken, CommunityPostId,userId2;
    LayoutInflater inflater;


    public CommunityCommentListAdater(List<CommunityCommentListResult> communityCommentListResultList, Context context, String userId, String finalAccessToken, String communityPostId) {
        this.communityCommentListResultList = communityCommentListResultList;
        this.context = context;
        this.userId = userId;
        this.finalAccessToken = finalAccessToken;
        CommunityPostId = communityPostId;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CommunityCommentListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.community_comment_layout_xml, parent, false);
        CommunityCommentListViewHolder myViewHolder = new CommunityCommentListViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityCommentListViewHolder holder, @SuppressLint("RecyclerView") int position) {

        try {
            Glide.with(context).load(API_Client.BASE_IMAGE_URL+communityCommentListResultList.get(position).getImage())
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(holder.userImage);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        try {
            Log.e("listImage",API_Client.BASE_IMAGE_URL+communityCommentListResultList.get(position).getImage());
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        holder.userName.setText(communityCommentListResultList.get(position).getUsername());
        holder.userComment.setText(communityCommentListResultList.get(position).getComment());

        try {
            Log.e("dhfskjahfd",userId+"_"+communityCommentListResultList.get(position).getUser());
        } catch (Exception exception) {
            exception.printStackTrace();
        }


        if(userId.equals(String.valueOf(communityCommentListResultList.get(position).getUser()))){
            holder.edit_delete_layout.setVisibility(View.VISIBLE);
        }else{
            holder.edit_delete_layout.setVisibility(View.GONE);
        }



        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPosition = String.valueOf(position);
                String commentId =  String.valueOf(communityCommentListResultList.get(position).getId());
                String commentString = communityCommentListResultList.get(position).getComment();
                String currentUserImage = communityCommentListResultList.get(position).getImage();

                // open popup for edit comment

                alert_dialog(commentId,commentString, position,currentUserImage);



            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String commentId =  String.valueOf(communityCommentListResultList.get(position).getId());

                // delete_api
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.delete_comment_xml);
                AppCompatTextView cancel_layout = dialog.findViewById(R.id.cancel_layout);
                AppCompatTextView delete_layout = dialog.findViewById(R.id.delete_layout);



                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


                delete_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // delete_api
                        delete_api(commentId,position);
                        dialog.dismiss();
                    }
                });

                cancel_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


            }
        });


    }

    private void delete_api(String commentId, int currentPosition) {



            // comment list api...........

            final ProgressDialog pd = new ProgressDialog(context);
            pd.setCancelable(false);
            pd.setMessage("loading...");
            pd.show();

            Call<CommonSuccessMsgModel> call = API_Client.getClient().COMMON_SUCCESS_MSG_MODEL_CALL(finalAccessToken,CommunityPostId,commentId);

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

                                removeItem(currentPosition);
                                //notifyItemRemoved(currentPosition);
                               // notifyDataSetChanged();


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

    public void removeItem(int position) {
        try {
            communityCommentListResultList.remove(position);
            notifyDataSetChanged();
        } catch (IndexOutOfBoundsException index) {
            index.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void alert_dialog(String commentId, String commentString, int currentPosition, String currentUserImage) {

        Dialog dialogs;

        View alertLayout = inflater.inflate(R.layout.edit_comment_xml, null);
        final AppCompatButton cancel_button = alertLayout.findViewById(R.id.cancel_button);
        final AppCompatButton update_button = alertLayout.findViewById(R.id.update_button);
        final AppCompatEditText edittext = alertLayout.findViewById(R.id.edittext);
        final CircleImageView userImage = alertLayout.findViewById(R.id.userImage);


        Log.e("dfjskdf",currentUserImage+"ok");

        Glide.with(alertLayout).load(API_Client.BASE_IMAGE_URL+currentUserImage).placeholder(R.drawable.ic_launcher_background).load(userImage);
        edittext.setText(commentString);

        dialogs = new Dialog(context);
        dialogs.setContentView(alertLayout);
        dialogs.setCancelable(false);
       // dialogs.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogs.getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT,WindowManager.LayoutParams.FILL_PARENT);
        dialogs.show();
        dialogs.setCanceledOnTouchOutside(true);

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogs.dismiss();
            }
        });

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogs.dismiss();

                String newCommentString = edittext.getText().toString();
                // edit comment api
                edit_comment_api(commentId, newCommentString, currentPosition);

            }
        });


        }

    private void edit_comment_api(String commentId,String commentString, int currentPosition) {


            // comment list api...........

            final ProgressDialog pd = new ProgressDialog(context);
            pd.setCancelable(false);
            pd.setMessage("loading...");
            pd.show();

            Call<AddCommentModel> call = API_Client.getClient().ADD_COMMENT_MODEL_CALL(finalAccessToken,userId,CommunityPostId,commentId,commentString);

            call.enqueue(new Callback<AddCommentModel>() {
                @Override
                public void onResponse(Call<AddCommentModel> call, Response<AddCommentModel> response) {
                    pd.dismiss();
                    try {
                        //if api response is successful ,taking message and success
                        if (response.isSuccessful()) {

                            String success = String.valueOf(response.body().getSuccess());
                            String message = String.valueOf(response.body().getMessage());


                            if (success.equals("true") || success.equals("True")) {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                                communityCommentListResultList.get(currentPosition).setComment(commentString);
                                notifyItemInserted(currentPosition);
                                notifyDataSetChanged();

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
                public void onFailure(Call<AddCommentModel> call, Throwable t) {
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
        return communityCommentListResultList.size();
    }
}
class CommunityCommentListViewHolder extends RecyclerView.ViewHolder {

    TextView userComment,userName,edit,delete;
    CircleImageView userImage;
    RelativeLayout edit_delete_layout;

    public CommunityCommentListViewHolder(@NonNull View itemView) {
        super(itemView);

        userImage  = itemView.findViewById(R.id.userImage2145);
        userComment  = itemView.findViewById(R.id.userComment);
        userName  = itemView.findViewById(R.id.userName);
        delete  = itemView.findViewById(R.id.delete);
        edit  = itemView.findViewById(R.id.edit);
        edit_delete_layout  = itemView.findViewById(R.id.edit_delete_layout);
    }
}