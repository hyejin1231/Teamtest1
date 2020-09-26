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
import com.google.firebase.database.DatabaseReference;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth Auth; //파이어ㅔㅂ이스에 대한 권한 가져오기

    private EditText et_email;
    private EditText et_pw;
    private EditText et_name;
    private EditText et_age;
    private EditText et_pw_check;
    private Button btn_signup;

    DatabaseReference database;
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
                                FirebaseUser user = Auth.getCurrentUser();
                                Toast.makeText(SignUp.this, "회원가입 성공",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Login2.class);

                                startActivity(intent);

                                //updateUI(user);
                            } else {

                                // If sign in fails, display a message to the user.
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
