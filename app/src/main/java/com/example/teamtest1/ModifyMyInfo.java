package com.example.teamtest1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ModifyMyInfo extends AppCompatActivity {

    private FirebaseAuth Auth;

    TextView tv_modify_showemail;
    EditText et_modify_pw,et_modify_nickname,et_modify_pw_check;
    String uids,key,key1,key2;
    Button btn_modify;
    ImageView iv_modify_profile;
    private Button btn_profilepic;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private Uri filePath2;
    Uri uri;
    Bitmap img;
    String filename2;

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
        btn_profilepic = findViewById(R.id.btn_profilepic);
        iv_modify_profile = findViewById(R.id.iv_modify_profile);

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


        //정보수정 버튼
        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String password = et_modify_pw.getText().toString();
                final String passwordCheck = et_modify_pw_check.getText().toString();
                final String nickName = et_modify_nickname.getText().toString();
                final FirebaseUser user = Auth.getCurrentUser();

                //아 이건 pid만드는거구나 멍청쓰...
//                String pic_num="";
//                for(int i = 0; i< 10; i++) {
//                    pic_num += String.valueOf((char)((int) (random.nextInt(26)) + 97));
//                }

                if (filePath2 != null) {
                    FirebaseStorage storage = FirebaseStorage.getInstance();

                    //Unique한 파일명을 만들자.
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
                    Date now = new Date();
                    filename2 = uids + formatter.format(now) + ".png";
                    //storage 주소와 폴더 파일명을 지정해 준다.
//                    StorageReference storageRef = storage.getReferenceFromUrl("gs://teamtest1-6b76d.appspot.com").child("images/" + filename);
                    StorageReference storageRef = storage.getReferenceFromUrl("gs://teamtest1-6b76d.appspot.com").child(filename2);
                    //올라가거라...
                    storageRef.putFile(filePath2)
                            //성공시
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                                    Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            //실패시
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
//                            progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                                }
                            });
//            진행중
//                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                            @SuppressWarnings("VisibleForTests") //이걸 넣어 줘야 아랫줄에 에러가 사라진다. 넌 누구냐?
//                                    double progress = (100 * taskSnapshot.getBytesTransferred()) /  taskSnapshot.getTotalByteCount();
//                            //dialog에 진행률을 퍼센트로 출력해 준다
//                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
//                        }
//                    });
                } else {
                    Toast.makeText(getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
                }




                /////////////////////////////////////////////////////////
                /////////////////////////////////////////////////////////
                /////////////////////////////////////////////////////////
                databaseReference.orderByChild("uid").equalTo(uids).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            key1 = child.getKey();
                        }

                        //abcd = et_sd_name.getText().toString();
                        key2 = et_modify_nickname.getText().toString();

                        if (password.equals(passwordCheck)) { //auth쪽
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
                                        }else{ //모든게 적절하게 입력됐을 경우..
                                            snapshot.getRef().child(key1).child("nickName").setValue(key2);
                                            snapshot.getRef().child(key1).child("photoUrl").setValue(filename2);
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


                });//레퍼런스




            }
        });

        //여기부터 다혜
        //권한체크 후 권한요청
        btn_profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,1);
            }
        });

    }//oncreate


    //여기도 다혜
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                try {
                    filePath2 = data.getData();
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in2 = getContentResolver().openInputStream(filePath2);

                    img = BitmapFactory.decodeStream(in2);
//                    Bitmap img = BitmapFactory.decodeStream(in);
                    in2.close();
                    iv_modify_profile.setImageBitmap(img);

                    uri = data.getData();


                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 이미지 경로 알아오는 함수..?
    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "title", null);
//        String path = MediaStore.Images.Media.in
        return Uri.parse(path);
    }
}