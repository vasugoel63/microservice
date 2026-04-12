package com.example.demo.KafkaProducer;

import com.example.demo.event.PatientCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

public class PatientCreatedEventSerializer implements Serializer<PatientCreatedEvent> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, PatientCreatedEvent data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing PatientCreatedEvent", e);
        }
    }
}