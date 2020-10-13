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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductAdapter.CustomViewHolder> {

    private ArrayList<Product> arrayList;
    private Context context;
    String key;
    String abcd,test;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

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

//        Uri uri1 = Uri.parse(arrayList.get(position).getImage());
//
//
//        Glide.with(holder.itemView).asBitmap()
//                .load(uri1)
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
//                        holder.iv_adProductImage.setImageBitmap(resource);
//                    }
//                });

//                Glide.with(holder.itemView)
//                .load(arrayList.get(position).getImage())
//                .into(holder.iv_adProductImage);

        holder.tv_adProductTitle.setText(arrayList.get(position).getTitle());
        holder.tv_adProductPrice.setText(arrayList.get(position).getPrice());
//        holder.tv_adProductBid.setText(arrayList.get(position).getBid());
        holder.tv_adProductBid.setText(String.valueOf(arrayList.get(position).getBid()));

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference = database.getReference("Product"); // DB 테이블 연동

        test = arrayList.get(position).getUnique();

        databaseReference.orderByChild("unique").equalTo(test).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child : snapshot.getChildren()) {
                    key = child.getKey();
                }
                long now = System.currentTimeMillis();
                Date today = new Date(now);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String date = simpleDateFormat.format(today);
                String deadline = snapshot.child(key).child("deadline").getValue().toString();


                if (deadline.equals(date)) {
                    holder.tv_adAlarmDead.setVisibility(View.VISIBLE);
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
                                .into(holder.iv_adProductImage);
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
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0 );
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView tv_adProductTitle,tv_adProductBid,tv_adProductPrice,tv_adAlarmDead;
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
            this.tv_adAlarmDead = itemView.findViewById(R.id.tv_adAlarmDead);

            btn_adProductDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = getAdapterPosition();
                    abcd = arrayList.get(position).getUnique();

                    databaseReference.orderByChild("unique").equalTo(abcd).addListenerForSingleValueEvent(new ValueEventListener() {
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
