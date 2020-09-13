package com.example.teamtest1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView btn_Sell = findViewById(R.id.btn_Sell);
        ImageView btn_Mypage = findViewById(R.id.btn_Mypage);

        btn_Sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent SellIntent = new Intent(getApplicationContext(), Sell.class);
                startActivity(SellIntent);
            }
        });

        btn_Mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MypageIntent = new Intent(getApplicationContext(), MyPage.class);
                startActivity(MypageIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu); // 타이틀바의 아이콘 3개를 위해서
        return true;
    }



}
