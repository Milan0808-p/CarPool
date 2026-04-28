package com.example.demo.entity.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
public class ChatMessage {
    private String senderId;
    private String receiverId;
    private String content;
    private String timestamp;
}
