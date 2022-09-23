package com.webnmobapps.alahmaar.adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.webnmobapps.alahmaar.R;
import com.webnmobapps.alahmaar.model.AddCommentModel;
import com.webnmobapps.alahmaar.model.CommunityCommentListModel;
import com.webnmobapps.alahmaar.model.CommunityCommentListResult;
import com.webnmobapps.alahmaar.model.CommuntyPostResult;
import com.webnmobapps.alahmaar.retrofit.API_Client;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunityAdapter extends RecyclerView.Adapter<CommunityViewHolder> {

    Context context;
    List<CommuntyPostResult> communtyPostResultList = new ArrayList<>();
    private String userId;
    List<CommunityCommentListResult> communityCommentListResultList = new ArrayList<>();
    RecyclerView comment_recycler_view;
    String finalAccessToken;

    public CommunityAdapter(Context context, List<CommuntyPostResult> communtyPostResultList, String userId, String finalAccessToken) {
        this.context = context;
        this.communtyPostResultList = communtyPostResultList;
        this.userId = userId;
        this.finalAccessToken = finalAccessToken;
    }

    @NonNull
    @Override
    public CommunityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.community_design, parent, false);
        CommunityViewHolder myViewHolder = new CommunityViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.community_post_description.setText(communtyPostResultList.get(position).getTopicDescription());
        holder.community_no_of_comment.setText(String.valueOf(communtyPostResultList.get(position).getComment()+" Comment"));
        holder.community_no_of_like.setText(String.valueOf(communtyPostResultList.get(position).getLikes()+" Like"));
        holder.community_headline.setText(communtyPostResultList.get(position).getHeaddings());
        holder.community_post_time.setText(communtyPostResultList.get(position).getDate());

        Glide.with(context).load(API_Client.BASE_IMAGE_URL+communtyPostResultList.get(position).getImage()).placeholder(R.drawable.ic_launcher_background).into(holder.community_post_image);

        holder.comment_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 String postId = String.valueOf(communtyPostResultList.get(position).getId());
                comments(holder,postId);
            }
        });
    }

// String post_id, int position, List<Post_Response> post_responses

    private void comments(CommunityViewHolder holder, String postId) {

        Log.e("postId",postId);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.comment_pop_up_window, null);
        comment_recycler_view = customView.findViewById(R.id.comment_recycler_view);
        EditText comment_edt = customView.findViewById(R.id.txt_comment);
        ImageView send_comment = customView.findViewById(R.id.img_comment_sent);
        ImageView dismiss_popup_icon = customView.findViewById(R.id.dismiss_popup_icon);
        PopupWindow popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setAnimationStyle(R.style.popup_window_animation);
        popupWindow.showAtLocation(customView, Gravity.BOTTOM, 40, 60);

       // List<Comment> user_comment = post_responses.get(position).getComments();



        send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textData = comment_edt.getText().toString();

                add_comment_api(textData,postId);
                comment_edt.setText("");
            }
        });

        comment_list_api(context,postId);

        dismiss_popup_icon.setOnClickListener(v -> popupWindow.dismiss());

     /*   send_comment.setOnClickListener(v -> {
            String str_edt_comment = comment_edt.getText().toString();
            if (str_edt_comment.equals("")) {
                Toast.makeText(context, context.getString(R.string.comment), Toast.LENGTH_SHORT).show();

              //  add comment

                comment_edt.requestFocus();
            } else {
              //  post_comment(str_edt_comment, post_id, comment_edt);
                comment_edt.setText("");
                popupWindow.dismiss();
            }
        });*/
    }

    private void add_comment_api(String commentString, String postId) {

        // add comment api

            final ProgressDialog pd = new ProgressDialog(context);
            pd.setCancelable(false);
            pd.setMessage("loading...");
            pd.show();

            Call<AddCommentModel> call = API_Client.getClient().ADD_COMMENT_MODEL_CALL_POST(finalAccessToken,userId,postId,commentString);

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

                                Toast.makeText(context,message , Toast.LENGTH_SHORT).show();
                                //CommunityViewHolder holder, Context context,String postId
                                comment_list_api(context,postId);
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

   /* private void add_comment_api() {

            // comment list api...........

            final ProgressDialog pd = new ProgressDialog(context);
            pd.setCancelable(false);
            pd.setMessage("loading...");
            pd.show();

            Call<CommunityCommentListModel> call = API_Client.getClient().comments_list(userId);

            call.enqueue(new Callback<CommunityCommentListModel>() {
                @Override
                public void onResponse(Call<CommunityCommentListModel> call, Response<CommunityCommentListModel> response) {
                    pd.dismiss();
                    try {
                        //if api response is successful ,taking message and success
                        if (response.isSuccessful()) {

                            String success = String.valueOf(response.body().getSuccess());


                            if (success.equals("true") || success.equals("True")) {
                                communityCommentListResultList = response.body().getData();
                                // Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                                //








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
                public void onFailure(Call<CommunityCommentListModel> call, Throwable t) {
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

        }*/

    private void comment_list_api(Context context,String postId) {

        // comment list api...........
        
            final ProgressDialog pd = new ProgressDialog(context);
            pd.setCancelable(false);
            pd.setMessage("loading...");
            pd.show();

            Call<CommunityCommentListModel> call = API_Client.getClient().comments_list(finalAccessToken,postId);

            call.enqueue(new Callback<CommunityCommentListModel>() {
                @Override
                public void onResponse(Call<CommunityCommentListModel> call, Response<CommunityCommentListModel> response) {
                    pd.dismiss();
                    try {
                        //if api response is successful ,taking message and success
                        if (response.isSuccessful()) {

                            String success = String.valueOf(response.body().getSuccess());
                          

                            if (success.equals("true") || success.equals("True")) {

                               // Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                                //
                                communityCommentListResultList = response.body().getData();

                                Toast.makeText(context, "List size is:"+communityCommentListResultList.size(), Toast.LENGTH_SHORT).show();

                                if(communityCommentListResultList.isEmpty()){
                                    Toast.makeText(context, "This post doesn't have any comment yet", Toast.LENGTH_SHORT).show();
                                }
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                                    comment_recycler_view.setLayoutManager(linearLayoutManager);
                                    CommunityCommentListAdater communityCommentListAdater = new CommunityCommentListAdater(communityCommentListResultList, context,userId, finalAccessToken, postId);
                                    comment_recycler_view.setAdapter(communityCommentListAdater);



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
                public void onFailure(Call<CommunityCommentListModel> call, Throwable t) {
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
        return communtyPostResultList.size();
    }
}
class CommunityViewHolder extends RecyclerView.ViewHolder {


    AppCompatTextView comment_layout, community_headline,community_post_time,community_post_description,community_no_of_comment,community_no_of_like;
    LinearLayout daflkjlkdjf;
    AppCompatImageView comment_img_layout,community_post_image;

    public CommunityViewHolder(@NonNull View itemView) {
        super(itemView);
        comment_layout = itemView.findViewById(R.id.comment_layout);
        daflkjlkdjf = itemView.findViewById(R.id.daflkjlkdjf);
        comment_img_layout = itemView.findViewById(R.id.comment_img_layout);
        community_post_description = itemView.findViewById(R.id.community_post_description);
        community_no_of_comment = itemView.findViewById(R.id.community_no_of_comment);
        community_no_of_like = itemView.findViewById(R.id.community_no_of_like);
        community_post_image = itemView.findViewById(R.id.community_post_image);
        community_headline = itemView.findViewById(R.id.community_headline);
        community_post_time = itemView.findViewById(R.id.community_post_time);
    }
}