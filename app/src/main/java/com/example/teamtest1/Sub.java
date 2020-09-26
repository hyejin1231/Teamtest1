package com.example.teamtest1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class Sub extends AppCompatActivity {

    ImageView tv_image;
    TextView tv_title;
    TextView tv_bid;
    TextView tv_price;
    EditText edit_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);


        tv_image = findViewById(R.id.tv_image);
        tv_title = findViewById(R.id.tv_title);
        tv_bid = findViewById(R.id.tv_bid);
        tv_price = findViewById(R.id.tv_price);
        edit_detail = findViewById(R.id.edit_detail);
        Intent intent = getIntent();

        Glide.with(this).load(intent.getExtras().getString("image")).into(tv_image);

        tv_title.setText(intent.getExtras().getString("title"));
        tv_bid.setText(intent.getExtras().getString("bid"));
        tv_price.setText(intent.getExtras().getString("price"));
        edit_detail.setText(intent.getExtras().getString("detail"));

    }
}
