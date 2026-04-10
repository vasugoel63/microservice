// package com.example.billing_service.grpc;

// import io.grpc.stub.StreamObserver;
// import net.devh.boot.grpc.server.service.GrpcService;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

// import com.example.billing_service.entities.BillingAccount;
// import com.example.billing_service.service.BillingService;

// import billing.BillingRequest;
// import billing.BillingResponse;
// import billing.BillingServiceGrpc;

// @GrpcService
// public class BillingGrpService extends
// BillingServiceGrpc.BillingServiceImplBase {

// private final BillingService billingService;

// private static final Logger log =
// LoggerFactory.getLogger(BillingGrpService.class);

// // Constructor injection
// public BillingGrpService(BillingService billingService) {
// this.billingService = billingService;
// }

// @Override
// public void createBillingAccount(
// BillingRequest billingRequest,
// StreamObserver<BillingResponse> responseObserver) {

// log.info("create billing request received {}", billingRequest);

// BillingAccount saved = billingService.createAccount(
// billingRequest.getPatientId(),
// billingRequest.getName(),
// billingRequest.getEmail());

// BillingResponse response = BillingResponse.newBuilder()
// .setAccountId(saved.getId().toString()) // Use actual DB ID
// .setStatus(saved.getStatus()) // Use actual status
// .build();

// responseObserver.onNext(response);
// responseObserver.onCompleted();
// }
// }