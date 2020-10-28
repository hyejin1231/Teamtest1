package com.example.teamtest1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Sub extends AppCompatActivity {

    ImageView img_sub_diss,img_sub_good,img_sub_soso,img_sub_angry,img_sub_smile;
    ImageView tv_image;
    TextView tv_title;
    TextView tv_bid;
    TextView tv_price;
    TextView edit_detail;
    TextView tv_count;
    TextView tv_category;
    TextView tv_bidCount;
    ImageView img_btnBackMain,img_btnLike,img_btnLike2;


    Button btn_SubBtn,btn_price,btn_sub_infoGo;
    TextView tv_subDate,tv_subDeadline,tv_subAlarm, tv_sub_sellerInfo;;
    String key,key1,key2,unique,unique1;
    String key3;
    String Message;
    String destinationUID;
    String nowBid;
    String seller;
    int nowBidInt;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final String currentUid = user.getUid();
    int like_check=1;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference,databaseReference_like,databaseReference_User;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        tv_bidCount = findViewById(R.id.tv_bidCount);
        tv_sub_sellerInfo = findViewById(R.id.tv_sub_sellerInfo);
        tv_category = findViewById(R.id.tv_category);
        tv_image = findViewById(R.id.tv_image);
        tv_title = findViewById(R.id.tv_title);
        tv_bid = findViewById(R.id.tv_bid);
        tv_price = findViewById(R.id.tv_price);
        edit_detail = findViewById(R.id.edit_detail);
        tv_count = findViewById(R.id.tv_count);
        img_btnBackMain = findViewById(R.id.img_btnBackMain);
        btn_SubBtn  = findViewById(R.id.btn_SubBtn);
        btn_price = findViewById(R.id.btn_price);
        tv_subDeadline = findViewById(R.id.tv_subDeadline);
        tv_subDate = findViewById(R.id.tv_subDate);
        tv_subAlarm=findViewById(R.id.tv_subAlarm);
        img_btnLike = findViewById(R.id.img_btnLike);
        //img_btnLike2 = findViewById(R.id.img_btnLike2);
        btn_sub_infoGo = findViewById(R.id.btn_sub_infoGo);

        img_sub_smile = findViewById(R.id.img_sub_smile);
        img_sub_angry = findViewById(R.id.img_sub_angry);
        img_sub_diss = findViewById(R.id.img_sub_diss);
        img_sub_good = findViewById(R.id.img_sub_good);
        img_sub_soso = findViewById(R.id.img_sub_soso);


        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Product");
        databaseReference_User = database.getReference("User");
        databaseReference_like = database.getReference("Like");

        Intent intent = getIntent();
        unique = intent.getExtras().getString("unique");
//        nowBid = intent.getExtras().getString("bid");

        Glide.with(this).asBitmap()
                .load(intent.getExtras().getString("image"))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        tv_image.setImageBitmap(resource);
                    }
                });


//        Glide.with(this).load(intent.getExtras().getString("image")).into(tv_image);
//        tv_bid.setText(intent.getExtras().getString("bid"));

        tv_count.setText(intent.getExtras().getString("count"));
//        tv_title.setText(intent.getExtras().getString("title"));
//        tv_price.setText(intent.getExtras().getString("price"));
//        edit_detail.setText(intent.getExtras().getString("detail"));



        databaseReference.orderByChild("unique").equalTo(unique).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    key = child.getKey();
                }
                tv_price.setText(snapshot.child(key).child("price").getValue().toString());
//                tv_count.setText(snapshot.child(key).child("count").getValue().toString());
                tv_title.setText(snapshot.child(key).child("title").getValue().toString());
                edit_detail.setText(snapshot.child(key).child("detail").getValue().toString());
                tv_bid.setText(snapshot.child(key).child("bid").getValue().toString());
                tv_bidCount.setText(snapshot.child(key).child("bidCount").getValue().toString()+"명 참여");
                tv_subDate.setText(snapshot.child(key).child("date").getValue().toString());
                tv_subDeadline.setText(snapshot.child(key).child("deadline").getValue().toString());
                tv_category.setText(snapshot.child(key).child("category").getValue().toString());
                seller = snapshot.child(key).child("seller").getValue().toString();

                nowBid = snapshot.child(key).child("bid").getValue().toString();

                FirebaseStorage storage = FirebaseStorage.getInstance("gs://teamtest1-6b76d.appspot.com");
                StorageReference storageReference = storage.getReference();

                String path = snapshot.child(key).child("image").getValue().toString();

                storageReference.child(path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
//                        Toast.makeText(context, "성공", Toast.LENGTH_SHORT).show();

                        Glide.with(getApplicationContext())
                                .load(uri)
                                .into(tv_image);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
                    }
                });

                long now = System.currentTimeMillis();
                Date today = new Date(now);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String date = simpleDateFormat.format(today);

                String deadline = snapshot.child(key).child("deadline").getValue().toString();

                int Caldate = Integer.parseInt(date.replace("-", "")) - Integer.parseInt(deadline.replace("-", ""));

                if(deadline.equals(date)) {
                    tv_subAlarm.setVisibility(View.VISIBLE);
                }
                if(Caldate > 0) {
                    btn_SubBtn.setVisibility(View.INVISIBLE);
                    btn_price.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference_User.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tv_sub_sellerInfo.setText(snapshot.child(seller).child("id").getValue().toString());

                int estimate = Integer.parseInt(snapshot.child(seller).child("estimate").getValue().toString());

                if (estimate >= 0 && estimate <= 20) {
                    img_sub_angry.setVisibility(View.VISIBLE);

                    img_sub_smile.setVisibility(View.INVISIBLE);
                    img_sub_good.setVisibility(View.INVISIBLE);
                    img_sub_soso.setVisibility(View.INVISIBLE);
                    img_sub_diss.setVisibility(View.INVISIBLE);
                }else if(estimate > 20 && estimate <= 40) {
                    img_sub_diss.setVisibility(View.VISIBLE);

                    img_sub_angry.setVisibility(View.INVISIBLE);
                    img_sub_smile.setVisibility(View.INVISIBLE);
                    img_sub_good.setVisibility(View.INVISIBLE);
                    img_sub_soso.setVisibility(View.INVISIBLE);
                }else if(estimate > 40 && estimate <= 60){
                    img_sub_soso.setVisibility(View.VISIBLE);

                    img_sub_diss.setVisibility(View.INVISIBLE);
                    img_sub_angry.setVisibility(View.INVISIBLE);
                    img_sub_smile.setVisibility(View.INVISIBLE);
                    img_sub_good.setVisibility(View.INVISIBLE);
                }else if(estimate > 60 && estimate <= 80) {
                    img_sub_smile.setVisibility(View.VISIBLE);

                    img_sub_diss.setVisibility(View.INVISIBLE);
                    img_sub_angry.setVisibility(View.INVISIBLE);
                    img_sub_good.setVisibility(View.INVISIBLE);
                    img_sub_soso.setVisibility(View.INVISIBLE);
                }else {
                    img_sub_good.setVisibility(View.VISIBLE);

                    img_sub_smile.setVisibility(View.INVISIBLE);
                    img_sub_soso.setVisibility(View.INVISIBLE);
                    img_sub_diss.setVisibility(View.INVISIBLE);
                    img_sub_angry.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_sub_infoGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SubSellerInfo.class);
                intent.putExtra("seller", seller);
                startActivity(intent);
            }
        });


        btn_SubBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (seller.equals(currentUid)) {
                    Toast.makeText(getApplicationContext(), "본인 상품 입찰 불가", Toast.LENGTH_SHORT).show();
                } else {
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(Sub.this);
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
                                    String selectBid = "";
                                    if (!SelectedItems.isEmpty()) {
                                        int index = (int) SelectedItems.get(0);
                                        selectBid = ListItems.get(index);
                                    }
                                    int intBid = Integer.parseInt(selectBid.replace("원", "")); // 선택한 값 숫자로 변환
                                    nowBidInt = Integer.parseInt(nowBid);
                                    final int attendBid = intBid + nowBidInt;
//                                currentUid

                                    databaseReference.orderByChild("unique").equalTo(unique).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot child : snapshot.getChildren()) {
                                                key3 = child.getKey();
                                            }
                                            String bidCountString = snapshot.child(key3).child("bidCount").getValue().toString();
                                            int bidCount = Integer.parseInt(bidCountString);

                                            String presentBidder = snapshot.child(key3).child("bidder").getValue().toString();

                                            if (presentBidder.equals(currentUid)) {

                                            } else {
                                                bidCount++;
                                            }

//                                        Toast.makeText(Sub.this,  key3, Toast.LENGTH_SHORT).show(); // 실행할 코드
                                            snapshot.getRef().child(key3).child("bid").setValue(attendBid);
                                            snapshot.getRef().child(key3).child("bidder").setValue(currentUid);
                                            snapshot.getRef().child(key3).child("bidCount").setValue(bidCount);


                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
//                                    Intent intent = new Intent(itemView.getContext(), MainActivity.class);
//                                    itemView.getContext().startActivity(intent);
//                                Toast.makeText(Sub.this, attendBid, Toast.LENGTH_SHORT).show(); // 실행할 코드

                                    Toast.makeText(getApplicationContext(), "확인 누름", Toast.LENGTH_SHORT).show(); // 실행할 코드


                                    databaseReference.orderByChild("unique").equalTo(unique).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot child : snapshot.getChildren()) {
                                                key3 = child.getKey();
                                            }


                                            String presentBidder = snapshot.child(key3).child("bidder").getValue().toString();

                                            tv_bid.setText(String.valueOf(attendBid) + "원");

                                            int bidCountUpdate = Integer.parseInt(tv_bidCount.getText().toString().replace("명 참여", ""));

                                            if (presentBidder.equals(currentUid)) {
                                                tv_bidCount.setText(bidCountUpdate + "명 참여");
                                            } else {
                                                bidCountUpdate++;
                                                tv_bidCount.setText(bidCountUpdate + "명 참여");
                                            }


                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }
                            });

                    builder.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(Sub.this, "취소 누름", Toast.LENGTH_SHORT).show(); // 실행할 코드
                                }
                            });
                    builder.show();
                }
            }
        });

        img_btnBackMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent1);
                finish();
               // moveTaskToBack(false);
            }
        });


        databaseReference_like.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    key = child.getKey();

                    if (snapshot.child(key).child("id").getValue().equals(currentUid)
                            && snapshot.child(key).child("unique").getValue().equals(unique)) {
                       like_check *=0;
                    }
                    //안존재하면...
                    else {
                        like_check *=1;
                    }
                }

                    //이미 관심리스트에 존재하면...
                    if (like_check==0) {
                       img_btnLike.setImageResource(R.drawable.fullheart);
                    }
                    //안존재하면...
                    else if(like_check==1) {
                        img_btnLike.setImageResource(R.drawable.emptyheart);
                    }
                }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "관심상품 등록실패", Toast.LENGTH_SHORT).show();
            }
        });


        //클릭 => 관심상품 등록
        img_btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference_like.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            key1 = child.getKey();

                            if (like_check==0) {
                                Toast.makeText(getApplicationContext(), "이미 관심상품으로 등록되어있습니다.", Toast.LENGTH_SHORT).show();
                            }
                            else if(like_check==1) {
                            Like like_add = new Like(currentUid, unique);
                            databaseReference_like.push().setValue(like_add);
                            Toast.makeText(getApplicationContext(), "관심상품 등록", Toast.LENGTH_SHORT).show();
                            img_btnLike.setImageResource(R.drawable.fullheart);
                            like_check=0;
                        }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "관심상품 등록실패", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        img_btnLike.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                databaseReference_like.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            key2 = child.getKey();

                            if (((snapshot.child(key2).child("unique").getValue()).equals(unique)) &&
                                    ((snapshot.child(key2).child("id").getValue()).equals(currentUid))) {
                                snapshot.getRef().child(key2).removeValue();
                                Toast.makeText(getApplicationContext(), "관심상품 취소", Toast.LENGTH_SHORT).show();
                                img_btnLike.setImageResource(R.drawable.emptyheart);
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }

                });
                return false;
            }
        });



        //권이 추 가 부분 상세구매에서 채팅 연동 완료. uid 정상적 출력, for문 잘못 달아서 여태 오류 뜬 것 으로 추정
        btn_price.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                if (seller.equals(currentUid)) {
                    Toast.makeText(getApplicationContext(), "본인 물품 구매 불가 ", Toast.LENGTH_SHORT).show(); // 실행할 코드
                }else {
                    databaseReference.orderByChild("unique").equalTo(unique).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot child : snapshot.getChildren()) {
                                Message = child.getKey();
                                destinationUID = snapshot.child(Message).child("seller").getValue().toString();
                            }
                            Toast.makeText(getApplicationContext(), destinationUID, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
                            intent.putExtra("destinationUID", destinationUID);
                            startActivity(intent);
                            finish();
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
