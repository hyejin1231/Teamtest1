package com.example.teamtest1;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private ArrayList<Product> arrayList;
    private Context context;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    int count;
    String abcd,abcde;
    String key,key1,key2,test;;
    String destinationUID;
    String uniqueTest;
    public CustomAdapter(ArrayList<Product> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, final int position) {
        // 각 아이템들에 대한 매칭
//        Glide.with(holder.itemView)
//                .load(arrayList.get(position).getImage())
//                .into(holder.iv_productImage);

//        Uri uri = Uri.parse(arrayList.get(position).getImage());
////
////        Glide.with(holder.itemView).asBitmap()
////                .load(uri)
////                .into(new SimpleTarget<Bitmap>() {
////                    @Override
////                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
////                        holder.iv_productImage.setImageBitmap(resource);
////                    }
////                });

        holder.tv_productBid.setText(String.valueOf(arrayList.get(position).getBid()));
//        holder.tv_productBid.setText(arrayList.get(position).getBid());
        holder.tv_productTitle.setText(arrayList.get(position).getTitle());
        holder.tv_productPrice.setText(arrayList.get(position).getPrice());
        holder.tv_viewCnt.setText(String.valueOf(arrayList.get(position).getCount()));

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference = database.getReference("Product"); // DB 테이블 연동

        test = arrayList.get(position).getUnique();




        databaseReference.orderByChild("unique").equalTo(test).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child : snapshot.getChildren()) {
                    uniqueTest = child.getKey();
                }
                long now = System.currentTimeMillis();
                Date today = new Date(now);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String date = simpleDateFormat.format(today);
                String deadline = snapshot.child(uniqueTest).child("deadline").getValue().toString();

                String image = arrayList.get(position).getImage();
                FirebaseStorage storage = FirebaseStorage.getInstance("gs://teamtest1-6b76d.appspot.com");
                StorageReference storageReference = storage.getReference();

                String path = snapshot.child(uniqueTest).child("image").getValue().toString();

//                Toast.makeText(context, path, Toast.LENGTH_SHORT).show(); // 실행할 코드
                storageReference.child(path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
//                        Toast.makeText(context, "성공", Toast.LENGTH_SHORT).show();

                        Glide.with(holder.itemView)
                                .load(uri)
                                .into(holder.iv_productImage);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "실패", Toast.LENGTH_SHORT).show();
                    }
                });

                int Caldate = Integer.parseInt(date.replace("-", "")) - Integer.parseInt(deadline.replace("-", ""));


                if (deadline.equals(date)) {
                    holder.tv_alaram.setVisibility(View.VISIBLE);
                }

                if(Caldate > 0 ) {
                    snapshot.getRef().child(uniqueTest).child("status").setValue("complete");
                    holder.btn_bid.setVisibility(View.INVISIBLE);
                    holder.btn_Buynow.setVisibility(View.INVISIBLE);
                    holder.tv_alaram.setVisibility(View.VISIBLE);
                    holder.tv_alaram.setText("판매 종료!!!");
                    holder.tv_alaram.setTextColor(Color.parseColor("#1838EC"));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        holder.tv_productDetail.setText(arrayList.get(position).getDetail());
//        holder.tv_productSeller.setText(arrayList.get(position).getSeller());
//        holder.tv_productDate.setText(arrayList.get(position).getDate());
//        holder.tv_productDeadline.setText(arrayList.get(position).getDeadline());
//        holder.tv_productCategory.setText(arrayList.get(position).getCategory());
//        holder.tv_productStatus.setText(arrayList.get(position).getStatus());

    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0 );
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_productImage;
        TextView tv_productTitle;
        TextView tv_productBid;
        TextView tv_productPrice;
        TextView tv_viewCnt;
        Button btn_bid,btn_Buynow;
        TextView tv_alaram;
//        TextView tv_productSeller;
//        TextView tv_productDate;
//        TextView tv_productDeadline;
//        TextView tv_productDetail;
//        TextView tv_productCategory;
//        TextView tv_productStatus;
        private FirebaseDatabase database;
        private DatabaseReference databaseReference;


        public CustomViewHolder(@NonNull final View itemView) {
            super(itemView);
            this.tv_productTitle = itemView.findViewById(R.id.tv_productTitle);
            this.tv_productPrice = itemView.findViewById(R.id.tv_productPrice);
            this.tv_productBid = itemView.findViewById(R.id.tv_productBid);
            this.iv_productImage = itemView.findViewById(R.id.iv_productImage);
            this.tv_viewCnt = itemView.findViewById(R.id.tv_viewCnt);
            this.btn_bid = itemView.findViewById(R.id.btn_bid);
            this.btn_Buynow = itemView.findViewById(R.id.btn_Buynow);
            this.tv_alaram = itemView.findViewById(R.id.tv_alaram);
//            this.tv_productSeller = itemView.findViewById(R.id.tv_productSeller);
//            this.tv_productDate = itemView.findViewById(R.id.tv_productDate);
//            this.tv_productDeadline = itemView.findViewById(R.id.tv_productDeadline);
//            this.tv_productCategory = itemView.findViewById(R.id.tv_productCategory);
//            this.tv_productStatus = itemView.findViewById(R.id.tv_productStatus);
//            this.tv_productDetail = itemView.findViewById(R.id.tv_productDetail);

            database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
            databaseReference = database.getReference("Product"); // DB 테이블 연동

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    Bitmap bitmap = ((BitmapDrawable)iv_productImage.getDrawable()).getBitmap();
//                    float scale = (float)(1024/(float)bitmap.getWidth());
//                    int image_w = (int) (bitmap.getWidth() * scale);
//                    int image_h = (int) (bitmap.getHeight() * scale);
//                    Bitmap resize = Bitmap.createScaledBitmap(bitmap, image_w, image_h,true);
//
//                    resize.compress(Bitmap.CompressFormat.JPEG,100,stream);
//                    byte[] byteArray = stream.toByteArray();

                    int position = getAdapterPosition();
                    abcd = arrayList.get(position).getUnique();

                    count = arrayList.get(position).getCount() + 1;

                    databaseReference.orderByChild("unique").equalTo(abcd).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot child : snapshot.getChildren()) {
                                key = child.getKey();
                            }

                            snapshot.getRef().child(key).child("count").setValue(count);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    Intent intent = new Intent(v.getContext(), Sub.class);

                    intent.putExtra("unique", arrayList.get(position).getUnique());
                    intent.putExtra("count", String.valueOf(arrayList.get(position).getCount()));
                    intent.putExtra("image", arrayList.get(position).getImage());
                    intent.putExtra("title", arrayList.get(position).getTitle());
                    intent.putExtra("price", arrayList.get(position).getPrice());
//                    intent.putExtra("bid", arrayList.get(position).getBid());
                    intent.putExtra("bid", String.valueOf(arrayList.get(position).getBid()));
                    intent.putExtra("detail", arrayList.get(position).getDetail());

                    v.getContext().startActivity(intent);
                }
            });
            btn_bid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final List<String> ListItems = new ArrayList<>();
                    ListItems.add("500");
                    ListItems.add("1000");
                    ListItems.add("5000");
                    ListItems.add("10000");
                    ListItems.add("50000");
                    final CharSequence[] items =  ListItems.toArray(new String[ ListItems.size()]);

                    final List SelectedItems  = new ArrayList();
                    int defaultItem = 0;
                    SelectedItems.add(defaultItem);

                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                    builder.setTitle("입찰가격을 선택해주세요!!");
                    builder.setSingleChoiceItems(items, defaultItem,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SelectedItems.clear();
                                    SelectedItems.add(which);
                                }
                            });
                    builder.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    int position = getAdapterPosition();
                                    abcde = arrayList.get(position).getUnique();
                                    String selectBid ="";
                                    if (!SelectedItems.isEmpty()) {
                                        int index = (int) SelectedItems.get(0);
                                        selectBid = ListItems.get(index);
                                    }
                                    int intBid = Integer.parseInt(selectBid);

                                    final int attendBid = arrayList.get(position).getBid()+ intBid;
                                    Intent intent = ((Activity)context).getIntent();
                                    final String buyer = intent.getStringExtra("uid");
//                                    final String buyer = arrayList.get(position).getBuyer();
                                    databaseReference.orderByChild("unique").equalTo(abcde).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for(DataSnapshot child : snapshot.getChildren()) {
                                                key1 = child.getKey();
                                            }

                                            // 입찰가격bid이랑 입찰자 (bidder) uid 담기
                                            snapshot.getRef().child(key1).child("bid").setValue(attendBid);
                                            snapshot.getRef().child(key1).child("buyer").setValue(buyer);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
//                                    Intent intent = new Intent(itemView.getContext(), MainActivity.class);
//                                    itemView.getContext().startActivity(intent);
                                    Toast.makeText(itemView.getContext(), "확인 누름", Toast.LENGTH_SHORT).show(); // 실행할 코드

                                    tv_productBid.setText(String.valueOf(attendBid));
                                }
                            });
                    builder.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(itemView.getContext(), "취소 누름", Toast.LENGTH_SHORT).show(); // 실행할 코드
                                }
                            });
                    builder.show();
                }
            });

            btn_Buynow.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    String Seller = arrayList.get(position).getSeller();

                    databaseReference.orderByChild("seller").equalTo(Seller).addListenerForSingleValueEvent(new ValueEventListener(){
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot child : snapshot.getChildren()){
                                key2 = child.getKey();
                            }
                            assert key2 != null;
                            destinationUID = snapshot.child(key2).child("seller").getValue().toString();


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    Intent intent = new Intent(view.getContext(),MessageActivity.class);
                    intent.putExtra("destinationUID",destinationUID);
                    view.getContext().startActivity(intent);

                }
            });



        }
    }
}

