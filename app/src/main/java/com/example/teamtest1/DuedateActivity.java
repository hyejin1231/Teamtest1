package com.example.teamtest1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DuedateActivity extends AppCompatActivity {

    private RecyclerView DuedateRecycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Product> arrayList;

    ImageView img_btnClickBack ;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    String uniqueTest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duedate);

        DuedateRecycler = findViewById(R.id.DuedateRecycler);
        DuedateRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        DuedateRecycler.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();

        img_btnClickBack = findViewById(R.id.img_btnClickBack);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Product");

        databaseReference.orderByChild("unique").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();


                for (DataSnapshot child : snapshot.getChildren()) {
                    uniqueTest = child.getKey();
                    long now = System.currentTimeMillis();
                    Date today = new Date(now);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String date = simpleDateFormat.format(today);
                    String deadline = snapshot.child(uniqueTest).child("deadline").getValue().toString();


                    int Caldate = Integer.parseInt(deadline.replace("-", ""))- Integer.parseInt(date.replace("-", ""));
                    if(Caldate ==1 || Caldate ==2 ) {
                        Product pd = child.getValue(Product.class);
                        arrayList.add(0,pd);
                    }


                }


                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter = new CustomAdapter(arrayList, this);
        DuedateRecycler.setAdapter(adapter);


        img_btnClickBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}