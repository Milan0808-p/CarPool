package com.example.demo.service.chat;

import com.example.demo.entity.chat.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(String channel, ChatMessage message) {

        redisTemplate.convertAndSend(channel, message);
    }
}