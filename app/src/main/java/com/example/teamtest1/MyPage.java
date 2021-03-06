package com.example.teamtest1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.GoogleApiClient;
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

import java.util.ArrayList;

public class MyPage extends AppCompatActivity {

    private FirebaseAuth auth; //파이어 베이스 인증 객체
    private GoogleApiClient mGoogleApiClient;
    //Uri uri;

    private TextView tv_result; // 닉네임 text
    private ImageView iv_profile; // 이미지 뷰
    private TextView tv_id;
    //private  TextView tv_token;
    private Button btn_customerChat;
    private ImageView img_MyWarn;
    private TextView tv_MyWarn;
    private TextView tv_Message;


    private TextView tv_participate;
    private  TextView tv_UserEstimateCount;
    private  ImageView img_UserFace_smile,img_UserFace_dis,img_UserFace_angry,img_UserFace_good,img_UserFace_soso;

    private ImageView btn_buylist;
    private ImageView btn_selllist;
    private ImageView btn_likelist;
    private Button btn_logout;
    private Button btn_modify;
    ImageView btn_bidlist;


    ImageView img_btnMyBack;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ArrayList<User> arrayList;

//    String uids;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);


        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference = database.getReference("User"); // DB 테이블 연동
        //Intent intent = getIntent();
//        String nickName = intent.getStringExtra("nickName"); //MainActivity로 부터 닉네임 전달받음.
//        String photoUrl = intent.getStringExtra("photoUrl"); //MainActivity로 부터 프로필사진 url 전달받음.
//        String myId = intent.getStringExtra("myId");
//        String myToken = intent.getStringExtra("myToken");

//        uids = intent.getStringExtra("uid");
//        Toast.makeText(getApplicationContext(),uid,Toast.LENGTH_SHORT).show();
//
        tv_result = findViewById(R.id.tv_result);
//        tv_result.setText(nickName);
//
        iv_profile = findViewById(R.id.iv_profile);
//        Glide.with(this).load(photoUrl).into(iv_profile); //프로필 uri를 이미지 뷰에 세팅
//
        tv_id = findViewById(R.id.tv_email_login);
//        tv_id.setText(myId);

        //tv_token = findViewById(R.id.tv_token);
        //tv_token.setText(myToken);

        img_MyWarn = findViewById(R.id.img_MyWarn);
        tv_MyWarn = findViewById(R.id.tv_MyWarn);
        tv_Message = findViewById(R.id.tv_Message);

        btn_bidlist = findViewById(R.id.btn_bidlist);
        tv_participate = findViewById(R.id.tv_participate);
        tv_UserEstimateCount = findViewById(R.id.tv_UserEstimateCount);
        img_UserFace_smile = findViewById(R.id.img_UserFace_smile);
        img_UserFace_dis = findViewById(R.id.img_UserFace_dis);
        img_UserFace_angry = findViewById(R.id.img_UserFace_angry);
        img_UserFace_good = findViewById(R.id.img_UserFace_good);
        img_UserFace_soso = findViewById(R.id.img_UserFace_soso);
        btn_modify = findViewById(R.id.btn_modify);
        btn_customerChat = findViewById(R.id.btn_customer_chat);

//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this /* FragmentActivity */,MyPage /* OnConnectionFailedListener */)
//                .addApi(Auth.GOOGLE_SIGN_IN_API)
//                .build();


        // 코드가 지저분해도 참아줘 ..^_^ _혜진

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUid = user.getUid();

        databaseReference.orderByChild("uid").equalTo(currentUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    key = child.getKey();
//                    key = uids; //다혜수정
                }

                //다혜다혜
                //이미지뷰에 선택된 이미지 로딩시키는 코드임
                FirebaseStorage storage = FirebaseStorage.getInstance("gs://teamtest1-6b76d.appspot.com");
                StorageReference storageReference = storage.getReference();

                //이미지지파일이름 가져오는거...
               String path = (String) snapshot.child(key).child("photoUrl").getValue();


//                if(snapshot.child(key).child("photoUrl").getValue().equals(null)){
//                    databaseReference.child(key).child("photoUrl").push().setValue("default");
//                }
//                else {
                    if (path.equals("default")) {
                        Glide.with(MyPage.this)
                                .load(R.drawable.logo_main)
                                .into(iv_profile);

                    }
                    else {
                        storageReference.child("myprofile").child(path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(MyPage.this)
                                        .load(uri)
                                        .into(iv_profile);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MyPage.this, "실패", Toast.LENGTH_SHORT).show();
                                    Glide.with(MyPage.this)
                                            .load(e)
                                           .into(iv_profile);
                            }
                        });

                    }









                tv_id.setText(snapshot.child(key).child("id").getValue().toString());
                tv_result.setText(snapshot.child(key).child("nickName").getValue().toString());

                tv_UserEstimateCount.setText(snapshot.child(key).child("estimate").getValue().toString());
                int count = Integer.parseInt(snapshot.child(key).child("estimateUser").getValue().toString()) -1 ;

                tv_participate.setText( count+ "명 참여");
                int progress = Integer.parseInt(snapshot.child(key).child("estimate").getValue().toString());

                if(snapshot.child(key).child("warn").getValue().toString().isEmpty()) {
                    tv_MyWarn.setVisibility(View.INVISIBLE);
                    img_MyWarn.setVisibility(View.INVISIBLE);
                } else if (snapshot.child(key).child("warn").getValue().toString().equals("경고4회")) {
                    img_MyWarn.setVisibility(View.VISIBLE);
                    tv_MyWarn.setText(snapshot.child(key).child("warn").getValue().toString());
                    tv_MyWarn.setVisibility(View.VISIBLE);
                    tv_Message.setVisibility(View.VISIBLE);
                }else if (snapshot.child(key).child("warn").getValue().toString().equals("경고5회")){
                    img_MyWarn.setVisibility(View.VISIBLE);
                    tv_MyWarn.setText(snapshot.child(key).child("warn").getValue().toString());
                    tv_MyWarn.setVisibility(View.VISIBLE);
                    tv_Message.setText("경고 5회로 강제 회원탈퇴 될 예정입니다.");
                    tv_Message.setVisibility(View.VISIBLE);
                }else {
                    img_MyWarn.setVisibility(View.VISIBLE);
                    tv_MyWarn.setText(snapshot.child(key).child("warn").getValue().toString());
                    tv_MyWarn.setVisibility(View.VISIBLE);
                }

                if (progress >= 0 && progress <= 20) {
                    img_UserFace_angry.setVisibility(View.VISIBLE);

                    img_UserFace_smile.setVisibility(View.INVISIBLE);
                    img_UserFace_good.setVisibility(View.INVISIBLE);
                    img_UserFace_soso.setVisibility(View.INVISIBLE);
                    img_UserFace_dis.setVisibility(View.INVISIBLE);
                }else if(progress > 20 && progress <= 40) {
                    img_UserFace_dis.setVisibility(View.VISIBLE);

                    img_UserFace_angry.setVisibility(View.INVISIBLE);
                    img_UserFace_smile.setVisibility(View.INVISIBLE);
                    img_UserFace_good.setVisibility(View.INVISIBLE);
                    img_UserFace_soso.setVisibility(View.INVISIBLE);
                }else if(progress > 40 && progress <= 60){
                    img_UserFace_soso.setVisibility(View.VISIBLE);

                    img_UserFace_dis.setVisibility(View.INVISIBLE);
                    img_UserFace_angry.setVisibility(View.INVISIBLE);
                    img_UserFace_smile.setVisibility(View.INVISIBLE);
                    img_UserFace_good.setVisibility(View.INVISIBLE);
                }else if(progress > 60 && progress <= 80) {
                    img_UserFace_smile.setVisibility(View.VISIBLE);

                    img_UserFace_dis.setVisibility(View.INVISIBLE);
                    img_UserFace_angry.setVisibility(View.INVISIBLE);
                    img_UserFace_good.setVisibility(View.INVISIBLE);
                    img_UserFace_soso.setVisibility(View.INVISIBLE);
                }else {
                    img_UserFace_good.setVisibility(View.VISIBLE);

                    img_UserFace_smile.setVisibility(View.INVISIBLE);
                    img_UserFace_soso.setVisibility(View.INVISIBLE);
                    img_UserFace_dis.setVisibility(View.INVISIBLE);
                    img_UserFace_angry.setVisibility(View.INVISIBLE);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        img_btnMyBack = findViewById(R.id.img_btnMyBack);
//        btn_main = findViewById(R.id.btn_main);
        btn_buylist = findViewById(R.id.btn_buylist);
        btn_selllist = findViewById(R.id.btn_selllist);
        btn_likelist = findViewById(R.id.btn_likelist);
        btn_logout = findViewById(R.id.btn_logout);


        auth = FirebaseAuth.getInstance();

        img_btnMyBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                //finish();
            }
        });

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

        btn_likelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LikelistActivity.class);
                startActivity(intent);

            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
//                        new ResultCallback<Status>() {
//                            @Override
//                            public void onResult(Status status) {
//                                FirebaseAuth.getInstance().signOut();
//                                Intent i1 = new Intent(getApplicationContext(), Login2.class);
//                                startActivity(i1);
//                                Toast.makeText(getApplicationContext(), "Logout Successfully!", Toast.LENGTH_SHORT).show();
//                            }
//                        });
                FirebaseAuth.getInstance().signOut();


                Intent intent = new Intent(getApplicationContext(), Login2.class);
                startActivity(intent);
//                finish();
                Toast.makeText(MyPage.this, "로그아웃",Toast.LENGTH_SHORT).show();
            }
        });

        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ModifyMyInfo.class);
                // auth 쓰면 intent로 굳이 주고받을 필요가 없는거 같아서 이거 주석 처리할게!!
//                intent.putExtra("uid" , uids);
//                intent.putExtra("uid",currentUid);
                startActivity(intent);
            }
        }); // 관리자와의 1:1 채팅 구현
        btn_customerChat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MessageActivity.class);
                intent.putExtra("destinationUID","lj2EwOLJVNTCCkmQrLF7dhuA3vF2");
                startActivity(intent);
            }
        });

        btn_bidlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BidList.class);
                startActivity(intent);
            }
        });


    }//oncreate


}
