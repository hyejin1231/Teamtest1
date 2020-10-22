package com.example.teamtest1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SellingDetailActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ArrayList<Product> arrayList;
    private DatePickerDialog.OnDateSetListener callbackMethod;

    ImageView iv_sd_profile;
    TextView tv_sd_name;
    TextView tv_sd_bid;
    TextView tv_sd_buyer;
    TextView tv_sd_seller;
    Button btn_sdel_detail, btn_update_detail;
    String p_id, p_id_key;
    EditText et_sd_name;
    String abcd,unique;
    TextView tv_sd_deadline;
    Button btn_sd_complete,btn_sd_date;
    TextView tv_sd_uniqueKey;
    EditText et_sd_content,tv_sd_price;

    String key,test,key2;

    long now = System.currentTimeMillis();
    Date today = new Date(now);

    SimpleDateFormat format = new SimpleDateFormat("yyyy");
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
    SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd");

    int year = Integer.parseInt(format.format(today));
    int month  = Integer.parseInt(dateFormat.format(today));
    int day = Integer.parseInt(dateFormat2.format(today));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling_detail);


        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Product");

        Intent intent_selling = getIntent();

        this.InitializeListener();

        tv_sd_bid = findViewById(R.id.tv_sd_bid);
        btn_sd_date = findViewById(R.id.btn_sd_date);
        et_sd_content = findViewById(R.id.et_sd_content);
        tv_sd_uniqueKey = findViewById(R.id.tv_sd_uniqueKey);
        iv_sd_profile = findViewById(R.id.iv_sd_profile);
        //tv_sd_name = findViewById(R.id.tv_sd_name);
        tv_sd_price = findViewById(R.id.tv_sd_price);
        tv_sd_buyer = findViewById(R.id.tv_sd_buyer);
        tv_sd_seller = findViewById(R.id.tv_sd_seller);
        btn_sdel_detail = findViewById(R.id.btn_sdel_detail);
        btn_update_detail = findViewById(R.id.btn_update_detail);
        et_sd_name = findViewById(R.id.et_sd_name);
        tv_sd_deadline = findViewById(R.id.tv_sd_deadline);
        btn_sd_complete = findViewById(R.id.btn_sd_complete);

        test = intent_selling.getExtras().getString("unique");
        tv_sd_uniqueKey.setText(test);
        databaseReference.orderByChild("unique").equalTo(test).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child : snapshot.getChildren()) {
                    key = child.getKey();
                }

                et_sd_content.setText(snapshot.child(key).child("detail").getValue().toString());
                tv_sd_bid.setText(snapshot.child(key).child("bid").getValue().toString() + "원");
                tv_sd_price.setText(snapshot.child(key).child("price").getValue().toString());
                FirebaseStorage storage = FirebaseStorage.getInstance("gs://teamtest1-6b76d.appspot.com");
                StorageReference storageReference = storage.getReference();
                String path = snapshot.child(key).child("image").getValue().toString();
                storageReference.child(path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
//                        Toast.makeText(context, "성공", Toast.LENGTH_SHORT).show();

                        Glide.with(getApplicationContext())
                                .load(uri)
                                .override(300,300)
                                .into(iv_sd_profile);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Glide.with(this).load(intent_selling.getExtras().getString("iv_sd_profile")).override(300,300).into(iv_sd_profile);
//        tv_sd_price.setText(intent_selling.getExtras().getString("tv_sd_price"));
        tv_sd_buyer.setText(intent_selling.getExtras().getString("tv_sd_buyer"));
        tv_sd_seller.setText(intent_selling.getExtras().getString("tv_sd_seller"));
        unique = intent_selling.getExtras().getString("unique");
        et_sd_name.setText(intent_selling.getExtras().getString("tv_sd_name"));
        tv_sd_deadline.setText(intent_selling.getExtras().getString("deadline"));

        btn_sdel_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.orderByChild("unique").equalTo(unique).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            p_id_key = child.getKey();
                        }

                        snapshot.getRef().child(p_id_key).removeValue();
                        Toast.makeText(getApplicationContext(), "상품이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), SellistActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "상품 삭제실패", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });//삭제버튼

        btn_update_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.orderByChild("unique").equalTo(unique).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            p_id_key = child.getKey();
                        }

                        abcd = et_sd_name.getText().toString();
                        String updateContent = et_sd_content.getText().toString();
                        String updateDeadline = tv_sd_deadline.getText().toString();
                        String updatePrice = tv_sd_price.getText().toString();

                        snapshot.getRef().child(p_id_key).child("price").setValue(updatePrice);
                        snapshot.getRef().child(p_id_key).child("title").setValue(abcd);
                        snapshot.getRef().child(p_id_key).child("detail").setValue(updateContent);
                        snapshot.getRef().child(p_id_key).child("deadline").setValue(updateDeadline);
                        Toast.makeText(getApplicationContext(), "상품정보가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), SellistActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "상품 삭제실패", Toast.LENGTH_SHORT).show();
                    }


                });
            }

        });//업데이트 버튼

        // 판매중인 상품을 즉시구매하면서 거래 완료되면 판매종료로 바꿔야하니까 필요한 버튼
        btn_sd_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SellingDetailActivity.this)
                        .setMessage("판매 종료 하겠습니까?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                databaseReference.orderByChild("unique").equalTo(unique).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot child : snapshot.getChildren()) {
                                            key2 = child.getKey();
                                        }

                                        snapshot.getRef().child(key2).child("status").setValue("complete");
                                        Toast.makeText(getApplicationContext(), "판매 종료되었습니다.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), SellistActivity.class);
                                        startActivity(intent);
                                        finish();

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(SellingDetailActivity.this, "취소", Toast.LENGTH_SHORT).show(); // 실행할 코드
                    }
                }).show();


            }
        });

    }

    public void OnClickHandler(View view)
    {
        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, year , month, day);

        dialog.show();
    }


    public void InitializeListener() {
        callbackMethod = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                tv_sd_deadline.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            }
        };
    }
}