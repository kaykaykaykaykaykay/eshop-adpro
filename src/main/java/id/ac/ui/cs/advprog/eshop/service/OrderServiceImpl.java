package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Order createOrder(Order order) {
        Order existing = orderRepository.findById(order.getId());
        if (existing != null) {
            throw new IllegalStateException("Order with id " + order.getId() + " already exists");
        }
        return orderRepository.save(order);
    }

    @Override
    public Order updateStatus(String orderId, String status) {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new NoSuchElementException("Order not found: " + orderId);
        }
        if (!OrderStatus.contains(status)) {
            throw new IllegalArgumentException("Invalid order status: " + status);
        }
        order.setStatus(status);
        orderRepository.save(order);
        return order;
    }

    @Override
    public Order findById(String orderId) {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new NoSuchElementException("Order not found: " + orderId);
        }
        return order;
    }

    @Override
    public List<Order> findAllByAuthor(String author) {
        return orderRepository.findAllByAuthor(author);
    }
}