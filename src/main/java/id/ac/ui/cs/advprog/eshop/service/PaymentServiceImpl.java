package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        String id = UUID.randomUUID().toString();
        Payment payment = new Payment(id, order, method, paymentData);

        String initialStatus = determineInitialStatus(method, paymentData);
        if (initialStatus != null) {
            payment.setStatus(initialStatus);
        }

        paymentRepository.save(payment);
        return payment;
    }

    private String determineInitialStatus(String method, Map<String, String> paymentData) {
        if ("VOUCHER".equals(method)) {
            return isValidVoucher(paymentData.get("voucherCode"))
                    ? PaymentStatus.SUCCESS.name()
                    : PaymentStatus.REJECTED.name();
        }

        if ("CASH_ON_DELIVERY".equals(method)) {
            return isValidCod(paymentData)
                    ? PaymentStatus.SUCCESS.name()
                    : PaymentStatus.REJECTED.name();
        }

        return null;
    }

    private boolean isValidCod(Map<String, String> paymentData) {
        String address = paymentData.get("address");
        String deliveryFee = paymentData.get("deliveryFee");

        return address != null && !address.isEmpty()
                && deliveryFee != null && !deliveryFee.isEmpty();
    }

    private boolean isValidVoucher(String code) {
        if (code == null || code.length() != 16) return false;
        if (!code.startsWith("ESHOP")) return false;

        int digitCount = 0;
        for (char c : code.toCharArray()) {
            if (Character.isDigit(c)) {
                digitCount++;
            }
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