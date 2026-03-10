package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PaymentRepository {
    private List<Payment> paymentData = new ArrayList<>();

    public Payment save(Payment payment) {
        int index = -1;
        for (int i = 0; i < paymentData.size(); i++) {
            if (paymentData.get(i).getId().equals(payment.getId())) {
                index = i;
                break;
            }
        }
        if (index >= 0) {
            paymentData.set(index, payment);
        } else {
            paymentData.add(payment);
        }
        return payment;
    }

    public Payment findById(String id) {
        for (Payment payment : paymentData) {
            if (payment.getId().equals(id)) {
                return payment;
            }
        }
        return null;
    }

    public List<Payment> findAll() {
        return new ArrayList<>(paymentData);
    }
}