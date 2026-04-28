package com.example.demo.service.chat;

import com.example.demo.entity.chat.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String msg = message.toString();
            ChatMessage chatMessage = objectMapper.readValue(msg, ChatMessage.class);

            // Send to WebSocket topic (ride-specific)
//            messagingTemplate.convertAndSend(
//                    "/topic/ride/" + chatMessage.getRideId(),
//                    chatMessage
//            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
