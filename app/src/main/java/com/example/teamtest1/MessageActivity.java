package com.example.teamtest1;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MessageActivity extends AppCompatActivity{
    private String destinationUID;
    private String Uid;
    private String ChatRoomUid;
    private EditText editText;
    private RecyclerView recyclerView;
    @SuppressLint("SimpleDateFormat")

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH.mm");
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        Uid = user.getUid();
        destinationUID = getIntent().getStringExtra("destinationUID");
        final Button Button = findViewById(R.id.messageActivity_button);
        editText = findViewById(R.id.messageActivity_editText);
        recyclerView = findViewById(R.id.messageActivity_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CheckChatRoom();
        Button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ChatModel chatModel = new ChatModel();
                chatModel.User.put(Uid, true);
                chatModel.User.put(destinationUID, true);
                String nullTextMeee = "DestinationUid"+destinationUID+"Uid"+ Uid+"부릉부릉?";
                Toast.makeText(MessageActivity.this,nullTextMeee,Toast.LENGTH_LONG).show();
                if (ChatRoomUid == null) {
                    String nullTextMeeee = "DestinationUid"+destinationUID+"Uid"+ Uid+"살짝 되나?";
                    Toast.makeText(MessageActivity.this,nullTextMeeee,Toast.LENGTH_LONG).show();
                    FirebaseDatabase.getInstance().getReference().child("ChatRooms").push().setValue(chatModel).addOnSuccessListener(new OnSuccessListener<Void>(){
                        @Override
                        public void onSuccess(Void aVoid) {
                            CheckChatRoom();
                        }
                    });

                } else {
                    ChatModel.Comment comment = new ChatModel.Comment();
                    comment.Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    comment.message = editText.getText().toString();
                    comment.timeStamp = ServerValue.TIMESTAMP;
                    String notnullTextMessage = "DestinationUid"+destinationUID+"Uid"+ Uid +"RoomNum"+ChatRoomUid+"정상 전송 완료";
                    FirebaseDatabase.getInstance().getReference().child("ChatRooms").child(ChatRoomUid).child("comments").push().setValue(comment);
                    Toast.makeText(MessageActivity.this,notnullTextMessage,Toast.LENGTH_LONG).show();
                }

            }
        });
        CheckChatRoom();

    }

    private void CheckChatRoom(){
        FirebaseDatabase.getInstance().getReference().child("ChatRooms").orderByChild("User/"+Uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot item : dataSnapshot.getChildren()){
                    ChatModel chatModel = item.getValue(ChatModel.class);
                    assert chatModel != null;
                    if(chatModel.User.containsKey(destinationUID)){
                        ChatRoomUid = item.getKey();
                        recyclerView.setLayoutManager(new LinearLayoutManager((MessageActivity.this)));
                        recyclerView.setAdapter(new RecyclerViewAdapter());
                        String nullTextMeee = "DestinationUid"+destinationUID+"Uid"+ Uid+"살짝 되나?";
                        Toast.makeText(MessageActivity.this,nullTextMeee,Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(MessageActivity.this,"실패"+destinationUID,Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        List<ChatModel.Comment> comments;
        User User;
        public RecyclerViewAdapter(){
            comments = new ArrayList<>();
            FirebaseDatabase.getInstance().getReference().child("User").child(destinationUID)
                    .addListenerForSingleValueEvent(new ValueEventListener(){
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User = snapshot.getValue(User.class);
                            getMessageList();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



        }
        void getMessageList(){
            FirebaseDatabase.getInstance().getReference().child("ChatRooms").child(ChatRoomUid).child("comments").addValueEventListener(new ValueEventListener(){
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    comments.clear();
                    for(DataSnapshot item : snapshot.getChildren()){
                        comments.add(item.getValue(ChatModel.Comment.class));
                    }

                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MessageActivity.this, "저장 실패!", Toast.LENGTH_SHORT).show();
                }
            });

        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message,parent,false);

            return new MessageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            MessageViewHolder messageViewHolder = ((MessageViewHolder)holder);
            if(comments.get(position).Uid.equals(destinationUID)){
                messageViewHolder.textView_message.setText(destinationUID);
                messageViewHolder.textView_message.setText(comments.get(position).message);
                messageViewHolder.textView_message.setBackgroundResource(R.drawable.out_message_bg);
                messageViewHolder.linearLayout_destination.setVisibility(View.VISIBLE);
            }
            else{
                messageViewHolder.linearLayout_destination.setVisibility(View.INVISIBLE);
                messageViewHolder.textView_message.setBackgroundResource(R.drawable.in_message_bg);
                messageViewHolder.textView_message.setText(comments.get(position).message);


            }
            messageViewHolder.textView_message.setTextSize(25);
            long unixTime = (long) comments.get(position).timeStamp;
            Date date = new Date(unixTime);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            String time = simpleDateFormat.format(date);
            messageViewHolder.textView_Timestamp.setText(time);

        }

        @Override
        public int getItemCount() {
            return comments.size();
        }

        private class MessageViewHolder extends RecyclerView.ViewHolder{
            public TextView textView_message;
            public TextView textView_name;
            public ImageView imageView_profile;
            public LinearLayout linearLayout_main;
            public LinearLayout linearLayout_destination;
            public TextView textView_Timestamp;
            public MessageViewHolder(View view) {
                super(view);
                textView_message = view.findViewById(R.id.messageItem_textView_message);
                textView_name = view.findViewById(R.id.messageItem_textView_name);
                imageView_profile = view.findViewById(R.id.messageItem_imageView_profile);
                linearLayout_destination = view.findViewById(R.id.messageItem_linear_Layout_destination);
                textView_Timestamp = view.findViewById(R.id.messageItem_textView_timeStamp);
            }
        }
    }
    @Override
    public void onBackPressed(){
        finish();
        overridePendingTransition(R.anim.fromleft,R.anim.fromtoright);

    }
}