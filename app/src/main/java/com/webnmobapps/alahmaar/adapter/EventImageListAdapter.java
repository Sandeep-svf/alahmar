package com.webnmobapps.alahmaar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.webnmobapps.alahmaar.R;
import com.webnmobapps.alahmaar.basicAndroidFunction.RegisterActivity;
import com.webnmobapps.alahmaar.model.EventGallry;
import com.webnmobapps.alahmaar.retrofit.API_Client;

import java.util.ArrayList;

import in.aabhasjindal.otptextview.OtpTextView;

public class EventImageListAdapter extends RecyclerView.Adapter<ImageListViewHolder> {

     Context context;
    ArrayList<EventGallry> imageListModelList = new ArrayList<>();

    public EventImageListAdapter(Context context, ArrayList<EventGallry> imageListModelList) {
        this.context = context;
        this.imageListModelList = imageListModelList;
    }

    @NonNull
    @Override
    public ImageListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_image_list_design, parent, false);
        ImageListViewHolder myViewHolder = new ImageListViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageListViewHolder holder, @SuppressLint("RecyclerView") int position) {

        try {
            Glide.with(context).load(API_Client.BASE_IMAGE_URL+imageListModelList.get(position).getImage()).placeholder(R.drawable.ic_launcher_background).into(holder.event_list_image);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.event_list_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String image_link = API_Client.BASE_IMAGE_URL+imageListModelList.get(position).getImage();

               // popup(position,context,image_link);

                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(image_link), "image/*");
                    intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Someting went really wrong! Flling sorry for you", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public static int getWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }


    private void popup(int position, Context context, String image_link) {

        // open popup for show image......
        AlertDialog dialogs;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View alertLayout = inflater.inflate(R.layout.test_dialog_xml_otp2, null);
        final AppCompatImageView image_layout = alertLayout.findViewById(R.id.image_layout);

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        dialogs = alert.create();
        dialogs.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // dialogs.getWindow().setLayout(((getWidth(context) / 100) * 90), LinearLayout.LayoutParams.MATCH_PARENT);
       // dialogs.getWindow().setLayout(100,100);
        dialogs.show();
        dialogs.setCanceledOnTouchOutside(true);
        Glide.with(context).load(image_link).placeholder(R.drawable.ic_launcher_background).into(image_layout);
    }

    @Override
    public int getItemCount() {
        return imageListModelList.size();
    }
}
class ImageListViewHolder extends RecyclerView.ViewHolder {

    ShapeableImageView event_list_image;
    public ImageListViewHolder(@NonNull View itemView) {
        super(itemView);
        event_list_image = itemView.findViewById(R.id.event_list_image);
    }
}