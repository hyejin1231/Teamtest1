package com.example.teamtest1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Sub extends AppCompatActivity {

    ImageView tv_image;
    TextView tv_title;
    TextView tv_bid;
    TextView tv_price;
    TextView edit_detail;
    TextView tv_count;
    ImageView img_btnBackMain,img_btnLike;
    Button btn_SubBtn,btn_price;
    TextView tv_subDate,tv_subDeadline,tv_subAlarm;
    String key,key1,key2,unique,unique1;
    String key3;
    String Message;
    String destinationUID;
    String nowBid;
    int nowBidInt;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final String currentUid = user.getUid();

    private FirebaseDatabase database;
    private DatabaseReference databaseReference,databaseReference_like;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);


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

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Product");
        databaseReference_like = database.getReference("Like");

        Intent intent = getIntent();
        unique = intent.getExtras().getString("unique");
        nowBid = intent.getExtras().getString("bid");

        Glide.with(this).asBitmap()
                .load(intent.getExtras().getString("image"))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        tv_image.setImageBitmap(resource);
                    }
                });


//        Glide.with(this).load(intent.getExtras().getString("image")).into(tv_image);

        tv_count.setText(intent.getExtras().getString("count"));
        tv_title.setText(intent.getExtras().getString("title"));
        tv_bid.setText(intent.getExtras().getString("bid"));
        tv_price.setText(intent.getExtras().getString("price"));
        edit_detail.setText(intent.getExtras().getString("detail"));

        databaseReference.orderByChild("unique").equalTo(unique).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    key = child.getKey();
                }

                tv_subDate.setText(snapshot.child(key).child("date").getValue().toString());
                tv_subDeadline.setText(snapshot.child(key).child("deadline").getValue().toString());

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

        btn_SubBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                String selectBid ="";
                                if (!SelectedItems.isEmpty()) {
                                    int index = (int) SelectedItems.get(0);
                                    selectBid = ListItems.get(index);
                                }
                                int intBid = Integer.parseInt(selectBid); // 선택한 값 숫자로 변환
                                nowBidInt = Integer.parseInt(nowBid);
                               final int attendBid = intBid + nowBidInt;
//                                currentUid

                                databaseReference.orderByChild("unique").equalTo(unique).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot child: snapshot.getChildren()) {
                                            key3 = child.getKey();
                                        }

//                                        Toast.makeText(Sub.this,  key3, Toast.LENGTH_SHORT).show(); // 실행할 코드
                                        snapshot.getRef().child(key3).child("bid").setValue(attendBid);
                                        snapshot.getRef().child(key3).child("bidder").setValue(currentUid);


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
//                                    Intent intent = new Intent(itemView.getContext(), MainActivity.class);
//                                    itemView.getContext().startActivity(intent);
//                                Toast.makeText(Sub.this, attendBid, Toast.LENGTH_SHORT).show(); // 실행할 코드

                                tv_bid.setText(String.valueOf(attendBid));
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
        });

        img_btnBackMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent1);
                finish();
            }
        });


        //다혜가 추가한 부분 (10/12) 다혜야 밑에 두 주석 위로 올릴게
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        final String currentUid = user.getUid();

        //클릭 => 관심상품 등록
        img_btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //***이건 잘 들어오는거 보면 id랑 unique의 문제는 아님
                //Like like_add = new Like(currentUid, unique);
                //databaseReference_like.push().setValue(like_add);
                //Toast.makeText(getApplicationContext(), "관심상품 등록", Toast.LENGTH_SHORT).show();

                //Like 테이블 참조.orderByChild("id").equalTo(currentUid)
                databaseReference_like.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            key1 = child.getKey();
                        }if (((snapshot.child(key1).child("unique").getValue()).equals(unique)) && ((snapshot.child(key1).child("id").getValue()).equals(currentUid))) {
                            Toast.makeText(getApplicationContext(), "이미 관심상품으로 등록되어있습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Like like_add = new Like(currentUid, unique);
                            databaseReference_like.push().setValue(like_add);
                            Toast.makeText(getApplicationContext(), "관심상품 등록", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "관심상품 등록실패", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }); //권이 추가 부분 상세구매에서 채팅 연동 완료. uid 정상적 출력, for문 잘못 달아서 여태 오류 뜬 것 으로 추정
        btn_price.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                databaseReference.orderByChild("unique").equalTo(unique).addListenerForSingleValueEvent(new ValueEventListener(){
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot child : snapshot.getChildren()){
                            Message = child.getKey();
                            destinationUID = snapshot.child(Message).child("seller").getValue().toString();
                        }
                        Toast.makeText(getApplicationContext(), destinationUID, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
                        intent.putExtra("destinationUID",destinationUID);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        img_btnLike.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                databaseReference_like.orderByChild("id").equalTo(currentUid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            key2 = child.getKey();

                            if (((snapshot.child(key2).child("unique").getValue()).equals(unique)) &&
                                    ((snapshot.child(key2).child("id").getValue()).equals(currentUid))) {
                                snapshot.getRef().child(key2).removeValue();
                                Toast.makeText(getApplicationContext(), "관심상품 취소", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //Toast.makeText(getApplicationContext(), "관심상품 등록실패", Toast.LENGTH_SHORT).show();
                    }

                });
                return false;
            }
        });


    }
}
