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

            kafkaTemplate.send(TOPIC, envelope);

            System.out.println("Kafka 메시지 전송 완료: " + envelope);
        } catch (Exception e) {
            throw new RuntimeException("Kafka 메시지 전송 실패", e);
        }
    }
}
