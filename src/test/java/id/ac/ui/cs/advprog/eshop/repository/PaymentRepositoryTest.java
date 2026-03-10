package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRepositoryTest {
    private PaymentRepository paymentRepository;
    private Order order;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();

        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(2);
        products.add(product);

        order = new Order("13652556-012a-4c07-b546-54eb1396d79b",
                products, 1708560000L, "Safira Sudrajat");
    }

    @Test
    void testSaveCreate() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("pay-001", order, "VOUCHER", paymentData);

        Payment result = paymentRepository.save(payment);
        assertEquals("pay-001", result.getId());

        Payment found = paymentRepository.findById("pay-001");
        assertEquals("pay-001", found.getId());
    }

    @Test
    void testSaveUpdate() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("pay-001", order, "VOUCHER", paymentData);
        paymentRepository.save(payment);

        payment.setStatus("SUCCESS");
        paymentRepository.save(payment);

        Payment found = paymentRepository.findById("pay-001");
        assertEquals("SUCCESS", found.getStatus());
    }

    @Test
    void testFindByIdFound() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("pay-001", order, "VOUCHER", paymentData);
        paymentRepository.save(payment);

        Payment found = paymentRepository.findById("pay-001");
        assertNotNull(found);
        assertEquals("pay-001", found.getId());
    }

    @Test
    void testFindByIdNotFound() {
        Payment found = paymentRepository.findById("not-exist");
        assertNull(found);
    }

    @Test
    void testFindAll() {
        Map<String, String> paymentData1 = new HashMap<>();
        paymentData1.put("voucherCode", "ESHOP1234ABC5678");
        Map<String, String> paymentData2 = new HashMap<>();
        paymentData2.put("voucherCode", "ESHOP5678ABC1234");

        paymentRepository.save(new Payment("pay-001", order, "VOUCHER", paymentData1));
        paymentRepository.save(new Payment("pay-002", order, "VOUCHER", paymentData2));

        List<Payment> all = paymentRepository.findAll();
        assertEquals(2, all.size());
    }
}