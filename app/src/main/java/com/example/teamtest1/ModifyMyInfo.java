package com.example.teamtest1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ModifyMyInfo extends AppCompatActivity {

    private FirebaseAuth Auth;

    TextView tv_modify_showemail;
    EditText et_modify_pw,et_modify_nickname,et_modify_pw_check;
    String uids,key,key1,key2;
    Button btn_modify;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_my_info);
        Auth = FirebaseAuth.getInstance();

        tv_modify_showemail = findViewById(R.id.tv_modify_showemail);
        et_modify_pw = findViewById(R.id.et_modify_pw);
        et_modify_nickname = findViewById(R.id.et_modify_nickname);
        btn_modify = findViewById(R.id.btn_modify);
        et_modify_pw_check = findViewById(R.id.et_modify_pw_check);

        Intent intent = getIntent();
        uids = intent.getStringExtra("uid");

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference = database.getReference("User"); // DB 테이블 연동

//        tv_modify_showemail.setText(intent.getExtras().getString("tv_sd_price"));
//        et_modify_pw.setText(intent.getExtras().getString("tv_sd_buyer"));
//        et_modify_nickname.setText(intent.getExtras().getString("tv_sd_seller"));
//        //tv_modify_showemail.setText();

        databaseReference.orderByChild("uid").equalTo(uids).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    key = child.getKey();
                }
                tv_modify_showemail.setText(snapshot.child(key).child("id").getValue().toString());
                et_modify_pw.setText(snapshot.child(key).child("pw").getValue().toString());
                et_modify_nickname.setText(snapshot.child(key).child("nickName").getValue().toString());

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String password = et_modify_pw.getText().toString();
                final String passwordCheck = et_modify_pw_check.getText().toString();
                final String nickName = et_modify_nickname.getText().toString();



                final FirebaseUser user = Auth.getCurrentUser();
                databaseReference.orderByChild("uid").equalTo(uids).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            key1 = child.getKey();
                        }

                        //abcd = et_sd_name.getText().toString();
                        key2 = et_modify_nickname.getText().toString();

                        if (password.equals(passwordCheck)) {
                            snapshot.getRef().child(key1).child("pw").setValue(password); //디비안의 비번 바꿔줌
                            user.updatePassword(password); //파베권한의 비번 바꿔줌

                            databaseReference.orderByChild("nickName").equalTo(nickName).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot child : snapshot.getChildren()) {
                                        String key = child.getKey();
                                        String key3 = snapshot.getRef().child(key).child("nickName").toString();
                                        if(nickName.equals(key3)){
                                            Toast.makeText(ModifyMyInfo.this, "중복된 닉네임입니다.", Toast.LENGTH_SHORT).show();
                                        }else{
                                            snapshot.getRef().child(key1).child("nickName").setValue(key2);
                                            Toast.makeText(getApplicationContext(), "회원정보가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), MyPage.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getApplicationContext(), "회원정보 수정 실패", Toast.LENGTH_SHORT).show();

                                }
                            });





                        }else {
                            Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "회원정보 수정 실패", Toast.LENGTH_SHORT).show();
                    }


                });

            }
        });
    }
}