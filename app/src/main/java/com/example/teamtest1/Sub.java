package com.example.teamtest1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

public class Sub extends AppCompatActivity {

    ImageView tv_image;
    TextView tv_title;
    TextView tv_bid;
    TextView tv_price;
    EditText edit_detail;
    TextView tv_count;
    ImageView img_btnBackMain;

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

        Intent intent = getIntent();

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

        img_btnBackMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent1);
                finish();
            }
        });

    }
}
