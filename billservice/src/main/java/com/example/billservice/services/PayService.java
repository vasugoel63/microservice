package com.example.billservice.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.billservice.dto.BillingAccountResponse;
import com.example.billservice.dto.FailPaymentRequest;
import com.example.billservice.dto.TransactionDTO;
import com.example.billservice.dto.VerifyPaymentRequest;
import com.example.billservice.entities.Bill;
import com.example.billservice.entities.BillStatus;
import com.example.billservice.entities.PaymentMethod;
import com.example.billservice.entities.Transaction;
import com.example.billservice.entities.TransactionStatus;
import com.example.billservice.event.BillUpdateEvent;
import com.example.billservice.kafkaProducer.BillKafkaProducer;
import com.example.billservice.repository.BillRepository;
import com.example.billservice.repository.TransactionRepository;
import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

@Service
public class PayService {

    @Value("${razorpay.api.key}")
    private String apiKey;

    @Value("${razorpay.api.secret}")
    private String apiSecret;

    private BillRepository billRepository;
    private TransactionRepository transactionRepository;
    private BillKafkaProducer billKafkaProducer;

    public PayService(BillRepository billRepository, TransactionRepository transactionRepository,
            BillKafkaProducer billKafkaProducer) {
        this.billRepository = billRepository;
        this.transactionRepository = transactionRepository;
        this.billKafkaProducer = billKafkaProducer;
    }

    public Map<String, Object> createOrder(int amount, String currency, UUID billId) throws RazorpayException {
        RazorpayClient razorpayClient = new RazorpayClient(apiKey, apiSecret);
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount * 100);
        orderRequest.put("currency", currency);

        Order order = razorpayClient.orders.create(orderRequest);
        Bill bill = billRepository.findById(billId).get();
        bill.setRazorpayOrderId(order.get("id"));
        bill.setStatus(BillStatus.PENDING);
        billRepository.save(bill);
        Map<String, Object> response = new HashMap<>();
        response.put("orderId", order.get("id"));
        response.put("amount", amount);
        response.put("currency", currency);
        response.put("billId", billId);

        return response;

    }

    public boolean verifyPayment(VerifyPaymentRequest request) throws RazorpayException {
        RazorpayClient razorpayClient = new RazorpayClient(apiKey, apiSecret);

        String generatedSignature = Utils.getHash(
                request.getRazorpay_order_id()
                        + "|"
                        + request.getRazorpay_payment_id(),
                apiSecret);

        boolean isValid = generatedSignature.equals(
                request.getRazorpay_signature());

        if (isValid) {
            Bill bill = billRepository.findByRazorpayOrderId(request.getRazorpay_order_id());
            if (bill == null) {
                return false;
            }

            RestTemplate restTemplate = new RestTemplate();
            String url = "http://billing-service:4002/api/billing/" + bill.getPatientId();
            BillingAccountResponse billingAccount = restTemplate.getForObject(url, BillingAccountResponse.class);
            if (billingAccount == null) {
                throw new RuntimeException("Billing account not found");
            }
            System.out.println("Billing response: " + billingAccount);
            // txnDTO.setBillingAccountId(billingAccount.getId());
            // transactionService.createTransaction(txnDTO, savedBill);
            Payment payment = razorpayClient.payments.fetch(
                    request.getRazorpay_payment_id());

            String method = payment.get("method");

            Transaction transaction = new Transaction();
            transaction.setBill(bill);
            transaction.setAmount(request.getAmount());
            transaction.setBillingAccountId(billingAccount.getId());
            transaction.setTransactionRefId("TXN-" + System.currentTimeMillis());
            // transaction.setPaymentMethod(PaymentMethod.UPI);
            switch (method.toLowerCase()) {

                case "upi":
                    transaction.setPaymentMethod(
                            PaymentMethod.UPI);
                    break;

                case "card":
                    transaction.setPaymentMethod(
                            PaymentMethod.CARD);
                    break;

                case "wallet":
                    transaction.setPaymentMethod(
                            PaymentMethod.WALLET);
                    break;

                case "netbanking":
                    transaction.setPaymentMethod(
                            PaymentMethod.NETBANKING);
                    break;

                default:
                    transaction.setPaymentMethod(PaymentMethod.UPI);
            }
            transaction.setStatus(TransactionStatus.SUCCESS);
            transaction.setRazorpay_order_id(
                    request.getRazorpay_order_id());

            transaction.setRazorpay_payment_id(
                    request.getRazorpay_payment_id());
            Transaction savedTransaction = transactionRepository.save(transaction);
            bill.setStatus(BillStatus.PAID);
            BillUpdateEvent event = new BillUpdateEvent();
            event.setPatientId(bill.getPatientId());
            event.setAmount(bill.getTotalAmount());
            billKafkaProducer.sendBillCreatedEvent(event);
            bill.setPaidDateTime(LocalDateTime.now());
            billRepository.save(bill);
        }
        return isValid;
    }

    public boolean failPayment(FailPaymentRequest request) throws RazorpayException {
        Bill bill = billRepository.findByRazorpayOrderId(request.getRazorpay_order_id());
        if (bill == null) {
            return false;
        }

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://billing-service:4002/api/billing/" + bill.getPatientId();
        BillingAccountResponse billingAccount = restTemplate.getForObject(url, BillingAccountResponse.class);
        if (billingAccount == null) {
            throw new RuntimeException("Billing account not found");
        }
        System.out.println("Billing response: " + billingAccount);
        // txnDTO.setBillingAccountId(billingAccount.getId());
        // transactionService.createTransaction(txnDTO, savedBill);
        Transaction transaction = new Transaction();
        transaction.setBill(bill);
        transaction.setAmount(request.getAmount());
        transaction.setBillingAccountId(billingAccount.getId());
        transaction.setTransactionRefId("TXN-" + System.currentTimeMillis());
        transaction.setPaymentMethod(PaymentMethod.UPI);
        transaction.setStatus(TransactionStatus.FAILED);
        transaction.setRazorpay_order_id(request.getRazorpay_order_id());
        transaction.setRazorpay_payment_id(request.getRazorpay_payment_id());
        transaction.setFailureReason(request.getFailure_reason());
        Transaction savedTransaction = transactionRepository.save(transaction);
        bill.setStatus(BillStatus.FAILED);
        billRepository.save(bill);
        return true;
    }
}
