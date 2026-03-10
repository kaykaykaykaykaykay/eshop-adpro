package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private PaymentService paymentService;

    private Order order;

    @BeforeEach
    void setUp() {
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
    void testCreateOrderPage() throws Exception {
        mockMvc.perform(get("/order/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("CreateOrder"));
    }

    @Test
    void testOrderHistoryPage() throws Exception {
        mockMvc.perform(get("/order/history"))
                .andExpect(status().isOk())
                .andExpect(view().name("OrderHistory"));
    }

    @Test
    void testPostOrderHistory() throws Exception {
        List<Order> orders = List.of(order);
        doReturn(orders).when(orderService).findAllByAuthor("Safira Sudrajat");

        mockMvc.perform(post("/order/history")
                        .param("author", "Safira Sudrajat"))
                .andExpect(status().isOk())
                .andExpect(view().name("OrderList"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attribute("author", "Safira Sudrajat"));
    }

    @Test
    void testPayOrderPage() throws Exception {
        doReturn(order).when(orderService).findById(order.getId());

        mockMvc.perform(get("/order/pay/" + order.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("OrderPay"))
                .andExpect(model().attributeExists("order"));
    }

    @Test
    void testPayOrderPageNotFound() throws Exception {
        doThrow(new NoSuchElementException()).when(orderService).findById("invalid-id");

        mockMvc.perform(get("/order/pay/invalid-id"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/order/history"));
    }

    @Test
    void testPostPayOrderVoucher() throws Exception {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("pay-001", order, "VOUCHER", paymentData);

        doReturn(order).when(orderService).findById(order.getId());
        doReturn(payment).when(paymentService).addPayment(any(), eq("VOUCHER"), any());

        mockMvc.perform(post("/order/pay/" + order.getId())
                        .param("method", "VOUCHER")
                        .param("voucherCode", "ESHOP1234ABC5678"))
                .andExpect(status().isOk())
                .andExpect(view().name("OrderPaid"))
                .andExpect(model().attribute("paymentId", "pay-001"));
    }

    @Test
    void testPostPayOrderCOD() throws Exception {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("address", "Jl. Raya No. 1");
        paymentData.put("deliveryFee", "10000");
        Payment payment = new Payment("pay-002", order, "CASH_ON_DELIVERY", paymentData);

        doReturn(order).when(orderService).findById(order.getId());
        doReturn(payment).when(paymentService).addPayment(any(), eq("CASH_ON_DELIVERY"), any());

        mockMvc.perform(post("/order/pay/" + order.getId())
                        .param("method", "CASH_ON_DELIVERY")
                        .param("address", "Jl. Raya No. 1")
                        .param("deliveryFee", "10000"))
                .andExpect(status().isOk())
                .andExpect(view().name("OrderPaid"))
                .andExpect(model().attribute("paymentId", "pay-002"));
    }
}