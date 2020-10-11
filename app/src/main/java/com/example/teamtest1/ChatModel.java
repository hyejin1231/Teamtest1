package com.example.teamtest1;

import org.w3c.dom.Comment;

import java.util.HashMap;
import java.util.Map;

public class ChatModel{
    public Map<String,Boolean> User = new HashMap<>();
    public Map<String, Comment> comments = new HashMap<>();
    public static class Comment{
        public String Uid;
        public String message;
        public Object timeStamp;
    }
}
