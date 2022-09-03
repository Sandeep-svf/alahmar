package com.webnmobapps.alahmaar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.webnmobapps.alahmaar.R;
import com.webnmobapps.alahmaar.model.NotificationListResult;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationViewHolder> {

    private Context context;
    List<NotificationListResult> notificationListResultList = new ArrayList<>();

    public NotificationAdapter(Context context, List<NotificationListResult> notificationListResultList) {
        this.context = context;
        this.notificationListResultList = notificationListResultList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_design, parent, false);
        NotificationViewHolder myViewHolder = new NotificationViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {

        holder.notification_heading.setText(notificationListResultList.get(position).getNotificationHeadding());
        holder.notificatin_subheading.setText(notificationListResultList.get(position).getNotificationMessage());
        holder.notification_date.setText(notificationListResultList.get(position).getRecievedDate());
    }

    @Override
    public int getItemCount() {
        return notificationListResultList.size();
    }
}
class NotificationViewHolder extends RecyclerView.ViewHolder {

    AppCompatTextView notification_heading , notificatin_subheading,notification_date;

    public NotificationViewHolder(@NonNull View itemView) {
        super(itemView);

        notification_heading = itemView.findViewById(R.id.event_heading);
        notificatin_subheading = itemView.findViewById(R.id.notificatin_subheading);
        notification_date = itemView.findViewById(R.id.notification_date);
    }
}