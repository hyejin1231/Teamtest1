package com.example.teamtest1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
    private DatabaseReference databaseReference,databaseReference_User;

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
        databaseReference_User = database.getReference("User");

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
//        holder.tv_like_title.setText("제품명 " + arrayList.get(position).getTitle());
//        holder.tv_like_price.setText("가격 " + String.valueOf(arrayList.get(position).getPrice()) + "원");
//        holder.tv_like_seller.setText("판매자 " + arrayList.get(position).getSeller());
//        holder.tv_like_buyer.setText("구매자 " + arrayList.get(position).getBuyer());

        holder.tv_like_title.setText("제품명 " + arrayList.get(position).getTitle());
        holder.tv_like_price.setText("가격 " + String.valueOf(arrayList.get(position).getPrice()) + "원");
        holder.tv_like_buyer.setText("구매자 " + arrayList.get(position).getBuyer());
        holder.tv_like_deadline.setText("마감일 " + arrayList.get(position).getDeadline());

        final String seller = arrayList.get(position).getSeller();
        String status = arrayList.get(position).getStatus();
        final String bidder = arrayList.get(position).getBidder();

        if (status.equals("selling")){
            holder.tv_like_status.setText("판매 중!!");
            holder.tv_like_status.setTextColor(Color.parseColor("#EC1313"));
        }else if(status.equals("complete")) {
            holder.tv_like_status.setText("판매 종료!!");
            holder.tv_like_status.setTextColor(Color.parseColor("#1838EC"));
        } else if(status.equals("due")) {
            if (bidder.equals("")) {
                holder.tv_like_status.setText("판매기간 종료!");
                holder.tv_like_buyer.setText("입찰자 없음");
                holder.tv_like_status.setTextColor(Color.parseColor("#BE151414"));
            }else {
                holder.tv_like_status.setText("낙찰자와 채팅필요!");
                databaseReference_User.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        holder.tv_like_buyer.setText("입찰자 " + snapshot.child(bidder).child("id").getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                holder.tv_like_status.setTextColor(Color.parseColor("#4CAF50"));
            }
        }

        databaseReference_User.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.tv_like_seller.setText("판매자 " + snapshot.child(seller).child("id").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
        TextView tv_like_deadline;
        TextView tv_like_status;

        public CustomViewHolder3(@NonNull View itemView) {
            super(itemView);

            this.iv_like_image = itemView.findViewById(R.id.iv_like_image);
            this.tv_like_title = itemView.findViewById(R.id.tv_like_title);
            this.tv_like_price = itemView.findViewById(R.id.tv_like_price);
            this.tv_like_seller = itemView.findViewById(R.id.tv_like_seller);
            this.tv_like_buyer = itemView.findViewById(R.id.tv_like_buyer);
            this.tv_like_deadline = itemView.findViewById(R.id.tv_like_deadline);
            this.tv_like_status = itemView.findViewById(R.id.tv_like_status);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

//                    Intent intent = new Intent(view.getContext(), LikelistActivity.class);
//                    intent.putExtra("unique", arrayList.get(position).getUnique());


                    //상품이 판매중인 경우
                    if((arrayList.get(position).getStatus()).equals("selling")){
                        Intent intent_selling = new Intent(view.getContext(),SellingDetailActivity.class);

                        intent_selling.putExtra("iv_sd_profile",arrayList.get(position).getImage());//
                        intent_selling.putExtra("tv_sd_price", String.valueOf(arrayList.get(position).getPrice()));
                        intent_selling.putExtra("tv_sd_seller", arrayList.get(position).getSeller());
                        intent_selling.putExtra("tv_sd_buyer", arrayList.get(position).getBuyer());
                        intent_selling.putExtra("tv_sd_name", arrayList.get(position).getTitle());
                        intent_selling.putExtra("unique", arrayList.get(position).getUnique());
                        intent_selling.putExtra("deadline", arrayList.get(position).getDeadline());
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
                        intent_complete.putExtra("deadline", arrayList.get(position).getDeadline());

                        view.getContext().startActivity(intent_complete);
                    }
                    else if((arrayList.get(position).getStatus()).equals("due")) {
                        Intent intent_deadline = new Intent(view.getContext(), DeadlineCompleteActivity.class);
                        intent_deadline.putExtra("iv_sd_profile",arrayList.get(position).getImage());//
                        intent_deadline.putExtra("tv_sd_seller", arrayList.get(position).getSeller());
                        intent_deadline.putExtra("tv_sd_name", arrayList.get(position).getTitle());
                        intent_deadline.putExtra("unique",arrayList.get(position).getUnique());
                        intent_deadline.putExtra("tv_sd_bidder",arrayList.get(position).getBidder());
                        intent_deadline.putExtra("bid", String.valueOf(arrayList.get(position).getBid()));
                        intent_deadline.putExtra("deadline", arrayList.get(position).getDeadline());
                        view.getContext().startActivity(intent_deadline);
                    }
                }
            });

        }
    }
}
