package com.example.demo.controller;

import com.example.demo.entity.chat.ChatMessage;
import com.example.demo.service.chat.RedisPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // PRIVATE MESSAGE
    @MessageMapping("/private-message")
    public void sendPrivateMessage(ChatMessage message) {
        messagingTemplate.convertAndSendToUser(
                message.getReceiverId(),   // target user
                "/queue/messages",
                message
        );
        System.out.println("Message received:In controller " + message);
    }
}