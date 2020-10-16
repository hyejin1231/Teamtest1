package com.example.teamtest1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.Date;

public class DeadlineCompleteActivity extends AppCompatActivity {

    TextView tv_dead_title,tv_dead_seller,tv_dead_bidder,tv_dead_bid,tv_dead_deadline;
    Button btn_dead_chat,btn_dead_update,btn_dead_selDate;
    ImageView img_dead_profile;
    String unique;
    String key,key1;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private DatePickerDialog.OnDateSetListener callbackMethod;

    long now = System.currentTimeMillis();
    Date today = new Date(now);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat format = new SimpleDateFormat("yyyy");
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
    SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd");
    String date = simpleDateFormat.format(today);
    int year = Integer.parseInt(format.format(today));
    int month  = Integer.parseInt(dateFormat.format(today));
    int day = Integer.parseInt(dateFormat2.format(today));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deadline_complete);

        tv_dead_bid = findViewById(R.id.tv_dead_bid);
        tv_dead_bidder = findViewById(R.id.tv_dead_bidder);
        tv_dead_deadline = findViewById(R.id.tv_dead_deadline);
        tv_dead_seller = findViewById(R.id.tv_dead_seller);
        tv_dead_title = findViewById(R.id.tv_dead_title);

        btn_dead_chat = findViewById(R.id.btn_dead_chat);
        btn_dead_update = findViewById(R.id.btn_dead_update);
        btn_dead_selDate = findViewById(R.id.btn_dead_selDate);

        img_dead_profile = findViewById(R.id.img_dead_profile);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Product");

        Intent intent_deadline = getIntent();

        this.InitializeListener();
        unique = intent_deadline.getExtras().getString("unique");

        databaseReference.orderByChild("unique").equalTo(unique).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child: snapshot.getChildren()) {
                    key = child.getKey();
                }

                FirebaseStorage storage = FirebaseStorage.getInstance("gs://teamtest1-6b76d.appspot.com");
                StorageReference storageReference = storage.getReference();
                String path = snapshot.child(key).child("image").getValue().toString();
                storageReference.child(path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
//                        Toast.makeText(context, "성공", Toast.LENGTH_SHORT).show();

                        Glide.with(getApplicationContext())
                                .load(uri)
                                .override(300,300)
                                .into(img_dead_profile);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        tv_dead_title.setText(intent_deadline.getExtras().getString("tv_sd_name"));
        tv_dead_seller.setText(intent_deadline.getExtras().getString("tv_sd_seller"));
        tv_dead_deadline.setText(intent_deadline.getExtras().getString("deadline"));
        tv_dead_bidder.setText(intent_deadline.getExtras().getString("tv_sd_bidder"));
        tv_dead_bid.setText(intent_deadline.getExtras().getString("bid"));

        String bidder = intent_deadline.getExtras().getString("tv_sd_bidder");

        if (bidder.equals("")) {
            tv_dead_bidder.setText("입찰자가 없습니다.");
            btn_dead_selDate.setVisibility(View.VISIBLE);
            btn_dead_update.setVisibility(View.VISIBLE);
        }


        // 입찰자가 한명도 없을 경우 마감일 늘려서 판매 중으로 바꾸고 위한 마감일 변경
        btn_dead_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.orderByChild("unique").equalTo(unique).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child: snapshot.getChildren()){
                            key1 = child.getKey();
                        }

                        String updateDead = tv_dead_deadline.getText().toString();

                        snapshot.getRef().child(key1).child("deadline").setValue(updateDead);
                        snapshot.getRef().child(key1).child("status").setValue("selling");
                        Toast.makeText(getApplicationContext(), "마감일이 수정되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), SellistActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


        // 권이가 채팅 연결할 부분
        //  tv_dead_seller와 tv_dead_bidder 두개 연결하면 됨
        btn_dead_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }
    public void OnClickHandler(View view)
    {
        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, year , month, day);

        dialog.show();
    }


    public void InitializeListener() {
        callbackMethod = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                tv_dead_deadline.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            }
        };
    }
}


























