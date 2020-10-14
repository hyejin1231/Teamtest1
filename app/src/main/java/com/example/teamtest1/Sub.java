package com.example.teamtest1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import java.util.Date;

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
    String key,key1,unique;

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

        img_btnBackMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent1);
                finish();
            }
        });


        //다혜가 추가한 부분 (10/12)
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String currentUid = user.getUid();

        //클릭 => 관심상품 등록
        img_btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Like 테이블 참조
                databaseReference_like.orderByChild("id").equalTo(currentUid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            key1 = child.getKey();

                        } if (((snapshot.child(key1).child("unique").getValue()).equals(unique)) &&
                                ((snapshot.child(key1).child("id").getValue()).equals(currentUid))) {
                            Toast.makeText(getApplicationContext(), "이미 관심상품으로 등록되어있습니다.", Toast.LENGTH_SHORT).show();
                        }else{
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
        });

        //롱클릭 => 관심상품 등록취소
        //이미지뷰라서 롱클릭 안먹나???
        img_btnLike.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                databaseReference_like.orderByChild("id").equalTo(currentUid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            key1 = child.getKey();

                            if (((snapshot.child(key1).child("unique").getValue()).equals(unique)) &&
                                    ((snapshot.child(key1).child("id").getValue()).equals(currentUid))) {
                                snapshot.getRef().child(key1).removeValue();
                                Toast.makeText(getApplicationContext(), "관심상품 취소", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "관심상품 등록실패", Toast.LENGTH_SHORT).show();
                    }

                });
                return false;
            }
        });


    }
}
