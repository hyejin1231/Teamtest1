package com.example.teamtest1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.service.autofill.Dataset;
import android.text.Layout;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.teamtest1.fragment.ChatFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ChatList extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        getSupportFragmentManager().beginTransaction().replace(R.id.FL_Chat_list,new ChatFragment()).commit();




    }
}