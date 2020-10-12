package com.example.teamtest1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class LikeListAdapter extends RecyclerView.Adapter <LikeListAdapter.CustomViewHolder3> {

    private ArrayList<Product> arrayList;
    private Context context;

    public LikeListAdapter(ArrayList<Product> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_like_list,parent,false);
        LikeListAdapter.CustomViewHolder3 holder3 = new LikeListAdapter.CustomViewHolder3(view);
        return holder3;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder3 holder, int position) {

        Glide.with(holder.itemView)
                .load(arrayList.get(position).getImage())
                .into(holder.iv_like_image);
        holder.tv_like_title.setText("제품명 " + arrayList.get(position).getTitle());
        holder.tv_like_price.setText("가격 " + String.valueOf(arrayList.get(position).getPrice()) + "원");
        holder.tv_like_seller.setText("판매자 " + arrayList.get(position).getSeller());
        holder.tv_like_buyer.setText("구매자 " + arrayList.get(position).getBuyer());
    }

    @Override
    public int getItemCount() {
        return(arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder3 extends RecyclerView.ViewHolder {

        ImageView iv_like_image;
        TextView tv_like_title;
        TextView tv_like_price;
        TextView tv_like_seller;
        TextView tv_like_buyer;

        public CustomViewHolder3(@NonNull View itemView) {
            super(itemView);

            this.iv_like_image = itemView.findViewById(R.id.iv_like_image);
            this.tv_like_title = itemView.findViewById(R.id.tv_like_title);
            this.tv_like_price = itemView.findViewById(R.id.tv_like_price);
            this.tv_like_seller = itemView.findViewById(R.id.tv_like_seller);
            this.tv_like_buyer = itemView.findViewById(R.id.tv_like_buyer);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    Intent intent = new Intent(view.getContext(), LikeDetailActivity.class);

                    intent.putExtra("iv_ld_profile",arrayList.get(position).getImage());//
                    intent.putExtra("tv_ld_price", String.valueOf(arrayList.get(position).getPrice()));
                    intent.putExtra("tv_ld_seller", arrayList.get(position).getSeller());
                    intent.putExtra("tv_ld_buyer", arrayList.get(position).getBuyer());
                    intent.putExtra("tv_ld_name", arrayList.get(position).getTitle());
                    intent.putExtra("unique", arrayList.get(position).getUnique());

                    view.getContext().startActivity(intent);
                }
            });

        }
    }
}
