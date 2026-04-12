// package com.example.demo.grpc;

// import java.util.UUID;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;

// import io.grpc.ManagedChannel;
// import io.grpc.ManagedChannelBuilder;
// import billing.BillingRequest;
// import billing.BillingResponse;
// import billing.BillingServiceGrpc;

// import lombok.extern.slf4j.Slf4j;

// @Service
// @Slf4j
// public class BillingServiceGrpcClient {

//     private final BillingServiceGrpc.BillingServiceBlockingStub blockingStub;

//     public BillingServiceGrpcClient(
//             @Value("${billing.service.billing-service:localhost}") String serverAddress,
//             @Value("${billing.service.grpc.port:9001}") int serverPort) {

//         log.info("Connecting to billing service at {}:{}", serverAddress, serverPort);

//         ManagedChannel channel = ManagedChannelBuilder
//                 .forAddress(serverAddress, serverPort)
//                 .usePlaintext()
//                 .build();

//         this.blockingStub = BillingServiceGrpc.newBlockingStub(channel);
//     }

//     public BillingResponse createBillingAccount(String patientId, String name, String email) {

//         BillingRequest request = BillingRequest.newBuilder()
//                 .setPatientId(patientId)
//                 .setName(name)
//                 .setEmail(email)
//                 .build();

//         BillingResponse response = blockingStub.createBillingAccount(request);

//         log.info("Received response from billing service via gRPC: {}", response);

//         return response;
//     }
// }