package com.example.teamtest1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CompleteDetailActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference1;
    private ArrayList<Product> arrayList;

    ImageView iv_cd_profile;
    TextView tv_cd_name;
    TextView tv_cd_price;
    TextView tv_cd_buyer;
    TextView tv_cd_seller;
    Button btn_cdel_detail;
    String p_id, p_id_key,unique;
    //Button btn_like;//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_detail);

        database = FirebaseDatabase.getInstance(); //파이어벵스 데이터베이스 연동
        databaseReference = database.getReference("Product");
        databaseReference1 = database.getReference("Like");//

        Intent intent_complete = getIntent();

        iv_cd_profile = findViewById(R.id.iv_cd_profile);
        tv_cd_name = findViewById(R.id.tv_cd_name);
        tv_cd_price = findViewById(R.id.tv_cd_price);
        tv_cd_buyer = findViewById(R.id.tv_cd_buyer);
        tv_cd_seller = findViewById(R.id.tv_cd_seller);
        btn_cdel_detail = findViewById(R.id.btn_cdel_detail);
        //btn_like = findViewById(R.id.btn_like); //


        Glide.with(this).load(intent_complete.getExtras().getString("iv_sd_profile")).override(300,300).into(iv_cd_profile);
        tv_cd_name.setText(intent_complete.getExtras().getString("tv_sd_name"));
        tv_cd_price.setText(intent_complete.getExtras().getString("tv_sd_price"));
        tv_cd_buyer.setText(intent_complete.getExtras().getString("tv_sd_buyer"));
        tv_cd_seller.setText(intent_complete.getExtras().getString("tv_sd_seller"));
        unique = intent_complete.getExtras().getString("unique");


        btn_cdel_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.orderByChild("unique").equalTo(unique).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot child : snapshot.getChildren()) {
                            p_id_key = child.getKey();
                        }

                        snapshot.getRef().child(p_id_key).removeValue();
                        Toast.makeText(getApplicationContext(),"상품이 삭제되었습니다.",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), SellistActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(),"상품 삭제실패",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
}