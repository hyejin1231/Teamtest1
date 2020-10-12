package com.example.teamtest1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class SellingDetailActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ArrayList<Product> arrayList;

    ImageView iv_sd_profile;
    TextView tv_sd_name;
    TextView tv_sd_price;
    TextView tv_sd_buyer;
    TextView tv_sd_seller;
    Button btn_sdel_detail, btn_update_detail;
    String p_id, p_id_key;
    EditText et_sd_name;
    String abcd,unique;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling_detail);


        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Product");

        Intent intent_selling = getIntent();

        iv_sd_profile = findViewById(R.id.iv_sd_profile);
        //tv_sd_name = findViewById(R.id.tv_sd_name);
        tv_sd_price = findViewById(R.id.tv_sd_price);
        tv_sd_buyer = findViewById(R.id.tv_sd_buyer);
        tv_sd_seller = findViewById(R.id.tv_sd_seller);
        btn_sdel_detail = findViewById(R.id.btn_sdel_detail);
        btn_update_detail = findViewById(R.id.btn_update_detail);
        et_sd_name = findViewById(R.id.et_sd_name);

        Glide.with(this).load(intent_selling.getExtras().getString("iv_sd_profile")).override(300,300).into(iv_sd_profile);
        tv_sd_price.setText(intent_selling.getExtras().getString("tv_sd_price"));
        tv_sd_buyer.setText(intent_selling.getExtras().getString("tv_sd_buyer"));
        tv_sd_seller.setText(intent_selling.getExtras().getString("tv_sd_seller"));
        unique = intent_selling.getExtras().getString("unique");
        et_sd_name.setText(intent_selling.getExtras().getString("tv_sd_name"));


        btn_sdel_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.orderByChild("unique").equalTo(unique).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            p_id_key = child.getKey();
                        }

                        snapshot.getRef().child(p_id_key).removeValue();
                        Toast.makeText(getApplicationContext(), "상품이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), SellistActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "상품 삭제실패", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });//삭제버튼

        btn_update_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.orderByChild("unique").equalTo(unique).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            p_id_key = child.getKey();
                        }

                        abcd = et_sd_name.getText().toString();

                        snapshot.getRef().child(p_id_key).child("p_name").setValue(abcd);
                        Toast.makeText(getApplicationContext(), "상품정보가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), SellistActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "상품 삭제실패", Toast.LENGTH_SHORT).show();
                    }


                });
            }

        });//업데이트 버튼

    }
}