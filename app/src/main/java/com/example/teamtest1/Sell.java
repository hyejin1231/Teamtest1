package com.example.teamtest1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;

public class Sell extends AppCompatActivity {

    Button btn_register,btn_gallery;
    EditText edit_bid, edit_price, edit_title, edit_detail;
    ImageView img_writeImage;
    Uri uri;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        btn_gallery = findViewById(R.id.btn_gallery);
        btn_register = findViewById(R.id.btn_register);
        edit_bid = findViewById(R.id.edit_bid);
        edit_detail = findViewById(R.id.edit_detail);
        edit_title = findViewById(R.id.edit_title);
        img_writeImage = findViewById(R.id.img_writeImage);
        edit_price = findViewById(R.id.edit_price);

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference = database.getReference("Product"); // DB 테이블 연동

        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,1);
            }
        });

        // register 버튼을 누르면 파이어베이스에 데이터 저장 가능??!!ㅠㅠ 제발..
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                 String imageUrl =  getPath(uri);;
                String title = edit_title.getText().toString();
                String detail = edit_detail.getText().toString();
                String bid = edit_bid.getText().toString();
                String price = edit_price.getText().toString();

                //String title, String detail, String price, String bid, String image
                Product product = new Product(title, detail, price, bid );
//                databaseReference.child("Pd_04").push().setValue(product);
                databaseReference.push().setValue(product);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(data.getData());

                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    img_writeImage.setImageBitmap(img);

                    uri = data.getData();


                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 이미지 경로 알아오는 함수..?
//    private String getPath(Uri uri)
//    {
//        String[] projection = { MediaStore.Images.Media.DATA };
//        Cursor cursor = managedQuery(uri, projection, null, null, null);
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        return cursor.getString(column_index);
//    }
}
