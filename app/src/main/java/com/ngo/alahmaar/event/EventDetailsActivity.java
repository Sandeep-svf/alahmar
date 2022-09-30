package com.ngo.alahmaar.event;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ngo.alahmaar.R;
import com.ngo.alahmaar.adapter.EventImageListAdapter;
import com.ngo.alahmaar.model.EventGallry;

import java.util.ArrayList;
import java.util.List;

public class EventDetailsActivity extends AppCompatActivity {

    RecyclerView rcv_event_image_list;
    List<ImageListModel> imageListModelList = new ArrayList<>();
    ConstraintLayout event_details_back;
    ArrayList<EventGallry> eventGallryArrayList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        intis();


        eventGallryArrayList = (ArrayList<EventGallry>) getIntent().getSerializableExtra("list");
        try {
            Log.e("jgkdhfgj", String.valueOf(eventGallryArrayList.size()));
            Log.e("jgkdhfgj", String.valueOf(eventGallryArrayList.get(0).getImage()));
            Log.e("jgkdhfgj", String.valueOf(eventGallryArrayList.get(1).getImage()));
            Log.e("jgkdhfgj", String.valueOf(eventGallryArrayList.get(2).getImage()));

        } catch (Exception e) {
            e.printStackTrace();
        }

        add_data_in_list();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(EventDetailsActivity.this, LinearLayoutManager.HORIZONTAL,false);
        rcv_event_image_list.setLayoutManager(linearLayoutManager);

        EventImageListAdapter eventImageListAdapter = new EventImageListAdapter(EventDetailsActivity.this,eventGallryArrayList);
        rcv_event_image_list.setAdapter(eventImageListAdapter);

        event_details_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void add_data_in_list() {
        ImageListModel imageListModel = new ImageListModel();
        imageListModel.setEventImage(R.drawable.group_image1);
        imageListModelList.add(imageListModel);

        imageListModel = new ImageListModel();
        imageListModel.setEventImage(R.drawable.group_image2);
        imageListModelList.add(imageListModel);

        imageListModel = new ImageListModel();
        imageListModel.setEventImage(R.drawable.user_profile1);
        imageListModelList.add(imageListModel);

        imageListModel = new ImageListModel();
        imageListModel.setEventImage(R.drawable.user_profile2);
        imageListModelList.add(imageListModel);

        imageListModel = new ImageListModel();
        imageListModel.setEventImage(R.drawable.group_image1);
        imageListModelList.add(imageListModel);

        imageListModel = new ImageListModel();
        imageListModel.setEventImage(R.drawable.group_image2);
        imageListModelList.add(imageListModel);

        imageListModel = new ImageListModel();
        imageListModel.setEventImage(R.drawable.user_profile1);
        imageListModelList.add(imageListModel);

        imageListModel = new ImageListModel();
        imageListModel.setEventImage(R.drawable.user_profile2);
        imageListModelList.add(imageListModel);


    }

    private void intis() {
        rcv_event_image_list = findViewById(R.id.rcv_event_image_list);
        event_details_back = findViewById(R.id.event_details_back);
    }
}