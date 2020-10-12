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

public class BuyListAdapter extends RecyclerView.Adapter<BuyListAdapter.CustomViewHolder> {

    private ArrayList<Product> arrayList;
    private Context context;


//    public interface RecyclerViewClickListener {
//        void onClick(View v, int position);
//    }

    public BuyListAdapter(ArrayList<Product> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }





    @NonNull
    @Override
    public BuyListAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_buy_list, parent, false);
        BuyListAdapter.CustomViewHolder holder = new BuyListAdapter.CustomViewHolder(view);
        return holder;

    }


    @Override
    public void onBindViewHolder(@NonNull BuyListAdapter.CustomViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getImage())
                .into(holder.iv_buy_image);
        holder.tv_buy_title.setText("제품명 " + arrayList.get(position).getTitle());
        holder.tv_buy_price.setText("가격 " + String.valueOf(arrayList.get(position).getPrice()) + "원");
        holder.tv_buy_seller.setText("판매자 " + arrayList.get(position).getSeller());
        holder.tv_buy_buyer.setText("구매자 " + arrayList.get(position).getBuyer());

    }

    @Override
    public int getItemCount() {
        //삼항 연사자
        return (arrayList != null ? arrayList.size() : 0);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        //implements View.OnClickListener
        ImageView iv_buy_image;
        TextView tv_buy_title;
        TextView tv_buy_price;
        TextView tv_buy_seller;
        TextView tv_buy_buyer;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_buy_image = itemView.findViewById(R.id.iv_buy_image);
            this.tv_buy_title = itemView.findViewById(R.id.tv_buy_title);
            this.tv_buy_price = itemView.findViewById(R.id.tv_buy_price);
            this.tv_buy_seller = itemView.findViewById(R.id.tv_buy_seller);
            this.tv_buy_buyer = itemView.findViewById(R.id.tv_buy_buyer);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    Intent intent_buy = new Intent(view.getContext(), BuyDetailActivity.class);

                    intent_buy.putExtra("iv_bd_profile",arrayList.get(position).getImage());//
                    intent_buy.putExtra("tv_bd_price", String.valueOf(arrayList.get(position).getPrice()));
                    intent_buy.putExtra("tv_bd_seller", arrayList.get(position).getSeller());
                    intent_buy.putExtra("tv_bd_buyer", arrayList.get(position).getBuyer());
                    intent_buy.putExtra("tv_bd_name", arrayList.get(position).getTitle());
                    intent_buy.putExtra("unique", arrayList.get(position).getUnique());

                    view.getContext().startActivity(intent_buy);
                }
            });


        }
    }
}
