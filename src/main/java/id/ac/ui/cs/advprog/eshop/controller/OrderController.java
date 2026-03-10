package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/create")
    public String createOrderPage() {
        return "CreateOrder";
    }

    @GetMapping("/history")
    public String orderHistoryPage() {
        return "OrderHistory";
    }

    @PostMapping("/history")
    public String orderHistoryResult(@RequestParam String author, Model model) {
        List<Order> orders = orderService.findAllByAuthor(author);
        model.addAttribute("orders", orders);
        model.addAttribute("author", author);
        return "OrderList";
    }

    @GetMapping("/pay/{orderId}")
    public String payOrderPage(@PathVariable String orderId, Model model) {
        try {
            Order order = orderService.findById(orderId);
            model.addAttribute("order", order);
            return "OrderPay";
        } catch (NoSuchElementException e) {
            return "redirect:/order/history";
        }
    }

    @PostMapping("/pay/{orderId}")
    public String payOrderSubmit(@PathVariable String orderId,
                                 @RequestParam String method,
                                 @RequestParam(required = false) String voucherCode,
                                 @RequestParam(required = false) String address,
                                 @RequestParam(required = false) String deliveryFee,
                                 Model model) {
        Order order = orderService.findById(orderId);

        Map<String, String> paymentData = new HashMap<>();
        if (method.equals("VOUCHER")) {
            paymentData.put("voucherCode", voucherCode);
        } else if (method.equals("CASH_ON_DELIVERY")) {
            paymentData.put("address", address);
            paymentData.put("deliveryFee", deliveryFee);
        }

        Payment payment = paymentService.addPayment(order, method, paymentData);
        model.addAttribute("paymentId", payment.getId());
        return "OrderPaid";
    }
}