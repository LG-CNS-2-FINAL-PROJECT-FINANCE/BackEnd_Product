package com.ddiring.BackEnd_Product.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private static final String TOPIC = "notification";

    //단일 사용자 알림 전송 (단일도 List.of 처리)
    public void sendNotification(String userSeq, String notificationType, String title, String message) {
        sendNotification(List.of(userSeq), notificationType, title, message);
    }

    //다중 사용자 알림 전송
    public void sendNotification(List<String> userSeq, String notificationType, String title, String message) {
        try {
            NotificationPayload payload = NotificationPayload.builder()
                    .userSeq(userSeq)
                    .notificationType(notificationType)
                    .title(title)
                    .message(message)
                    .build();

            EventEnvelope<NotificationPayload> envelope = EventEnvelope.<NotificationPayload>builder()
                    .eventId(UUID.randomUUID().toString())
                    .timestamp(Instant.now())
                    .payload(payload)
                    .build();

            String json = objectMapper.writeValueAsString(envelope);
            kafkaTemplate.send(TOPIC, json);

            System.out.println("Kafka 메시지 전송 완료: " + json);
        } catch (Exception e) {
            throw new RuntimeException("Kafka 메시지 전송 실패", e);
        }
    }
}
