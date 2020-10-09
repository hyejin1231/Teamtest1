package com.example.teamtest1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth Auth; //파이어ㅔㅂ이스에 대한 권한 가져오기

    private EditText et_email;
    private EditText et_pw;
    private EditText et_name;
    private EditText et_age;
    private EditText et_pw_check;
    private Button btn_signup;

    // 혜진 코드 삽입
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

//    DatabaseReference database;
    //ProgressDialog mDialog;

    private static final String TAG = "SignActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        et_email = findViewById(R.id.et_email);
        et_pw = findViewById(R.id.et_pw);
        et_age = findViewById(R.id.et_age);
        et_name = findViewById(R.id.name);
        btn_signup = findViewById(R.id.btn_signup);
        et_pw_check = findViewById(R.id.et_pw_check);

        // 혜진 코드 삽입
        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference = database.getReference("User"); // DB 테이블 연동


        Auth = FirebaseAuth.getInstance();

        //회원가입 버튼을 누르면
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_signup:
                        Log.e("클릭", "클릭");
                        signUp();
                        break;
                }
            }
        });
    }


    private void signUp() {
        String email = et_email.getText().toString();
        String password = et_pw.getText().toString();
        String passwordCheck = et_pw_check.getText().toString();

        if (password.equals(passwordCheck)) {
//            if (TextUtils.isEmpty(email)) {
//                Toast.makeText(SignActivity.this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
//                return;
//            } else if (TextUtils.isEmpty(password)) {
//                Toast.makeText(SignActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
//                return;
//            } else if (password.length() < 4) {
//                Toast.makeText(SignActivity.this, "비밀번호는 최소 4자 이상이어야 합니다.", Toast.LENGTH_SHORT).show();
//                return;
//            }


            Auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                //Log.d(TAG, "createUserWithEmail:success");
                                final FirebaseUser user = Auth.getCurrentUser();
                                Toast.makeText(SignUp.this, "회원가입 성공",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Login2.class);

                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        String myid = user.getEmail();
                                        String nickName = user.getDisplayName();
                                        String photoUrl = String.valueOf(user.getPhotoUrl());
                                        String my_uid = user.getUid();



                                            User user = new User(photoUrl, myid, nickName,my_uid);

                                            databaseReference.push().setValue(user);

                                            Toast.makeText(getApplicationContext(),"user 등록 완료", Toast.LENGTH_SHORT).show();

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                startActivity(intent);

                                //updateUI(user);
                            } else {

                                // If sign in fails, display  message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUp.this, "회원가입 실패",
                                        Toast.LENGTH_SHORT).show();
                                ///updateUI(null);

                            }

                            // ...
                        }
                    });
        } else {
            Toast.makeText(SignUp.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();

        }
    }
}
