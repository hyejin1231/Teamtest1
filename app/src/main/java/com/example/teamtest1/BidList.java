package com.example.teamtest1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BidList extends AppCompatActivity {

    private RecyclerView BidCycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Product> arrayList;
    private ArrayList<String> arrayList2;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    ImageView btn_bidList_back;
    TextView textView49;


    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUid = user.getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_list);

        BidCycler = findViewById(R.id.BidCycler);
        BidCycler.setHasFixedSize(true);
        btn_bidList_back = findViewById(R.id.btn_bidList_back);
        layoutManager = new LinearLayoutManager(this);
        BidCycler.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
        arrayList2 = new ArrayList<>();
        textView49 = findViewById(R.id.textView49);



        database = FirebaseDatabase.getInstance(); //파이어벵스 데이터베이스 연동
        databaseReference = database.getReference("Product");


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                arrayList2.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Product pd = child.getValue(Product.class);
                    arrayList2.add(pd.getBidder());

//                    for (int i = 0; i< arrayList2.size(); i++) {
//                        if (arrayList2.get(i).getBidder().equals(currentUid)){
//                            textView49.setText("입찰내역");
////                        Toast.makeText(BidList.this, arrayList2.get(i).getBidder(), Toast.LENGTH_SHORT).show();
//                        }else{
//
//                        }
//                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        if (arrayList2.contains(currentUid) == true) {

            databaseReference.orderByChild("bidder").equalTo(currentUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    arrayList.clear();

                    for (DataSnapshot child : snapshot.getChildren()) {
                        Product product = child.getValue(Product.class);
                        arrayList.add(product);
                    }

                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        adapter = new CustomAdapter(arrayList, this);
        BidCycler.setAdapter(adapter);
//        }else {
//            Toast.makeText(BidList.this, currentUid, Toast.LENGTH_SHORT).show();
//        }

//        if (arrayList2.contains(currentUid) == true) {
//            textView49.setText("입찰내역");
//        }else {
//            Toast.makeText(BidList.this, currentUid, Toast.LENGTH_SHORT).show();
//            textView49.setText("입찰내역 없음");
//        }

        btn_bidList_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}