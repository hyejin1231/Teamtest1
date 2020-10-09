package com.example.teamtest1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdminMemberAdapter extends RecyclerView.Adapter<AdminMemberAdapter.CustomViewHolder> {

    private ArrayList<User> arrayList;
    private Context context;
    String aaa;

    public AdminMemberAdapter(ArrayList<User> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public AdminMemberAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admember_item,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdminMemberAdapter.CustomViewHolder holder, int position) {

        String abcd = arrayList.get(position).getId();
        Glide.with(holder.itemView).load(arrayList.get(position).getPhotoUrl()).into(holder.img_admemImg); //프로필 uri를 이미지 뷰에 세팅
        holder.tv_admemuid.setText(arrayList.get(position).getUid());
//        holder.tv_admemid.setText(arrayList.get(position).getId());
//        Toast.makeText(context, arrayList.get(position).getUid(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
         return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{

        ImageView img_admemImg;
        TextView tv_admemid,tv_admemuid;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.img_admemImg = itemView.findViewById(R.id.img_admemImg);
            this.tv_admemid = itemView.findViewById(R.id.tv_adminID);
            this.tv_admemuid = itemView.findViewById(R.id.tv_admemuid);


//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    final int position = getAdapterPosition();
//                    aaa = arrayList.get(position).getId();
//
//                    Toast.makeText(context, aaa,Toast.LENGTH_SHORT).show();
//
//                }
//            });

        }
    }
}
