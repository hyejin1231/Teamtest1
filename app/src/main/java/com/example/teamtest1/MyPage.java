package com.example.teamtest1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

public class MyPage extends AppCompatActivity {

    private FirebaseAuth auth; //파이어 베이스 인증 객체


    private TextView tv_result; // 닉네임 text
    private ImageView iv_profile; // 이미지 뷰
    private TextView tv_id;
    //private  TextView tv_token;

    private Button btn_buylist;
    private Button btn_selllist;
    //private Button btn_likelist;
    private Button btn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        Intent intent = getIntent();
        String nickName = intent.getStringExtra("nickName"); //MainActivity로 부터 닉네임 전달받음.
        String photoUrl = intent.getStringExtra("photoUrl"); //MainActivity로 부터 프로필사진 url 전달받음.
        String myId = intent.getStringExtra("myId");
        String myToken = intent.getStringExtra("myToken");

        tv_result = findViewById(R.id.tv_result);
        tv_result.setText(nickName);

        iv_profile = findViewById(R.id.iv_profile);
        Glide.with(this).load(photoUrl).into(iv_profile); //프로필 uri를 이미지 뷰에 세팅

        tv_id = findViewById(R.id.tv_email_login);
        tv_id.setText(myId);

        //tv_token = findViewById(R.id.tv_token);
        //tv_token.setText(myToken);

        btn_buylist = findViewById(R.id.btn_buylist);
        btn_selllist = findViewById(R.id.btn_selllist);
        //btn_likelist = findViewById(R.id.btn_likelist);
        btn_logout = findViewById(R.id.btn_logout);

        auth = FirebaseAuth.getInstance();

        btn_buylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BuylistActivity.class);
                startActivity(intent);

            }
        });

        btn_selllist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SellistActivity.class);
                startActivity(intent);

            }
        });

        //아직 자바파일 안만들어뒀음
//        btn_likelist.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), LikeListActivity.class);
//                startActivity(intent);
//
//            }
//        });


        //로그아웃할 때
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                Toast.makeText(MyPage.this, "로그아웃",Toast.LENGTH_SHORT).show();
            }
        });



    }
}
