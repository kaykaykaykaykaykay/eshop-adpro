package id.ac.ui.cs.advprog.eshop.service;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {
    @InjectMocks
    PaymentServiceImpl paymentService;
    @Mock
    PaymentRepository paymentRepository;
    private Order order;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo");
        product.setProductQuantity(2);
        products.add(product);

        order = new Order("13652556-012a-4c07-b546-54eb1396d79b",
                products, 1708560000L, "Safira Sudrajat");
    }

    // Voucher
    @Test
    void testAddPaymentVoucherValidCode() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        Payment result = paymentService.addPayment(order, "VOUCHER", paymentData);

        assertEquals("SUCCESS", result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentVoucherInvalidLength() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP123");

        Payment result = paymentService.addPayment(order, "VOUCHER", paymentData);
        assertEquals("REJECTED", result.getStatus());
    }

    @Test
    void testAddPaymentVoucherDoesNotStartWithESHOP() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "SHOP12345ABC6789");

        Payment result = paymentService.addPayment(order, "VOUCHER", paymentData);
        assertEquals("REJECTED", result.getStatus());
    }

    @Test
    void testAddPaymentVoucherInsufficientNumbers() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP123ABCDEFGH");

        Payment result = paymentService.addPayment(order, "VOUCHER", paymentData);
        assertEquals("REJECTED", result.getStatus());
    }

    // Cash on delivery
    @Test
    void testAddPaymentCODValidData() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("address", "Jl. Raya No. 1");
        paymentData.put("deliveryFee", "10000");

        Payment result = paymentService.addPayment(order, "CASH_ON_DELIVERY", paymentData);
        assertEquals("SUCCESS", result.getStatus());
    }

    @Test
    void testAddPaymentCODNullAddress() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("address", null);
        paymentData.put("deliveryFee", "10000");

        Payment result = paymentService.addPayment(order, "CASH_ON_DELIVERY", paymentData);
        assertEquals("REJECTED", result.getStatus());
    }

    @Test
    void testAddPaymentCODEmptyAddress() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("address", "");
        paymentData.put("deliveryFee", "10000");

        Payment result = paymentService.addPayment(order, "CASH_ON_DELIVERY", paymentData);
        assertEquals("REJECTED", result.getStatus());
    }

    @Test
    void testAddPaymentCODNullDeliveryFee() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("address", "Jl. Raya No. 1");
        paymentData.put("deliveryFee", null);

        Payment result = paymentService.addPayment(order, "CASH_ON_DELIVERY", paymentData);
        assertEquals("REJECTED", result.getStatus());
    }

    @Test
    void testAddPaymentCODEmptyDeliveryFee() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("address", "Jl. Raya No. 1");
        paymentData.put("deliveryFee", "");

        Payment result = paymentService.addPayment(order, "CASH_ON_DELIVERY", paymentData);
        assertEquals("REJECTED", result.getStatus());
    }

    // Set status
    @Test
    void testSetStatusSuccess() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("pay-001", order, "VOUCHER", paymentData);

        paymentService.setStatus(payment, "SUCCESS");

        assertEquals("SUCCESS", payment.getStatus());
        assertEquals("SUCCESS", order.getStatus());
    }

    @Test
    void testSetStatusRejected() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("pay-001", order, "VOUCHER", paymentData);

        paymentService.setStatus(payment, "REJECTED");

        assertEquals("REJECTED", payment.getStatus());
        assertEquals("FAILED", order.getStatus());
    }

    @Test
    void testSetStatusInvalid() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("pay-001", order, "VOUCHER", paymentData);

        assertThrows(IllegalArgumentException.class, () ->
                paymentService.setStatus(payment, "MEOW"));
    }

    // Get payment
    @Test
    void testGetPaymentFound() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("pay-001", order, "VOUCHER", paymentData);
        doReturn(payment).when(paymentRepository).findById("pay-001");

        Payment result = paymentService.getPayment("pay-001");
        assertEquals("pay-001", result.getId());
    }

    @Test
    void testGetPaymentNotFound() {
        doReturn(null).when(paymentRepository).findById("not-exist");

        assertThrows(NoSuchElementException.class, () ->
                paymentService.getPayment("not-exist"));
    }

    // Get all payments
    @Test
    void testGetAllPayments() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        List<Payment> payments = List.of(
                new Payment("pay-001", order, "VOUCHER", paymentData),
                new Payment("pay-002", order, "VOUCHER", paymentData)
        );
        doReturn(payments).when(paymentRepository).findAll();

        List<Payment> result = paymentService.getAllPayments();
        assertEquals(2, result.size());
    }
}
