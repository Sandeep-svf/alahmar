package com.webnmobapps.alahmaar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.webnmobapps.alahmaar.R;
import com.webnmobapps.alahmaar.model.CommunityCommentListResult;
import com.webnmobapps.alahmaar.retrofit.API_Client;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Comment_Adapter extends RecyclerView.Adapter<CommentViewHolder> {

    private Context context;
    List<CommunityCommentListResult> communityCommentListResultList = new ArrayList<>();
    private String userId,userIdAPI,postId;

    public Comment_Adapter(Context context, List<CommunityCommentListResult> communityCommentListResultList, String userId, String postId) {
        this.context = context;
        this.communityCommentListResultList = communityCommentListResultList;
        this.userId = userId;
        this.postId = postId;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_design, parent, false);
        CommentViewHolder myViewHolder = new CommentViewHolder(view);
        return myViewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {

        userIdAPI = String.valueOf(communityCommentListResultList.get(position).getId());
        if(userId==userIdAPI)  holder.ED_layout.setVisibility(View.VISIBLE);
     //   holder.comment_user_name.setText(communityCommentListResultList.get(position).getHeaddings());
      //  holder.comment_user_message.setText(communityCommentListResultList.get(position).getTopicDescription());
        Glide.with(context).load(API_Client.BASE_IMAGE_URL+communityCommentListResultList.get(position).getImage()).placeholder(R.drawable.ic_launcher_background).into(holder.comment_user_image);
    }

    @Override
    public int getItemCount() {
        return communityCommentListResultList.size();
    }
}
class CommentViewHolder extends RecyclerView.ViewHolder {

    CircleImageView comment_user_image;
    TextView comment_user_name,comment_user_message;
    AppCompatTextView edit_comment_layout, delete_comment_layout;
    LinearLayout ED_layout;

    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);
        comment_user_image = itemView.findViewById(R.id.friends_images);
        comment_user_name = itemView.findViewById(R.id.friend_name);
        comment_user_message = itemView.findViewById(R.id.comment_message);
        delete_comment_layout = itemView.findViewById(R.id.delete_comment_layout);
        edit_comment_layout = itemView.findViewById(R.id.edit_comment_layout);
        ED_layout = itemView.findViewById(R.id.ED_layout);
    }
}