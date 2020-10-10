package com.example.teamtest1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Sell extends AppCompatActivity {

    TextView tv_writeToday,tv_writeDeadline;
    Button btn_register,btn_gallery,btn_SelectDate;
    EditText edit_bid, edit_price, edit_title, edit_detail;
    ImageView img_writeImage;
    Uri uri;
    Bitmap img;
    String uids;
    private DatePickerDialog.OnDateSetListener callbackMethod;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    Random random = new Random();
    long now = System.currentTimeMillis();
    Date today = new Date(now);

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat format = new SimpleDateFormat("yyyy");
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
    SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd");
    int year = Integer.parseInt(format.format(today));
    String date = simpleDateFormat.format(today);
    int month  = Integer.parseInt(dateFormat.format(today));
    int day = Integer.parseInt(dateFormat2.format(today));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        tv_writeDeadline = findViewById(R.id.tv_writeDeadline);
        tv_writeToday = findViewById(R.id.tv_writeToday);
        btn_gallery = findViewById(R.id.btn_gallery);
        btn_register = findViewById(R.id.btn_register);
        edit_bid = findViewById(R.id.edit_bid);
        edit_detail = findViewById(R.id.edit_detail);
        edit_title = findViewById(R.id.edit_ntTtile);
        img_writeImage = findViewById(R.id.img_writeImage);
        edit_price = findViewById(R.id.edit_price);
        btn_SelectDate=findViewById(R.id.btn_SelectDate);

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference = database.getReference("Product"); // DB 테이블 연동

        tv_writeToday.setText(date);

        Intent intent = getIntent();
        uids = intent.getStringExtra("uid");

        this.InitializeListener();

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
                String image = getImageUri(Sell.this, img).toString();
                String title = edit_title.getText().toString();
                String detail = edit_detail.getText().toString();
                String bid = edit_bid.getText().toString();
                String price = edit_price.getText().toString();
                String deadline = tv_writeDeadline.getText().toString();
                int count = 0;

                String unique = "";

                for(int i = 0; i< 10; i++) {
                    unique += String.valueOf((char) ((int) (random.nextInt(26)) + 97));
                }

                //String title, String detail, String price, String bid, String image
                Product product = new Product(title, detail, price, bid, image,count,unique,date,deadline,uids );
//                databaseReference.child("Pd_04").push().setValue(product);
                databaseReference.push().setValue(product);

                Toast.makeText(getApplicationContext(),"등록 완료", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();

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
                tv_writeDeadline.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            }
        };
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(data.getData());

                    img = BitmapFactory.decodeStream(in);
//                    Bitmap img = BitmapFactory.decodeStream(in);
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
    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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
