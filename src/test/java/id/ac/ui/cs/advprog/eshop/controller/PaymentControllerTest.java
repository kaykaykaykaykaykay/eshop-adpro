package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    private Payment payment;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(2);
        products.add(product);

        Order order = new Order("13652556-012a-4c07-b546-54eb1396d79b",
                products, 1708560000L, "Safira Sudrajat");

        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        payment = new Payment("pay-001", order, "VOUCHER", paymentData);
    }

    @Test
    void testPaymentDetailPage() throws Exception {
        mockMvc.perform(get("/payment/detail"))
                .andExpect(status().isOk())
                .andExpect(view().name("PaymentDetail"));
    }

    @Test
    void testPaymentDetailResultFound() throws Exception {
        doReturn(payment).when(paymentService).getPayment("pay-001");

        mockMvc.perform(get("/payment/detail/pay-001"))
                .andExpect(status().isOk())
                .andExpect(view().name("PaymentDetailResult"))
                .andExpect(model().attributeExists("payment"));
    }

    @Test
    void testPaymentDetailResultNotFound() throws Exception {
        doThrow(new NoSuchElementException()).when(paymentService).getPayment("not-exist");

        mockMvc.perform(get("/payment/detail/not-exist"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payment/detail"));
    }

    @Test
    void testAdminListPage() throws Exception {
        List<Payment> payments = List.of(payment);
        doReturn(payments).when(paymentService).getAllPayments();

        mockMvc.perform(get("/payment/admin/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("PaymentAdminList"))
                .andExpect(model().attributeExists("payments"));
    }

    @Test
    void testAdminDetailPage() throws Exception {
        doReturn(payment).when(paymentService).getPayment("pay-001");

        mockMvc.perform(get("/payment/admin/detail/pay-001"))
                .andExpect(status().isOk())
                .andExpect(view().name("PaymentAdminDetail"))
                .andExpect(model().attributeExists("payment"));
    }

    @Test
    void testAdminSetStatusSuccess() throws Exception {
        doReturn(payment).when(paymentService).getPayment("pay-001");
        doReturn(payment).when(paymentService).setStatus(any(), eq("SUCCESS"));

        mockMvc.perform(post("/payment/admin/set-status/pay-001")
                        .param("status", "SUCCESS"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payment/admin/list"));
    }

    @Test
    void testAdminSetStatusRejected() throws Exception {
        doReturn(payment).when(paymentService).getPayment("pay-001");
        doReturn(payment).when(paymentService).setStatus(any(), eq("REJECTED"));

        mockMvc.perform(post("/payment/admin/set-status/pay-001")
                        .param("status", "REJECTED"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payment/admin/list"));
    }
}