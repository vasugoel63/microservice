// package com.example.billing_service.Kafkaconsumer;

// import com.example.billing_service.Event.PatientCreatedEvent;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import org.apache.kafka.common.serialization.Deserializer;

// public class PatientCreatedEventDeserializer implements Deserializer<PatientCreatedEvent> {

//     private final ObjectMapper objectMapper = new ObjectMapper();

//     @Override
//     public PatientCreatedEvent deserialize(String topic, byte[] data) {
//         try {
//             System.out.println(data);
//             return objectMapper.readValue(data, PatientCreatedEvent.class);
//         } catch (Exception e) {
//             throw new RuntimeException(e);
//         }
//     }
// }