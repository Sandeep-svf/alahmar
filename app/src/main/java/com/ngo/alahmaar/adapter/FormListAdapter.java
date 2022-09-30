package com.ngo.alahmaar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ngo.alahmaar.R;
import com.ngo.alahmaar.model.UserFormListResult;
import com.ngo.alahmaar.retrofit.API_Client;
import com.ngo.alahmaar.userForm.FormDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class FormListAdapter extends RecyclerView.Adapter<FormListViewHolder> {

    private Context context;
    private List<UserFormListResult> formListModelList = new ArrayList<>();

    public FormListAdapter(Context context, List<UserFormListResult> formListModelList) {
        this.context = context;
        this.formListModelList = formListModelList;
    }

    @NonNull
    @Override
    public FormListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.form_list_design, parent, false);
        FormListViewHolder myViewHolder = new FormListViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FormListViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.form_name.setText(formListModelList.get(position).getName());

        holder.form_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = API_Client.FORM_URL+formListModelList.get(position).getLink();

                Intent intent = new Intent(context, FormDetailsActivity.class);
                intent.putExtra("url",url);
                context.startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {
        return formListModelList.size();
    }
}
class FormListViewHolder extends RecyclerView.ViewHolder {

    AppCompatTextView form_name;
    ConstraintLayout form_layout;
    public FormListViewHolder(@NonNull View itemView) {
        super(itemView);
        form_name = itemView.findViewById(R.id.form_name);
        form_layout = itemView.findViewById(R.id.form_layout);
    }
}