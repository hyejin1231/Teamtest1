package com.example.teamtest1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

public class BuyDetailActivity extends AppCompatActivity {


    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ArrayList<Product> arrayList;

    ImageView iv_bd_profile;
    TextView tv_bd_name;
    TextView tv_bd_price;
    TextView tv_bd_buyer;
    TextView tv_bd_seller;
    Button btn_del_detail;
    String p_id, p_id_key,unique;
    String key,test;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_detail);

        database = FirebaseDatabase.getInstance(); //파이어벵스 데이터베이스 연동
        databaseReference = database.getReference("Product");

        Intent intent_buy = getIntent();

        iv_bd_profile = findViewById(R.id.iv_bd_profile);
        tv_bd_name = findViewById(R.id.tv_bd_name);
        tv_bd_price = findViewById(R.id.tv_bd_price);
        tv_bd_buyer = findViewById(R.id.tv_bd_buyer);
        tv_bd_seller = findViewById(R.id.tv_bd_seller);
        btn_del_detail = findViewById(R.id.btn_bdel_detail);


        test = intent_buy.getExtras().getString("unique");
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

                        Glide.with(getApplicationContext())
                                .load(uri)
                                .override(300,300)
                                .into(iv_bd_profile);
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
       // Glide.with(this).load(intent_buy.getExtras().getString("iv_sd_profile")).override(300,300).into(iv_bd_profile);
        tv_bd_name.setText(intent_buy.getExtras().getString("tv_bd_name"));
        tv_bd_price.setText(intent_buy.getExtras().getString("tv_bd_price"));
        tv_bd_buyer.setText(intent_buy.getExtras().getString("tv_bd_buyer"));
        tv_bd_seller.setText(intent_buy.getExtras().getString("tv_bd_seller"));
        unique = intent_buy.getExtras().getString("unique");

        btn_del_detail.setOnClickListener(new View.OnClickListener() {
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
                        Intent intent = new Intent(getApplicationContext(), BuylistActivity.class);
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