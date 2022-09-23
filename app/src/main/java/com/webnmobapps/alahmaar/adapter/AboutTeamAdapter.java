package com.webnmobapps.alahmaar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.webnmobapps.alahmaar.R;
import com.webnmobapps.alahmaar.model.AboutTeamData;
import com.webnmobapps.alahmaar.retrofit.API_Client;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AboutTeamAdapter extends RecyclerView.Adapter<AboutTeamViewHolder> {

    Context context;
    List<AboutTeamData> aboutTeamDataList = new ArrayList<>();

    public AboutTeamAdapter(Context context, List<AboutTeamData> aboutTeamDataList) {
        this.context = context;
        this.aboutTeamDataList = aboutTeamDataList;
    }

    @NonNull
    @Override
    public AboutTeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.about_team_design, parent, false);
        AboutTeamViewHolder myViewHolder = new AboutTeamViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AboutTeamViewHolder holder, int position) {

        Glide.with(context).load(API_Client.BASE_IMAGE_URL+aboutTeamDataList.get(position).getImage())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.userImage);

        holder.userName.setText(aboutTeamDataList.get(position).getName());
        holder.userComment.setText(aboutTeamDataList.get(position).getDesignation());

    }

    @Override
    public int getItemCount() {
        return aboutTeamDataList.size();
    }
}
class AboutTeamViewHolder extends RecyclerView.ViewHolder {

    CircleImageView userImage;
    TextView userName, userComment;

    public AboutTeamViewHolder(@NonNull View itemView) {
        super(itemView);
        userImage = itemView.findViewById(R.id.userImage);
        userName = itemView.findViewById(R.id.userName);
        userComment = itemView.findViewById(R.id.userComment);
    }
}