package com.example.teamtest1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class SellDetailActivity extends AppCompatActivity {


    ImageView iv_sd_profile;
    TextView tv_sd_name;
    TextView tv_sd_price;
    TextView tv_sd_buyer;
    TextView tv_sd_seller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_detail);


        Intent intent = getIntent();

//
//        database = FirebaseDatabase.getInstance(); //파이어벵스 데이터베이스 연동
//        databaseReference = database.getReference("Product");

        iv_sd_profile = findViewById(R.id.iv_sd_profile);
        tv_sd_name = findViewById(R.id.tv_sd_name);
        tv_sd_price = findViewById(R.id.tv_sd_price);
        tv_sd_buyer = findViewById(R.id.tv_sd_buyer);
        tv_sd_seller = findViewById(R.id.tv_sd_seller);

        //Bundle extras = getIntent().getExtras();

        tv_sd_name.setText(intent.getExtras().getString("tv_sd_name"));
        tv_sd_price.setText(intent.getExtras().getString("tv_sd_price"));
        tv_sd_buyer.setText(intent.getExtras().getString("tv_sd_buyer"));
        tv_sd_seller.setText(intent.getExtras().getString("tv_sd_seller"));
    }
}