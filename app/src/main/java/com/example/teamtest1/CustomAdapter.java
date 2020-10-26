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
import android.widget.ArrayAdapter;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    ArrayAdapter<String> spinner_arrayAdapter;



    int count;
    String abcd,abcde;
    String key,key1,key2,test;
    String currentSeller;
    String destinationUID;
    String uniqueTest;
    String btnLike;
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

        holder.tv_productBid.setText(String.valueOf(arrayList.get(position).getBid()) + "원");
//        holder.tv_productBid.setText(arrayList.get(position).getBid());
        holder.tv_productTitle.setText(arrayList.get(position).getTitle());
        holder.tv_productPrice.setText(arrayList.get(position).getPrice() + "원");
        holder.tv_viewCnt.setText(String.valueOf(arrayList.get(position).getCount()));
        holder.tv_productBidCount.setText(arrayList.get(position).getBidCount() +"명 참여");

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference = database.getReference("Product"); // DB 테이블 연동


        test = arrayList.get(position).getUnique();
        final FirebaseStorage storage = FirebaseStorage.getInstance("gs://teamtest1-6b76d.appspot.com");
        final StorageReference storageReference = storage.getReference();



        databaseReference.orderByChild("unique").equalTo(test).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    uniqueTest = child.getKey();

                    long now = System.currentTimeMillis();
                    Date today = new Date(now);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String date = simpleDateFormat.format(today);
                    String deadline = (String) snapshot.child(uniqueTest).child("deadline").getValue();

                    String image = arrayList.get(position).getImage();


                    //String path = (String)snapshot.child(uniqueTest).child("image").getValue();
                    String path = (String) snapshot.child(uniqueTest).child("image").getValue();

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

                    if (Caldate > 0) {
                        snapshot.getRef().child(uniqueTest).child("status").setValue("due");
                        holder.btn_bid.setVisibility(View.INVISIBLE);
                        holder.btn_Buynow.setVisibility(View.INVISIBLE);
                        holder.tv_alaram.setVisibility(View.VISIBLE);
                        holder.tv_alaram.setText("판매 종료");
                        holder.tv_alaram.setTextColor(Color.parseColor("#1838EC"));
                    }

                    if (snapshot.child(uniqueTest).child("status").getValue().equals("complete")) {
                        holder.btn_bid.setVisibility(View.INVISIBLE);
                        holder.btn_Buynow.setVisibility(View.INVISIBLE);
                        holder.tv_alaram.setVisibility(View.VISIBLE);
                        holder.tv_alaram.setText("판매 종료");
                        holder.tv_alaram.setTextColor(Color.parseColor("#1838EC"));
                    }

                    if (snapshot.child(uniqueTest).child("status").getValue().equals("selling")) {
                        holder.btn_bid.setVisibility(View.VISIBLE);
                        holder.btn_Buynow.setVisibility(View.VISIBLE);
                        holder.tv_alaram.setVisibility(View.VISIBLE);
                        holder.tv_alaram.setText("판매 중");
                        holder.tv_alaram.setTextColor(Color.parseColor("#FF0000"));
                    }

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
        TextView tv_alaram,tv_productBidCount;
//        TextView tv_productSeller;
//        TextView tv_productDate;
//        TextView tv_productDeadline;
//        TextView tv_productDetail;
//        TextView tv_productCategory;
//        TextView tv_productStatus;
        private FirebaseDatabase database;
        private DatabaseReference databaseReference,databaseReference_like;


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUid = user.getUid();

        public CustomViewHolder(@NonNull final View itemView) {
            super(itemView);
            this.tv_productBidCount = itemView.findViewById(R.id.tv_productBidCount);
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
            databaseReference_like = database.getReference("Like");

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
                    intent.putExtra("bid", String.valueOf(arrayList.get(position).getBid()));
                    intent.putExtra("detail", arrayList.get(position).getDetail());
                    intent.putExtra("btnLike",btnLike);

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    itemView.getContext().startActivity(intent);


                }
            });
            btn_bid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (arrayList.get(getAdapterPosition()).getSeller().equals(currentUid)) {
                        Toast.makeText(itemView.getContext(), "본인 상품 입찰 불가 ", Toast.LENGTH_SHORT).show(); // 실행할 코드
                    } else {

//                        Toast.makeText(itemView.getContext(), " 입찰 ok ", Toast.LENGTH_SHORT).show();

                        final List<String> ListItems = new ArrayList<>();
                        ListItems.add("500원");
                        ListItems.add("1000원");
                        ListItems.add("5000원");
                        ListItems.add("10000원");
                        ListItems.add("50000원");
                        final CharSequence[] items = ListItems.toArray(new String[ListItems.size()]);

                        final List SelectedItems = new ArrayList();
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
                                        String selectBid = "";
                                        if (!SelectedItems.isEmpty()) {
                                            int index = (int) SelectedItems.get(0);
                                            selectBid = ListItems.get(index);
                                        }
                                        int intBid = Integer.parseInt(selectBid.replace("원", ""));

                                        final int attendBid = arrayList.get(position).getBid() + intBid;
                                        Intent intent = ((Activity) context).getIntent();
                                        final String bidder = FirebaseAuth.getInstance().getUid();
                                        //final String bidder = intent.getStringExtra("uid");
//                                    final String buyer = arrayList.get(position).getBuyer();
                                        databaseReference.orderByChild("unique").equalTo(abcde).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot child : snapshot.getChildren()) {
                                                    key1 = child.getKey();
                                                }
                                                String bidCountString = snapshot.child(key1).child("bidCount").getValue().toString();
                                                int bidCount = Integer.parseInt(bidCountString);
                                                int position = getAdapterPosition();
//                                            String presentBidder = arrayList.get(position).getBidder();
                                                String presentBidder = snapshot.child(key1).child("bidder").getValue().toString();
//                                            int bidCountUpdate = arrayList.get(position).getBidCount();

                                                if (presentBidder.equals(bidder)) {

                                                } else {
                                                    bidCount++;
                                                }
                                                // 입찰가격bid이랑 입찰자 (bidder) uid 담기
                                                snapshot.getRef().child(key1).child("bid").setValue(attendBid);
                                                snapshot.getRef().child(key1).child("bidder").setValue(bidder);
                                                snapshot.getRef().child(key1).child("bidCount").setValue(bidCount);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                        Toast.makeText(itemView.getContext(), "확인 누름", Toast.LENGTH_SHORT).show(); // 실행할 코드

                                        tv_productBid.setText(String.valueOf(attendBid) + "원");

                                        String presentBidder = arrayList.get(position).getBidder();
                                        int bidCountUpdate = arrayList.get(position).getBidCount();
                                        if (presentBidder.equals(bidder)) {
                                            tv_productBidCount.setText(bidCountUpdate + "명 참여");
                                        } else {
                                            bidCountUpdate++;
//                                            Toast.makeText(itemView.getContext(), bidder, Toast.LENGTH_SHORT).show(); // 실행할 코드
                                            tv_productBidCount.setText(bidCountUpdate + "명 참여");
                                        }

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
                }
            });



                btn_Buynow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        int position = getAdapterPosition();
                        String Seller = arrayList.get(position).getSeller();

                        if (arrayList.get(getAdapterPosition()).getSeller().equals(currentUid)) {
                            Toast.makeText(itemView.getContext(), "본인 물품 구매 불가", Toast.LENGTH_SHORT).show(); // 실행할 코드
                        }else {

                            databaseReference.orderByChild("seller").equalTo(Seller).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot child : snapshot.getChildren()) {
                                        key2 = child.getKey();
                                        destinationUID = snapshot.child(key2).child("seller").getValue().toString();
//                                Toast.makeText(view.getContext(),destinationUID,Toast.LENGTH_SHORT).show();
                                    }
                                    assert key2 != null;
                                    Intent intent = new Intent(view.getContext(), MessageActivity.class);
                                    intent.putExtra("destinationUID", destinationUID);
                                    view.getContext().startActivity(intent);


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                    }
                });




        }
    }
}

