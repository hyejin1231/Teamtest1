package com.example.teamtest1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductAdapter.CustomViewHolder> {

    private ArrayList<Product> arrayList;
    private Context context;
    String key;
    String abcd;

    public AdminProductAdapter(ArrayList<Product> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adproduct_item,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, int position) {

        Uri uri1 = Uri.parse(arrayList.get(position).getImage());


        Glide.with(holder.itemView).asBitmap()
                .load(uri1)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        holder.iv_adProductImage.setImageBitmap(resource);
                    }
                });

//                Glide.with(holder.itemView)
//                .load(arrayList.get(position).getImage())
//                .into(holder.iv_adProductImage);

        holder.tv_adProductTitle.setText(arrayList.get(position).getTitle());
        holder.tv_adProductPrice.setText(arrayList.get(position).getPrice());
        holder.tv_adProductBid.setText(arrayList.get(position).getBid());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0 );
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView tv_adProductTitle,tv_adProductBid,tv_adProductPrice;
        Button btn_warn,btn_adProductDel;
        ImageView iv_adProductImage;

        private FirebaseDatabase database;
        private DatabaseReference databaseReference;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            database = FirebaseDatabase.getInstance();
            databaseReference = database.getReference("Product");

            this.iv_adProductImage = itemView.findViewById(R.id.iv_adProductImage);
            this.tv_adProductTitle = itemView.findViewById(R.id.tv_adProductTitle);
            this.tv_adProductBid = itemView.findViewById(R.id.tv_adProductBid);
            this.tv_adProductPrice = itemView.findViewById(R.id.tv_adProductPrice);
            this.btn_warn = itemView.findViewById(R.id.btn_warn);
            this.btn_adProductDel = itemView.findViewById(R.id.btn_adProductDel);

            btn_adProductDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = getAdapterPosition();
                    abcd = arrayList.get(position).getTitle();

                    databaseReference.orderByChild("title").equalTo(abcd).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot child : snapshot.getChildren()) {
                                key = child.getKey();
                            }

                            snapshot.getRef().child(key).removeValue();
                            Toast.makeText(context, "해당 상품이 삭제되었습니다.",Toast.LENGTH_SHORT).show();
                            // 바뀐 사항 바로 체크 가능
                            Intent intent = new Intent(context, AdminGoods.class);
                            context.startActivity(intent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });

        }
    }

}
