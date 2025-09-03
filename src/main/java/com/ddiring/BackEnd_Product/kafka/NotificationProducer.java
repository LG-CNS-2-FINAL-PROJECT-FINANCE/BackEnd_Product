package com.ddiring.BackEnd_Product.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "notification";

    // 단일 대상
    public void sendNotification(String userSeq, String notificationType, String title, String message) {
        sendNotification(List.of(userSeq), notificationType, title, message);
    }

    // 다중 대상
    public void sendNotification(List<String> userSeq, String notificationType, String title, String message) {
        NotificationEvent event = NotificationEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .timestamp(Instant.now())
                .userSeq(userSeq)
                .notificationType(notificationType)
                .title(title)
                .message(message)
                .build();

        kafkaTemplate.send(TOPIC, event);
        System.out.println("Kafka 메시지 전송 완료: " + event);
    }
}
