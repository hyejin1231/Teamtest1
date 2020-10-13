package com.example.teamtest1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminMemberAdapter extends RecyclerView.Adapter<AdminMemberAdapter.CustomViewHolder> {

    private ArrayList<User> arrayList;
    private Context context;
    String aaa;
    String uid;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    public AdminMemberAdapter(ArrayList<User> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public AdminMemberAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admember_item,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdminMemberAdapter.CustomViewHolder holder, int position) {

        String abcd = arrayList.get(position).getId();
        Glide.with(holder.itemView).load(arrayList.get(position).getPhotoUrl()).into(holder.img_admemImg); //프로필 uri를 이미지 뷰에 세팅
        holder.tv_admemuid.setText(arrayList.get(position).getUid());
        holder.tv_admemid.setText(arrayList.get(position).getId());
//        Toast.makeText(context, arrayList.get(position).getUid(),Toast.LENGTH_SHORT).show();
    }



    @Override
    public int getItemCount() {
         return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageView img_admemImg;
        TextView tv_admemid, tv_admemuid;
        Button btn_adMemDrop, btn_adMemWarn;

        public CustomViewHolder(@NonNull final View itemView) {
            super(itemView);

            this.img_admemImg = itemView.findViewById(R.id.img_admemImg);
            this.tv_admemid = itemView.findViewById(R.id.tv_admemid);
            this.tv_admemuid = itemView.findViewById(R.id.tv_admemuid);
            this.btn_adMemDrop = itemView.findViewById(R.id.btn_adMemDrop);
            this.btn_adMemWarn = itemView.findViewById(R.id.btn_adMemWarn);

            database = FirebaseDatabase.getInstance();
            databaseReference = database.getReference("User");


            btn_adMemDrop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = getAdapterPosition();
                    uid = arrayList.get(position).getUid();

                    new AlertDialog.Builder(itemView.getContext()) // TestActivity 부분에는 현재 Activity의 이름 입력.
                            .setMessage("회원을 탈퇴시키겠습니까?")     // 제목 부분 (직접 작성)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {      // 버튼1 (직접 작성)
                                public void onClick(DialogInterface dialog, int which) {

                                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            snapshot.getRef().child(uid).removeValue();
                                            Toast.makeText(context, "해당 사용자가 탈퇴되었습니다.", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
//                                    Intent intent = new Intent(itemView.getContext(), MainActivity.class);
//                                    itemView.getContext().startActivity(intent);
                                    Toast.makeText(itemView.getContext(), "확인 누름", Toast.LENGTH_SHORT).show(); // 실행할 코드
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {     // 버튼2 (직접 작성)
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(itemView.getContext(), "취소 누름", Toast.LENGTH_SHORT).show(); // 실행할 코드
                                }
                            })
                            .show();

                }


            });
        }




        }
    }















