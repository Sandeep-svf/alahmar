package com.webnmobapps.alahmaar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.webnmobapps.alahmaar.R;
import com.webnmobapps.alahmaar.event.EventDetailsActivity;
import com.webnmobapps.alahmaar.model.EventGallry;
import com.webnmobapps.alahmaar.model.EventResult;
import com.webnmobapps.alahmaar.retrofit.API_Client;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {

    private Context context;
    private ArrayList<EventResult> eventAdapterList = new ArrayList<>();
    ArrayList<EventGallry> eventGallry2List = new ArrayList<>();


    public EventAdapter(Context context, ArrayList<EventResult> eventAdapterList) {
        this.context = context;
        this.eventAdapterList = eventAdapterList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_design2, parent, false);
        EventViewHolder myViewHolder = new EventViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.event_heading.setText(eventAdapterList.get(position).getHeadding());
        holder.event_subheading.setText(eventAdapterList.get(position).getDescription());
        holder.event_created_at.setText(eventAdapterList.get(position).getDate());
        Glide.with(context).load(API_Client.BASE_IMAGE_URL+eventAdapterList.get(position).getImage()).placeholder(R.drawable.ic_launcher_background).into(holder.event_profile);
     //   holder.event_profile.setImageResource(eventAdapterList.get(position).getEventProfile());

        Log.e("nfkjdsdkj",API_Client.BASE_IMAGE_URL+eventAdapterList.get(position).getImage());


       /* EventGallry eventGallry = new EventGallry();
        eventGallry.setImage(API_Client.BASE_IMAGE_URL+eventAdapterList.get(position).getImage());
        eventGallry2List.add(eventGallry);*/

        holder.event_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                eventGallry2List = eventAdapterList.get(position).getGallery();

                    Intent intent = new Intent(context, EventDetailsActivity.class);
                    intent.putExtra("list",eventGallry2List);
                    context.startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {
        return eventAdapterList.size();
    }
}
class EventViewHolder extends RecyclerView.ViewHolder {

    AppCompatImageView event_profile;
    AppCompatTextView event_heading,event_subheading,event_created_at;
    ConstraintLayout event_layout;

    public EventViewHolder(@NonNull View itemView) {
        super(itemView);
        event_created_at = itemView.findViewById(R.id.event_created_at);
        event_subheading = itemView.findViewById(R.id.event_subheading);
        event_heading = itemView.findViewById(R.id.event_heading);
        event_profile = itemView.findViewById(R.id.event_image);
        event_layout = itemView.findViewById(R.id.event_list_layout);
    }
}