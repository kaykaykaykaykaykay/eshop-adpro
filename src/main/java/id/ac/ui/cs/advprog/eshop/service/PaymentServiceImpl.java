package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;

import java.util.*;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        String id = UUID.randomUUID().toString();
        Payment payment = new Payment(id, order, method, paymentData);

        if (method.equals("VOUCHER")) {
            String code = paymentData.get("voucherCode");
            if (isValidVoucher(code)) {
                payment.setStatus("SUCCESS");
            } else {
                payment.setStatus("REJECTED");
            }
        } else if (method.equals("CASH_ON_DELIVERY")) {
            String address = paymentData.get("address");
            String deliveryFee = paymentData.get("deliveryFee");
            if (address == null || address.isEmpty() ||
                    deliveryFee == null || deliveryFee.isEmpty()) {
                payment.setStatus("REJECTED");
            } else {
                payment.setStatus("SUCCESS");
            }
        }

        paymentRepository.save(payment);
        return payment;
    }

    private boolean isValidVoucher(String code) {
        if (code == null || code.length() != 16) return false;
        if (!code.startsWith("ESHOP")) return false;
        int digitCount = 0;
        for (char c : code.toCharArray()) {
            if (Character.isDigit(c)) digitCount++;
        }
        return digitCount == 8;
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        if (!PaymentStatus.contains(status)) {
            throw new IllegalArgumentException("Invalid payment status: " + status);
        }
        payment.setStatus(status);
        if (status.equals(PaymentStatus.SUCCESS.name())) {
            payment.getOrder().setStatus(OrderStatus.SUCCESS.name());
        } else if (status.equals(PaymentStatus.REJECTED.name())) {
            payment.getOrder().setStatus(OrderStatus.FAILED.name());
        }
        paymentRepository.save(payment);
        return payment;
    }

    @Override
    public Payment getPayment(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId);
        if (payment == null) {
            throw new NoSuchElementException("Payment not found: " + paymentId);
        }
        return payment;
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}