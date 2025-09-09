package com.ddiring.BackEnd_Product.kafka;

import lombok.*;

import java.time.Instant;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventEnvelope<T> {
    private String eventId;
    private Instant timestamp;
    private T payload;
}