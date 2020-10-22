package com.example.teamtest1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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


public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Product> arrayList;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference,databaseReference2;

    String key;
    String uids;
    EditText edit_Search;
    ImageView img_btnWrite, img_btnMypage,img_btnNotice, img_btnSearch,img_btnHotClick,img_btnChat,img_btnduedate;
    String modify_name,modify_pw,modify_email;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUid = user.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img_btnNotice = findViewById(R.id.img_SC_btnNotice);
        img_btnSearch = findViewById(R.id.img_SC_btnSearch);
        edit_Search = findViewById(R.id.edit_SC_Search);
        //img_btnHotClick = findViewById(R.id.img_SC_btnHotClick);
        img_btnWrite = findViewById(R.id.img_SC_btnWrite);
        img_btnMypage = findViewById(R.id.img_SC_btnMypage);
        img_btnChat = findViewById(R.id.img_SC_btnChat);
        //img_btnduedate = findViewById(R.id.img_btnduedate);
        recyclerView =  findViewById(R.id.SCrecyclerView);
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 기존 성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); // Product 객체를 담을 어레이 리스트

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference = database.getReference("Product"); // DB 테이블 연동
        databaseReference2 = database.getReference("User"); // DB 테이블 연동

        Intent intent = getIntent();
         uids = intent.getStringExtra("uid");
        // modify_name = intent.getStringExtra(g

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스 데이터 받아오는 곳
                arrayList.clear(); // 기존 배열 리스트가 존재하지 않게 초기화
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터리스트를 추출해내는것
                    Product product = snapshot.getValue(Product.class); // 만들어뒀던 product 객체에 데이터를 담는다.
                    arrayList.add(product); // 담은 데이터를 배열 리스트에 넣고 리사이클러뷰로 보낼 준비

                }
                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // DB 가져오던 중 에러 발생시
                Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

        databaseReference2.orderByChild("uid").equalTo(currentUid).addListenerForSingleValueEvent(new ValueEventListener() {
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
                    Glide.with(MainActivity.this)
                            .load(R.drawable.logo_main)
                            .into(img_btnMypage);

                }
                else {
                    storageReference.child("myprofile").child(path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(MainActivity.this)
                                    .load(uri)
                                    .into(img_btnMypage);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "실패", Toast.LENGTH_SHORT).show();
//                                    Glide.with(MyPage.this)
//                                            .load(e)
//                                            .into(iv_profile);
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        adapter = new CustomAdapter(arrayList,this);
        recyclerView.setAdapter(adapter); //리사이클러뷰에 어뎁터 연결


        img_btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Sell.class);
                intent.putExtra("uid" , uids);
                startActivity(intent);
                finish();
            }
        });

        img_btnMypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyPage.class);
                intent.putExtra("uid" , uids);

                //문제 해결
                startActivity(intent);
//                finish();
            }
        });

        img_btnNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Notice.class);
                startActivity(intent);
            }
        });

        img_btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search = edit_Search.getText().toString();
                Intent intent = new Intent(getApplicationContext(), SearchResult.class);

                intent.putExtra("search", search);

                startActivity(intent);
            }
        });

        img_btnChat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ChatList.class);
                startActivity(intent);
            }
        });








    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu1: //카테고리
                Intent intent = new Intent(getApplicationContext(), Category.class);
                startActivity(intent);
                break;

            case R.id.menu2: //핫클릭
                Intent intent2 = new Intent(getApplicationContext(), HotClickView.class);
                startActivity(intent2);
                break;

            case R.id.menu3: //마감임박
                Intent intent3 = new Intent(getApplicationContext(), DuedateActivity.class);
                startActivity(intent3);
                break;
        }


        return super.onOptionsItemSelected(item);
    }
}
