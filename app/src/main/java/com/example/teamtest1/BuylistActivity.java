package com.example.teamtest1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BuylistActivity extends AppCompatActivity {

    private FirebaseAuth Auth;
    private RecyclerView recyclerView2;
    private RecyclerView.Adapter adapter2;
    private RecyclerView.LayoutManager layoutManager2;
    private ArrayList<Product> arrayList2;
    private FirebaseDatabase database2;
    private DatabaseReference databaseReference2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buylist);

        recyclerView2 = findViewById(R.id.Buy_recyclerView); //아이디 연결
        recyclerView2.setHasFixedSize(true); //리사이클러뷰 기존성능 강화
        layoutManager2 = new LinearLayoutManager(this);
        recyclerView2.setLayoutManager(layoutManager2);
        arrayList2 = new ArrayList<>(); //product 객체를 담을 어레이 리스트 (어댑터 쪽으로)

        database2 = FirebaseDatabase.getInstance(); //파이어베이스 데이터베이스 연동
        databaseReference2 = database2.getReference("Product");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUid = user.getUid();

        databaseReference2.orderByChild("buyer").equalTo(currentUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList2.clear(); //기존 배열리스트가 존재하지 않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class); //만들어뒀던 User 객체에 데이터를 담는다.
                    arrayList2.add(product); //담은 데이터드을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                }
                adapter2.notifyDataSetChanged();//리스트 저장 및 새로고침
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        adapter2 = new BuyListAdapter(arrayList2, this);
        recyclerView2.setAdapter(adapter2); //리사이클러뷰에 어댑터 연결
    }

}
