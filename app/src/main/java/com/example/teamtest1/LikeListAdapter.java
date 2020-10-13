package com.example.teamtest1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class LikeListAdapter extends RecyclerView.Adapter <LikeListAdapter.CustomViewHolder3> {

    private ArrayList<Product> arrayList;
    private Context context;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    String test;
    String key;

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
    public void onBindViewHolder(@NonNull final CustomViewHolder3 holder, int position) {

//        Glide.with(holder.itemView)
//                .load(arrayList.get(position).getImage())
//                .into(holder.iv_like_image);
        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference = database.getReference("Product"); // DB 테이블 연동

        test = arrayList.get(position).getUnique();
        databaseReference.orderByChild("unique").equalTo(test).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child : snapshot.getChildren()) {
                    key = child.getKey();
                }

                FirebaseStorage storage = FirebaseStorage.getInstance("gs://teamtest1-6b76d.appspot.com");
                StorageReference storageReference = storage.getReference();
                String path = snapshot.child(key).child("image").getValue().toString();
                storageReference.child(path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
//                        Toast.makeText(context, "성공", Toast.LENGTH_SHORT).show();

                        Glide.with(holder.itemView)
                                .load(uri)
                                .into(holder.iv_like_image);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "실패", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
