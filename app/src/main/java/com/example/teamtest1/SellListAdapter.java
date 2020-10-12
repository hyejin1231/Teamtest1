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

public class SellListAdapter extends RecyclerView.Adapter<SellListAdapter.CustomViewHolder> {

    private ArrayList<Product> arrayList;
    private Context context;


    public SellListAdapter(ArrayList<Product> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }





    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sell_list, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;

    }


    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getImage())
                .into(holder.iv_sell_image);
        holder.tv_sell_title.setText("제품명 " + arrayList.get(position).getTitle());
        holder.tv_sell_price.setText("가격 " + String.valueOf(arrayList.get(position).getPrice()) + "원");
        holder.tv_sell_seller.setText("판매자 " + arrayList.get(position).getSeller());
        holder.tv_sell_buyer.setText("구매자 " + arrayList.get(position).getBuyer());

    }

    @Override
    public int getItemCount() {
        //삼항 연사자
        return (arrayList != null ? arrayList.size() : 0);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        //implements View.OnClickListener
        ImageView iv_sell_image;
        TextView tv_sell_title;
        TextView tv_sell_price;
        TextView tv_sell_seller;
        TextView tv_sell_buyer;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_sell_image = itemView.findViewById(R.id.iv_sell_image);
            this.tv_sell_title = itemView.findViewById(R.id.tv_sell_title);
            this.tv_sell_price = itemView.findViewById(R.id.tv_sell_price);
            this.tv_sell_seller = itemView.findViewById(R.id.tv_sell_seller);
            this.tv_sell_buyer = itemView.findViewById(R.id.tv_sell_buyer);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    //상품이 판매중인 경우
                    if((arrayList.get(position).getStatus()).equals("selling")){
                        Intent intent_selling = new Intent(view.getContext(),SellingDetailActivity.class);

                        intent_selling.putExtra("iv_sd_profile",arrayList.get(position).getImage());//
                        intent_selling.putExtra("tv_sd_price", String.valueOf(arrayList.get(position).getPrice()));
                        intent_selling.putExtra("tv_sd_seller", arrayList.get(position).getSeller());
                        intent_selling.putExtra("tv_sd_buyer", arrayList.get(position).getBuyer());
                        intent_selling.putExtra("tv_sd_name", arrayList.get(position).getTitle());
                        //intent_selling.putExtra("p_id",arrayList.get(position).getId());

                        view.getContext().startActivity(intent_selling);
                    }
                    else if((arrayList.get(position).getStatus()).equals("complete")){ //상품이 판매완료인 경우
                        Intent intent_complete = new Intent(view.getContext(), CompleteDetailActivity.class);

                        intent_complete.putExtra("iv_sd_profile",arrayList.get(position).getImage());//
                        intent_complete.putExtra("tv_sd_price", String.valueOf(arrayList.get(position).getPrice()));
                        intent_complete.putExtra("tv_sd_seller", arrayList.get(position).getSeller());
                        intent_complete.putExtra("tv_sd_buyer", arrayList.get(position).getBuyer());
                        intent_complete.putExtra("tv_sd_name", arrayList.get(position).getTitle());
                        intent_complete.putExtra("unique",arrayList.get(position).getUnique());

                        view.getContext().startActivity(intent_complete);
                    }

                }
            });


        }
    }
}
